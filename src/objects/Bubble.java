package objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

public class Bubble extends Model3D
{
  private Sphere s = new Sphere();
  public float radius = 1;
  public float[] speed = {5f, 0.01f, 0.01f};
  public float[] color = {1, 1, 1};
  public boolean showBubble=true; //Options for 3D rendering, if false it wont show
  public int checkpoint=0;  //checkPoint, which is next point on path

  public Bubble(float r) {
    super(r);
    radius = r;
  }
  
  public void setPos(float[] pos) {
    m_nX = pos[0];
    m_nY = pos[1];
    m_nZ = pos[2];
  }

  public void move(int timeMilis) {
	  Point next=Refactored.bubblesPath.get(checkpoint);
	  Point direction=new Point(next.x-m_nX,next.y-m_nY,next.z-m_nZ);
	  if(checkpoint<(Refactored.bubblesPath.path.size()-1)){
		  if(direction.sum()<0.1){
			  checkpoint++;
			  next=Refactored.bubblesPath.get(checkpoint);
			  direction=new Point(next.x-m_nX,next.y-m_nY,next.z-m_nZ);
		  }
		  direction.normalize();	//Get direction vector and normalize it
		  m_nX += direction.x*speed[0]*timeMilis;
		  m_nY += direction.y*speed[1]*timeMilis;
		  m_nZ += direction.z*speed[2]*timeMilis;
	  }
  }
  public void reverseSpeed(){
    speed[0]=-speed[0];
    speed[1]=-speed[1];
    speed[2]=-speed[2];
  }
  public void render3D()
  {
	  if(showBubble){
		  // model view stack 
		  GL11.glMatrixMode(GL11.GL_MODELVIEW);

		  // save current matrix
		  GL11.glPushMatrix();

		  // TRANSLATE 
		  GL11.glTranslatef(m_nX, m_nY, m_nZ);

		  // ROTATE and SCALE
		  //    GL11.glTranslatef(0, 0, -5f);
		  if (m_rZ!=0)
			  GL11.glRotatef(m_rZ, 0, 0, 1);
		  if (m_rY!=0)
			  GL11.glRotatef(m_rY, 0, 1, 0);
		  if (m_rX!=0)
			  GL11.glRotatef(m_rX, 1, 0, 0);
		  if (m_sX!=1 || m_sY!=1 || m_sZ!=1)
			  GL11.glScalef(m_sX, m_sY, m_sZ);
		  GL11.glTranslatef(0, 0, 0f);    

		  renderModel();

		  // discard current matrix
		  GL11.glPopMatrix();
	  }
  }

  private void renderModel()
  {
    GL11.glColor3f(color[0], color[1], color[2]);
    s.draw(radius, 64, 64);
  }
  //Method is checking whether the bubble gets out of visible terrain area, terrain
  boolean isOut(Terrain t){
	  //TODO
	  if(m_nX>t.m_nX+t.tW) //Checking x is not optimal
		  return true;
	  if(m_nY<t.m_nY || m_nY>t.m_nY+t.tW)
	      return true;
	  if(m_nZ<t.m_nZ || m_nZ>t.m_nZ+t.tW)
		  return true;
	  return false;
	       
  }

}
