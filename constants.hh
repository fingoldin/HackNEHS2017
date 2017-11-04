class Constants
{
public:
	static double FrictionCoefficient;
	static double Gravity;
};

#define PORT  1024
#define THRESH  0.0001
#define RECEIVE_BUFSIZE  8
#define RECEIVE_TIMEOUT_S  0
#define RECEIVE_TIMEOUT_US  20000
#define CONNECT_SIGNATURE  "ASFF"
#define COMMAND_SIGNATURE  "DFHD"
#define CONNECT_CONFIRMATION  "4232"
