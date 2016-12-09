#include <stdio.h>

int main(void)
{
	int b;
	for (b = 99; b >= 0; b--)
	{
		switch (b)
		{
		case 0:
			printf("No more bottles of beer on the wall, no more bottles of beer.\n");
			printf("Go to the store and buy some more, 99 bottles of beer on the wall.\n");
			break;
		case 1:
			printf("1 bottle of beer on the wall, 1 bottle of beer.\n");
			printf("Take one down and pass it around, no more bottles of beer on the wall\n");
			break;
		default:
			printf("%d bottles of beer on the wall, %d bottles of beer.\n", b, b);      
			printf("Take one down and pass it around, %d %s of beer on the wall.\n",b - 1,((b - 1) > 1)? "bottles" : "bottle");
			break;
		}
	}
	return 0;
}
