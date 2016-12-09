#include <sys/time.h>
#include <sys/types.h>
#include <unistd.h>
#include <string.h>
#include <stdio.h>
#include <string.h>

int getrandom(void *rand, size_t size)
{
    struct timeval tp;
    int i = 0;
    int ran;

    gettimeofday(&tp, NULL);
    srandom(tp.tv_sec | tp.tv_usec);

    while(1)
    {
        if (!(size / (4*(i+1)))) break;

        ran = random();
        memcpy(rand+(i*4), (void *)&ran, 4);
        i++;
    }
}
