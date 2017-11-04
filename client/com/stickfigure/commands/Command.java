package com.stickfigure.commands;

import com.stickfigure.Scheduler;
import com.stickfigure.player.Player;

public class Command {
	protected Boolean finished;
	
	protected Player player;
	
	public Command() {
		finished = false;
		player = null;
	}
	
	public void enter(Player character) {
		player = character;
	}
	
	public void _loop() {
		
	}
	
	public void loop() {
		if(!isFinished())
			_loop();
		else
			Scheduler.exitCommand(player, this);
	}
	
	public void exit(Player player) {
		
	}
	
	protected Boolean isFinished() {
		return finished;
	}
	
	public Boolean conflictsWith(Command command) {
		return false;
	}
}
