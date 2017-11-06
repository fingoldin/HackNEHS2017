package com.stickfigure;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class Networking implements Runnable {
	public void start(String username, String ip_address, int port)
	{
		// change to hex
		//send every .5 secnds until message connected

		byte[] buffer = ("ASFF").getBytes();

		InetAddress address;
		System.out.println(ip_address);
		try {
			address = InetAddress.getByName(ip_address);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		DatagramPacket packet = new DatagramPacket(
				buffer, buffer.length, address, port
				);
	
		try {
			datagramSocket = new DatagramSocket(port);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		
		byte receiveData[] = new byte[8];

		DatagramPacket receivePacket = new DatagramPacket(receiveData,
                           receiveData.length);

		while(true)
		{
			try {
				datagramSocket.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				datagramSocket.receive(receivePacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String sentence = new String( receivePacket.getData(), 0,
                                 receivePacket.getLength());
			
			System.out.println(sentence);
			
			if (sentence.equals("4232"))					
			{
				System.out.println("Starting game");
				break;
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void run() {
		byte receiveData[] = new byte[8];

		DatagramPacket receivePacket = new DatagramPacket(receiveData,
                           receiveData.length);

		while(true)
		{
			try {
				datagramSocket.receive(receivePacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String sentence = new String( receivePacket.getData(), 0,
                                 receivePacket.getLength());
			
			System.out.println(sentence);
		}
	}
	
	static private DatagramSocket datagramSocket;
}
