package objects;

import java.util.LinkedList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

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
	    GL11.glBegin(GL11.GL_QUADS); // draw independent quads
	    GL11.glColor4f(0.8f, 0.8f, 0.8f,0.5f);
	    
	    for(int i=0; i<path.size()-1;i++){
	    	Vector3f v1=path.get(i);
	    	Vector3f v2=path.get(i+1);
	    	if(i%2==0){
		    	GL11.glVertex3f( v1.x-pathWidth/2, 0.008f, v1.z-pathWidth/2);    // upper left vertex
		    	GL11.glVertex3f(  v1.x-pathWidth/2, 0.008f, v1.z+pathWidth/2);    // lower left vertex
		    	GL11.glVertex3f( v2.x+pathWidth/2, 0.008f, v2.z+pathWidth/2);    // lower right vertex
			    GL11.glVertex3f(  v2.x+pathWidth/2, 0.008f, v2.z-pathWidth/2);    // upper right vertex
	    	}
	    	else{
	    		GL11.glVertex3f( v1.x-pathWidth/2, 0.008f, v1.z-pathWidth/2);    // upper left vertex
		    	GL11.glVertex3f(  v2.x-pathWidth/2, 0.008f, v2.z+pathWidth/2);    // lower left vertex
		    	GL11.glVertex3f( v2.x+pathWidth/2, 0.008f, v2.z+pathWidth/2);    // lower right vertex
			    GL11.glVertex3f(  v1.x+pathWidth/2, 0.008f, v1.z-pathWidth/2);    // upper right vertex
	    	}
	    	
	    }
	    GL11.glEnd();
	    
	  }
}
