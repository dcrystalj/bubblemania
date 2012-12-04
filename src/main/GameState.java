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
	//main state
	/*
	 * 0=main menu
	 * 1=game
	 * 2=level end
	 * 3=game over
	 * 4=upgrade
	 */
	public static int state=0;
	
	//Bubbles+towers
	public static int numberOfBubbles = 15;
	public static Set<Bubble> bubbles=new HashSet<Bubble>();
	public static Set<Bubble> points=new HashSet<Bubble>();
	public static Set<Tower> towers=new HashSet<Tower>();
	public static boolean running=true;	//Running thread for moving bubbles 

	//Bubbles path
	public static Vector3f startPoint;
	public static Vector3f endPoint;
	public static BubblePath bubblesPath;
	public static BubblePath path;

	//Other objects and variables
	public static float WIDTH=500;	//BaseWindow width
	public static Terrain t_bg;
	public static Cube c_begin, c_end;
	
	//Current game state: lives, money,..
	public static int lives=30;
	public static int money=100;
	public static int lvl=1;
	
	//Threads
	public static Thread popBubbles;
	public static Thread moveBubbles;
	
	public static void startingObjects(){
		moveBubbles = new Thread(new Runnable()	//Deffining new thread for drawing bubbles
	    {
	    	@Override
	    	public void run()
	    	{
	    		while (GameState.running) {
	    			try {
	    				for (Bubble b : GameState.bubbles){
	    					b.move();
	    					if(GameState.state==1)
	    						b.move();
	    					if(b.isOut(GameState.t_bg)){	//If bubble gets out of terrain
	    						//TODO remove bubble from list, this just hides it!!!
	    						b.show=false;
	    					}
	    				}
	    				
	    				Thread.sleep(15);
	    			} catch (Exception e) {
	    			}
	    		}
	    	}
	    });
	    popBubbles = new Thread(new Runnable()	//Deffining new thread for drawing bubbles
	    {
	    	@Override
	    	public void run()
	    	{
	    		while (GameState.running) {
	    			try {
	    				for(Tower t : GameState.towers){
	    					t.popBubble();
	    				}
	    				Thread.sleep(600);
	    			} catch (Exception e) {
	    			}
	    		}
	    	}
	    });
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
		Tower to = new Tower(10);
		to.setPosition(startPoint.x+225, 0.0f, startPoint.z+180);
		towers.add(to);
		to = new Tower(10);
		to.setPosition(startPoint.x+410, 0.01f, startPoint.z+270);
		to.setPosition(startPoint.x+410, 0.0f, startPoint.z+270);
		towers.add(to);
		//Start all running threads
		popBubbles.start();
		moveBubbles.start();
		running=true;
	}
	@SuppressWarnings("deprecation")
	public static void resetObjects(){
		//Delete all bubbles and towers
		popBubbles.stop();
		moveBubbles.stop();
		bubbles=new HashSet<Bubble>();	
		towers=new HashSet<Tower>();
		//Stop all running threads
		running=false;
	}
}
