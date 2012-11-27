package objects;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import window.BaseWindow;

public class Refactored extends BaseWindow 
{

  float posX = 0, posY = 0, posZ = 0, rotX = 0, rotY = 0, scale = 1;
  float posX3 = 0, posY3 = 0, posZ3 = 0, rotX3 = 0, rotY3 = 0, scale3 = 1;
  
  Terrain t_bg;
  Cube c_begin, c_end;
	/**
	 * Initial setup of projection of the scene onto screen, lights etc.
	 */
  protected void setupView()
	{    
    initializeModels();
    
    // enable depth buffer (off by default)
    GL11.glEnable(GL11.GL_DEPTH_TEST); 
    // enable culling of back sides of polygons
    GL11.glEnable(GL11.GL_CULL_FACE);

    // mapping from normalized to window coordinates
    GL11.glViewport(0, 0, 1024, 768);

    // setup projection matrix stack
    GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glLoadIdentity();
    GLU.gluPerspective(45, 1024 / (float)768, 1.0f, 400.0f);

    setCameraMatrix();    
	}
    
  protected void setCameraMatrix()
  {
    // model view stack 
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glLoadIdentity();
    // setup view space; 
    // translate to 0,2,4 and rotate 30 degrees along x 
    GL11.glTranslatef(-WIDTH/2, -WIDTH/4, -200.0f);
    GL11.glRotatef(15.0f, 1.0f, 0.0f, 0.0f);    
  }

  /** 
   * can be used for 3D model initialization
   */
  protected void initializeModels()
  {
    t_bg = new Terrain();
    c_begin= new Cube(WIDTH);
    c_end = new Cube(WIDTH); 
  }
  /**
   * Resets the view of current frame
   */
  protected void resetView()
  {
    // clear color and depth buffer
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
  }
  
  /**
   * Renders current frame
   */
  protected void renderFrame()
  {
	//draw terrain
    t_bg.setPosition(posX, posY, posZ);
    t_bg.setRotation(rotX, rotY, 0);
    t_bg.setScaling(scale, scale, scale);
    t_bg.render3D();
    
    //draw beginning and anding of bubbles
   
    GL11.glDisable(GL11.GL_CULL_FACE);
    c_end.deltax=WIDTH-c_end.cW; c_end.deltay=0; c_end.deltaz=WIDTH-c_end.cW;
    c_end.setPosition(posX+c_end.deltax, posY+c_end.deltay, posZ+c_end.deltaz);
    c_end.setRotation(rotX, rotY, 0);
    c_end.setScaling(scale, scale, scale);
    c_end.render3D();
    
    c_begin.setPosition(posX, posY, posZ);
    c_begin.setRotation(rotX, rotY, 0);
    c_begin.setScaling(scale, scale, scale);
    c_begin.render3D();
    GL11.glEnable(GL11.GL_CULL_FACE);
  }
  
  /**
   * Processes Keyboard and Mouse input and spawns actions
   */  
  protected void processInput()
  {
    if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
      posX-=0.1;
    if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
      posX+=0.1;
    if (Keyboard.isKeyDown(Keyboard.KEY_UP))
      posY+=0.1;
    if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
      posY-=0.1;
    if (Keyboard.isKeyDown(Keyboard.KEY_HOME))
      posZ-=0.1;
    if (Keyboard.isKeyDown(Keyboard.KEY_END))
      posZ+=0.1;
    if (Keyboard.isKeyDown(Keyboard.KEY_Q))
      rotX+=0.01;
    if (Keyboard.isKeyDown(Keyboard.KEY_A))
      rotX-=0.01;   
    if (Keyboard.isKeyDown(Keyboard.KEY_E))
      rotY+=0.05;
    if (Keyboard.isKeyDown(Keyboard.KEY_D))
      rotY-=0.05;   
    if (Keyboard.isKeyDown(Keyboard.KEY_W))
      scale+=0.0005;    
    if (Keyboard.isKeyDown(Keyboard.KEY_S))
      scale-=0.0005;
    
    if (Keyboard.isKeyDown(Keyboard.KEY_J))
      posX3-=0.01;
    if (Keyboard.isKeyDown(Keyboard.KEY_L))
      posX3+=0.01;
    if (Keyboard.isKeyDown(Keyboard.KEY_I))
      posY3+=0.01;
    if (Keyboard.isKeyDown(Keyboard.KEY_K))
      posY3-=0.01;
    if (Keyboard.isKeyDown(Keyboard.KEY_INSERT))
      posZ3-=0.01;
    if (Keyboard.isKeyDown(Keyboard.KEY_DELETE))
      posZ3+=0.01;
    
    
    if (Keyboard.isKeyDown(Keyboard.KEY_R))
      rotX3++;
    if (Keyboard.isKeyDown(Keyboard.KEY_F))
      rotX3--;    
    if (Keyboard.isKeyDown(Keyboard.KEY_T))
      rotY3++;
    if (Keyboard.isKeyDown(Keyboard.KEY_G))
      rotY3--;    
    if (Keyboard.isKeyDown(Keyboard.KEY_Z))
      scale3+=0.01;    
    if (Keyboard.isKeyDown(Keyboard.KEY_H))
      scale3-=0.01;
            
    super.processInput();
  }
  
  public static void main(String[] args) {
    (new Refactored()).execute();
  }  
}
