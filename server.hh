#include <netinet/in.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <stdlib.h>
#include <stdint.h>
#include <map>
#include <vector>
#include <string.h>
#include <sys/time.h>

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
		firstloop = true;
		lastID = 0;
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
		servaddr.sin_port = htons(PORT);

		if(bind(server_fd, (struct sockaddr*)&servaddr, sizeof(servaddr)) < 0) {
			printf("Socket binding failed\n");
			return 1;
		}

		struct timeval tv;
		tv.tv_sec = RECEIVE_TIMEOUT_S;
		tv.tv_usec = RECEIVE_TIMEOUT_US;
		if (setsockopt(server_fd, SOL_SOCKET, SO_RCVTIMEO, &tv, sizeof(tv)) < 0) {
			printf("Could not set timeout\n");
			return 1;
		}

		return 0;
	}

	void loop()
	{
		double dt = 0.01;

		if(firstloop) {
			gettimeofday(&time, NULL);
			firstloop = false;
		}
		else {
			struct timeval p_time;
			memcpy(&p_time, &time, sizeof(struct timeval));

			gettimeofday(&time, NULL);

			dt = (double)(time.tv_sec - p_time.tv_sec) + 0.000001 * (double)(time.tv_usec - p_time.tv_usec);
		}

		receivePacket();

		for(std::map<unsigned long, Player*>::iterator it = players.begin(); it != players.end(); it++)
			it->second->loop(dt);

		sendOutputPackets();

		printf("dt: %f\n", dt);
	}

private:

	void receivePacket()
	{
		char buf[RECEIVE_BUFSIZE];

		struct sockaddr_in remote_addr;
		socklen_t addr_len = sizeof(remote_addr);


		int recvlen = recvfrom(server_fd, buf, RECEIVE_BUFSIZE, 0, (struct sockaddr*)&remote_addr, &addr_len);
		if(recvlen > -1)
			buf[recvlen] = '\0';

		if(recvlen == strlen(CONNECT_SIGNATURE) && strcmp(buf, CONNECT_SIGNATURE) == 0) {
			handleConnect(remote_addr.sin_addr.s_addr);
		}
		else if(recvlen == (strlen(COMMAND_SIGNATURE) + 1) && strcmp(buf + 1, COMMAND_SIGNATURE) == 0) {
			std::map<unsigned long, Player*>::iterator player = players.find(remote_addr.sin_addr.s_addr);

			if(player != players.end())
                        	handleCommand((PacketCommandType)*(uint8_t*)&buf[0], player->second);
		}

		printf("received %d bytes\n", recvlen);
	}

	void sendOutputPackets()
	{
		int data_len = players.size() * 15;
		char *data = (char*)malloc(data_len);

                // send positions and animation frames
		int i = 0;
                for(std::map<unsigned long, Player*>::iterator it = players.begin(); it != players.end(); it++)
                {
			*(uint16_t*)(i * 15 + data) = it->second->getID();
			*(float*)(i * 15 + data + 2) = it->second->getXPos();
			*(float*)(i * 15 + data + 6) = it->second->getYPos();
			*(float*)(i * 15 + data + 10) = it->second->getAnimPos();
			data[i * 15 + 14] = (uint8_t)it->second->getAnimID();

			i++;
        	}

		for(std::map<unsigned long, Player*>::iterator it = players.begin(); it != players.end(); it++)
                	sendPacket(it->first, data, data_len);
	}

	void handleCommand(PacketCommandType type, Player *player)
	{
		switch(type)
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

	void handleConnect(unsigned long ip)
	{
		char confirmation[] = CONNECT_CONFIRMATION;

		if(sendPacket(ip, confirmation, strlen(confirmation)) == 0)
			players.insert(std::pair<unsigned long, Player*>(ip, new Player(ip, getNextID())));
	}

	int sendPacket(unsigned long ip, char *buf, int buf_len)
	{
		struct sockaddr_in dest_addr;
		memset((char*)&dest_addr, 0, sizeof(dest_addr));
		dest_addr.sin_family = AF_INET;
		dest_addr.sin_port = htons(PORT);
		dest_addr.sin_addr.s_addr = ip;

		if(sendto(server_fd, buf, buf_len, 0, (struct sockaddr*)&dest_addr, sizeof(dest_addr)) < 0) {
			printf("Sendto failed\n");
			return 1;
		}

		return 0;
	}

	uint16_t getNextID()
	{
		return ++lastID;
	}

	int server_fd;

	uint16_t lastID;

	bool firstloop;

	struct timeval time;

	std::map<unsigned long, Player*> players;
};
