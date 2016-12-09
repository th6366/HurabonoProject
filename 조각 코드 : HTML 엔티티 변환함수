#include <stdio.h>
#include <malloc.h>

#define ENT_HTML_QUOTE_NONE        0
#define ENT_HTML_QUOTE_SINGLE    1
#define ENT_HTML_QUOTE_DOUBLE    2

#define ENT_COMPAT        ENT_HTML_QUOTE_DOUBLE
#define ENT_QUOTES        (ENT_HTML_QUOTE_DOUBLE | ENT_HTML_QUOTE_SINGLE)
#define ENT_NOQUOTES    ENT_HTML_QUOTE_NONE

static const struct {
    unsigned short charcode;
    char *entity;
    int entitylen;
    int flags;
}basic_entities[]={
    {'&', "&amp;amp;", 5,0},
    {'"', "&amp;quot;",6,ENT_HTML_QUOTE_DOUBLE},
    {'\'',"&amp;#039;",6,ENT_HTML_QUOTE_SINGLE},
    {'<', "&amp;lt;",    4,0},
    {'>', "&amp;gt;",    4,0},
    {0,NULL,0,0}
};

int htmlspecialchars(char *,char **,int);

int main(int argc,char **argv){

    char *src="<br>' \" & &This is test\n^^;\n\n";
    char *ret;

    htmlspecialchars(src,&ret,ENT_COMPAT);
    printf("%s\n",ret);
    free(ret);

    return 0;

}

int htmlspecialchars(char *src,char **ret,int type){

    int len=0;
    int entity_len=0;
    int size=0;
    char *tmp;

    len=strlen(src);
    tmp=src;

    while(*src!='\0'){

        switch(*src){

            case '&'://    &

                entity_len+=basic_entities[0].entitylen;
                break;

            case '"'://    "

                if((type==ENT_COMPAT)||(type=ENT_QUOTES)) entity_len+=basic_entities[1].entitylen;
                break;

            case '\''://    '

                if((type!=ENT_COMPAT)||(type==ENT_QUOTES)) entity_len+=basic_entities[2].entitylen;
                break;

            case '<'://    <

                entity_len+=basic_entities[3].entitylen;
                break;

            case '>'://    >

                entity_len+=basic_entities[4].entitylen;
                break;

        }

        ++src;

    }

    src=tmp;
    size=len+entity_len;

    if(((*ret)=(char *)malloc(sizeof(char)*size+1))==NULL) return 1;
    memset((*ret),0x0,sizeof(char)*size+1);

    tmp=(*ret);

    while(*src!='\0'){

        switch(*src){

            case '&'://    &

                memcpy((*ret),basic_entities[0].entity,basic_entities[0].entitylen);
                (*ret)+=basic_entities[0].entitylen;
                break;

            case '"'://    "

                if((type==ENT_COMPAT)||(type=ENT_QUOTES)){
                    memcpy((*ret),basic_entities[1].entity,basic_entities[1].entitylen);
                    (*ret)+=basic_entities[1].entitylen;
                }
                break;

            case '\''://    '

                if((type!=ENT_COMPAT)||(type==ENT_QUOTES)){
                    memcpy((*ret),basic_entities[2].entity,basic_entities[2].entitylen);
                    (*ret)+=basic_entities[2].entitylen;
                }
                break;

            case '<'://    <

                memcpy((*ret),basic_entities[3].entity,basic_entities[3].entitylen);
                (*ret)+=basic_entities[3].entitylen;
                break;

            case '>'://    >

                memcpy((*ret),basic_entities[4].entity,basic_entities[4].entitylen);
                (*ret)+=basic_entities[4].entitylen;
                break;

            default:

                *(*ret)=*src;
                ++(*ret);

        }

        ++src;

    }

    (*ret)=tmp;
    return 0;

}
