
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
