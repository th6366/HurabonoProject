#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <fcntl.h>
#include <unistd.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <arpa/inet.h>
 
 
#define BUF_LEN 128
int main(int argc,char *argv[])
{
    char buffer[BUF_LEN];
    struct sockaddr_in server_addr,client_addr;
    char temp[20];
    int server_fd,client_fd;
    int len,msg_size;
    char test[20];
    if(argc !=2)
    {
        printf("Usege ./filename [PORT] \n");
        exit(0);
    }
 
    if((server_fd = socket(AF_INET,SOCK_STREAM,0)) == -1)
    {
        printf("Server: can not Open Socket\n");
        exit(0);
    }
    
    memset(&server_addr,0x00,sizeof(server_addr));
    
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    server_addr.sin_port = htons(atoi(argv[1]));
    
    if(bind(server_fd, (struct sockaddr *)&server_addr, sizeof(server_addr))< 0)
    {
        printf("Server: cat not bind local addrss\n");
        exit(0);
        
    }
    
    
    if(listen(server_fd,5) < 0)
    {
        printf("Server: cat not listen connnect.\n");
        exit(0);
    }
    
    
    memset(buffer,0x00,sizeof(buffer));
    len = sizeof(client_addr);
    printf("=====[PORT] : %d =====\n",atoi(argv[1]));
    printf("Server : wating connection request.\n");
 
    
    while(1)
    {
        client_fd = accept(server_fd,(struct sockaddr *)&client_addr,(socklen_t *)&len);
        
        if(client_fd < 0)
        {
            printf("Server: accept failed\n");
            exit(0);
        }
        inet_ntop(AF_INET,&client_addr.sin_addr.s_addr,temp,sizeof(temp));
        printf("Server: %s client connect.\n",temp);
        send(client_fd,"test test test",0xff,0);
           msg_size = recv(client_fd,(char *)buffer,BUF_LEN,0);
           send(client_fd,(char *)buffer,msg_size,0);
        close(client_fd);
        printf("Server: %s client closed.\n",temp);
        
        
        
    }
    
    close(server_fd);
    
    return 0;
}
