import java.io.Serializable;
 
public class Hand implements Serializable{
 
    public static final int HANDVALUE_GUU = 0; //주먹
    public static final int HANDVALUE_CHO = 1; //가위
    public static final int HANDVALUE_PAA = 2; //보
   
    public static final Hand[] hand = {
       
        new Hand(HANDVALUE_GUU),
        new Hand(HANDVALUE_CHO),
        new Hand(HANDVALUE_PAA),
       
    };
   
    private static final String[] name ={"주먹","가위","보"};
   
    private int handvalue; //손의 값
    private Hand(int handvalue){
        this.handvalue = handvalue;
       
    }
    public static Hand getHand(int handvalue){ // 0, 1, 2
        return hand[handvalue];
    }
    public boolean isStongerThan(Hand h){ //이길경우 true
        return fight(h) == 1;
       
    }
    public boolean isWeakerThan(Hand h){ //질경우 true
        return fight(h) == -1;
    }
    private int fight(Hand h){
        if(this == h){ //무승무
            return 0;
        }else if((this.handvalue+1)%3==h.handvalue){ // 0 == 1, 1 == 2, 2 ==0
            return 1;
        }else{ //졌음
            return -1;
        }
    }
   
    public String toString(){      
        return name[handvalue];
    }
 
}
 
