import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import java.util.Vector;


public class W_Shooting {

	public static void main(String[] args) 
	{
		System.out.println("ストライクウィッチ-ズ Start!");
		W_Shooting_frame wsf=new W_Shooting_frame();
	}

}
class W_Shooting_frame extends Frame implements KeyListener, Runnable
{
	//기본 윈도우를 형성하는 프레임을 만든다
	//KeyListener : 키보드 입력 이벤트를 받는다
	//Runnable : 스레드를 가능하게 한다

	public final static int UP_PRESSED		=0x001;
	public final static int DOWN_PRESSED	=0x002;
	public final static int LEFT_PRESSED	=0x004;
	public final static int RIGHT_PRESSED	=0x008;
	public final static int FIRE_PRESSED	=0x010;
	
	GameScreen gamescreen;//Canvas 객체를 상속한 화면 묘화 메인 클래스

	Thread mainwork;//스레드 객체
	boolean roof=true;//스레드 루프 정보
	Random rnd = new Random();	 // 랜덤 선언

	//게임 제어를 위한 변수
	int status;//게임의 상태
	int cnt;//루프 제어용 컨트롤 변수
	int delay;//루프 딜레이. 1/1000초 단위.
	long pretime;//루프 간격을 조절하기 위한 시간 체크값
	int keybuff;//키 버퍼값

	//AudioClip[] aclip=new AudioClip[3];

	//게임용 변수
	int score;//점수
	int mylife;//남은 목숨
	int gamecnt;//게임 흐름 컨트롤
	int scrspeed=16;//스크롤 속도
	int level;//게임 레벨

	int myx,myy;//플레이어 위치. 화면 좌표계에 *100 된 상태.
	int myspeed;//플레이어 이동 속도
	int mydegree;//플레이어 이동 방향
		//보통 4방향키-8방향 조작계에서는 이동 방향을 각도로 관리하지 않지만 여기서는 장래 터치스크린 인터페이스로
		//이식될 것을 고려해 4방향키 조작계를 0, 45, 90, 135, 180, 225, 270, 315도 방향으로 조작하는 것으로 한다.
	int mywidth, myheight;//플레이어 캐릭터의 너비 높이
	int mymode=1;//플레이어 캐릭터의 상태 (0부터 순서대로 무적,등장(무적),온플레이,데미지,사망)
	int myimg;//플레이어 이미지
	int mycnt;
	boolean myshoot=false;//총알 발사가 눌리고 있는가
	int myshield;//실드 남은 수비량
	boolean my_inv=false;//키보드 반전

	int gScreenWidth=640;//게임 화면 너비
	int gScreenHeight=480;//게임 화면 높이

	Vector bullets=new Vector();//총알 관리. 총알의 갯수를 예상할 수 없기 때문에 가변적으로 관리한다.
	Vector enemies=new Vector();//적 캐릭터 관리.
	Vector effects=new Vector();//이펙트 관리
	Vector items=new Vector();//아이템 관리
	//가변 테이블을 사용한 관리는 처리속도에 악영향을 끼칠 수 있다.

	//속도를 위해서는 크기를 넉넉하게 잡은 테이블을 사용하는데, 소스가 지저분해지고, 불필요한 메모리를 낭비하게 되므로 적절한 것을 선택한다.
	//또, C 베이스 플랫폼으로 이식할 경우를 고려야 한다면 class나 Vector, Hashtable 같은 것은 이식하기 어려워지므로 가급적 피한다.
	
	W_Shooting_frame(){

		//기본적인 윈도우 정보 세팅. 게임과 직접적인 상관은 없이 게임 실행을 위한 창을 준비하는 과정.
		setIconImage(makeImage("./rsc/icon.png"));
		setBackground(new Color(0xffffff));//윈도우 기본 배경색 지정 (R=ff, G=ff, B=ff : 하얀색)
		setTitle("ストライクウィッチ-ズ Fan Game");//윈도우 이름 지정
		setLayout(null);//윈도우의 레이아웃을 프리로 설정
		setBounds(100,100,640,480);//윈도우의 시작 위치와 너비 높이 지정
		setResizable(false);//윈도우의 크기를 변경할 수 없음
		setVisible(true);//윈도우 표시
		
		addKeyListener(this);//키 입력 이벤트 리스너 활성화
		addWindowListener(new MyWindowAdapter());//윈도우의 닫기 버튼 활성화

		gamescreen=new GameScreen(this);//화면 묘화를 위한 캔버스 객체
		gamescreen.setBounds(0,0,gScreenWidth,gScreenHeight);
		add(gamescreen);//Canvas 객체를 프레임에 올린다

		systeminit();
		initialize();
	}

	public void systeminit(){//프로그램 초기화

		status=0;
		cnt=0;
		delay=17;// 17/1000초 = 58 (프레임/초)
		keybuff=0;

		mainwork=new Thread(this);
		mainwork.start();
	}
	public void initialize(){//게임 초기화

		Init_TITLE();
		gamescreen.repaint();
	}

	// 스레드 파트
	public void run(){
		try
		{
			while(roof){
				pretime=System.currentTimeMillis();

				gamescreen.repaint();//화면 리페인트
				process();//각종 처리
				keyprocess();//키 처리

				if(System.currentTimeMillis()-pretime<delay) Thread.sleep(delay-System.currentTimeMillis()+pretime);
					//게임 루프를 처리하는데 걸린 시간을 체크해서 딜레이값에서 차감하여 딜레이를 일정하게 유지한다.
					//루프 실행 시간이 딜레이 시간보다 크다면 게임 속도가 느려지게 된다.

				if(status!=4) cnt++;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// 키 이벤트 리스너 처리
	public void keyPressed(KeyEvent e) {
		//if(status==2&&(mymode==2||mymode==0)){
		if(status==2){
			switch(e.getKeyCode()){
			case KeyEvent.VK_SPACE:
				keybuff|=FIRE_PRESSED;
				break;
			case KeyEvent.VK_LEFT:
				keybuff|=LEFT_PRESSED;//멀티키의 누르기 처리
				break;
			case KeyEvent.VK_UP:
				keybuff|=UP_PRESSED;
				break;
			case KeyEvent.VK_RIGHT:
				keybuff|=RIGHT_PRESSED;
				break;
			case KeyEvent.VK_DOWN:
				keybuff|=DOWN_PRESSED;
				break;
			case KeyEvent.VK_1:
				if(myspeed>1) myspeed--;
				break;
			case KeyEvent.VK_2:
				if(myspeed<9) myspeed++;
				break;
			case KeyEvent.VK_3:
				if(status==2) status=4;
				break;
			/*case KeyEvent.VK_1:
				System.out.println("이펙트 테스트");
				Effect effect=new Effect(0, RAND(30,gScreenWidth-30)*100,RAND(30,gScreenHeight-30)*100, 0);
				effects.add(effect);
				break;*/
			default:
				break;
			}
		} else if(status!=2) keybuff=e.getKeyCode();
	}
	public void keyReleased(KeyEvent e) {
		//if(status==2&&(mymode==2||mymode==0)){
		//if(status==2){
			switch(e.getKeyCode()){
			case KeyEvent.VK_SPACE:
				keybuff&=~FIRE_PRESSED;
				myshoot=true;
				break;
			case KeyEvent.VK_LEFT:
				keybuff&=~LEFT_PRESSED;//멀티키의 떼기 처리
				break;
			case KeyEvent.VK_UP:
				keybuff&=~UP_PRESSED;
				break;
			case KeyEvent.VK_RIGHT:
				keybuff&=~RIGHT_PRESSED;
				break;
			case KeyEvent.VK_DOWN:
				keybuff&=~DOWN_PRESSED;
				break;
			}
		//}
		//PC 환경에서는 기본적으로 키보드의 반복입력을 지원하지만,
		//그렇지 않은 플랫폼에서는 키 버퍼값에 떼고 눌렀을 때마다 값을 변경해 리피트 여부를 제어한다.
	}
	public void keyTyped(KeyEvent e) {
	}

	// 각종 판단, 변수나 이벤트, CPU 관련 처리
	private void process(){
		switch(status){
		case 0://타이틀화면
			break;
		case 1://스타트
			process_MY();
			if(mymode==2) status=2;
			break;
		case 2://게임화면
			process_MY();
			process_ENEMY();
			process_BULLET();
			process_EFFECT();
			process_GAMEFLOW();
			process_ITEM();
			break;
		case 3://게임오버
			process_ENEMY();
			process_BULLET();
			process_GAMEFLOW();
			break;
		case 4://일시정지
			break;
		default:
			break;
		}
		if(status!=4) gamecnt++;
	}

	// 키 입력 처리
	// 키 이벤트에서 입력 처리를 할 경우, 이벤트 병목현상이 발생할 수 있으므로 이벤트에서는 키 버퍼만을 변경하고, 루프 내에서 버퍼값에 따른 처리를 한다.
	private void keyprocess(){
		switch(status){
		case 0://타이틀화면
			if(keybuff==KeyEvent.VK_SPACE) {
				Init_GAME();
				Init_MY();
				status=1;
			}
			break;
		case 2://게임화면
			if(mymode==2||mymode==0){
				switch(keybuff){
				case 0:
					mydegree=-1;
					myimg=0;
					break;
				case FIRE_PRESSED:
					mydegree=-1;
					myimg=6;
					break;
				case UP_PRESSED:
					mydegree=0;
					myimg=2;
					break;
				case UP_PRESSED|FIRE_PRESSED:
					mydegree=0;
					myimg=6;
					break;
				case LEFT_PRESSED:
					mydegree=90;
					myimg=4;
					break;
				case LEFT_PRESSED|FIRE_PRESSED:
					mydegree=90;
					myimg=6;
					break;
				case RIGHT_PRESSED:
					mydegree=270;
					myimg=2;
					break;
				case RIGHT_PRESSED|FIRE_PRESSED:
					mydegree=270;
					myimg=6;
					break;
				case UP_PRESSED|LEFT_PRESSED:
					mydegree=45;
					myimg=4;
					break;
				case UP_PRESSED|LEFT_PRESSED|FIRE_PRESSED:
					mydegree=45;
					myimg=6;
					break;
				case UP_PRESSED|RIGHT_PRESSED:
					mydegree=315;
					myimg=2;
					break;
				case UP_PRESSED|RIGHT_PRESSED|FIRE_PRESSED:
					mydegree=315;
					myimg=6;
					break;
				case DOWN_PRESSED:
					mydegree=180;
					myimg=2;
					break;
				case DOWN_PRESSED|FIRE_PRESSED:
					mydegree=180;
					myimg=6;
					break;
				case DOWN_PRESSED|LEFT_PRESSED:
					mydegree=135;
					myimg=4;
					break;
				case DOWN_PRESSED|LEFT_PRESSED|FIRE_PRESSED:
					mydegree=135;
					myimg=6;
					break;
				case DOWN_PRESSED|RIGHT_PRESSED:
					mydegree=225;
					myimg=2;
					break;
				case DOWN_PRESSED|RIGHT_PRESSED|FIRE_PRESSED:
					mydegree=225;
					myimg=6;
					break;
				default:
					//System.out.println(""+keybuff);
					keybuff=0;
					mydegree=-1;
					myimg=0;
					break;
				}
			}
			break;
		case 3:
			if(gamecnt++>=200&&keybuff==KeyEvent.VK_SPACE){
				Init_TITLE();
				status=0;
				keybuff=0;
			}
			break;
		case 4:
			if(gamecnt++>=200&&keybuff==KeyEvent.VK_3) status=2;
			break;
		default:
			break;
		}
	}


	//서브루틴 일람
	public void Init_TITLE(){
		int i;
		/*gamescreen.bg=null;
		gamescreen.bg_f=null;
		for(i=0;i<4;i++) gamescreen.cloud[i]=null;
		for(i=0;i<4;i++) gamescreen.bullet[i]=null;
		gamescreen.enemy[0]=null;
		gamescreen.explo=null;
		gamescreen.item=null;
		gamescreen._start=null;
		gamescreen._over=null;
		System.gc();*/

		gamescreen.title=makeImage("./rsc/title.png");
		gamescreen.title_key=makeImage("./rsc/pushspace.png");

		//aclip[0]=myaudio.getClip("./snd/bgm_0.au");
		//aclip[0].loop();
	}
	public void Init_GAME(){
		int i;
		/*gamescreen.title=null;
		gamescreen.title_key=null;
		System.gc();*/

		gamescreen.bg=makeImage("./rsc/구름.JPG");//bg.png
		gamescreen.bgFlip=makeImage("./rsc/cloud_flip.jpg");//2013-10
		gamescreen.bg_f=makeImage("./rsc/bg_f.png");
		for(i=0;i<4;i++) gamescreen.cloud[i]=makeImage("./rsc/cloud"+i+".png");
		for(i=0;i<4;i++) gamescreen.bullet[i]=makeImage("./rsc/game/bullet_"+i+".png");
		gamescreen.enemy[0]=makeImage("./rsc/game/enemy0.png");
		gamescreen.explo=makeImage("./rsc/game/explode.png");
		gamescreen.item[0]=makeImage("./rsc/game/item0.png");
		gamescreen.item[1]=makeImage("./rsc/game/item1.png");
		gamescreen.item[2]=makeImage("./rsc/game/item2.png");//아이템 추가
		gamescreen._start=makeImage("./rsc/game/start.png");
		gamescreen._over=makeImage("./rsc/game/gameover.png");
		gamescreen.shield=makeImage("./rsc/game/shield.png");
		gamescreen.enemy[1]=makeImage("./rsc/game/enemy1.png");//보스 추가
		gamescreen.enemy[2]=makeImage("./rsc/game/enemy2.png");//위치 네우로이
		
		gamescreen.num=makeImage("./rsc/gnum.png");//2013-10
		gamescreen.uiUp=makeImage("./rsc/ui_up.png");//2013-10
		
		keybuff=0;
		bullets.clear();
		enemies.clear();
		effects.clear();
		items.clear();
		level=0;
		gamecnt=0;
	}
	public void Init_MY(){
		for(int i=0;i<9;i++){
			if(i<10)
				gamescreen.chr[i]=makeImage("./rsc/player/my_0"+i+".png");
			else
				gamescreen.chr[i]=makeImage("./rsc/player/my_"+i+".png");
		}
		Init_MYDATA();
	}
	public void Init_MYDATA(){
		score=0;
		myx=0;
		myy=23000;
		myspeed=4;
		mydegree=-1;
		//mywidth, myheight;//플레이어 캐릭터의 너비 높이
		mymode=1;
		myimg=2;
		mycnt=0;
		mylife=3;
		keybuff=0;
	}
	public void process_MY(){
		Bullet shoot;
		switch(mymode){
		case 1:
			myx+=200;
			if(myx>20000) mymode=2;
			break;
		case 0:
			if(mycnt--==0) {
				mymode=2;
				myimg=0;
			}
		case 2:
			if(mydegree!=-1&&my_inv) mydegree=(mydegree+180)%360;
			if(mydegree>-1) {
				myx-=(myspeed*Math.sin(Math.toRadians(mydegree))*100);
				myy-=(myspeed*Math.cos(Math.toRadians(mydegree))*100);
			}
			if(myimg==6) {
				myx-=20;
				if(cnt%4==0||myshoot){
					myshoot=false;
					shoot=new Bullet(myx+2500, myy+1500, 0, 0, RAND(245,265), 8);
					bullets.add(shoot);
					shoot=new Bullet(myx+2500, myy+1500, 0, 0, RAND(268,272), 9);
					bullets.add(shoot);
					shoot=new Bullet(myx+2500, myy+1500, 0, 0, RAND(275,295), 8);
					bullets.add(shoot);
				}
				//8myy+=70;
			}
			break;
		case 3:
			//keybuff=0;
			myimg=8;
			if(mycnt--==0) {
				mymode=0;
				mycnt=50;
			}
			break;
		}
		if(myx<2000) myx=2000;
		if(myx>62000) myx=62000;
		if(myy<3000) myy=3000;
		if(myy>45000) myy=45000;
	}
	public void process_ENEMY(){
		int i;
		Enemy buff;
		for(i=0;i<enemies.size();i++){
			buff=(Enemy)(enemies.elementAt(i));
			if(!buff.move()) enemies.remove(i);
		}
	}
	public void process_BULLET(){
		Bullet buff;
		Enemy ebuff;
		Effect expl;
		int i,j, dist;
		for(i=0;i<bullets.size();i++){
			buff=(Bullet)(bullets.elementAt(i));
			buff.move();
			if(buff.dis.x<10||buff.dis.x>gScreenWidth+10||buff.dis.y<10||buff.dis.y>gScreenHeight+10) {
				bullets.remove(i);//화면 밖으로 나가면 총알 제거
				continue;
			}
			if(buff.from==0) {//플레이어가 쏜 총알이 적에게 명중 판정
				for(j=0;j<enemies.size();j++){
					ebuff=(Enemy)(enemies.elementAt(j));
					dist=GetDistance(buff.dis.x,buff.dis.y, ebuff.dis.x,ebuff.dis.y);
					//if(dist<1500) {//중간점 거리가 명중 판정이 가능한 범위에 왔을 때
					if(dist<ebuff.hitrange) {//중간점 거리가 명중 판정이 가능한 범위에 왔을 때 - hitrange : 적 캐릭터마다 그림에 따라 명중판정되는 범위가 다르다
						if(ebuff.life--<=0){//적 라이프 감소
							if(ebuff.kind==1){
								if(gamecnt<2100) gamecnt=2100;
							}
							enemies.remove(j);//적 캐릭터 소거
							expl=new Effect(0, ebuff.pos.x, buff.pos.y, 0);
							effects.add(expl);//폭발 이펙트 추가
							//Item tem=new Item(ebuff.pos.x, buff.pos.y, RAND(1,(level+1)*20)/((level+1)*20));//난수 결과가 최대값일 때만 생성되는 아이템이 1이 된다
							int itemKind=RAND(1,100);
							Item tem;
							if(itemKind<=70)
								tem=new Item(ebuff.pos.x, buff.pos.y,0);
							else if(itemKind<=95)
								tem=new Item(ebuff.pos.x, buff.pos.y,2);
							else
								tem=new Item(ebuff.pos.x, buff.pos.y,1);
							items.add(tem);
						}
						//expl=new Effect(0, ebuff.pos.x, buff.pos.y, 0);
						expl=new Effect(0, buff.pos.x, buff.pos.y, 0);
						effects.add(expl);
						score++;//점수 추가
						bullets.remove(i);//총알 소거
						break;//총알이 소거되었으므로 루프 아웃
					}
				}
			} else {//적이 쏜 총알이 플레이어에게 명중 판정
				if(mymode!=2) continue;
				dist=GetDistance(myx/100,myy/100, buff.dis.x,buff.dis.y);
				if(dist<500) {
					if(myshield==0){
						mymode=3;
						mycnt=30;
						bullets.remove(i);
						expl=new Effect(0, myx-2000, myy, 0);
						effects.add(expl);
						if(--mylife<=0) {
							status=3;
							gamecnt=0;
						}
					} else {//실드가 있을 경우
						myshield--;
						bullets.remove(i);
					}
				}
			}
		}
	}
	public void process_EFFECT(){
		int i;
		Effect buff;
		for(i=0;i<effects.size();i++){
			buff=(Effect)(effects.elementAt(i));
			if(cnt%3==0) buff.cnt--;
			if(buff.cnt==0) effects.remove(i);
		}
	}
	public void process_GAMEFLOW(){
		int control=0;
		int newy=0, mode=0;
		// 보스 관련 수정사항 추가
		if(gamescreen.boss){
			// 보스가 생성되어 있는 상황의 처리
			if(level>1){// 게임 레벨이 2 이상이면, 보스전 도중에 소형 캐릭터들이 지원기로 나온다
				//지원기 등장 시나리오
				// : 게임카운트(gamecnt) 0~200 : 지원기 없음
				// : 게임카운트(gamecnt) 801~1000 : 지원기 60카운트 단위로 등장
				// : 게임카운트(gamecnt) 1601~2199 : 지원기 30카운터 단위로 등장
				if(800<gamecnt&&gamecnt<1000){// 보스가 등장하고 시간이 좀 지나서 소형 캐릭터들이 덤벼오기 시작한다
					if(gamecnt%60==0) {
						newy=RAND(30,gScreenHeight-30)*100;
						if(newy<24000) mode=0; else mode=1;
						Enemy en=new Enemy(this, 0, gScreenWidth*100, newy, 0,mode);
						enemies.add(en);
					}
				}else
				if(1600<gamecnt&&gamecnt<2200){// 보스전이 후반부에 들어서면서 소형 지원기들의 공격이 거세진다
					if(gamecnt%30==0) {
						Enemy en;
						newy=RAND(30,gScreenHeight-30)*100;
						if(newy<24000) mode=0; else mode=1;
						if(level>1&&RAND(1,100)<level*10)
							en=new Enemy(this, 2, gScreenWidth*100, newy, 2,0);
						else
							en=new Enemy(this, 0, gScreenWidth*100, newy, 0,mode);
						enemies.add(en);
					}
				}
			}
			if(gamecnt>2210){// 보스전 타임 아웃으로 보스전을 종료한다
				gamescreen.boss=false;
				gamecnt=0;
				System.out.println("보스 타임아웃");
			}
		}else{
			if(gamecnt<500) control=1;
			else if(gamecnt<1000) control=2;
			else if(gamecnt<1300) control=0;
			else if(gamecnt<1700) control=1;
			else if(gamecnt<2000) control=2;
			else if(gamecnt<2400) control=3;
			else {
				// 기존에 레벨만 올려주던 부분에서, 레벨을 올려주면서 보스 캐릭터를 등장시킨다
				System.out.println("보스 등장");
				gamescreen.boss=true;
				Enemy en=new Enemy(this, 1, gScreenWidth*100, 24000, 1, 0);// img 값이 1, kind 값이 1
				enemies.add(en);
				gamecnt=0;
				level++;
			}
			if(control>0) {
				newy=RAND(30,gScreenHeight-30)*100;
				if(newy<24000) mode=0; else mode=1;
			}
			Enemy en;
			switch(control){
			case 1:
				if(gamecnt%90==0) {
					if(RAND(1,3)!=3&&level>0)
						en=new Enemy(this, 2, gScreenWidth*100, newy, 2,mode);
					else
						en=new Enemy(this, 0, gScreenWidth*100, newy, 0,mode);
					enemies.add(en);
				}
				break;
			case 2:
				if(gamecnt%50==0) {
					if(RAND(1,3)!=3&&level>0)
						en=new Enemy(this, 2, gScreenWidth*100, newy, 2,mode);
					else
						en=new Enemy(this, 0, gScreenWidth*100, newy, 0,mode);
					enemies.add(en);
				}
				break;
			case 3:
				if(gamecnt%20==0) {
					if(RAND(1,3)!=3&&level>0)
						en=new Enemy(this, 2, gScreenWidth*100, newy, 2,mode);
					else
						en=new Enemy(this, 0, gScreenWidth*100, newy, 0,mode);
					enemies.add(en);
				}
				break;
			}
		}
	}
	public void process_ITEM(){
		int i, dist;
		Item buff;
		for(i=0;i<items.size();i++){
			buff=(Item)(items.elementAt(i));
			dist=GetDistance(myx/100,myy/100, buff.dis.x,buff.dis.y);
			if(dist<1000) {//아이템 획득
				switch(buff.kind){
				case 0://일반 득점
					score+=100;
					break;
				case 1://실드
					myshield=5;
					break;
				case 2://전멸 아이템
					//Enemy ebuff;
					//Effect expl;
					
					//적 전멸 효과
					int j=enemies.size();
					for(int k=0;k<j;k++){
						Enemy ebuff=(Enemy)(enemies.elementAt(k));
						if(ebuff==null) continue;//만일 해당 인덱스에 적 캐릭터가 생성되어있지 않을 경우를 대비
						if(ebuff.kind==1) {//해당 인덱스에 할당된 적 캐릭터가 보스 캐릭터일 경우는 전멸에 해당하지 않고 HP만 반으로 줄인다. 1 이하라면 1.
							score+=300;
							ebuff.life=ebuff.life/2;
							if(ebuff.life<=1) ebuff.life=1;
							continue;
						}
						Effect expl=new Effect(0, ebuff.pos.x,ebuff.pos.y, 0);
						effects.add(expl);//폭발 이펙트 추가
						ebuff.pos.x=-10000;//다음 처리에서 소거될 수 있도록
						score+=50;
						//enemies.remove(ebuff);//적 캐릭터 소거
					}
					
					//적 총알 전부 소거
					j=bullets.size();
					for(int k=0;k<j;k++){
						Bullet bbuff=(Bullet)(bullets.elementAt(k));
						if(bbuff.from!=0) {
							bbuff.pos.x=-10000;
							score++;
						}
						//bullets.remove(bbuff);
					}
					break;
				}
				items.remove(i);
			} else
				if(buff.move()) items.remove(i);
		}
	}

	public Image makeImage(String furl){
		Image img;
		Toolkit tk=Toolkit.getDefaultToolkit();
		img=tk.getImage(furl);
		try {
			//여기부터
			MediaTracker mt = new MediaTracker(this);
			mt.addImage(img, 0);
			mt.waitForID(0);
			//여기까지, getImage로 읽어들인 이미지가 로딩이 완료됐는지 확인하는 부분
		} catch (Exception ee) {
			ee.printStackTrace();
			return null;
		}	
		return img;
	}
	public int GetDistance(int x1,int y1,int x2,int y2){
		return Math.abs((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1));
	}
	public int RAND(int startnum, int endnum) //랜덤범위(startnum부터 ramdom까지), 랜덤값이 적용될 변수.
	{
		int a, b;
		if(startnum<endnum)
			b = endnum - startnum; //b는 실제 난수 발생 폭
		else
			b = startnum - endnum;
		a = Math.abs(rnd.nextInt()%(b+1));
		return (a+startnum);
	}
	int getAngle(int sx, int sy, int dx, int dy){
		int vx=dx-sx;
		int vy=dy-sy;
		double rad=Math.atan2(vx,vy);
		int degree=(int)((rad*180)/Math.PI);
		return (degree+180);
	}

	public boolean readGameFlow(String fname){
		String buff;
		try
		{
			BufferedReader fin=new BufferedReader(new FileReader(fname));
			if((buff=fin.readLine())!=null) {
				System.out.println(Integer.parseInt(buff));
			}
			fin.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
}

class GameScreen extends Canvas
{
	//실제 게임의 메인 제어를 행하는 클래스
	//가급적 화면 출력에서는 게임에서 공통으로 쓰이는 변수값의 변경 등을 행하지 않는 것이 좋다.
	W_Shooting_frame main;
	int cnt, gamecnt;

	//화면 그리기용 변수
	int x,y;//플레이어 캐릭터의 좌표

	Image dblbuff;//더블버퍼링용 백버퍼
	Graphics gc;//더블버퍼링용 그래픽 컨텍스트

	Image bg,bg_f, bgFlip;//배경화면 //2013-10
	Image cloud[]=new Image[5];//구름
	Image title, title_key;//타이틀화면

	Image chr[]=new Image[9];//플레이어 캐릭터
	Image enemy[]=new Image[5];//적 캐릭터
	Image bullet[]=new Image[5];//총알
	Image explo;//폭염
	Image item[]=new Image[3];//아이템
	
	Image num;//2013-10
	Image uiUp;//2013-10

	Image _start;//시작로고
	Image _over;//게임오버로고
	Image shield;//실드

	Font font;

	int v[]={-2,-1,0,1,2,1,0,-1};
	int v2[]={-8,-7,-6,-5,-4,-3,-2,-1,0,1,2,3,4,5,6,7,8,7,6,5,4,3,2,1,0,-1,-2,-3,-4,-5,-6,-7};
	int step=0;

	boolean boss=false;//보스관련 추가

	GameScreen(W_Shooting_frame main){
		this.main=main;
		font=new Font("Default",Font.PLAIN,9);
	}

	public void paint(Graphics g){
		if(gc==null) {
			dblbuff=createImage(main.gScreenWidth,main.gScreenHeight);//더블 버퍼링용 오프스크린 버퍼 생성. 필히 paint 함수 내에서 해 줘야 한다. 그렇지 않으면 null이 반환된다.
			if(dblbuff==null) System.out.println("오프스크린 버퍼 생성 실패");
			else gc=dblbuff.getGraphics();//오프스크린 버퍼에 그리기 위한 그래픽 컨텍스트 획득
			return;
		}
		update(g);
	}
	public void update(Graphics g){//화면 깜박거림을 줄이기 위해, paint에서 화면을 바로 묘화하지 않고 update 메소드를 호출하게 한다.
		cnt=main.cnt;
		gamecnt=main.gamecnt;
		if(gc==null) return;
		dblpaint();//오프스크린 버퍼에 그리기
		g.drawImage(dblbuff,0,0,this);//오프스크린 버퍼를 메인화면에 그린다.
	}
	public void dblpaint(){
		//실제 그리는 동작은 이 함수에서 모두 행한다.
		switch(main.status){
		case 0://타이틀화면
			Draw_TITLE();
			gc.setColor(new Color(0));
			gc.drawString("Education ver.", 10,40);
			break;
		case 1://게임 스타트
			Draw_BG();
			Draw_MY();
			Draw_BG2();
			drawImageAnc(_start, 0,270, 3);
			break;
		case 2://게임화면
		case 4://일시정지
			Draw_BG();
			Draw_MY();
			Draw_ENEMY();
			Draw_BULLET();
			Draw_EFFECT();
			Draw_ITEM();
			Draw_BG2();
			Draw_UI();
			break;
		case 3://게임오버
			Draw_BG();
			Draw_ENEMY();
			Draw_BULLET();
			Draw_BG2();
			drawImageAnc(_over, 320,240, 4);
			Draw_UI();
			break;
		default:
			break;
		}
	}

	public void Draw_TITLE(){
		gc.drawImage(title,0,0,this);
		if(cnt%20<10) gc.drawImage(title_key, 320-201,370, this);
	}
	public void Draw_BG(){
		int i;
		
		//원경을 움직이기 위한 추가 코드
		int step1 = (cnt%2560)/2;//2013-10
		step1=step1>=640?step1-1280:step1;//2013-10
		int step2 = step1>=0?640-step1:-(640+step1);//2013-10
		gc.drawImage(bg,0-step1,0,this);//2013-10
		gc.drawImage(bgFlip,step2,0,this);//2013-10
		
		for(i=0;i<12;i++) gc.drawImage(cloud[3], i*64-((cnt/1)%128), 370, this);//2013-10 : 원경이 움직이게 되면서 먼 곳의 작은 구름도 이동 속도를 조금 변경
		for(i=-1;i<14;i++) gc.drawImage(cloud[2], i*64-(cnt%128)*2, 395, this);
	}
	public void Draw_BG2(){
		int i;
		step=(cnt%(bg_f.getWidth(this)/main.scrspeed))*main.scrspeed;
		gc.drawImage(bg_f,0-step,540-bg_f.getHeight(this)+v[(cnt/20)%8]*2,this);
		//System.out.println("요기"+step);
		if(step>=bg_f.getWidth(this)-main.gScreenWidth) {
			gc.drawImage(bg_f,0-step+bg_f.getWidth(this),540-bg_f.getHeight(this)+v[(cnt/20)%8]*2,this);
		}
		for(i=-1;i<14;i++) gc.drawImage(cloud[0], i*128-(cnt%128)*8, 435, this);
	}
	public void Draw_MY(){
		int myx,myy;
		myx=main.myx/100;
		myy=main.myy/100;
		switch(main.mymode){
		case 0://무적
		case 1://무적이면서 등장
			if(main.cnt%20<10) drawImageAnc(chr[2+(main.cnt/5)%2], myx, myy, 4);
			break;
		case 2://온플레이
			if(main.myimg==6) drawImageAnc(chr[main.myimg+(main.cnt/3)%2], myx, myy, 4);
			else if(main.myimg!=8) drawImageAnc(chr[main.myimg+(main.cnt/5)%2], myx, myy, 4);
			else if(main.myimg==8) drawImageAnc(chr[main.myimg], myx, myy, 4);
			break;
		case 3://데미지
			if(main.cnt%6<3) drawImageAnc(chr[8], myx, myy, 4);
			break;
		}
		if(main.myshield>2) drawImageAnc(shield, (int)(Math.sin(Math.toRadians((cnt%72)*5))*16+myx), (int)(Math.cos(Math.toRadians((cnt%72)*5))*16+myy), (main.cnt/6%7)*64,0, 64,64, 4);//실드 라이프가 3 이상
		else if(main.myshield>0&&main.cnt%4<2) drawImageAnc(shield, (int)(Math.sin(Math.toRadians((cnt%72)*5))*16+myx), (int)(Math.cos(Math.toRadians((cnt%72)*5))*16+myy), (main.cnt/6%7)*64,0, 64,64, 4);//실드 라이프가 1, 2면 점멸
	}
	public void Draw_ENEMY(){
		int i;
		Enemy buff;
		for(i=0;i<main.enemies.size();i++){
			buff=(Enemy)(main.enemies.elementAt(i));
			switch(buff.img){
			case 0:
				drawImageAnc(enemy[0], buff.dis.x, buff.dis.y, ((cnt/8)%7)*36,0, 36,36, 4);
				break;
			case 1:
				drawImageAnc(enemy[1], buff.dis.x, buff.dis.y, 4);//보스 출력
				break;
			case 2://위치 네우로이
				switch(buff.mode){
				case 0://왼쪽에서 오른쪽으로 등장
				case 1://제자리에서 잠시 정지
				case 2://아래로 이동
				case 3://위로 이동
					drawImageAnc(enemy[2], buff.dis.x, buff.dis.y, 0,0,36,50,4);
					break;
				case 5://뒤로 돌아 퇴장
					drawImageAnc(enemy[2], buff.dis.x, buff.dis.y, 72,0,36,50,4);
					break;
				case 4://정지해서 손을 앞으로 내밀고 수평으로 총알 발사
					drawImageAnc(enemy[2], buff.dis.x, buff.dis.y, 36,0,36,50,4);
					break;
				}
			default:
				break;
			}
		}
	}
	public void Draw_BULLET(){
		Bullet buff;
		int i;
		for(i=0;i<main.bullets.size();i++){
			buff=(Bullet)(main.bullets.elementAt(i));
			switch(buff.img_num){
			case 0:
			case 1:
			case 2:
			case 3:
				drawImageAnc(bullet[buff.img_num], buff.dis.x-6,buff.dis.y-6, 4);
				break;
			}
		}
	}
	public void Draw_EFFECT(){
		int i;
		Effect buff;
		for(i=0;i<main.effects.size();i++){
			buff=(Effect)(main.effects.elementAt(i));
			drawImageAnc(explo, buff.dis.x, buff.dis.y, ((16-buff.cnt)%4)*64,((16-buff.cnt)/4)*64,64,64, 4);
		}
	}
	public void Draw_ITEM(){
		int i;
		Item buff;
		for(i=0;i<main.items.size();i++){
			buff=(Item)(main.items.elementAt(i));
			drawImageAnc(item[buff.kind], buff.dis.x, buff.dis.y, ((main.cnt/4)%7)*36,0, 36,36, 4);
		}
	}
	public void Draw_UI(){
		//String str1="SCORE "+main.score+"   LIFE "+main.mylife+"   SPEED "+main.myspeed+"  LEVEL "+(main.level+1);//2013-10
		String str2="[1] Speed down   [2] Speed up   [3] Pause";
//		gc.setColor(new Color(0));
//		gc.drawString(str1, 9,40);
//		gc.drawString(str1, 11,40);
//		gc.drawString(str1, 10,39);
//		gc.drawString(str1, 10,41);
//		gc.setColor(new Color(0xffffff));
//		gc.drawString(str1, 10,40);

		gc.setColor(new Color(0));
		gc.drawString(str2, 9,main.gScreenHeight-10);
		gc.drawString(str2, 11,main.gScreenHeight-10);
		gc.drawString(str2, 10,main.gScreenHeight-11);
		gc.drawString(str2, 10,main.gScreenHeight-9);
		gc.setColor(new Color(0xffffff));
		gc.drawString(str2, 10,main.gScreenHeight-10);
		
		//그래피컬 UI를 위한 추가 코드
		gc.drawImage(uiUp, 16, 25, this);//2013-10
		
		drawImageNum(num, 320, 40, main.score, 8);//2013-10
		drawImageNum(num, 52, 40, main.mylife, 2);//2013-10
		drawImageNum(num, 576, 40, main.level, 2);//2013-10
		
	}
	
	//2013-10
	public void drawImageNum(Image img, int x, int y, int value, int digit){
		//숫자를 이미지로 변환해 보여준다
		
		int width = img.getWidth(this)/10;
		int height = img.getHeight(this);
		
		String valueStr = String.valueOf(value);
		if(valueStr.length()<digit)
			valueStr = "0000000000".substring(0, digit-valueStr.length()) + valueStr;
		int _xx = x-valueStr.length()*width/2;
		for(int i=0;i<valueStr.length();i++)
			drawImageAnc(num, _xx+i*width, y, (valueStr.charAt(i)-'0')*width, 0, width,height, 0);

	}
	//2013-10
	
	public void drawImageAnc(Image img, int x, int y, int anc){
		//앵커값을 참조해 이미지 출력 위치를 보정한다.
		//예) anc==0 : 좌상단이 기준, anc==4 : 이미지 중앙이 기준
		int imgw, imgh;
		imgw=img.getWidth(this);
		imgh=img.getHeight(this);
		x=x-(anc%3)*(imgw/2);
		y=y-(anc/3)*(imgh/2);
		
		gc.drawImage(img, x,y, this);
	}
	public void drawImageAnc(Image img, int x, int y, int sx,int sy, int wd,int ht, int anc){//sx,sy부터 wd,ht만큼 클리핑해서 그린다.
		if(x<0) {
			wd+=x;
			sx-=x;
			x=0;
		}
		if(y<0) {
			ht+=y;
			sy-=y;
			y=0;
		}
		if(wd<0||ht<0) return;
		x=x-(anc%3)*(wd/2);
		y=y-(anc/3)*(ht/2);
		gc.setClip(x, y, wd, ht);
		gc.drawImage(img, x-sx, y-sy, this);
		gc.setClip(0,0, main.gScreenWidth+10,main.gScreenHeight+30);
	}


}
class Bullet
{
	// 게임에 등장하는 총알을 처리하기 위한 클래스
	// 메모리 효율을 위해서는 총알에 관한 최소한의 정보만 담는 것이 좋지만, 처리 샘플을 위해 간단한 자체 처리 루틴을 포함한다.
	Point dis;//총알의 표시 좌표. 실제 좌표보다 *100 상태이다.
	Point pos;//총알의 계산 좌표. 실제 좌표보다 *100 상태이다.
	Point _pos;//총알의 직전 좌표
	int degree;//총알의 진행 방향 (각도)
		//총알의 진행 방향은 x, y 증가량으로도 표시 가능하다. 하지만 그 경우 정밀한 탄막 구현이 어려워진다.
	int speed;//총알의 이동 속도
	int img_num;//총알의 이미지 번호
	int from;//총알을 누가 발사했는가
	Bullet(int x, int y, int img_num, int from, int degree, int speed){
		pos=new Point(x,y);
		dis=new Point(x/100,y/100);
		_pos=new Point(x,y);
		this.img_num=img_num;
		this.from=from;
		this.degree=degree;
		this.speed=speed;
	}
	public void move(){
		_pos=pos;//이전 좌표 보존
		pos.x-=(speed*Math.sin(Math.toRadians(degree))*100);
		pos.y-=(speed*Math.cos(Math.toRadians(degree))*100);
		dis.x=pos.x/100;
		dis.y=pos.y/100;
		//if(pos.x<0||pos.y>gScreenWidth*100||pos.y<0||pos.y>gScreenHeight*100) ebullet[i].pic=255;
	}
}
class Enemy
{
	//게임에 등장하는 적 캐릭터 관리 클래스
	W_Shooting_frame main;
	Point pos;
	Point _pos;
	Point dis;
	int img;
	int kind;
	int life;
	int mode;
	int cnt;
	int shoottype;
	int hitrange;//다양한 크기의 적 캐릭터 수용을 위해 명중 판정 범위를 추가한다.
	Bullet bul;
	Enemy(W_Shooting_frame main, int img, int x, int y, int kind, int mode){
		this.main=main;
		pos=new Point(x,y);
		_pos=new Point(x,y);
		dis=new Point(x/100,y/100);
		this.kind=kind;
		this.img=img;
		this.mode=mode;
		life=6+main.RAND(0,5)*main.level;//게임 레벨에 따라 라이프와 탄을 쏘는 시간이 짧아진다
		cnt=main.RAND(main.level*5,80);
		shoottype=main.RAND(0,4);
		hitrange=1500;
		switch(kind){
		case 0://큐브 네우로이 전용 셋팅
			break;
		case 1://보스 전용 셋팅
			life=400+300*main.level;
			mode=0;
			hitrange=12000;
			break;
		case 2://위치 네우로이 전용 셋팅
			life=20+main.RAND(0,10)*main.level;
			hitrange=2000;
			cnt=-(main.RAND(30,50));
			break;
		}
		/*
		if(kind==1){//보스일 경우 세부 파라미터를 재지정한다
			life=400+300*main.level;
			mode=0;
			hitrange=12000;
		}
		*/
	}
	public boolean move(){
		boolean ret=true;
		
		//우선은 공격
		switch(kind){
		case 2://위치 네우로이의 경우
			if(mode!=4) break;
			if(cnt<30&&cnt%5==0){
				bul=new Bullet(pos.x, pos.y, 2, 1, 90, 5);
				main.bullets.add(bul);
			}
			if(cnt>50){
				if(main.RAND(1,100)<50){
					mode=1;//새로 이동할 방향을 결정
					cnt=30;
				}else{
					mode=5;//퇴장
					cnt=0;
				}
			}
			break;
		case 0://일반 적 캐릭터일 경우
			switch(shoottype){//공격 형태에 따라 각기 다른 공격을 한다.
			case 0://플레이어를 향해 3발을 점사한다
				if(cnt%100==0||cnt%103==0||cnt%106==0) {//cnt로 공격 간격을 체크한다
					bul=new Bullet(pos.x, pos.y, 2, 1, main.getAngle(pos.x,pos.y,main.myx,main.myy), 3);
					main.bullets.add(bul);
				}
				break;
			case 1://타이머에 맞춰 4방향탄을 발사한다
				if(cnt%90==0||cnt%100==0||cnt%110==0) {
					bul=new Bullet(pos.x, pos.y, 2, 1, (0+(cnt%36)*10)%360, 3);
					main.bullets.add(bul);
					bul=new Bullet(pos.x, pos.y, 2, 1, (30+(cnt%36)*10)%360, 3);
					main.bullets.add(bul);
					bul=new Bullet(pos.x, pos.y, 2, 1, (60+(cnt%36)*10)%360, 3);
					main.bullets.add(bul);
					bul=new Bullet(pos.x, pos.y, 2, 1, (90+(cnt%36)*10)%360, 3);
					main.bullets.add(bul);
				}
				break;
			case 2://짧은 간격으로 플레이어 근처를 향해 한 발씩 발사한다
				if(cnt%30==0||cnt%60==0||cnt%90==0||cnt%120==0||cnt%150==0||cnt%180==0) {
					bul=new Bullet(pos.x, pos.y, 2, 1, (main.getAngle(pos.x,pos.y,main.myx,main.myy)+main.RAND(-20,20))%360, 2);
					main.bullets.add(bul);
				}
				break;
			case 3://플레이어를 향해 3갈래탄을 발사한다
				if(cnt%90==0||cnt%110==0||cnt%130==0){
					bul=new Bullet(pos.x, pos.y, 2, 1, main.getAngle(pos.x,pos.y,main.myx,main.myy), 2);
					main.bullets.add(bul);
					bul=new Bullet(pos.x, pos.y, 2, 1, (main.getAngle(pos.x,pos.y,main.myx,main.myy)-20)%360, 2);
					main.bullets.add(bul);
					bul=new Bullet(pos.x, pos.y, 2, 1, (main.getAngle(pos.x,pos.y,main.myx,main.myy)+20)%360, 2);
					main.bullets.add(bul);
				}
				break;
			case 4://아무런 공격도 하지않는다
				break;
			}
			break;
		case 1://보스 캐릭터일 경우는, mode에 따라 공격 방식을 바꾼다.
			int lv,i;
			switch(mode){//mode 값은 원래 움직임을 결정한다. 움직임에 맞춰 공격 방식도 바꿔준다.
			case 5:
				if(main.level>=10) lv=5; else lv=(10-main.level)*5;
				if(cnt%lv==0||cnt%(lv+5)==0||cnt%(lv+15)==0) {
					for(i=0;i<4+(50-lv)/5;i++){
						bul=new Bullet(pos.x, pos.y, 2, 1, (30*i+(cnt%36)*10)%360, 5);
						main.bullets.add(bul);
					}
					/*bul=new Bullet(pos.x, pos.y, 2, 1, (30+(cnt%36)*10)%360, 4);
					main.bullets.add(bul);
					bul=new Bullet(pos.x, pos.y, 2, 1, (60+(cnt%36)*10)%360, 4);
					main.bullets.add(bul);
					bul=new Bullet(pos.x, pos.y, 2, 1, (90+(cnt%36)*10)%360, 4);
					main.bullets.add(bul);*/
				}
				break;
			case 7:
				if(main.level>=10) lv=1; else lv=10-main.level;
				if(cnt%lv==0){
					bul=new Bullet(pos.x-3000+main.RAND(-10,+10)*100, pos.y+main.RAND(10,80)*100, 2, 1, 90, 5+(10-lv)/2);
					main.bullets.add(bul);
				}
				break;
			}
			break;
		}

		//이동 처리
		switch(kind){
		case 2://위치 네우로이의 경우
			/*
			위치 네우로이 이동 시나리오
			1. 오른쪽에서 등장하여 왼쪽으로 이동하다 화면 70~80% 정도 위치에 정지한다.
			2. 플레이어의 y좌표를 참조하여 상하로 일정 간격 이동하다
			3. 정지하여
			4. 손을 앞으로 내밀고 공격
			5. 2~4를 몇 번 수행한 후
			6. 뒤로 돌아 물러간다 
			*/
			switch(mode){
			case 0://왼쪽에서 오른쪽으로 등장
				pos.x-=500;
				if(cnt>=0&&pos.x<main.gScreenWidth*80) {
					mode=1;
					cnt=0;
				}
				break;
			case 1://제자리에서 잠시 정지
				if(pos.x>main.gScreenWidth*80){//만일 아직 충분한 x위치가 아니면 좀 더 왼쪽으로 이동한다
					mode=0;
					break;
				}
				if(cnt>=30) {
					if(pos.y>main.myy) mode=3;
					else mode=2;
					cnt=0;
				}
				break;
			case 2://아래로 이동
				if(pos.y<main.gScreenHeight*90&&cnt<20)
					pos.y+=250;
				else {
					mode=4;//공격
					cnt=0;
				}
				break;
			case 3://위로 이동
				if(pos.y>6400&&cnt<20)
					pos.y-=250;
				else {
					mode=4;//공격
					cnt=0;
				}
				break;
			case 5://뒤로 돌아 퇴장
				pos.x+=350;
				break;
			case 4://정지해서 손을 앞으로 내밀고 총알 발사
				break;
			}
			break;
		case 0://일반 캐릭터
			switch(mode){
			case 0:
				pos.x-=500;
				pos.y+=80;
				if(pos.x<main.myx) mode=2;
				break;
			case 1:
				pos.x-=500;
				pos.y-=80;
				if(pos.x<main.myx) mode=3;
				break;
			case 2:
				pos.x+=600;
				pos.y+=240;
				break;
			case 3:
				pos.x+=600;
				pos.y-=240;
				break;
			}
			break;
		case 1://보스캐릭터
		/*
			보스 캐릭터 움직임 시나리오 (mode 값에 따라)
			0. 화면 우측에서 조금 안쪽으로 들어온다.
			1. 플레이어보다 위에 있으면 아래로(mode=2), 아래에 있으면 위로(mode=3) 일정 간격 (실제 좌표 기준으로 120도트) 이동한다.
			4. 화면 중앙까지 나온다
			5. 0.의 위치까지 되돌아간다.
			6. 화면 바깥으로 나가 사라진다.
			7. 잠시 그 자리에서 대기한 뒤 mode=1로 돌아간다
			8. 잠시 그 자리에서 대기한 뒤 mode=5로 전환한다
		*/
			if(main.gamecnt==1200) mode=4;
			if(main.gamecnt==2210) mode=6;
			switch(mode){
			case 0:
				pos.x-=100;
				if(pos.x<53000) mode=1;
				break;
			case 1:
				if(cnt%30==0){
					if(pos.y>main.myy) mode=3;
					else if(pos.y<main.myy) mode=2;
					_pos.x=pos.x;
					_pos.y=pos.y;//이동 거리를 체크하기 위해 이동이 시작되는 위치를 지정
				}
				break;
			case 2:
				if(pos.y+400<42000)
					pos.y+=400;
				else{
					cnt=0;
					mode=7;
				}
				if(pos.y-_pos.y>12000) {
					cnt=0;
					mode=7;
				}
				break;
			case 3:
				if(pos.y-400>6000)
					pos.y-=400;
				else{
					cnt=0;
					mode=7;
				}
				if(_pos.y-pos.y>12000) {
					cnt=0;
					mode=7;
				}
				break;
			case 4:
				pos.x-=800;
				if(pos.x<30000) {
					mode=8;
					cnt=0;
				}
				break;
			case 5:
				pos.x+=350;
				if(pos.x>53000) mode=1;
				break;
			case 6:
				pos.x+=500;
				break;
			case 7:
				if(cnt>100) mode=1;
				break;
			case 8:
				if(cnt>90) mode=5;
				break;
			}
			break;
		}
		dis.x=pos.x/100;
		dis.y=pos.y/100;
		if(dis.x<0||dis.x>640||dis.y<0||dis.y>480) ret=false;

		cnt++;
		return ret;
	}
}
class Effect
{
	//폭발 등의 이펙트 관리 클래스
	Point pos;
	Point _pos;
	Point dis;
	int img;
	int kind;
	int cnt;
	Effect(int img, int x, int y, int kind){
		pos=new Point(x,y);
		_pos=new Point(x,y);
		dis=new Point(x/100,y/100);
		this.kind=kind;
		this.img=img;
		cnt=16;
	}
}
class Item
{
	//아이템 관리 클래스
	Point pos;
	Point dis;
	int speed;
	int cnt;
	int kind;
	Item(int x, int y, int kind){
		this.kind=kind;
		pos=new Point(x,y);
		dis=new Point(x/100,y/100);
		speed=-200;
		cnt=0;
	}
	public boolean move(){
		boolean ret=false;
		pos.x-=speed;
		cnt++;
		if(cnt>=50) speed=200;
		else if(cnt>=30) speed=100;
		else if(cnt>=20) speed=-100;
		dis.x=pos.x/100;
		if(pos.x<0) ret=true;
		return ret;
	}
}

class MyWindowAdapter extends WindowAdapter
{
	// 윈도우를 닫기 위한 부가 클래스. 실제 닫는 동작은
	// setVisible(false);
	// dispose();
	// System.exit(0);
	// 이상 세 라인으로 이루어진다.
	//
	MyWindowAdapter(){
	}
	public void windowClosing(WindowEvent e) {
		Window wnd = e.getWindow();
		wnd.setVisible(false);
		wnd.dispose();
		System.exit(0);
	}
}
