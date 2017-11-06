package com.stickfigure;

import java.io.IOException;

public class Main {
	static private Networking network = new Networking();
	static private StartUpScreen startUp = new StartUpScreen();
	
	public static void main(String[] args)
	{
		String[] res = startUp.goRender();
		
		System.out.println(res[0]);
		
		network.start(res[0], res[1], Constants.PORT);
		
		Renderer desktop;
		try {
			desktop = new Renderer();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		double time = System.currentTimeMillis();
		
		InputHandler inputHandler = new InputHandler(desktop);
		
		Scheduler.init();
		
		while(!inputHandler.ShouldExit())
		{
			double p_time = time;
			time = System.currentTimeMillis();
			double dt = time - p_time;
			
			Scheduler.loop(dt);
			
			// Render call
			
			for(int i = 0; i < Scheduler.nplayers(); i++)
				Renderer.drawSprite(Scheduler.players[i]);
			
			System.out.println(dt);
		}
		
		Scheduler.cleanup();
	}
}
