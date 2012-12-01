package main;


import java.util.HashSet;
import java.util.Set;

import objects.Bubble;
import objects.BubblePath;
import objects.Cube;
import objects.Terrain;
import objects.Tower;

import org.lwjgl.util.vector.Vector3f;

//Class which has saved state of current game
public class GameState {
	//Bubbles+towers
	public static int numberOfBubbles = 10;
	public static Set<Bubble> bubbles=new HashSet<Bubble>();
	public static Set<Bubble> points=new HashSet<Bubble>();
	public static Set<Tower> towers=new HashSet<Tower>();
	public static boolean running=true;	//Running thread for moving bubbles 

	//Bubbles path
	public static Vector3f startPoint;
	public static Vector3f endPoint;
	public static BubblePath bubblesPath;

	//Other objects and variables
	public static float WIDTH;	//BaseWindow width
	public static Terrain t_bg;
	public static Cube c_begin, c_end;

	public static void startingObjects(){
		
		//Create n bubbles
		//Radius of bubble
		float radius=3.f;
		for(int i=0; i<numberOfBubbles;i++){
			Bubble b = new Bubble(radius);
			float[] start={c_begin.m_nX-b.safetyDistance*(i+2), c_begin.m_nY+10, c_begin.m_nZ+c_begin.cW/2};
			b.setPos(start);
			//	    	System.out.println(b.toString()+", Begin: "+c_begin.toString());
			bubbles.add(b);
		}
		//Create towers
		Tower to = new Tower(radius*1.5f);
		to.setPosition(startPoint.x+225, 0.01f, startPoint.z+180);
		towers.add(to);
		to = new Tower(radius*1.5f);
		to.setPosition(startPoint.x+410, 0.01f, startPoint.z+270);
		towers.add(to);
	}
}
