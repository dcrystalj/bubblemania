package main;


import java.util.HashSet;
import java.util.Set;

import objects.Bubble;
import objects.BubblePath;
import objects.Cube;
import objects.Obj3D;
import objects.Terrain;

import org.lwjgl.util.vector.Vector3f;

import threads.ThreadMoveBubbles;
import towers.Tower;
import towers.TowerGun;

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
	public static Set<Tower> towers = new HashSet<Tower>();
	public static boolean running=true;	//Running thread for moving bubbles 

	//Bubbles path
	public static Vector3f startPoint;
	public static Vector3f endPoint;
	public static BubblePath bubblesPath;
	public static BubblePath path;

	//Other objects and variables
	public static float WIDTH=500;	//BaseWindow width
	public static Terrain t_bg;
	public static Obj3D startObject,endObject;
	public static Cube c_end,c_begin;
	public static boolean standardObjectsDrawing=true;
	
	
	//Current game state: lives, money,..
	public static int lives=30;
	public static volatile int money=100;	//volatile, changed in thread
	public static int lvl=1;
	
	//Threads
	public static Thread moveBubbles;
	
	//For knowing which tower we are building, 1=towerGun, 2=TripleGun
	public static int buildTower;
	
	//Lighting set
	public static boolean lighting=true;
	
	//Popped bubbles, how many bubbles we have poped in game, volatile because it is changed in thread
	public static volatile int poppedBubbles=0;

	public static void startingObjects(){
		
		//Create n bubbles
		//Radius of bubble
		float radius=3.f;
		for(int i=0; i<numberOfBubbles;i++){
			Bubble b = new Bubble(radius);
			//-lvl*40, because of level rendering
			float[] start={startPoint.x-Bubble.safetyDistance*(i+2)-lvl*40, startPoint.y+10, startPoint.y+10};
			b.setPos(start);
			bubbles.add(b);
		}
		if(lvl==1)
			towers=startingTowers();
		
		//Start all running threads
		running=true;
	    moveBubbles = new ThreadMoveBubbles();
		moveBubbles.start();
		startShooting();
		
	}
	@SuppressWarnings("deprecation")
	public static void resetObjects(){
		//Delete all bubbles and towers
		running=false;
		stopShooting();
		if(moveBubbles!=null)
			moveBubbles.stop();
		
		bubbles=new HashSet<Bubble>();	
		//Stop all running threads
		running=false;
	}
	//Kill all tower threads, so they stop shooting
	public static void stopShooting(){
		for(Tower t : towers)
			t.shoot.kill();
	}
	public static void startShooting(){
		for(Tower t : towers)
			t.shoot.startThread();
		
	}
	//In starting level we can only have tower gun
	public static Set<Tower> startingTowers(){
		//Create towers
		Set<Tower> towersStart=new HashSet<Tower>();
		TowerGun to = new TowerGun(10);
		to.setPosition(startPoint.x+215, 0.0f, startPoint.z+180);
		towersStart.add(to);
		to = new TowerGun(10);
		to.setPosition(startPoint.x+400, 0.01f, startPoint.z+280);
		towersStart.add(to);
		return towersStart;
	}
}
