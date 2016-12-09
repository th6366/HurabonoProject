import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
 
public class GamePlay implements Serializable{
     
   
    private static final long serialVersionUID = 1L;
 
    List<Player> enemyList = new ArrayList<Player>(); //적을 담고 있는 리스트
   
    UserPlayer user; // 생성된 유저를 담을수있는 변수
    Player com; //현재 스테이지의 적.
    int stageLevel; //스테이지 레벨.    
   
    //생성자.
    public GamePlay() {      
        //몬스터 설정
        enemyList.add(new Player("도살자", new RandomStrategy(2000))); //0레벨 몬스터
        enemyList.add(new Player("벨리알",1, new WinningStrategy(2320))); //1레벨 몬스터
        enemyList.add(new Player("최종보스",2, new ProbStrategy(21000)));  //3레벨 몬스터
 
    }
   
   
    public void GameStart(){
       
        Scanner scn = new Scanner(System.in);
       
        while (true) {
            try {
                System.out.println("===================aaaa==================");
                System.out.println("       가위바위보 [대전 액션]");
                System.out.println("1. 새 게임 ");
                System.out.println("2. 게임이어서하기");
                System.out.println("3. 게임종료");
                System.out.println("===================aaaa==================");
                System.out.print("메뉴를 선택해주세요!\n:");
                int menuNum = scn.nextInt();
               
                if (menuNum==1) {
                    scn.nextLine();
                    System.out.print("게임에 사용할 사용자 이름을 입력해주세요!\n:");
                    String name = scn.nextLine();
                    user = new UserPlayer(name); //사용자 생성.
                    break;
                   
                   
                }else if(menuNum==2){
                    ObjectInputStream ois;
                   
                    try {
                        ois = new ObjectInputStream(new FileInputStream("data.txt"));
                        GamePlay load = (GamePlay)ois.readObject();
                        this.enemyList = load.enemyList;
                        this.user = load.user;
                        //this.com =    load.com;
                        this.stageLevel =load.stageLevel;
                       
                    } catch (FileNotFoundException e) {
                        System.out.println("예외:"+e);  
                        continue;
                       
                    } catch (IOException e) {
                    } catch (ClassNotFoundException e) {                      
                    }
                    System.out.println("Load.........완료!");
                    break;
                }else if(menuNum==3){
                    System.out.println("게임을 종료합니다....");
                    System.exit(0);
                }else{              
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("잘못된 값을 입력하셨습니다. ");
                scn.nextLine();
            }
        }//while-----------
    }//GameStart()----------
   
   
   
    /**
     * 현재 사용자와 컴퓨터의 에너지를 출력.
     */
    public void showpHp(){
        System.out.println("======================에너지바=========================");
        System.out.println("사용자 : "+user.toLeftView() +" VS "+com.toRightView() +": 컴퓨터");
        System.out.println("=======================================================");
    }
   
 
    /**
     * 게임 실행
     */
    public void play(){  
       
        GameStart();  //시작메뉴 호출  
       
        com = enemyList.get(stageLevel); //스테이지 몬스터 세팅!  
       
        while(!(user.hp<=0)){    //유저의 에너지가 0이면 반복문 종료
           
            Hand comHand = com.nextHand(); //컴퓨터 공격형태        
            showpHp(); //에너지바 표시
            //System.out.println("[개발자 힌트모드 가동 : "+comHand.toString()+"]");
            Hand userHand = user.nextHand(); //사용자 공격형태      
           
           
            if(userHand==null){
                System.out.println("게임을 저장후 종료합니다.");
                try {
                    ObjectOutputStream obs = new ObjectOutputStream(new FileOutputStream("data.txt"));
                    obs.writeObject(this);
                    //obs.flush();
                    obs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
                       
            System.out.println();
            System.out.println("==========================알림=========================");
            System.out.println("       사용자 : <<"+userHand.toString()  +">> VS <<" + comHand.toString()+">>> : 컴퓨터");
           
           
           
            if(comHand.isStongerThan(userHand)){
                //System.out.println("Winner:"+com);
                System.out.println("컴퓨터["+com.getName()+"](이)가 공격을합니다");
                com.win();
                //user.lose(comHand,com);
                user.lose(com);
            }else if(userHand.isStongerThan(comHand)){
               
                //System.out.println("Winner:"+user);
                System.out.println("사용자["+user.getName()+"](이)가 공격을합니다");
                //com.lose(userHand,user);
                com.lose(user);
                user.win();
            }else {
                //System.out.println("Even...");
                System.out.println("알림: 비겼습니다! ~ 다시!");  
                System.out.println("=======================================================");
                com.even();
                user.even();
            }
           
               
            if(com.hp<=0){    
                showpHp();
                stageLevel++;
                if(stageLevel<enemyList.size()){
                    user.levelUp();
                    System.out.println("You Win!");
                    System.out.println("스테이지"+stageLevel+" 클리어! 다음스테이지로 넘어갑니다.");
                    com =  enemyList.get(stageLevel);
                    //showpHp();
                }else{
                    System.out.println("You Win!");
                    System.out.println("모든 스테이지를 통과했습니다. Game Clear!!");
                    System.out.println("만든이 : @-@");
                    user.end();            
                }          
            }else if(user.hp<=0){
                showpHp();
                System.out.println("===You Lose!===");
                System.out.println("===GAME OVER===");
                user.end();
                break;
            }      
        }//while----------
           
    }//play() 메서드 -----------------
   
   
    //main()메서드
    public static void main(String[] args) {      
        new GamePlay().play(); //게임 실행.    
    }//main()------------
   
}//class GamePlay--------------------
