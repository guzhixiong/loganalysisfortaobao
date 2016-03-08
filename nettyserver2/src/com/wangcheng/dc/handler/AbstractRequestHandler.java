package com.wangcheng.dc.handler;

import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;

import com.wangcheng.dc.IConstants;
import com.wangcheng.dc.utils.StringUtils;

public abstract class AbstractRequestHandler implements RequestHandler {

	protected Properties properties = null;

	protected ExecutorService executer = null;

	public static final String IP = IConstants.LogNames.IP;
	public static final String LOGTIME = IConstants.LogNames.LOGTIME;
	public static final String URL = IConstants.LogNames.URL;
	public static final String USER_AGENT = IConstants.LogNames.USER_AGENT;
	public static final String REFERER = IConstants.LogNames.REFERER;
	public static final String PARAM_REFERER = IConstants.LogNames.PARAM_REFERER;
	public static final String PARAM_UID = IConstants.LogNames.PARAM_UID;
	public static final String PARAM_PID = IConstants.LogNames.PARAM_PID;
	public static final String PARAM_TBNICK = IConstants.LogNames.PARAM_TBNICK;
	public static final String PARAM_TITLE = IConstants.LogNames.PARAM_TITLE;

	public static final String PROVINCE = IConstants.LogNames.PROVINCE;
	public static final String CITY = IConstants.LogNames.CITY;
	public static final String ADDRESS = IConstants.LogNames.ADDRESS;
	public static final String TITLE = IConstants.LogNames.TITLE;

	public static final String LOG_DELIMETER_STRING = IConstants.LogNames.LOG_DELIMETER_STRING;

	public static final String ROW_DELIMIER_STRING = IConstants.LogNames.ROW_DELIMIER_STRING;

	public static final String REPLACE_NULL_STRING = IConstants.REPLACE_NULL_STRING;

	protected static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd" + LOG_DELIMETER_STRING + "HH:mm:ss");

	@Override
	public void initialize(Properties properties) throws Exception {
		this.properties = properties;
	}

	protected final String getFormattedContent(final String ip,
			final HttpRequest request) {
		StringBuilder sb = new StringBuilder();
		new SoftReference<StringBuilder>(sb);

		QueryStringDecoder queryStringDecoder = new QueryStringDecoder(
				request.getUri());

		Map<String, List<String>> params = queryStringDecoder.getParameters();

		String url = request.getHeader(HttpHeaders.Names.REFERER);

		String agent = request.getHeader(HttpHeaders.Names.USER_AGENT);

		agent = StringUtils.replaceAll(agent, IConstants.BLANK_STRING,
				IConstants.REPLACE_BLANK_STRING);
 

		List<String> uidParams = params.get(PARAM_UID);// 店铺id
		  
		String uid = REPLACE_NULL_STRING; 
		//String tbnick = REPLACE_NULL_STRING;  

		if (uidParams != null && !uidParams.isEmpty()) {
			uid = uidParams.get(0);
		}
		  
		
		/*if (tbnickParams != null && !tbnickParams.isEmpty()) {
			tbnick = tbnickParams.get(0);
		}*/

		if (org.apache.commons.lang.StringUtils.isBlank(url)) {
			url = REPLACE_NULL_STRING; 
		} 
		

		sb.append(sdf.format(new Date()));
		sb.append(LOG_DELIMETER_STRING);

		sb.append(uid);
		sb.append(LOG_DELIMETER_STRING);

		sb.append(ip);
		sb.append(LOG_DELIMETER_STRING);

		sb.append(agent);
		sb.append(LOG_DELIMETER_STRING);

		sb.append(url);
		sb.append(LOG_DELIMETER_STRING); 
		
		//sb.append(tbnick);
		//sb.append(LOG_DELIMETER_STRING);

		if (uidParams != null) {
			uidParams.clear();
			uidParams = null;
		} 

		params.clear();

		params = null;

		queryStringDecoder = null;

		url = null; 
		agent = null;

		uid = null; 

		return sb.toString();
	}

}
