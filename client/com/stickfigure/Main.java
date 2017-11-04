package com.stickfigure;

public class Main {
	public static desktop_game context;
	public static InputHandler inputHandler;
	
	public static void main(String []args) {
		context = new desktop_game();
		inputHandler = new InputHandler(desktop_game);
		
		Scheduler.init();
		
		double time = System.currentTimeMillis() / 1000.0;
		
		while(!inputHandler.ShouldExit())
		{
			double p_time = time;
			time = System.currentTimeMillis() / 1000.0;
			double dt = time - p_time;
			
			Scheduler.loop(dt);
		}
		
		Scheduler.cleanup();
	}
}
