#include <stdlib.h>
#include <stdio.h>
#include <math.h>

#include "constants.hh"

double signum(double x)
{
	if(x > 0)
		return 1.0;
	else if(x < 0)
		return -1.0;
	else
		return 0.0;
}

class Player
{
public:

	Player()
	{
		x_pos = 0.0;
		y_pos = 0.0;
		x_velocity = 0.0;
		y_velocity = 0.0;
		x_force = 0.0;
		y_force = 0.0;
		mass = 10.0;
		on_ground = true;
	}

	void loop(double dt)
	{
		if(on_ground) {
			double x_velocity_old = x_velocity;

			double fric = Constants::FrictionCoefficient * Constants::Gravity * dt;

			double x_dv = -signum(x_velocity) * fmin(fric, fabs(x_velocity)) + (x_force / mass) * dt;

			x_velocity += x_dv;
			x_pos += 0.5 * (x_velocity + x_velocity_old) * dt;

			if(y_force > 0) {
				y_velocity = (y_force / mass - Constants::Gravity) * dt;

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
			double y_dv = (y_force / mass - Constants::Gravity) * dt;

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

	void applyForce(double x_v, double y_v) {
		x_force += x_v;
		y_force += y_v;
	}

	void clearForces() {
		x_force = 0;
		y_force = 0;
	}

	void applyImpulse(double x_v, double y_v) {
		x_velocity += x_v / mass;
		y_velocity += y_v / mass;
	}

private:

	double x_pos;
	double y_pos;

	double x_velocity;
	double y_velocity;

	double x_force;
	double y_force;

	double mass;

	bool on_ground;
};
