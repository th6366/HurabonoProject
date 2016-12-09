import java.util.Scanner;
 
class UserPlayer extends Player{ //플레이어를 상속받아서 유저기능만 추가.
   
    private static final long serialVersionUID = 1L;
 
    //private int stageLevel; //스테이지 레벨 저장.
   
    //생성자
    public UserPlayer() {
         super("사용자");
    }
   
    public UserPlayer(String name) {
        super(name);
    }
   
   
    @Override
    public Hand nextHand() {  
          //가위 : 1 , 바위 : 2, 보 : 3
        Scanner scn = new Scanner(System.in);  
        boolean chk=true;
       
        currentHand = null;
       
        while(chk){
            //{"주먹","가위","보"};
            System.out.println("시작~ 가위! 바위! 보!! (주먹:1, 가위:2, 보:3, 종료:exit)");
            System.out.print("=>");
            String strR = scn.nextLine();
               
            if(strR.equals("1")||strR.equals("2")||strR.equals("3")){
               int intR = Integer.parseInt(strR)-1;
               currentHand = Hand.getHand(intR);
               chk=false;
            }else if(strR.equals("exit")){
               currentHand = null;
               chk=false;
            }else{
                System.out.println("[msg] : 잘못된 입력입니다. 다시 시작합니다!");
            }
        }
       
        return currentHand;    
    }//nextHand()---------------------------
   
   
 
    /**
     * 유저 레벨업, 레벨업시 랜덤으로 능력치 증가
     */
    public int levelUp(){      
            if(++lv > 2){ //레벨 증가 하고 2보다 크면 레벨을 다시 2로.
                lv=2;
            }else{ // 0(초기레벨), 1, 2
               
                int rnd = (int)(Math.random()*4)+1; //1~4
               
                int sPower =  (int)Math.round(5*(rnd/10.0));
                int rPower =  (int)Math.round(20*(rnd/10.0));
                int pPower =  (int)Math.round(10*(rnd/10.0));
               
                scissorsPower +=sPower;
                rockPower += rPower;
                paperPower += pPower;
               
                fullHp(); //HP회복
               
                System.out.println("=========================info===========================");
                System.out.println(getName() +"님의 레벨이 "+lv+"에서 "+(lv+1)+"로 레벨업이 되었습니다.");
                System.out.print("가위 공격력: "+sPower+" UP!  ");
                System.out.print("바위 공격력: "+rPower+" UP!  ");
                System.out.print("보 공격력: "+pPower+" UP!\n");
                System.out.println("========================================================");
               
            }//if--------
            return lv;
        }//levelUp()-----------------
