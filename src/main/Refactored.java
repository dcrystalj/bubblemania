package main;


import java.util.LinkedList;

import objects.Bubble;
import objects.BubblePath;
import objects.Cube;
import objects.Terrain;
import objects.Tower;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import window.BaseWindow;

public class Refactored extends BaseWindow 
{

  float scale = 1,speed=0.5f*10, x=WIDTH/2,y=WIDTH/4,z=WIDTH, lx=-x,ly=-y,lz=-z, angley=0, angle=-0.1f ;
  public static Vector3f rotation = new Vector3f(0, 0, 0);
  public static float mouseSpeed = 0.01f;
  public static final int maxLookUp = 85;
  public static final int maxLookDown = -85;
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

	  // mapping from normalized to window coordinates
	  glViewport(0, 0, 1024, 768);

	  // setup projection matrix stack
	  glMatrixMode(GL_PROJECTION);
	  glLoadIdentity();
	  GLU.gluPerspective(45, 1024 / (float)768, 1.0f, 1000.0f);
	  
	  
	  
	  setCameraMatrix();    
  }
 
  protected void setCameraMatrix()
  {
    // model view stack 
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    // setup view space; 
    // translate to 0,2,4 and rotate 30 degrees along x 
//    glTranslatef(-WIDTH/2, -WIDTH/4, -WIDTH);
    //glRotatef(15.0f, 1.0f, 0.0f, 0.0f);   
//    GLU.gluLookAt(WIDTH/2, WIDTH/4, -WIDTH, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
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
   
    
    Thread t = new Thread(new Runnable()	//Deffining new thread for drawing bubbles
    {

    	@Override
    	public void run()
    	{
    		while (GameState.running) {
    			try {
    				for (Bubble b : GameState.bubbles){
    					b.move(20);
    					if(b.isOut(GameState.t_bg)){	//If bubble gets out of terrain
    						//TODO remove bubble from list, this just hides it!!!
    						b.show=false;
    					}
    				}
    				
    				Thread.sleep(10);
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
    				Thread.sleep(500);
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
    //renderFramefirst();
  }
  
  /**
   * Renders current frame
   */
  
  protected void renderFrame()
  {
	  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	  // Reset transformations
	  glLoadIdentity();
	  // Set the camera
	  //      glTranslatef(position.x, position.y, position.z);
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
    if (Mouse.isGrabbed()) {
        float mouseDX = Mouse.getDX()* mouseSpeed * 0.16f;;
        float mouseDY = Mouse.getDY()* mouseSpeed * 0.16f;;
        
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
        
    }
    while (Mouse.next()) {
        if (Mouse.isButtonDown(0)) {
            Mouse.setGrabbed(true);
        }
        if (Mouse.isButtonDown(1)) {
            Mouse.setGrabbed(false);
        }

    }
	lx = (float) Math.sin(angle);
	ly = (float) Math.tan(angley);
	lz = (float) -Math.cos(angle);
	 System.out.println(GameState.money);
    super.processInput();
  }
  public static void main(String[] args) {
    (new Refactored()).execute();
  }  

}
