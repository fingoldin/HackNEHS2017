package com.stickfigure.commands;

import com.stickfigure.player.Player;

public class MoveRightCommand extends Command {
	public void enter(Player player) {
		player.applyForce(10.0, 0.0);
	}
	
	public void exit(Player player) {
		player.clearForces();
	}
}
