package objects;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import window.BaseWindow;

public class Refactored extends BaseWindow 
{

  float posX = 0, posY = 0, posZ = 0, rotX = 0, rotY = 0, scale = 1;
  float posX3 = 0, posY3 = 0, posZ3 = 0, rotX3 = 0, rotY3 = 0, scale3 = 1;
  
  Cube m_pUser, m_pUser2, m_pUser3;

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
    GLU.gluPerspective(45, 1024 / (float)768, 1.0f, 30.0f);

    setCameraMatrix();    
	}
    
  protected void setCameraMatrix()
  {
    // model view stack 
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glLoadIdentity();
    // setup view space; 
    // translate to 0,2,4 and rotate 30 degrees along x 
    GL11.glTranslatef(0, -2f, -4.0f);
    GL11.glRotatef(15.0f, 1.0f, 0.0f, 0.0f);    
  }

  /** 
   * can be used for 3D model initialization
   */
  protected void initializeModels()
  {
    m_pUser = new Cube();
    m_pUser2 = new  Cube();
    m_pUser3 = new  Cube();
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
    m_pUser.setPosition(posX, posY, posZ);
    m_pUser.setRotation(rotX, rotY, 0);
    m_pUser.setScaling(scale, scale, scale);

    m_pUser.render3D();
   
    m_pUser2.setPosition(posX+2, posY, posZ);
    m_pUser2.setRotation(rotX, rotY, 0);
    m_pUser2.setScaling(scale, scale, scale);

    m_pUser2.render3D();
    
    m_pUser3.setPosition(posX3-2, posY3, posZ3);
    m_pUser3.setRotation(rotX3, rotY3, 0);
    m_pUser3.setScaling(scale3, scale3, scale3);

    m_pUser3.render3D();
  }
  
  /**
   * Processes Keyboard and Mouse input and spawns actions
   */  
  protected void processInput()
  {
    if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
      posX-=0.01;
    if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
      posX+=0.01;
    if (Keyboard.isKeyDown(Keyboard.KEY_UP))
      posY+=0.01;
    if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
      posY-=0.01;
    if (Keyboard.isKeyDown(Keyboard.KEY_HOME))
      posZ-=0.01;
    if (Keyboard.isKeyDown(Keyboard.KEY_END))
      posZ+=0.01;
    if (Keyboard.isKeyDown(Keyboard.KEY_Q))
      rotX++;
    if (Keyboard.isKeyDown(Keyboard.KEY_A))
      rotX--;    
    if (Keyboard.isKeyDown(Keyboard.KEY_E))
      rotY++;
    if (Keyboard.isKeyDown(Keyboard.KEY_D))
      rotY--;    
    if (Keyboard.isKeyDown(Keyboard.KEY_W))
      scale+=0.01;    
    if (Keyboard.isKeyDown(Keyboard.KEY_S))
      scale-=0.01;
    
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
