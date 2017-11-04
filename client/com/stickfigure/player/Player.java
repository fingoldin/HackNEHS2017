package com.stickfigure.player;

import java.util.ArrayList;

import com.stickfigure.Constants;
import com.stickfigure.Scheduler;
import com.stickfigure.commands.Command;
import com.stickfigure.commands.RestingCommand;

public class Player {
	protected ArrayList<Command> active_commands;
	protected Command default_command;
	
	protected String name;
	
	protected Boolean on_ground;
	
	protected double x_velocity;
	protected double y_velocity;
	protected double x_pos;
	protected double y_pos;
	
	protected double x_force;
	protected double y_force;
	
	protected double mass;
	
	public Player(String n) {
		name = n;
		default_command = new RestingCommand();
		on_ground = true;
		x_velocity = 0;
		y_velocity = 0;
		x_pos = 0;
		y_pos = 0;
		x_force = 0;
		y_force = 0;
		mass = 10.0;
	}
	
	public void loop(double dt) {
		if(active_commands.size() == 0)
			default_command.loop();
		else {
			for(int i = 0; i < active_commands.size(); i++)
				active_commands.get(i).loop();
		}
		
		if(on_ground) {
			double x_velocity_old = x_velocity;
			
			double fric = Constants.FrictionCoefficient * Constants.Gravity * dt;
			
			double x_dv = -Math.signum(x_velocity) * Math.min(fric, Math.abs(x_velocity)) + (x_force / mass) * dt;
			
			x_velocity += x_dv;
			x_pos += 0.5 * (x_velocity + x_velocity_old) * dt;
			
			if(y_force > 0) {
				y_velocity = (y_force / mass - Constants.Gravity) * dt;
				
				if(y_velocity > 0) {
					y_pos += 0.5 * y_velocity * dt;
					on_ground = false;
				}
				else
					y_velocity = 0;	
			}
		}
		else {
			double y_velocity_old = y_velocity;
			double y_dv = (y_force / mass - Constants.Gravity) * dt;
			
			double x_velocity_old = x_velocity;
			double x_dv = (x_force / mass) * dt;
			
			y_velocity += y_dv;
			x_velocity += x_dv;
			
			y_pos += 0.5 * (y_velocity + y_velocity_old) * dt;
			x_pos += 0.5 * (x_velocity + x_velocity_old) * dt;
			
			if(y_pos < 0) {
				y_pos = 0;
				on_ground = true;
				y_velocity = 0;
			}
		}
	}
	
	public void applyForce(double x_v, double y_v) {
		x_force += x_v;
		y_force += y_v;
	}
	
	public void clearForces() {
		x_force = 0;
		y_force = 0;
	}
	
	public void applyImpulse(double x_v, double y_v) {
		x_velocity += x_v / mass;
		y_velocity += y_v / mass;
	}
	
	public Command DefaultCommand() {
		return default_command;
	}
	
	public int activeCommandsSize() {
		return active_commands.size();
	}
	
	public Boolean addCommand(Command command) {
		
		for(int i = 0; i < active_commands.size(); i++) {
			if(active_commands.get(i).conflictsWith(command))
				return false;
		}
		
		active_commands.add(command);
		
		return true;
	}
	
	public Boolean removeCommand(Command command) {
		int i = active_commands.indexOf(command);
		if(i == -1)
			return false;
	
		active_commands.remove(i);
		return true;
	}
}
