package objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Timer;
import org.lwjgl.util.glu.GLU;

//Class for saving object tower
public class Tower extends Model3D {
  
	public float[] color = {0, 1, 0};
	public float shootingRadius = 35;
	public float cW=10;
	
	public Tower(float w) {
		super(w);
		// TODO Auto-generated constructor stub
	}

	@Override
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
//			    GL11.glTranslatef(cW/2, cW/2, cW/2);
			    GL11.glTranslatef(WIDTH/2-deltax, WIDTH/2-deltay, WIDTH/2-deltaz);
			    if (m_rZ!=0)
			      GL11.glRotatef(m_rZ, 0, 0, 1);
			    if (m_rY!=0)
			      GL11.glRotatef(m_rY, 0, 1, 0);
			    if (m_rX!=0)
			      GL11.glRotatef(m_rX, 1, 0, 0);
			    if (m_sX!=1 || m_sY!=1 || m_sZ!=1)
			      GL11.glScalef(m_sX, m_sY*50, m_sZ);	//SCALING ON Y!!!
			    
//			    GL11.glTranslatef(-cW/2, -cW/2, -cW/2);    
			    GL11.glTranslatef(-WIDTH/2+deltax, -WIDTH/2+deltay, -WIDTH/2+deltaz);
			    renderModel();
			    
			    // discard current matrix
			    GL11.glPopMatrix();
		  }
	  }
	private void renderModel()
	{
		//Drawing shooting range of tower
	    GL11.glBegin(GL11.GL_LINE_LOOP);
	    GL11.glColor3f(color[0],color[0],color[0]);
	    float DEG2RAD = 3.14159f/180.f;
	    for (int i=0; i<360; i++)
	    {
	       float degInRad = i*DEG2RAD;
	       GL11.glVertex3f((float)Math.cos(degInRad)*shootingRadius+cW/2, 0, (float)Math.sin(degInRad)*shootingRadius+cW/2);
	    }
	  
	    GL11.glEnd();
		
	    GL11.glBegin(GL11.GL_QUADS); // draw independent triangles
	    GL11.glColor3f(color[0],color[1],color[2]);
	    
	    //right
	    GL11.glVertex3f( cW,0.0f,0.0f);    // lower right vertex
	    GL11.glVertex3f( cW,0.0f, cW);    // upper right vertex
	    GL11.glVertex3f( cW, cW, cW);    // upper left vertex
	    GL11.glVertex3f( cW, cW,0.0f);    // lower left vertex

	    //left
	    GL11.glVertex3f(0.0f,0.0f,0.0f);    // lower right vertex
	    GL11.glVertex3f(0.0f, cW,0.0f);    // lower left vertex
	    GL11.glVertex3f(0.0f, cW, cW);    // upper left vertex
	    GL11.glVertex3f(0.0f,0.0f, cW);    // upper right vertex
	    
	    //bottom
	    GL11.glColor3f(color[0],color[1],color[2]);
	    GL11.glVertex3f(0.0f,0.0f,0.0f);    // lower right vertex
	    GL11.glVertex3f(0.0f,0.0f, cW);    // lower left vertex
	    GL11.glVertex3f( cW, 0.0f, cW);    // upper left vertex
	    GL11.glVertex3f( cW,0.0f,0.0f);    // upper right vertex
	    
	    //back
	    GL11.glColor3f(color[0],color[1],color[2]);
	    GL11.glVertex3f(0.0f,0.0f,0.0f);    // lower right vertex
	    GL11.glVertex3f( cW,0.0f,0.0f);    // upper right vertex
	    GL11.glVertex3f( cW, cW,0.0f);    // lower left vertex
	    GL11.glVertex3f(0.0f, cW,0.0f);    // upper left vertex
	  
	    
	    //front
	    GL11.glColor3f(color[0],color[1],color[2]);
	    GL11.glVertex3f(0.0f,0.0f, cW);    // lower right vertex
	    GL11.glVertex3f(0.0f, cW, cW);    // upper right vertex
	    GL11.glVertex3f( cW, cW, cW);    // lower left vertex
	    GL11.glVertex3f( cW,0.0f, cW);    // upper left vertex
	    
	    
	    //top
	    GL11.glColor3f(color[0],color[1],color[2]);
	    GL11.glVertex3f(0.0f, cW, cW);    // lower right vertex
	    GL11.glVertex3f(0.0f, cW,0.0f);    // upper right vertex
	    GL11.glVertex3f( cW, cW,0.0f);    // lower left vertex
	    GL11.glVertex3f( cW, cW, cW);    // upper left vertex
	    
	    GL11.glEnd();
	    
	    
	  }
	public void popBubble(){	//Check whether there is a bubble in his radius and pops it
		for (Bubble b : GameState.bubbles){
			if(b.show){	//If not popped yet or not gone out of visible area
				float x = b.m_nX-this.m_nX;
				float y = b.m_nY-this.m_nY;
				float z = b.m_nZ-this.m_nZ;
				float dToBubble=(float)Math.sqrt(x*x + y*y + z*z)-b.radius; //distance from tower to bubble
				if(dToBubble<=shootingRadius){
					b.show=false;	//If we popped one bubble we quit function
					
					try {
						wait(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
			}
		}
		
	}

	
}
