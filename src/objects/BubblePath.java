package objects;

import java.awt.Polygon;
import java.util.LinkedList;

import main.GameState;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import text.Bitmap;

public class BubblePath extends Model3D{
	LinkedList<Vector3f> path;	//list of point on path for bubbles
	float pathWidth=30;
	
	public BubblePath(Vector3f start, Vector3f end){
		super(30);
		float height=5;	//Height of path for bubbles
		//Generate simple path for bubbles
		path=new LinkedList<Vector3f>();
		start.y=(float) (height+1);
		path.add(start);
		path.add(new Vector3f(start.x+250,height,start.z));
		path.add(new Vector3f(start.x+250,height,start.z+250));
		path.add(new Vector3f(end.x-20,height,start.z+250));
		end.y=height;
		path.add(end);
		//We create polygon path, for building towers
		createListPolygon();
	}
	public Vector3f get(int index){	//Returns point on path
		return path.get(index);
	}
	public void render3D()
	  {
		 // model view stack 
	    GL11.glMatrixMode(GL11.GL_MODELVIEW);
	    
	    // save current matrix
	    GL11.glPushMatrix();

	    // TRANSLATE 
	    GL11.glTranslatef(m_nX, m_nY, m_nZ);

	    // ROTATE and SCALE
//	    GL11.glTranslatef(cW/2, cW/2, cW/2);
	    GL11.glTranslatef(WIDTH/2-deltax, WIDTH/2-deltay, WIDTH/2-deltaz);
	    if (m_rZ!=0)
	      GL11.glRotatef(m_rZ, 0, 0, 1);
	    if (m_rY!=0)
	      GL11.glRotatef(m_rY, 0, 1, 0);
	    if (m_rX!=0)
	      GL11.glRotatef(m_rX, 1, 0, 0);
	    if (m_sX!=1 || m_sY!=1 || m_sZ!=1)
	      GL11.glScalef(m_sX, m_sY, m_sZ);
//	    GL11.glTranslatef(-cW/2, -cW/2, -cW/2);    
	    GL11.glTranslatef(-WIDTH/2+deltax, -WIDTH/2+deltay, -WIDTH/2+deltaz);
	    renderModel();
	    
	    // discard current matrix
	    GL11.glPopMatrix();
	  }
	  
	  private void renderModel()
	  {
	    
//	    GL11.glColor4f(0.8f, 0.8f, 0.8f,0.50f);
	    GL11.glColor4f(1,1,1,1);	

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Bitmap.font.get(11));
		GL11.glBegin(GL11.GL_QUADS);
	    for(int i=0; i<path.size()-1;i++){
	    	Vector3f v1=path.get(i);
	    	Vector3f v2=path.get(i+1);
	    	if(i%2==0){
	    		GL11.glTexCoord2f(0, 0);GL11.glVertex3f( v1.x-pathWidth/2, 0.008f, v1.z-pathWidth/2);    // upper left vertex
	    		GL11.glTexCoord2f(3, 0);GL11.glVertex3f(  v1.x-pathWidth/2, 0.008f, v1.z+pathWidth/2);    // lower left vertex
	    		GL11.glTexCoord2f(3, 3);GL11.glVertex3f( v2.x+pathWidth/2, 0.008f, v2.z+pathWidth/2);    // lower right vertex
	    		GL11.glTexCoord2f(0, 3);GL11.glVertex3f(  v2.x+pathWidth/2, 0.008f, v2.z-pathWidth/2);    // upper right vertex
	    	}
	    	else{
	    		GL11.glTexCoord2f(0, 0);GL11.glVertex3f( v1.x-pathWidth/2, 0.008f, v1.z-pathWidth/2);    // upper left vertex
	    		GL11.glTexCoord2f(3, 0);GL11.glVertex3f(  v2.x-pathWidth/2, 0.008f, v2.z+pathWidth/2);    // lower left vertex
	    		GL11.glTexCoord2f(3, 3);GL11.glVertex3f( v2.x+pathWidth/2, 0.008f, v2.z+pathWidth/2);    // lower right vertex
	    		GL11.glTexCoord2f(0, 3);GL11.glVertex3f(  v1.x+pathWidth/2, 0.008f, v1.z-pathWidth/2);    // upper right vertex
	    	}
	    }
	    GL11.glEnd();
	    GL11.glDisable(GL11.GL_TEXTURE_2D);
	    GL11.glDisable(GL11.GL_BLEND);
	    
	  }
	  //Create list of polygons for collision detection when building tower
	  public void createListPolygon(){
		  GameState.pathPoly=new LinkedList<Polygon>();
		  for(int i=0; i<path.size()-1;i++){
		    	Vector3f v1=path.get(i);
		    	Vector3f v2=path.get(i+1);

		    	int[] x=new int[4];
		    	int[] y=new int[4];
		    	if(i%2==0){
		    		x[0]=(int)(v1.x-pathWidth/2);
		    		x[1]=(int)(v1.x-pathWidth/2);
		    		x[2]=(int)(v2.x+pathWidth/2);
		    		x[3]=(int)(v2.x+pathWidth/2);

		    		y[0]=(int)(v1.z-pathWidth/2);
		    		y[1]=(int)(v1.z+pathWidth/2);
		    		y[2]=(int)(v2.z+pathWidth/2);
		    		y[3]=(int)(v2.z-pathWidth/2);
		    	}
		    	else{
		    		x[0]=(int)(v1.x-pathWidth/2);
		    		x[1]=(int)(v2.x-pathWidth/2);
		    		x[2]=(int)(v2.x+pathWidth/2);
		    		x[3]=(int)(v1.x+pathWidth/2);

		    		y[0]=(int)(v1.z-pathWidth/2);
		    		y[1]=(int)(v2.z+pathWidth/2);
		    		y[2]=(int)(v2.z+pathWidth/2);
		    		y[3]=(int)(v1.z-pathWidth/2);
		    	}
		    	GameState.pathPoly.add(new Polygon(x,y,4));
		  }
	  }
}
