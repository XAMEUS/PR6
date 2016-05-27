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

#include "Main.h"


char* id;

int udp_port;
int tcp_port;
struct in_addr my_ip;
char *ip_str;

int next_port;
struct in_addr next_ip;

int multicast_port;
struct in_addr multicast_ip;

int main(int argc, char *argv[]){
    if(argc<4){
        fprintf(stderr,"Error, missing args\n\t1: id (string)\n\t2: udp_port (int)\n\t3: tcp_port (int)");
    }
    id = argv[1];
    udp_port = atoi(argv[2]);
    tcp_port = atoi(argv[3]);
    getOwnIp();
    int exit = 0;
    char* cmd = malloc(100);
    ip_str = IptoStr(inet_ntoa(my_ip));
    printf("%s\n",ip_str);
    while(!exit){
        fgets(cmd,99,stdin);
        char *token = strsep(&cmd, " ");
        if(strcmp(token,"RING") == 0){
          ring(cmd);
        }else if(strcmp(token, "JOIN")){

        }
    }

}

void getOwnIp(){
    struct ifaddrs *myaddrs, *ifa;
    struct sockaddr_in *s4;
    int status;
    status = getifaddrs(&myaddrs);
    if (status != 0){
        perror("Probleme de recuperation d'adresse IP");
        exit(1);
    }
    for (ifa = myaddrs; ifa != NULL; ifa = ifa->ifa_next){
        if (ifa->ifa_addr == NULL) continue;
        if ((ifa->ifa_flags & IFF_UP) == 0) continue;
        if ((ifa->ifa_flags & IFF_LOOPBACK) != 0) continue;
        if (ifa->ifa_addr->sa_family == AF_INET){
            s4 = (struct sockaddr_in *)(ifa->ifa_addr);
            my_ip = s4->sin_addr;
        }
    }
    freeifaddrs(myaddrs);
}

char *IptoStr(char* ip){
    char *str[4];
    str[0] = strsep(&ip, ".");
    str[1] = strsep(&ip, ".");
    str[2] = strsep(&ip, ".");
    str[3] = strsep(&ip, ".");

    char *r = malloc(15);
    sprintf(r,"%03d.%03d.%03d.%03d", atoi(str[0]),atoi(str[1]),atoi(str[2]),atoi(str[3]));
    return r;
}

char *PorttoStr(int port){
    char* r = malloc(4);
    sprintf(r, "%04d", port);
    return r;
}

char *InttoStr8(int i){
    char* r = malloc(4);
    sprintf(r, "%08d", i);
    return r;
}

void ring(char *cmd){
    next_port = udp_port;
    next_ip = my_ip;
    pthread_t th;
    pthread_create(&th,NULL,welcome,NULL);
    pthread_t th2;
    pthread_create(&th2,NULL,receive,NULL);
    //welcome/receiver/sender
}

void connectWELC(char *cmd){
    char *ip = malloc(100);
    char *port = malloc(4);
    ip = strsep(&cmd, " ");
    port = strsep(&cmd, " ");

    struct hostent* host;
    host=gethostbyname(ip);
    if(host==NULL){
        printf("Unknown\n");
    }
    struct in_addr *addr=(struct in_addr*)host->h_addr_list;

    struct sockaddr_in adress_sock;
    adress_sock.sin_family = AF_INET;
    adress_sock.sin_port = htons(atoi(port));
    adress_sock.sin_addr = *addr;
    int sock=socket(PF_INET,SOCK_STREAM,0);
    int r=connect(sock,(struct sockaddr *)&adress_sock, sizeof(struct sockaddr_in));
    if(r!=-1){
        char *buff = malloc(513);
        int size_rec=read(sock,buff,512*sizeof(char));
        buff[size_rec]='\0';
        char *token = strsep(&buff, " ");
        if(strcmp(token,"WELC")){
            token = strsep(&buff, " ");
            inet_aton(token, &next_ip);
            token = strsep(&buff, " ");
            next_port = atoi(token);
            token = strsep(&buff, " ");
            inet_aton(token, &multicast_ip);
            token = strsep(&buff, " ");
            multicast_port = atoi(token);

            char newc[512] = "NEWC ";
            strcat(newc,ip_str);
            strcat(newc," ");
            strcat(newc,PorttoStr(udp_port));
            strcat(newc,"\n");
            write(sock,newc,strlen(newc));
            int size_rec=read(sock,buff,512*sizeof(char));
            buff[size_rec]='\0';
            if(strcmp(buff,"ACKC")){
                //ok
            }
            //welcome/receiver/sender
        }
        /*
        printf("Caracteres recus : %d\n",size_rec);
        printf("Message : %s\n",buff);
        char *mess="SALUT!\n";
        write(sock,mess,strlen(mess));
        */
    }

}
