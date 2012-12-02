package objects;

import main.*;	//Import of main package
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Vector3f;

public class Bubble extends Model3D
{
  private Sphere s = new Sphere();
  public float radius = 1;
  public float[] speed = {5f, 0.01f, 0.01f};
  public float[] color = {1, 1, 1};
  public float safetyDistance=25;	//Distance between this and next bubble
  public int checkpoint=0;  //checkPoint, which is next point on path

  public Bubble(float r) {
	  super(r);
	  radius = r;
  }
  public Bubble(float r, Vector3f p){
	  super(r);
	  radius = r;
	  m_nX = p.x;
	  m_nY = p.y;
	  m_nZ = p.z;

  }
  
  public void setPos(float[] pos) {
    m_nX = pos[0];
    m_nY = pos[1];
    m_nZ = pos[2];
  }
  public void move(int timeMilis) {
	  Vector3f v1=GameState.bubblesPath.get(0);
	  Vector3f v2=GameState.bubblesPath.get(1);
	  Vector3f v3=GameState.bubblesPath.get(2);
	  Vector3f t=new Vector3f(m_nX, m_nY, m_nZ);
	  if(checkpoint==0){
		  m_nX += 5.f*timeMilis/100;
		  if(distanceBetween(v1,t)<1)
			  checkpoint++;
	  }
	  else if(checkpoint==1){
		  m_nZ += 5.f*timeMilis/100;
		  if(distanceBetween(v2,t)<2)
			  checkpoint++;
	  }
	  else if(checkpoint==2){
		  m_nX += 5.f*timeMilis/100;
		  if(distanceBetween(v3,t)<3)
			  checkpoint++;
	  }
	  else if(checkpoint==3){
		  m_nZ += 5.f*timeMilis/100;
	  }
	  //Direction using direction vector, deprecated :) :D
 //	  Vector3 next=Refactored.bubblesPath.get(checkpoint);
//	  Vector3 direction=new Vector3(next.x-this.m_nX,next.y-this.m_nY,next.z-this.m_nZ);
//	  if(checkpoint<(Refactored.bubblesPath.path.size()-1)){
//		  if(Math.abs(direction.len())<1){
//			  checkpoint++;
//			  next=Refactored.bubblesPath.get(checkpoint);
//			  direction=new Vector3(next.x-m_nX,next.y-m_nY,next.z-m_nZ);
//		  }
//		  direction.nor();	//Get direction vector and normalize it
//		  System.out.println("Dolzina: "+direction.toString());
//		  m_nX += direction.x*(speed[0]*timeMilis);
//		  m_nY += direction.y*(speed[1]*timeMilis);
//		  m_nZ += direction.z*(speed[2]*timeMilis);
//	  }
//	  Vector3 thisVec=new Vector3(m_nX,m_nY,m_nZ);
//	  thisVec = thisVec.slerp(next, 0.5f);
//	  m_nX += 5.f;
//	   //make object look towards currentTarget
  }
  public float distanceBetween(Vector3f v1, Vector3f v2){
	  //We DO NOT consider y coordinates
	  return (float)Math.sqrt(Math.pow(v1.x-v2.x,2)+Math.pow(v1.z-v2.z,2));
  }

  public void render3D()
  {
	  if(show){
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
  public boolean isOut(Terrain t){
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
