package objects;


import java.util.LinkedList;

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
  
  static Terrain t_bg;
  Cube c_begin, c_end;
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
	  
	  GameState.bubblesPath=new BubblePath(GameState.startPoint, GameState.endPoint);
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
    t_bg = new Terrain(WIDTH);
    c_begin= new Cube(WIDTH);
    c_end = new Cube(WIDTH);
    
    //set ending cube position
    c_end.m_nX=WIDTH-c_end.cW;
    c_end.m_nZ=WIDTH-c_end.cW;
    
    //Set start and end point for bubbles
    GameState.startPoint=new Vector3f(c_begin.m_nX,c_begin.m_nY+c_begin.cW,c_begin.m_nZ+c_begin.cW/2);
    GameState.endPoint=new Vector3f(c_end.m_nX+c_end.cW,c_end.m_nY+c_end.cW,c_end.m_nZ+c_end.cW/2);
    //Radius of bubble
    float radius=3.f;
    //Create n bubbles
    for(int i=0; i<GameState.numberOfBubbles;i++){
    	Bubble b = new Bubble(radius);
    	float[] start={c_begin.m_nX-b.safetyDistance*(i+2), c_begin.m_nY+c_begin.cW, c_begin.m_nZ+c_begin.cW/2};
    	b.setPos(start);
//    	System.out.println(b.toString()+", Begin: "+c_begin.toString());
    	GameState.bubbles.add(b);
    }
    Tower to = new Tower(radius*1.5f);
    to.setPosition(GameState.startPoint.x+223, 0, GameState.startPoint.z+200);
    GameState.towers.add(to);
    
    Thread t = new Thread(new Runnable()	//Deffining new thread for drawing bubbles
    {

    	@Override
    	public void run()
    	{
    		while (GameState.running) {
    			try {
    				for (Bubble b : GameState.bubbles){
    					b.move(20);
    					if(b.isOut(t_bg)){	//If bubble gets out of terrain
    						//TODO remove bubble from list, this just hides it!!!
    						b.show=false;
    					}
    				}
    				for(Tower t : GameState.towers){
    					t.popBubble();
    				}
    				Thread.sleep(20);
    			} catch (Exception e) {
    			}
    		}
    	}
    });
    
    t.start();	//Run thread
  }
  public void print(LinkedList<Vector3f> l){
  	  System.out.println(".............");
  	  for (Vector3f p : l){
  		  System.out.println("("+p.x+","+p.y+","+p.z+")");
  	  }
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
	  t_bg.render3D();

	  //draw beginning and ending of bubbles
	  c_begin.m_nX+=0.01; 
	  glDisable(GL_CULL_FACE);
	  c_begin.render3D();
	  c_end.render3D();
	  glEnable(GL_CULL_FACE);

	  //Draw bubbles
	  for (Bubble b : GameState.bubbles)
		  b.render3D();
	  //Draw towers
	  for (Tower t : GameState.towers)
		  t.render3D();

  }
  
  
  /**
   * Processes Keyboard and Mouse input and spawns actions
   */  
  protected void processInput()
  {
    if (Keyboard.isKeyDown(Keyboard.KEY_D)){
    	x += speed;
    }
    if (Keyboard.isKeyDown(Keyboard.KEY_A)){
    	x -= speed;
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
            
    super.processInput();
  }
  public static void main(String[] args) {
    (new Refactored()).execute();
  }  

}
