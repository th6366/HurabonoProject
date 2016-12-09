import java.applet.Applet;
import java.awt.Graphics;

public class Sample7 extends Applet implements Runnable
{
    int num;
    public void init()
    {
        Thread th;
        th = new Thread(this); th.start();
    }
    public void run()
    {
        try{
            for(int i=0; i<10; i++){
                num = i;
                repaint();
                Thread.sleep(1000);
            }
        }
        catch(InterruptedException  e){}
    }
    public void paint(Graphics g)
    {
        String str = num + " 입니다.";
        g.drawString(str, 10, 10);
    }
}
