package main;


import java.util.HashSet;
import java.util.Set;

import objects.Bubble;
import objects.BubblePath;
import objects.Cube;
import objects.Terrain;

import org.lwjgl.util.vector.Vector3f;

import threads.ThreadMoveBubbles;
import threads.ThreadPopBubblesTowerGun;
import threads.ThreadPopBubblesTripleGun;
import towers.TowerGun;
import towers.TripleGun;

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
	public static Set<TowerGun> towerGuns=new HashSet<TowerGun>();
	public static Set<TripleGun> tripleGuns=new HashSet<TripleGun>();
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
	public static Thread towerGunPopBubbles;
	public static Thread tripleGunPopBubbles;
	public static Thread moveBubbles;
	
	//For knowing which tower we are building, 1=towerGun, 2=TripleGun
	public static int buildTower;
	
	public static void startingObjects(){
		
		//Create n bubbles
		//Radius of bubble
		float radius=3.f;
		for(int i=0; i<numberOfBubbles;i++){
			Bubble b = new Bubble(radius);
			//-lvl*50, because of level rendering
			float[] start={c_begin.m_nX-Bubble.safetyDistance*(i+2)-lvl*60, c_begin.m_nY+10, c_begin.m_nZ+c_begin.cW/2};
			b.setPos(start);
			bubbles.add(b);
		}
		if(lvl==1)
			towerGuns=startingTowers();
		//Start all running threads
		running=true;
		towerGunPopBubbles = new ThreadPopBubblesTowerGun();
		tripleGunPopBubbles = new ThreadPopBubblesTripleGun();
	    moveBubbles = new ThreadMoveBubbles();
	    towerGunPopBubbles.start();
		tripleGunPopBubbles.start();
		moveBubbles.start();
		
	}
	@SuppressWarnings("deprecation")
	public static void resetObjects(){
		//Delete all bubbles and towers
		running=false;
		towerGunPopBubbles.stop();
		tripleGunPopBubbles.stop();
		moveBubbles.stop();
		
		bubbles=new HashSet<Bubble>();	
		//Stop all running threads
		running=false;
	}
	//In starting level we can only have tower gun
	public static Set<TowerGun> startingTowers(){
		//Create towers
		Set<TowerGun> towersStart=new HashSet<TowerGun>();
		TowerGun to = new TowerGun(10);
		to.setPosition(startPoint.x+225, 0.0f, startPoint.z+180);
		towersStart.add(to);
		to = new TowerGun(10);
		to.setPosition(startPoint.x+410, 0.01f, startPoint.z+270);
		to.setPosition(startPoint.x+410, 0.0f, startPoint.z+270);
		towersStart.add(to);
		return towersStart;
	}
}
