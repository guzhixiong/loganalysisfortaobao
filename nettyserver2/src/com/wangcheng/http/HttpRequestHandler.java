/*
 * Copyright 2009 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.wangcheng.http;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.LOCATION;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.SEE_OTHER;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.OK;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.util.CharsetUtil;

import com.wangcheng.dc.LogInfo;
import com.wangcheng.dc.handler.RequestHandler;

/**
 * @author <a href="http://www.jboss.org/netty/">The Netty Project</a>
 * @author Andy Taylor (andy.taylor@jboss.org)
 * @author <a href="http://gleamynode.net/">Trustin Lee</a>
 * 
 * @version $Rev: 2288 $, $Date: 2010-05-27 21:40:50 +0900 (Thu, 27 May 2010) $
 */
public class HttpRequestHandler extends SimpleChannelUpstreamHandler {

	private static final Logger log = Logger
			.getLogger(HttpRequestHandler.class);

	private List<RequestHandler> handlers = null;
	
	private static String buf = "<html><title>welcome</title><body>welcome to netty</body></html>";
	
	private static String CONTENT_TYPE_VALUE = "text/html; charset=UTF-8";
	
	private static String CONTENT_LENGTH_VALUE = "0";
	
	private static String STATUS_CODE = "302";
	private static String LOCATION_VALUE = "/fenxi001.gif";
	
	private static String XForwardedFor = "X-Real-IP";//"X-Forwarded-For";
	
	private static ChannelBuffer channelBuf = null;
	
	private static HttpRequestHandler instance = null;
	
	public static void initInstance(List<RequestHandler> requestHandlers) throws IOException{
		 
	  instance = new HttpRequestHandler(requestHandlers);  
		
	}
	public static final HttpRequestHandler getInstance(){
		return instance;
	}

	private HttpRequestHandler(List<RequestHandler> requestHandlers)  throws IOException {
		this.handlers = requestHandlers;
		
		FileInputStream  fin = new FileInputStream(NettyServer.getServerInstance().getDocumentRoot()+ File.separator + "index.html");
		   
		byte [] buffer = new byte[1024];
		
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		
		int n = fin.read(buffer);
		
		while(n>0){
			bout.write(buffer,0,n);
			n = fin.read(buffer);
		}
		 
		CONTENT_LENGTH_VALUE =Integer.toString(bout.size());
		
		channelBuf = ChannelBuffers.copiedBuffer(bout.toByteArray());
		
		LOCATION_VALUE = NettyServer.getServerInstance().getRedirectUrl();
		
	}

	@Override
	public final void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {

		HttpRequest request = (HttpRequest) e.getMessage();

		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
		
		//log.info(request.getUri());
 
			//if(request.getUri().indexOf("html")>0){
				
				String ip = null;
				  
				String xforwardfor = request.getHeader(XForwardedFor);
		
				if (xforwardfor != null) {
					ip = xforwardfor;
				}else{
					ip = ((InetSocketAddress) e.getRemoteAddress()).getAddress()
					.getHostAddress();
				}
				LogInfo logInfo = new LogInfo();
				for (RequestHandler handler : handlers) {
					handler.handle(ip, request,logInfo);
				}
			//} 

		// super.messageReceived(ctx, e);
		writeResponse(e, request, response);

	}

	private final void writeResponse(MessageEvent e, HttpRequest request,
			HttpResponse response) {

		// Build the response object.

		//response.setContent(channelBuf);
		response.setHeader(LOCATION,LOCATION_VALUE);
		response.setStatus(SEE_OTHER);
		response.setHeader(HttpHeaders.Names.REFERER,request.getUri());

		// Write the response.
		ChannelFuture future = e.getChannel().write(response); 

		future.addListener(ChannelFutureListener.CLOSE);

	}

	@Override
	public final void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		Channel ch = e.getChannel();
		Throwable cause = e.getCause();
		
		log.error("error", cause);
		
		if (cause instanceof TooLongFrameException) {
			sendError(ctx, BAD_REQUEST);
			return;
		} 

		if (ch.isConnected()) {
			sendError(ctx, INTERNAL_SERVER_ERROR);
		}
	}

	private final void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, status);
		response.setHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");
		response.setContent(ChannelBuffers.copiedBuffer(
				"Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));

		// Close the connection as soon as the error message is sent.
		ctx.getChannel().write(response)
				.addListener(ChannelFutureListener.CLOSE);
	}
}
