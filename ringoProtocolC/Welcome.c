#include "Main.h"
#include "Welcome.h"

int udp_port;
int tcp_port;
struct in_addr my_ip;

int next_port;
struct in_addr next_ip;

int multicast_port;
struct in_addr multicast_ip;

void *welcome(void* arg){
  printf("Starting welcome service...\n");
    int sock=socket(PF_INET,SOCK_STREAM,0);
    struct sockaddr_in address_sock;
    address_sock.sin_family=AF_INET;
    address_sock.sin_port=htons(tcp_port);
    address_sock.sin_addr.s_addr=htonl(INADDR_ANY);
    int r=bind(sock,(struct sockaddr *)&address_sock,sizeof(struct sockaddr_in));
    if(r==0){
        r=listen(sock,0);
        while(1){
            struct sockaddr_in caller;
            socklen_t size=sizeof(caller);
            int new=accept(sock,(struct sockaddr *)&caller,&size);
            if(new>=0){
                char welc[512] = "WELC ";
                strcat(welc,IptoStr(inet_ntoa(next_ip)));
                strcat(welc, " ");
                strcat(welc,PorttoStr(next_port));
                strcat(welc, " ");
                strcat(welc,IptoStr(inet_ntoa(multicast_ip)));
                strcat(welc, " ");
                strcat(welc,PorttoStr(multicast_port));
                strcat(welc, "\n");
                write(new,welc,strlen(welc)*sizeof(char));
                char buff[513];
                int recu = read(new, buff, 512*sizeof(char));
                buff[recu]='\0';
                char ip[15];
                char port[4];
                memcpy( ip, &buff[5], 15);
                memcpy( port, &buff[21], 4);
                inet_ntop(AF_INET,&next_ip,ip,INET_ADDRSTRLEN);
                next_port = atoi(port);
                char *ackc = "ACKC\n";
                write(new,ackc,strlen(ackc)*sizeof(char));
            }
            close(new);
        }
    }
    return NULL;
}
