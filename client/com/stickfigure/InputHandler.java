package com.stickfigure;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

import javax.swing.JPanel;

import com.stickfigure.commands.Command;
import com.stickfigure.commands.JumpCommand;
import com.stickfigure.commands.MoveLeftCommand;
import com.stickfigure.commands.MoveRightCommand;

public class InputHandler implements KeyListener {
	private Map<Integer,Command> stateCommands;
	private Map<Integer,Command> actionCommands;

	private Boolean shouldExit;
	
	public InputHandler(JPanel panel) {
		panel.addKeyListener(this);
		
		stateCommands.put(KeyEvent.VK_LEFT, new MoveLeftCommand());
		stateCommands.put(KeyEvent.VK_RIGHT, new MoveRightCommand());
	
		actionCommands.put(KeyEvent.VK_SPACE, new JumpCommand());
		
		shouldExit = false;
	}
	
	public void keyPressed(KeyEvent e) {
		Command command;
		
		if((command = stateCommands.get(e.getKeyCode())) != null)
			Scheduler.enterCommand(Scheduler.LocalPlayer(), command);
		
		else if((command = actionCommands.get(e.getKeyCode())) != null)
			Scheduler.enterCommand(Scheduler.LocalPlayer(), command);
	}
	
	public void keyReleased(KeyEvent e) {
		Command command;
		
		if((command = stateCommands.get(e.getKeyCode())) != null)
			Scheduler.exitCommand(Scheduler.LocalPlayer(), command);
	}
	
	public void keyTyped(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_ESCAPE)
			shouldExit = true;
	}
	
	public Boolean ShouldExit() {
		return shouldExit;
	}
}
