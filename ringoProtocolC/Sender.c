#include "Main.h"
#include "Sender.h"

int udp_port;
int tcp_port;
struct in_addr my_ip;
char *ip_str;

int next_port;
struct in_addr next_ip;

int multicast_port;
struct in_addr multicast_ip;

void send_msg(char* msg){
  int sock=socket(PF_INET,SOCK_DGRAM,0);
    struct sockaddr_in *saddr = malloc(sizeof(struct sockaddr_in));
    saddr->sin_family=AF_INET;
    saddr->sin_port=htons(next_port);
    saddr->sin_addr=next_ip;
    sendto(sock,msg,strlen(msg),0,(struct sockaddr *)saddr,(socklen_t)sizeof(struct sockaddr_in));
}
