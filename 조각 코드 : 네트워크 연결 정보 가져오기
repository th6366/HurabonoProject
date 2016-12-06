#include <stdio.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>
#include <sys/socket.h>
#include <unistd.h>
#include <stdlib.h>
#include <pwd.h>

const char *TCPFILE = "/proc/net/tcp";
const char *FORMAT ="%s%s%s%s%s%s%s%s%s%s";
static char states[16][20] = { "ESTABLISHED", "SYN_SENT", "SYN_RECV", "FIN_WAIT1", "FIN_WAIT2", "TIME_WAIT", "CLOSE", "CLOSE_WAIT", "LAST_ACK", "LISTEN", "CLOSING", "MAX_STATES"};

// 네트워크 연결 정보를 저장하기 위한 구조체
struct netinfo
{
    FILE *tcpfp;
    char localaddr[24];
    char localport[16];
    char remaddr[24];
    char remport[16];
    char stat[16];
    int  txq;
    int  rxq;
    int     uid;
    int  idx;
    char uname[24];
};

typedef struct netinfo TCPINFO;

// /proc/net/tcp 파일을 열어서 스트림을 만든다.
TCPINFO *nsopen()
{
    TCPINFO *ltcpinfo;
    if(access(TCPFILE,F_OK) !=0)
    {
        perror("ACCESS Error");
        return (TCPINFO *)NULL; 
    }
    ltcpinfo = (TCPINFO *)malloc(sizeof(*ltcpinfo)); 
    memset((void *)ltcpinfo, 0x00, sizeof(*ltcpinfo));

    ltcpinfo->tcpfp = NULL;
    ltcpinfo->tcpfp = fopen(TCPFILE, "r");
    if (ltcpinfo == NULL)
    {
        perror("ACCESS Error");
        free(ltcpinfo);
        return (TCPINFO *)NULL;
    }
    ltcpinfo->uid = -1;
    ltcpinfo->idx = 0;

    return ltcpinfo;
}

// /proc/net/tcp 파일 스트림을 닫는다.
void nsclose(TCPINFO *tf)
{
    if (tf->tcpfp == NULL)
        fclose(tf->tcpfp);
    free(tf);
}

// 유저 이름을 얻어온다.
char *getuname(char *uname, int uid, int size)
{
    struct passwd *pass_info = NULL;
    while((pass_info = getpwent()) != NULL)
    {
        if (pass_info->pw_uid == uid)
        {
            strncpy(uname, pass_info->pw_name, size);
            return uname;
        }
    }
    return (char *)NULL;
}

// tcp 정보를 읽어서 네트워크 정보를 가져온다.
TCPINFO *nsread(TCPINFO *info)
{
    char buf[256];
    char *tr;
    char null[16];
    char localaddr[24];
    char remaddr[24];
    char st[4];
    char trxqueue[20];
    char uid[8];
    char uname[36] = {0x00,};
    int  fnum;
    int  snum;
    struct in_addr in;
    char *addr;

    if (info->idx == 0)
    {
        if (fgets(buf, 256, info->tcpfp) == NULL)
        {
            return (TCPINFO *)NULL;
        }
        info->idx=1;
    }
    if (fgets(buf, 256, info->tcpfp) == NULL)
        return (TCPINFO *)NULL;
    // sl  local_address rem_address   st tx_queue rx_queue tr tm->when retrnsmt   uid  timeout inode
    // 0: 0100007F:A56F 00000000:0000 0A 00000000:00000000 00:00000000 00000000   105        0 10282 1 c34a1c00 3000 0 0 2 -1
    sscanf(buf,"%s %s %s %s %s %s %s %s %s %s", 
                                    null,      // sl : 미 사용 
                                    localaddr, // local_address 
                                    remaddr,     // rem_address
                                    st,        // status
                                    trxqueue,  // tx_queue & rx_queue
                                    null,      // tr & tm->when : 미 사용
                                    null,      // retrnsmt : 미사용
                                    uid,       // uid
                                    null,      // timeout : 미사용
                                    null);     // inde : 미사용
    // get localaddr
    sscanf(localaddr, "%x%[:]%x", &fnum, null, &snum);
    in.s_addr = fnum;
    addr = inet_ntoa(in);
    sprintf(info->localaddr, "%s", addr); 
    sprintf(info->localport, "%d", snum); 

    // get remaddr
    sscanf(remaddr, "%x%[:]%x", &fnum, null, &snum);
    in.s_addr = fnum;
    addr = inet_ntoa(in);
    sprintf(info->remaddr, "%s", addr); 
    sprintf(info->remport, "%d", snum); 

    // Status
    sscanf(st, "%x", &fnum);
    sprintf(info->stat,"%s", states[fnum]);

    // User Infomation
    if (getuname(uname, atoi(uid), 32) == NULL)
    {
        sprintf(info->uname,"%s", "");
    }
    else
        sprintf(info->uname,"%s", uname);
    info->uid = atoi(uid);
    return info; 

}

int main(int argc, char **argv)
{
    TCPINFO *tf;
    tf = nsopen();
    while (nsread(tf) != (TCPINFO *)NULL)
    {
        printf("%s:%s ---> %s:%s\t%s\t%s\n", tf->localaddr, tf->localport, 
                                        tf->remaddr, tf->remport, tf->stat, tf->uname);
    }
    nsclose(tf);
}
