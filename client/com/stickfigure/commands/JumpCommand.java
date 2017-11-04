package com.stickfigure.commands;

import com.stickfigure.player.Player;

public class JumpCommand extends Command {
	public void enter(Player player) {
		player.applyImpulse(0.0, 10.0);
	}
	
	protected Boolean isFinished() {
		return true;
	}
}
