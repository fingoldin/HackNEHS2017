#include "server.hh"

double Constants::FrictionCoefficient = 1.0;
double Constants::Gravity = 9.8;

int main()
{
	Server server;

	if(server.init() == 0)
		printf("Socket init successful\n");
	else
		return 1;

	while(true)
		server.loop();

	return 0;
}
