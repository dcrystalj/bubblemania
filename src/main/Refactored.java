package main;

import objects.Bubble;
import objects.BubblePath;
import objects.Cube;
import objects.Obj3D;
import objects.Terrain;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;

import text.Bitmap;
import threads.ThreadMoveTower;
import towers.Tower;
import HUD.*;
import window.BaseWindow;

public class Refactored extends BaseWindow 
{

	public static float scale = 1,speed=0.5f*10, x=WIDTH/2,y=WIDTH/4,z=WIDTH, lx=-x,ly=-y,lz=-z, angley=0, angle=-0.1f ;
	public static Vector3f rotation = new Vector3f(0, 0, 0);
	public static float mouseSpeed = 0.005f;
	public static final int maxLookUp = 85;
	public static final int maxLookDown = -85;
	public static Tower newTower=null;

	int xOrigin = -1;

	/**
	 * Initial setup of projection of the scene onto screen, lights etc.
	 */
	protected void setupView()
	{    
		
		initializeModels();
		// enable depth buffer (off by default)
		glEnable(GL_DEPTH_TEST); 
		// enable culling of back sides of polygons
		glEnable(GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
//		Lightning
//		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
//		//Later disabled for drawing shooting range an HUD
		if(GameState.lighting){
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, allocFloats(new float[] { 450f, 450f, 450f, 1f}));
			GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, allocFloats(new float[] { 0.8f, 1f, 0.8f, 1f}));
			GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE , allocFloats(new float[] { 4f, 4f, 4f, 3f}));

			GL11.glMaterialf(GL_FRONT, GL_SHININESS, 10.0f); // Shininess between 0 and 128
			GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, allocFloats(new float[] { 0.5f, 0.5f, 0.5f, 1f}));
			//
			// smooth shading - Gouraud
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glEnable(GL11.GL_LIGHT0);
		}
		
		// mapping from normalized to window coordinates
		glViewport(0, 0, 1024, 768);

		// setup projection matrix stack
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(45, 1024 / (float)768, 20.0f, 1200.0f);



		setCameraMatrix();    
	}

	protected void setCameraMatrix()
	{
		// model view stack 
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		GLU.gluLookAt(WIDTH/2, WIDTH/4, -WIDTH, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
	}

	/** 
	 * can be used for 3D model initialization
	 */
	protected void initializeModels()
	{
	    // textures
	    // enable 2D textures 
	    GL11.glEnable(GL11.GL_TEXTURE_2D);
	 // select modulate to mix texture with color for shading; GL_REPLACE, GL_MODULATE ...
	    GL11.glTexEnvf( GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE );
	    
	    //We draw basic objects just once, or if we need to
	    if(GameState.standardObjectsDrawing){
	    	GameState.t_bg = new Terrain(WIDTH);
	    	GameState.c_begin= new Cube(WIDTH);
	    	GameState.c_end = new Cube(WIDTH);
	    	//Starting object
	    	GameState.startObject=new Obj3D("Kugle.obj");
	    	GameState.startObject.m_nX=80;
	    	GameState.startObject.m_nZ=80;
	    	GameState.startObject.m_nY=10;
	    	GameState.startObject.setScaling(7, 7, 7);
	    	//Ending object
	    	GameState.endObject=new Obj3D("endTree.obj");
	    	GameState.endObject.m_nX=460;
	    	GameState.endObject.m_nZ=480;
	    	GameState.endObject.m_nY=0.02f;
	    	GameState.endObject.m_rY=180;
	    	//		GameState.startObject.setScaling(7, 7, 7);
	    	//set ending cube position
	    	GameState.c_end.m_nX=WIDTH-GameState.c_end.cW-20;
	    	GameState.c_end.m_nZ=WIDTH-GameState.c_end.cW;

	    	//Set start and end point for bubbles
	    	GameState.startPoint=new Vector3f(0,0,42);
	    	//		GameState.startPoint=new Vector3f(GameState.c_begin.m_nX,GameState.c_begin.m_nY,GameState.c_begin.m_nZ+GameState.c_begin.cW/2);
	    	GameState.endPoint=new Vector3f(GameState.c_end.m_nX+GameState.c_end.cW/2,GameState.c_end.m_nY,GameState.c_end.m_nZ+GameState.c_end.cW/2);
	    	GameState.bubblesPath=new BubblePath(GameState.startPoint, GameState.endPoint);

	    	//set text
	    	Hud.t_money = new Bitmap();
	    	Hud.t_lives = new Bitmap();
	    	Hud.t_lvl = new Bitmap();
	    	Hud.t_bubblesThisLvl = new Bitmap();
	    	Hud.t_lives.charPos[1]+=30;
	    	Hud.t_lvl.charPos[1]+=60;
	    	Hud.t_bubblesThisLvl.charPos[1]+=90;

	    	//set menu text
	    	Hud.t_1item = new Bitmap();
	    	Hud.t_2item = new Bitmap();
	    	Hud.t_3item = new Bitmap();
	    	Hud.t_finishedlvl = new Bitmap();
	    	Hud.t_gameover = new Bitmap();

	    	Hud.t_1item.charPos[0]=(int)(WIDTH-Hud.t_1item.textWidth("Start Game", 80)/2);
	    	Hud.t_1item.charPos[1]=500;

	    	Hud.t_2item.charPos[0]=(int)(WIDTH-Hud.t_1item.textWidth("EXIT", 80)/2);
	    	Hud.t_2item.charPos[1]=400;

	    	Hud.t_3item.charPos[0]=(int)(WIDTH-Hud.t_1item.textWidth("EXIT", 80)/2);
	    	Hud.t_3item.charPos[1]=300;
	    	GameState.standardObjectsDrawing=false;
	    }
	    if(GameState.state>0)
	    	GameState.startingObjects();

	}

	/**
	 * Resets the view of current frame
	 */

	protected void resetView()
	{

		if(GameState.lives<=0){
			GameState.running=false;
			GameState.state=3;
		}
		else if(allBublesAreOut() && GameState.state!=4){
			GameState.state=2;
		}
	}
	protected boolean allBublesAreOut(){
		if(GameState.state==0)	//if we are still in menu
			return false;
		for (Bubble b : GameState.bubbles){
			if(b.show)
				return false;
		}
		return true;
	}
	/**
	 * Renders current frame
	 */

	protected void renderFrame()
	{
		//		 clear color and depth buffer
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		// Reset transformations
		glLoadIdentity();
		if(GameState.state==0){
			Hud.renderFrameMainMenu();
			
		}
		else{
			
			//draw text
			if(GameState.state!=4){
				Hud.startHUD();
				GL11.glColor3f(1,1,1);
				Hud.t_money.renderString("Money:"+GameState.money,20);
				Hud.t_lives.renderString("Lives:"+GameState.lives,20);
				Hud.t_lvl.renderString("Level:"+GameState.lvl,20);		
				Hud.t_bubblesThisLvl.renderString("Bloons attributes for this level: number="+GameState.numberOfBubbles+", speed=" +
						""+Math.round(Bubble.speed*100.0)/100.0+", distance between="+Bubble.safetyDistance,20);	
				Hud.endHUD();
			}
			if(x>=WIDTH-30)
				x=WIDTH-30;
			if(y>=WIDTH-30 && GameState.state != 4)
				y=WIDTH-30;
			if(z>=WIDTH-30)
				z=WIDTH-30;
			if(x<=30)
				x=30;
			if(y<=30)
				y=30;
			if(z<=30)
				z=30;
			// Set the camera
			GLU.gluLookAt(
					x, y,  z,
					x+lx, y+ly,  z+lz,
					0.0f, 1.0f,  0.0f);
			//draw terrain
			GameState.t_bg.render3D();
			GameState.startObject.render3D();
			GameState.endObject.render3D();

			//Draw path
			GameState.bubblesPath.render3D();
			//Draw bubbles
			for (Bubble b : GameState.bubbles)
				b.render3D();
			//Draw towerGuns
			for (Tower t : GameState.towers)
				t.render3D();
			
			//Render new tower if we are building it
			if(newTower!=null)
				newTower.render3D();
			glEnable(GL_CULL_FACE);
			if(GameState.state==2){ //level done
				Hud.renderFrameLvlDone();
			}
			else if(GameState.state==3){ //game over
				Hud.renderFrameGameOver();
			}
			else if(GameState.state==4){
				Hud.renderFrameBuy();
				Hud.t_money.renderString("Money:"+GameState.money,20);
				//Build towerGun
				if(GameState.money>=200 && Mouse.isButtonDown(0)&& Hud.isInRectangle(849, 999, 649,499)){
					GameState.buildTower=1;
					ThreadMoveTower mt=new ThreadMoveTower();
					mt.start();	  
				}
				//Build triple gun
				else if(GameState.money>=500 && Mouse.isButtonDown(0)&& Hud.isInRectangle(849, 999, 479, 330)){
					GameState.buildTower=2;
					ThreadMoveTower mt=new ThreadMoveTower();
					mt.start();	  
				}
				

			}
		}

	}
	/**
	 * Processes Keyboard and Mouse input and spawns actions
	 */  
	protected void processInput()
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_D)){
			x += speed * (float)Math.sin(angle+Math.PI/2);
			z -= speed * (float)Math.cos(angle+Math.PI/2);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)){     
			x -= speed * (float)Math.sin(angle+Math.PI/2);
			z += speed * (float)Math.cos(angle+Math.PI/2);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)){
			x += lx * speed;
			z += lz * speed;
			y += ly * speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			x -= lx * speed;
			z -= lz * speed;
			y -= ly * speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			y += 0.1;
		}   
		//mouse in menu
		if(Mouse.isGrabbed() && GameState.state==0){	
			if(Mouse.getX()>=Hud.menuitemsx[0] && Mouse.getX()<=Hud.menuitemsx[1]){
				//start game
				if(Mouse.getY()>=Hud.menuitemsy[1] && Mouse.getY()<=Hud.menuitemsy[0]){   	        
					GameState.state=1;
					GameState.resetObjects();
					GameState.startingObjects();
				}
				if(Mouse.getY()>=Hud.menuitemsy[3] && Mouse.getY()<=Hud.menuitemsy[2]){
					BaseWindow.isRunning=false;
				}
			}
		}
		//next in buy menu
		if(Mouse.isButtonDown(0) && GameState.state==4){
			if(Mouse.getX()>=Hud.menuitemsx[0] && Mouse.getX()<=Hud.menuitemsx[1]){
				if(Mouse.getY()>=0 && Mouse.getY()<=60){
					nextLevel();
					x=WIDTH/2;y=WIDTH/4;z=WIDTH; lx=-x;ly=-y;lz=-z;
					angle=-0.1f;
					angley=0;
				}
			}
		}
		//mouse in game
		else if (Mouse.isGrabbed() && GameState.state != 4) {
			float mouseDX = Mouse.getDX()* mouseSpeed * 0.16f;
			float mouseDY = Mouse.getDY()* mouseSpeed * 0.16f;
			if(mouseDX>0){
				angle += mouseDX;
			}
			if(mouseDX<0){
				angle += mouseDX;
			}
			if(mouseDY>0){
				angley+=mouseDY;
			}
			if(mouseDY<0){
				angley+=mouseDY;
			}

			//        System.out.println(mouseDX+"   "+mouseDY);
			//level done
			if(GameState.state==2){	
				if(Mouse.getX()>=Hud.menuitemsx[0] && Mouse.getX()<=Hud.menuitemsx[1]){
					if(Mouse.getY()>=Hud.menuitemsy[3] && Mouse.getY()<=Hud.menuitemsy[2]){
						GameState.state = 4;
						x=WIDTH/2; y=800; z=WIDTH/2;
						angle=0; angley=-(float) (Math.PI/2-0.001);
						System.out.println(GameState.state);
					}
					else if(Mouse.getY()>=Hud.menuitemsy[5] && Mouse.getY()<=Hud.menuitemsy[4]){ //third button
						nextLevel();
					}
				}
			}
			//game over
			if(GameState.state==3){	
				if(Mouse.getX()>=Hud.menuitemsx[0] && Mouse.getX()<=Hud.menuitemsx[1]){ //right x position
					if(Mouse.getY()>=Hud.menuitemsy[1] && Mouse.getY()<=Hud.menuitemsy[0]){ //second button

						GameState.state = 1;
						initializeModels();
						scale = 1;
						speed=0.5f*10; x=WIDTH/2;y=WIDTH/4;z=WIDTH; lx=-x;ly=-y;lz=-z; angley=0; angle=-0.1f ;
						GameState.lives=30;
						GameState.money=100;
						GameState.lvl = 1;
						GameState.numberOfBubbles = 15;
						Bubble.safetyDistance=25;
						Bubble.speed = 1.3f;
						GameState.towers = GameState.startingTowers();

					}
					if(Mouse.getY()>=Hud.menuitemsy[3] && Mouse.getY()<=Hud.menuitemsy[2]){
						BaseWindow.isRunning=false;
					}
				}
			}
		}
		while (Mouse.next()) {
			if (Mouse.isButtonDown(0)) {
				Mouse.setGrabbed(true);
			}
			else {
				Mouse.setGrabbed(false);
			}

		}
		lx = (float) Math.sin(angle);
		ly = (float) Math.tan(angley);
		lz = (float) -Math.cos(angle);
		super.processInput();
	}
	private void nextLevel(){
		GameState.lvl++;
		GameState.state = 1;

		//Define harder level
		if(GameState.lvl%3==0){
			GameState.numberOfBubbles*=1.1; //We increase number of balloons
			Bubble.speed*=1.2f;
		}
		else if(GameState.lvl%3==1){
			Bubble.safetyDistance*=0.8;
			Bubble.speed*=1.2f;
		}
		else Bubble.speed*=1.2f;

		//Reset towers+bubbles
		GameState.resetObjects();
		//Initialize them again for new level
		initializeModels();
	}
	public static void main(String[] args) {
		(new Refactored()).execute();
	}  

}
