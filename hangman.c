#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <conio.h>
#include <windows.h>
#include "word.h"
 
int i;
int ok=0, temp=0;
int life=10;
char alpha[27];
char ran[LEN]="";
char fil[LEN]="";
char ans;
 
void Print();
 
int main() {
 srand(time(NULL));
 strcpy(ran,ran2[rand()%WORD]);
 for(i=0;i<strlen(ran);i++) fil[i] = '_';
 while(1) {
  while(1) {
   system("cls");
   printf("life : %d\n",life);
   Print();
   printf("입력했던 알파벳 : ");
   for(i=0;i<strlen(alpha);i++) printf("%c ",alpha[i]);
   puts("");
   printf("알파벳 입력 : ",life);
   fflush(stdin);
   scanf("%c",&ans);
   for(i=0,temp=0;i<strlen(alpha);i++) if(alpha[i] == ans) temp=1;
   if(temp==1) { printf("입력했던 알파벳입니다.\n"); getch(); }
   else { alpha[strlen(alpha)]=ans; break; }
  }// while
  for(i=0,temp=0;i<strlen(ran);i++) {
   if(ran[i] == ans && fil[i] != ans) {
    fil[i] = ans;
    ok++; temp++;
   }
  }
  if(temp==0) life--;
  if(ok==strlen(ran)) break;
  if(life==0) { puts("Game Over"); printf("정답은 %s입니다.\n",ran); return 0; }
 }
 system("cls");
 Print();
 printf("<< Clear >>\n");
 
 return 0;
}
 
void Print() {
 printf("┏");
 for(i=0;i<strlen(ran);i++) printf("━");
 printf("┓\n");
 printf("┃");
 for(i=0;i<strlen(ran);i++) {
  printf("%c ",fil[i]);
 }
 printf("┃\n");
 printf("┗");
 for(i=0;i<strlen(ran);i++) printf("━");
 printf("┛\n");
}
[출처] [C언어] 행맨게임|작성자 블루캡
