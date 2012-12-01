package objects;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.util.vector.Vector3f;

//Class which has saved state of current game
public class GameState {
	public static int numberOfBubbles = 10;
	public static Set<Bubble> bubbles=new HashSet<Bubble>();
	public static Set<Bubble> points=new HashSet<Bubble>();
	public static Set<Tower> towers=new HashSet<Tower>();
	public static BubblePath bubblesPath;
	public static boolean running=true;	//Running thread for moving bubbles 
	public static Vector3f startPoint;
	public static Vector3f endPoint;
	

}
