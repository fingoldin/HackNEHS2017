package com.stickfigure;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;


public class StartUpScreen extends JPanel
{	
	public JTextField usernameInput;
	public JTextField ipInput;
	public JFrame window;
	
	public StartUpScreen()
	{
		super();
		usernameInput = new JTextField(20);
		ipInput = new JTextField(20);
		window = new JFrame("Graphics Demo");
		done = false;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);  // Call JPanel's paintComponent method
								  //  to paint the background

		g.setColor(Color.GREEN);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
		g.drawString("STICKMEN FIGHT", 220, 200);


		g.setColor(Color.RED);
		// x, y, x_size, y_size
		g.drawRect(200, 250, 500, 300);

		g.setColor(Color.GREEN);
		// Draw a string of text starting at x = 55, y = 65:
		g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
		g.drawString("Username: ", 280, 300);
		g.drawString("Server IP: ", 280, 350);
	}

	class PressEnter implements ActionListener 
	{     
		StartUpScreen screen;
		
		public PressEnter(StartUpScreen screen)
		{
			this.screen = screen;
		}
		
		public void actionPerformed (ActionEvent e) 
		{     
			username = usernameInput.getText();
			ip = ipInput.getText();
			// USERNAME = "kevin";
			// IP_ADDRESS = "192.168.1.1";
			
			// Connect to server and upload data
			
			screen.setDone();
			
			System.out.println("done");
		}
	}

	public String[] goRender()
	{
		// JFrame window = new JFrame("Graphics Demo");
		// Set this window's location and size:
		// upper-left corner at 300, 300; width 200, height 150
		window.setBounds(0, 0, 900, 800);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.BLACK);  // the default color is light gray
		this.setLayout(null);
		
		// Button
		JButton button_enter = new JButton("Enter game");
		button_enter.setBounds(380, 400, 150, 50);
		this.add(button_enter);
		button_enter.addActionListener(new PressEnter(this));


		// Username input
		usernameInput.setBounds(440, 280, 180, 30);
		this.add(usernameInput);

		// IP server input
		ipInput.setBounds(440, 330, 180, 30);
		this.add(ipInput);

		Container c = window.getContentPane();
		c.add(this);
	
		window.setVisible(true);
		
		while(!done) { Thread.yield(); }
		
		window.dispose();
		
		System.out.println("finish");
		
		return new String[] { username, ip };
	}
	
	void setDone() { System.out.println("setdone"); done = true; }
	
	private Boolean done;
	private String username;
	private String ip;
}