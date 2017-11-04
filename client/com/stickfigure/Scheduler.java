package com.stickfigure;

import java.util.ArrayList;

import com.stickfigure.commands.Command;
import com.stickfigure.player.Player;

public class Scheduler {
	static private ArrayList<Player> players;
	static private Player localPlayer;
	
	static void init() {
		
	}
	
	static Player LocalPlayer() {
		return localPlayer;
	}
	
	static void addPlayer(String name, Boolean local) {
		Player newPlayer = new Player(name);
		
		newPlayer.DefaultCommand().enter(newPlayer);
		
		players.add(newPlayer);
		
		if(local)
			localPlayer = newPlayer;
	}
	
	static void removePlayer(Player player) {
		int i = players.indexOf(player);
		if(i != -1)
			players.remove(i);
	}
	
	public static void enterCommand(Player player, Command command) {
		if(player.addCommand(command)) {
			player.DefaultCommand().exit(player);
			command.enter(player);
		}
	}
	
	public static void exitCommand(Player player, Command command) {
		if(player.removeCommand(command)) {
			command.exit(player);
			
			if(player.activeCommandsSize() == 0)
				player.DefaultCommand().enter(player);
		}
	}
	
	public static void loop(double dt) {
		for(int i = 0; i < players.size(); i++) {
			players.get(i).loop(dt);
		}
	}
	
	static void cleanup() {
		
	}
}
