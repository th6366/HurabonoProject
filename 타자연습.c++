#include <stdio.h>
#include <conio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <windows.h>

#pragma comment(lib, "winmm.lib") //mmsystem.h 리눅스에서 gcc로 할려고했는데 포기를...

#define TEXTLEN 512 //문자열 길이를 정의

char texts[][TEXTLEN] = {"Hello", "Android Ice Cream Sandwich!! "};

const int size = sizeof(texts) / sizeof(texts[0]); //배열에 저장된 행을 확인

int gettext(char *str)
{
	  gets(str); //키보드 입력을 받음
	  int len = (int) strlen(str); //입력받은 글자수를 확인
	  int onebyte = 0;
	  int twobyte = 0;

	  for (int i=0; i<len; i++) //for문을 사용하여 카운트를 샘
	  {
		if (str[i] < 0) twobyte++;  // 2바이트 한글
		else onebyte++;           // 1바이트 영문
	  }

	  return onebyte+(twobyte / 2);
}

//프로그램 실행
void run() {
	char user[TEXTLEN];
	int sum = 0; //더하기
	double sum2 = 0; // 표준편차
	int diff; //표준편차 더하기
	int avg;
	int arr[size] = {0}; //표준 편차를 위한 평균 타수 저장
	
	//위에서 정의한 texts의 배열을 for문을 이용
	for(int i = 0 ; i < size; i++) {
		int taja_count = 0; //입력한 타자수를 저장

		printf("%s\n", texts[i]); //texts 배열에 저장된 값을 출력

		unsigned int time = timeGetTime(); //시작 시간을 time에 복사
		taja_count = gettext(user); //사용자 입력 후 count를 가져옴
		time = timeGetTime() - time; //딜레이 시간에서 현재 시간을 뺌

		if(taja_count == 0) break; //count가 0이면 정지

		float n = (float) 60 / (time / 1000); //시작시간을 이용 1분 단위로 계산
		arr[i] =  (int) n * taja_count; //시간에서 입력한 타자수를 곱함
		sum += arr[i];

		printf("타자 속도는 평균 %d타\n", arr[i]);
	}

	avg = sum / size; //평균을 구함

	for(int i = 0; i < size; i++) {
		diff = arr[i] - avg;
		sum2 += diff * diff;
	}

	//최종 출력
	printf("최종: 평균 타수는 %d타, 표준 편차 %d\n", avg, (int)sqrt(sum2 / (size - 1)));
}


int main() {
	run(); //프로그램을 시작
	puts("프로그램 종료");
	getchar();
	return 0;
}
