import java.util.Random;
 
public class ProbStrategy implements Strategy{
 
    private Random random;
    private int prevHandValue=0;
    private int currentHandValue= 0;
    private int[][] history = {        
            {1,1,1,},
            {1,1,1,},
            {1,1,1,},  
    };
    //[0][1]이면 0이니까 이전에 주먹을 내고 이번에 가위를 냈을때의 과거의 승수
    //[0][2]이면 이전에 주먹을 내고 이번에보를 냈을때의 과거의 승수
   
    public ProbStrategy(int seed) {
        random = new Random(seed);
    }
    @Override
    public Hand nextHand() {
        int bet = random.nextInt(getSum(currentHandValue)); //랜덤변수 발생
       
         //이때 getSum을 쓰는데
        //history[0][0] 값이 3
        //history[0][1] 값이 5
        //history[0][2] 값이 7
        //이면 주먹:가위:보 = 3:5:7로계산에 의한 난수값을 얻는다
        //즉 3/15의 확률로 주먹이 나오고 5/15로 가위 7/15로 보자기가 나오게 된다.
       
        int handvalue = 0;
       
        //위에서 설명한 부분을 실제로고르는 부분 다음에 낼 손을 고른다.
        if(bet<history[currentHandValue][0]){
            handvalue=0;
        }else if(bet < history[currentHandValue][0] + history[currentHandValue][1]){
            handvalue=1;
        }else{
            handvalue = 2;
        }
        prevHandValue = currentHandValue; ///currentHandValue 저장되어있는 값을 prevHandValue 으로
        currentHandValue = handvalue; // 현재 얻은 값을 currentHandValue에 저장.
        return Hand.getHand(handvalue);
       
    }      
    public int getSum(int hv){ //현재줄을 매개변수로 받음
        int sum =0;
        for(int i=0; i<3;i++){ //0, 1, 2
            sum += history[hv][i]; //sum = sum + history[hv][i];       
        }
        //리턴값은 [주먹][가위] + [주먹][보자기] + [주먹][주먹] 에 대한 합 값
        return sum;
    }
 
    @Override
    public void study(boolean win) { //주먹 , 가위 , 보자기
        if(win){ //이기면 그때의 값이 올라감
            history[prevHandValue][currentHandValue] ++;
        }else{
              //지거나 비기면 다른 경우의 수가 올라감
            history[prevHandValue][(currentHandValue+1)%3] ++;
            history[prevHandValue][(currentHandValue+2)%3] ++;
        }
    }
 
}
