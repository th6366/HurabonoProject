import java.util.Random;
 
public class RandomStrategy implements Strategy  {
    //이 클래스는 단순히 난수를 발생하여 Hand를 반환하는 전략을 취함/
    private Random random;
   
    public RandomStrategy(int seed) {
        random = new Random(seed);
        //seed값을 초기값으로 하는 난수 발생
        //public Random(long seed)
        //Creates a new random number generator using a single long seed.
        //The seed is the initial value of the internal state of the pseudorandom number generator which is maintained by method next(int).
    }
    @Override
    public Hand nextHand() {
        //매회 새로운 난수를 생성하여 그에 맞는 Hand인스턴스를 얻어온다.
        Hand randomHand = Hand.getHand(random.nextInt(3));  //0,1,2    
        return randomHand;
    }
 
    @Override
    public void study(boolean win) {
        //구현 내용 없음.
    }
 
}
