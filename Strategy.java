import java.io.Serializable;
 
public interface Strategy extends Serializable{
    public abstract Hand nextHand();
    public abstract void study(boolean win);
   
}
