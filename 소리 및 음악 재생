#include <stdio.h>
#include <windows.h>
#include <mmsystem.h>
#include <conio.h>
 
#pragma comment(lib, "winmm.lib")
 
#define SOUND_FILE_NAME ".\\res\\APPLAUSE.wav"
 
int main(int argc, char* argv[]){
    printf("아무 키나 입력하시면 소리 재생이 멈춥니다.\n");
    PlaySound(TEXT(SOUND_FILE_NAME), NULL, SND_FILENAME | SND_ASYNC | SND_LOOP);
    while (!_kbhit());
    PlaySound(NULL, 0, 0);
    return 0;
}
