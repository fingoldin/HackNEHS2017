#include <netinet/in.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <stdlib.h>
#include <stdint.h>
#include <map>
#include <vector>
#include <string.h>

#include "player.hh"

typedef struct receive_packet {
	uint16_t pid;
	uint8_t type;
	uint8_t command_type;
} receive_packet_t;

typedef struct send_packet_unit {
	uint16_t pid;
	float x_pos;
	float y_pos;
	float anim_state;
	float anim_id;
} send_packet_unit_t;

typedef struct send_packet {
	send_packet_unit_t *units;
	int unit_l;
} send_packet_t;

enum PacketType {
	PT_CONNECT = 0,
	PT_COMMAND,
	PT_COUNT
};

enum PacketCommandType {
	PCT_LEFT_P = 0,
	PCT_LEFT_M,
	PCT_RIGHT_P,
	PCT_RIGHT_M,
	PCT_JUMP,
	PCT_COUNT
};

class Server
{
public:
	Server()
	{
		server_fd = 0;
	}

	int init()
	{
		if((server_fd = socket(AF_INET, SOCK_DGRAM, 0)) < 0) {
			printf("Error creating socket\n");
			return 1;
		}

		struct sockaddr_in servaddr;

		memset((char*)&servaddr, 0, sizeof(servaddr));

		servaddr.sin_family = AF_INET;
		servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
		servaddr.sin_port = htons(SERVER_PORT);

		if(bind(server_fd, (struct sockaddr*)&servaddr, sizeof(servaddr)) < 0) {
			printf("Socket binding failed\n");
			return 1;
		}

		return 0;
	}

	void loop()
	{
		receive_packet_t data = receivePacket();

		if(data.pid)
		{
	//		handleInputPacket(data);

	//		sendOutputPackets();
		}
	}

private:

	receive_packet_t receivePacket()
	{
		receive_packet_t packet;

		char buf[RECEIVE_BUFSIZE];

		struct sockaddr_in remote_addr;
		socklen_t addr_len = sizeof(remote_addr);

		printf("before\n");

		int recvlen = recvfrom(server_fd, buf, RECEIVE_BUFSIZE, 0, (struct sockaddr*)&remote_addr, &addr_len);
		printf("after\n");
		if(recvlen == 4) {
			packet.pid = *(uint16_t*)&buf[0];
			packet.type = *(uint8_t*)&buf[2];
			packet.command_type = *(uint8_t*)&buf[3];
		}
		else
			packet.pid = 0;

		printf("received %d bytes\n", recvlen);


		return packet;
	}

	void handleInputPacket(receive_packet_t packet)
	{
		Player *player = players[packet.pid];

                if(!player)
                        printf("Packet with unrecognized player id received\n");
                else
                {
                        switch(packet.type)
                        {
                        case PT_COMMAND:
                                handleCommand(packet, player);
                                break;
                        case PT_CONNECT:
                                handleConnect(packet, player);
                                break;
                        default:
                                printf("Packet of unrecognized type received\n");
                        };
                }
	}

	void sendOutputPackets()
	{
		send_packet_t packet;

                // send positions and animation frames
                for(std::map<int, Player*>::iterator it = players.begin(); it != players.end(); it++)
                {

                }

                sendPacket(packet);
	}

	void sendPacket(send_packet_t packet)
	{

	}

	void handleCommand(receive_packet_t packet, Player *player)
	{
		switch(packet.command_type)
		{
		case PCT_LEFT_P:
			player->applyForce(-10.0, 0.0);
			break;
		case PCT_LEFT_M:
			player->clearForces();
			break;
		case PCT_RIGHT_P:
			player->applyForce(10.0, 0.0);
			break;
		case PCT_RIGHT_M:
			player->clearForces();
			break;
		case PCT_JUMP:
			player->applyImpulse(0.0, 10.0);
			break;
		default:
			printf("Packet of unrecognized command id received\n");
		}
	}

	void handleConnect(receive_packet_t packet, Player *player)
	{

	}

	int server_fd;

	std::map<int, Player*> players;
};
