#include <stdio.h>
#include <string.h>

void invertStr(char *a)
{
    int len, i;
    char b[20] = {0x00,};
    len = strlen(a);
    for (i = 0; i < len; i += 2)
    {
        b[len-i-2] = a[i];
        b[len-i-1] = a[i+1];
    }
    printf("%s\n", b);
}
int main()
{
    int i = 0;
    int len = 0;
    char a[][5] ={"은", "는","이","가","하는","하면","과","나는",""};
    char b[20] = {0x00,};
    char *c;

    c = *a;
    while(strlen(c) > 0)
    {
        printf("%s\n",c);
        invertStr(c);
        c=c+5;
    }
    invertStr("가나다라마바사");
}
