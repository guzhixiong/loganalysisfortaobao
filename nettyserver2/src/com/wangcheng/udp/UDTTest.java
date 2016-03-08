package com.wangcheng.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDTTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		DatagramSocket ds = new DatagramSocket(18881);
		byte [] buffer = new byte[10240];
		
		DatagramPacket dp = new DatagramPacket(buffer,buffer.length);
		
		while(true){
			ds.receive(dp);
		}
	}

}
