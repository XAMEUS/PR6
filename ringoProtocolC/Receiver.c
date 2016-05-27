#include "Main.h"
#include "Receiver.h"
#include "Sender.h"

int udp_port;
int tcp_port;
struct in_addr my_ip;
char *ip_str;

int next_port;
struct in_addr next_ip;

int multicast_port;
struct in_addr multicast_ip;

void *receive(void* arg){
  int sock=socket(PF_INET,SOCK_DGRAM,0);
  sock=socket(PF_INET,SOCK_DGRAM,0);
  struct sockaddr_in address_sock;
  address_sock.sin_family=AF_INET;
  address_sock.sin_port=htons(udp_port);
  address_sock.sin_addr.s_addr=htonl(INADDR_ANY);
  int r=bind(sock,(struct sockaddr *)&address_sock,sizeof(struct sockaddr_in));
  if(r==0){
    char tampon[513];
    while(1){
      int rec=recv(sock,tampon,512,0);
      tampon[rec]='\0';
      printf("Message recu : %s\n",tampon);
      send_msg(tampon);
    }
  }
  return NULL;
}
