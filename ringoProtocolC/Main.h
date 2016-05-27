#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <ifaddrs.h>
#include <sys/ioctl.h>
#include <net/if.h>
#include <netdb.h>
#include <unistd.h>
#include <pthread.h>
#include "Welcome.h"

extern int udp_port;
extern int tcp_port;
extern struct in_addr my_ip;

extern int next_port;
extern struct in_addr next_ip;

extern int multicast_port;
extern struct in_addr multicast_ip;


void ring(char *cmd);
void getOwnIp();
char *IptoStr(char* ip);
char *PorttoStr(int port);
