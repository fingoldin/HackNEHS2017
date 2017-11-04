package com.stickfigure.commands;

import com.stickfigure.player.Player;

public class RestingCommand extends Command {

	public void enter(Player player) {
		player.clearForces();
	}

}
