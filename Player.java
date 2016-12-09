import java.io.Serializable;
 
public class Player implements Serializable {
   
    private static final long serialVersionUID = 1L;
   
    private String name;
    private Strategy strategy;
    protected Hand currentHand;
   
    protected int hp=100;   //에너지
    protected int lv=0;     //레벨
    protected int scissorsPower = 5;  //가위파워
    protected int rockPower = 20;     //주먹파워
    protected int paperPower = 10;    //보 파워 
   
    protected int wincount;
    protected int losecount;
    protected int gamecount;
   
   
    public Player(String name) {
        this.name = name;
    }
    public Player(String name, Strategy strategy) {
        this(name,0,strategy);     
    }  
    public Player(String name, int lv, Strategy strategy) {    
        this.name = name;
        this.strategy = strategy;
        setlevel(lv);
    }
   
   
    /**
     * Player이름 얻기
     */
    public String getName() {
        return name;
    }  
   
    /**
     * Player에너지 회복
     */
    public void fullHp(){
        this.hp = 100;
    }
   
    /**
     * Player 다음손을 냄
     */
    public Hand nextHand(){
        currentHand = strategy.nextHand();
        return currentHand;
    }
   
    /**
     * Player 이겼을경우
     */
    public void win(){
        if(strategy!=null) strategy.study(true);
        wincount ++;
        gamecount++;
    }
   
    /**
     * Player 졌을경우
     */
    public void lose(){
        strategy.study(false);     
        losecount++;
        gamecount++;
    }
   
   
    /**
     * Player 졌을경우 ( 에너지 감소 )
     */
    public void lose(Player attacker){
        if(strategy!=null) strategy.study(false);      
        losecount++;
        gamecount++;   
       
        hpDown(attacker.power(attacker.currentHand)); //피 감소
    }
   
   
    /**
     * Player 비겼을경우
     */
    public void even(){
        gamecount++;
    }
 
    /**
     * 데미지에 따른 에너지 감소하는 메소드
     */
    public void hpDown(int damage){    
        hp-=damage;    
        if(hp<0) hp=0; //hp가 0보다 적어질경우 hp를 0으로 고정
       
        System.out.println("알림: "+name+"(이)가 "+damage+"만큼 데미지를 입었습니다.");
        System.out.println("=======================================================");
         
    }  
   
   
    /**
     * 레벨설정 메소드     *
     */
    public void setlevel(int lv){      
       
        if(lv > 2){ //레벨 증가 하고 2보다 크면 레벨을 다시 2로.
           this.lv=2;
        }else{
            this.lv = lv;
        }
           
        int rnd = (int)(Math.random()*4)+1; //1~4 , 능력치 랜덤 부여
        this.scissorsPower += (int)Math.round(scissorsPower*(rnd/10.0))*this.lv;
        this.rockPower += (int)Math.round(rockPower*(rnd/10.0))*this.lv;
        this.paperPower += (int)Math.round(paperPower*(rnd/10.0))*this.lv;
 
       
    }//levelUp()-----------------
   
   
   
    /**
     * 공격에 대한 공격력(데미지)수치 얻기*
     * @return 해당 공격에 대한 공격력은?
     */
    public int power(Hand h){
        int damage=0;
        //{"주먹","가위","보"};
        if("주먹".equals(h.toString())){
            damage = rockPower ;
        }else if("가위".equals(h.toString())){
            damage = scissorsPower;
        }else if("보".equals(h.toString())){
             damage = paperPower;
        }      
        return damage;
    }
   
   
   
    /**
     * 좌측용 데이터출력 라벨
     */
    public String toLeftView() {
        return "["+name+".lv"+(lv+1)+"] " + hp;
    }
   
    /**
     * 우측용 데이터출력 라벨
     * @return
     */
    public String toRightView() {
        return   hp+ " ["+name+".lv"+(lv+1)+"] ";
    }
   
    @Override
    public String toString() {
        return "[" + name + ":"+gamecount+"games, "+wincount+"win, " +losecount+"lose]";
    }
   
}
