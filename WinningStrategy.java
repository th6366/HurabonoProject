import java.util.Random;
 
public class WinningStrategy implements Strategy {
     //이 클래스는 전판이 이기면 다음에도똑같은 것을 내는 전략을 취함
    private Random random; //랜덤변수 생성 목적
    private boolean won = false; //전판이 이겼는지 졌는지에 대한 필드
    private Hand prevHand; //전판에 내밀었던 손을 저장함
    public WinningStrategy(int seed) {
        random = new Random(seed);
    }
   
    @Override
    public Hand nextHand() {
    //이겼을때 (won의 값 true), 이기면 이전값을 반환 ,
    //졌을경우 (won의 값false), 새로운 난수값(0,1,2의 값중에 하나가 나옴)을 발생하여 해당 Hand인스턴스 얻어오기
        if(!won){
            prevHand = Hand.getHand(random.nextInt(3));
        }
        return prevHand; //이겼다면 이전의 같을 그대로 반환
    }
 
    @Override
    public void study(boolean win) {
        won = win;
       
    }
}
