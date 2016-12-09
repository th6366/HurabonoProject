#include <pthread.h>
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
 
// 쓰레드 함수 
// 1초를 기다린후 (매개변수*매개변수)를  리턴합니다
void *t_function(void *data)
{
    int num = *((int *)data);
    printf("num %d\n", num);
    sleep(1);
    num *= num;
    printf("쓰레드 함수 종료 합니다\n");
    return (void *)(num);  // warning 발생
}
 
int main()
{
    pthread_t p_thread;
    int thr_id;
    int result;
    int a = 200;
 
    thr_id = pthread_create(&p_thread, NULL, t_function, (void *)&a);
    if (thr_id < 0)
    {
        perror("thread create error : ");
        exit(0);
    }
    // 쓰레드 식별자 p_thread 가 종료되길 기다렸다가 
    // 종료후에 리턴값을 받아옵니다. 
    pthread_join(p_thread, (void *)&result);
    printf("thread join : %d\n", result);
 
    printf("main() 종료\n");
 
    return 0;
}
