package main;

import objects.Bubble;
import objects.BubblePath;
import objects.Cube;
import objects.Terrain;
import objects.Tower;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import text.BitmapText;
import window.BaseWindow;

public class Refactored extends BaseWindow 
{

  float scale = 1,speed=0.5f*10, x=WIDTH/2,y=WIDTH/4,z=WIDTH, lx=-x,ly=-y,lz=-z, angley=0, angle=-0.1f ;
  public static Vector3f rotation = new Vector3f(0, 0, 0);
  public static float mouseSpeed = 0.01f;
  public static final int maxLookUp = 85;
  public static final int maxLookDown = -85;
  int xOrigin = -1;
  BitmapText t_money, t_lives, t_startgame, t_exit, t_lvl, t_gameover, t_finishedlvl;
  public static int[] menuitemsx= {240,760};
  public static int[] menuitemsy= {580,500,480,400};
  
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

	  // mapping from normalized to window coordinates
	  glViewport(0, 0, 1024, 768);

	  // setup projection matrix stack
	  glMatrixMode(GL_PROJECTION);
	  glLoadIdentity();
	  GLU.gluPerspective(45, 1024 / (float)768, 1.0f, 1500.0f);
	  
	  
	  
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
    GameState.t_bg = new Terrain(WIDTH);
    GameState.c_begin= new Cube(WIDTH);
    GameState.c_end = new Cube(WIDTH);
    //set ending cube position
    GameState.c_end.m_nX=WIDTH-GameState.c_end.cW-20;
    GameState.c_end.m_nZ=WIDTH-GameState.c_end.cW;
    
    //Set start and end point for bubbles
    GameState.startPoint=new Vector3f(GameState.c_begin.m_nX,GameState.c_begin.m_nY,GameState.c_begin.m_nZ+GameState.c_begin.cW/2);
    GameState.endPoint=new Vector3f(GameState.c_end.m_nX+GameState.c_end.cW/2,GameState.c_end.m_nY,GameState.c_end.m_nZ+GameState.c_end.cW/2);
    GameState.bubblesPath=new BubblePath(GameState.startPoint, GameState.endPoint);
    GameState.startingObjects();
   
    //set text
    t_money = new BitmapText();
    t_lives = new BitmapText();
    t_lvl = new BitmapText();
    t_lives.charPos[1]+=30;
    t_lvl.charPos[1]+=60;
    
    //setmenutext
    t_startgame = new BitmapText();
    t_exit = new BitmapText();
    t_finishedlvl = new BitmapText();
    t_gameover = new BitmapText();
    
    t_startgame.charPos[0]=(int)(WIDTH-t_startgame.textWidth("Start Game", 80)/2);
    t_startgame.charPos[1]=500;
    
    t_exit.charPos[0]=(int)(WIDTH-t_startgame.textWidth("EXIT", 80)/2);
    t_exit.charPos[1]=400;
    
    t_finishedlvl.charPos[0] = (int)(WIDTH-t_startgame.textWidth("Congratulations, you have finished lvl 8", 35)/2);
    t_finishedlvl.charPos[1] = menuitemsy[1]+20;
    
    Thread t = new Thread(new Runnable()	//Deffining new thread for drawing bubbles
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
    				
    				Thread.sleep(20);
    			} catch (Exception e) {
    			}
    		}
    	}
    });
    Thread pop = new Thread(new Runnable()	//Deffining new thread for drawing bubbles
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
    				System.out.println(GameState.lives);
    			} catch (Exception e) {
    			}
    		}
    	}
    });
    pop.start();
    t.start();	//Run thread
  }
  
  /**
   * Resets the view of current frame
   */
  
  protected void resetView()
  {
    // clear color and depth buffer
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    if(GameState.lives<=0)
    	GameState.state=3;
    else if(allBublesAreOut()){
    	GameState.state=2;
    }
  }
  protected boolean allBublesAreOut(){
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

	  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	  // Reset transformations
	  glLoadIdentity();
	  
	  if(GameState.state==0){
		  renderFrameMainMenu();
	  }
	  else{
	  
		  //draw text
		  this.startHUD();
		  this.t_money.renderString("Money:"+GameState.money,20);
		  this.t_lives.renderString("Lives:"+GameState.lives,20);
		  this.t_lvl.renderString("Level:"+GameState.lvl,20);		  
		  this.endHUD();
		  	  
		  // Set the camera
		  GLU.gluLookAt(
				  x, y,  z,
				  x+lx, y+ly,  z+lz,
				  0.0f, 1.0f,  0.0f);
	
		  //draw terrain
		  GameState.t_bg.render3D();
	
		  //draw beginning and ending of bubbles
		  glDisable(GL_CULL_FACE);
		  GameState.c_begin.render3D();
		  GameState.c_end.render3D();
	
		  //Draw path
		  GameState.bubblesPath.render3D();
		  //Draw bubbles
		  for (Bubble b : GameState.bubbles)
			  b.render3D();
		  //Draw towers
		  for (Tower t : GameState.towers)
			  t.render3D();
		  glEnable(GL_CULL_FACE);
		  if(GameState.state==2){ //lvl done
			  renderFrameLvlDone();
		  }
		  else if(GameState.state==3){ //gameover
			  renderFrameGameOver();
		  }
	  }

  }
  
  protected void renderFrameMainMenu(){
	  this.startHUD();
	  
	  //draw rectangle
	  GL11.glBegin(GL11.GL_QUADS);
	  GL11.glColor3f(0.3f, 0.2f, 0.4f);
	  for (int i = 0; i < menuitemsy.length; i+=2) {
		  GL11.glVertex2f(menuitemsx[0], menuitemsy[i]);
		  GL11.glVertex2f(menuitemsx[0], menuitemsy[i+1]);
		  GL11.glVertex2f(menuitemsx[1], menuitemsy[i+1]);
		  GL11.glVertex2f(menuitemsx[1], menuitemsy[i]);
	 }
	  GL11.glEnd();
	  
	  GL11.glColor3f(0.9f, 0.9f, 0.9f);
	  this.t_startgame.renderString("START GAME",80);
	  this.t_exit.renderString("EXIT",80);

	  this.endHUD();
	  
  }
   
  private void renderFrameGameOver() {
	  this.startHUD();
	  
	  //draw rectangle
	  GL11.glBegin(GL11.GL_QUADS);
	  GL11.glColor3f(0.3f, 0.2f, 0.4f);
	  for (int i = 0; i < menuitemsy.length; i+=2) {
		  GL11.glVertex2f(menuitemsx[0], menuitemsy[i]);
		  GL11.glVertex2f(menuitemsx[0], menuitemsy[i+1]);
		  GL11.glVertex2f(menuitemsx[1], menuitemsy[i+1]);
		  GL11.glVertex2f(menuitemsx[1], menuitemsy[i]);
	 }
	  GL11.glEnd();
	  
	  GL11.glColor3f(0.9f, 0.9f, 0.9f);
	  this.t_startgame.renderString("START GAME",80);
	  this.t_exit.renderString("EXIT",80);

	  this.endHUD();
	
}

private void renderFrameLvlDone() {
	this.startHUD();
	  //draw rectangle
	  glEnable (GL_BLEND);
	  glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	  GL11.glBegin(GL11.GL_QUADS);
	  GL11.glColor4f(0.3f, 0.2f, 0.4f,0.5f);
	  int i=2;
		  GL11.glVertex2f(menuitemsx[0], menuitemsy[i]);
		  GL11.glVertex2f(menuitemsx[0], menuitemsy[i+1]);
		  GL11.glVertex2f(menuitemsx[1], menuitemsy[i+1]);
		  GL11.glVertex2f(menuitemsx[1], menuitemsy[i]);
	 
	  GL11.glEnd();
	  
	  GL11.glColor3f(0.9f, 0.9f, 0.9f);
	  this.t_finishedlvl.renderString("Congratulations, you have finished lvl "+GameState.lvl,35);
	  this.t_exit.renderString("Next",80);

	  this.endHUD();
}

protected void startHUD() {
	    GL11.glMatrixMode(GL11.GL_PROJECTION);
	    GL11.glPushMatrix();
	    GL11.glLoadIdentity();
	    GL11.glOrtho(0, 1024, 0, 768, -1, 1);
	    GL11.glMatrixMode(GL11.GL_MODELVIEW);
	    GL11.glPushMatrix();
	    GL11.glLoadIdentity();
	  }
	  
	  protected void endHUD() {
	    GL11.glMatrixMode(GL11.GL_PROJECTION);
	    GL11.glPopMatrix();
	    GL11.glMatrixMode(GL11.GL_MODELVIEW);
	    GL11.glPopMatrix();
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
    	if(Mouse.getX()>=menuitemsx[0] && Mouse.getX()<=menuitemsx[1]){
    		//start game
    		if(Mouse.getY()>=menuitemsy[1] && Mouse.getY()<=menuitemsy[0]){
    	    	GameState.state=1;
    		}
    		if(Mouse.getY()>=menuitemsy[3] && Mouse.getY()<=menuitemsy[2]){
    	    	BaseWindow.isRunning=false;
    		}
    	}
    }
    //mouse in game
    else if (Mouse.isGrabbed()) {
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
        //lvl done
        if(GameState.state==2){	
        	if(Mouse.getX()>=menuitemsx[0] && Mouse.getX()<=menuitemsx[1]){
        		if(Mouse.getY()>=menuitemsy[3] && Mouse.getY()<=menuitemsy[2]){
        	    	GameState.lvl++;
        	    	GameState.state = 1;
        	    	initializeModels();
        	    }
        	}
        }
        //TODO if game over
        if(GameState.state==2){	
        	if(Mouse.getX()>=menuitemsx[0] && Mouse.getX()<=menuitemsx[1]){
        		if(Mouse.getY()>=menuitemsy[3] && Mouse.getY()<=menuitemsy[2]){
        	    	GameState.lvl++;
        	    	GameState.state = 1;
        	    	initializeModels();
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
  public static void main(String[] args) {
    (new Refactored()).execute();
  }  

}
