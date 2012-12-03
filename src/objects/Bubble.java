package objects;

import main.*;	//Import of main package
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Vector3f;

public class Bubble extends Model3D
{
  private Sphere s = new Sphere();
  public float radius = 1;
  public float[] color = {1, 1, 1};
  public float safetyDistance=25;	//Distance between this and next bubble
  public int checkpoint=0;  //checkPoint, which is next point on path
  float speed=0.5f;	//Speed of a bubble

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
  public void move() {
	  if(checkpoint<GameState.bubblesPath.path.size()){
		  //Bubbles next target destination
		  Vector3f next=GameState.bubblesPath.path.get(checkpoint);
		  //Direction to that target
		  Vector3f direction=new Vector3f(next.x-this.m_nX,next.y-this.m_nY,next.z-this.m_nZ);
		  direction.normalise();	//Normalise vector of direction to next checkpoint
		  //We move in that direction with some speed
		  direction.scale(speed);
		  m_nX+=direction.x;
		  m_nY+=direction.y;
		  m_nZ+=direction.z;
		  //If bubble is close enough to target it can move on to next checkpoint
		  //We add some random mistake option, that it doesnt look so automized
		  if(distanceBetween(new Vector3f(m_nX,m_nY,m_nZ), next)<1.5+Math.random()*6){
			  checkpoint++;
		  }
	  }
	  else if(this.show){
		  GameState.lives--;	//In that case bubble comes through so we lose 1 life
		  this.show=false;
	  }
	  
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
