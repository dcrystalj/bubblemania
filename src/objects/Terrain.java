package objects;
import org.lwjgl.opengl.GL11;

public class Terrain extends Model3D
{

public float tW;

public Terrain(float w) {
		super(w);
		this.tW=w;
}

  public void render3D()
  {
    // model view stack 
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
   /* 
    // save current matrix
    GL11.glPushMatrix();
    
    // TRANSLATE 
    GL11.glTranslatef(m_nX, m_nY, m_nZ);
    
    // ROTATE and SCALE
    GL11.glTranslatef(tW/2,tW/2,tW/2);
    if (m_rZ!=0)
      GL11.glRotatef(m_rZ, 0, 0, 1);
    if (m_rY!=0)
      GL11.glRotatef(m_rY, 0, 1, 0);
    if (m_rX!=0)
      GL11.glRotatef(m_rX, 1, 0, 0);
    if (m_sX!=1 || m_sY!=1 || m_sZ!=1)
      GL11.glScalef(m_sX, m_sY, m_sZ);
    GL11.glTranslatef(-tW/2,-tW/2,-tW/2);
    


    System.out.println("terain x "+ m_nX +"   "+ m_nY +"  "+ m_nZ);
*/
    renderModel();

    GL11.glPopMatrix();
    
    
    // discard current matrix
  }
  
  private void renderModel()
  {
    GL11.glBegin(GL11.GL_QUADS); // draw independent triangles
    GL11.glColor3f(0, 0, 1);
    
    //right
    GL11.glVertex3f( tW,0.0f,0.0f);    // lower right vertex
    GL11.glVertex3f( tW,0.0f, tW);    // upper right vertex
    GL11.glVertex3f( tW, tW, tW);    // upper left vertex
    GL11.glVertex3f( tW, tW,0.0f);    // lower left vertex

    //left
    GL11.glVertex3f(0.0f,0.0f,0.0f);    // lower right vertex
    GL11.glVertex3f(0.0f, tW,0.0f);    // lower left vertex
    GL11.glVertex3f(0.0f, tW, tW);    // upper left vertex
    GL11.glVertex3f(0.0f,0.0f, tW);    // upper right vertex
    
    //bottom
    GL11.glColor3f(0, 1, 0.9f);
    GL11.glVertex3f(0.0f,0.0f,0.0f);    // lower right vertex
    GL11.glVertex3f(0.0f,0.0f, tW);    // lower left vertex
    GL11.glVertex3f( tW,0.0f, tW);    // upper left vertex
    GL11.glVertex3f( tW,0.0f,0.0f);    // upper right vertex
    
    //back
    GL11.glColor3f(0, 0, 0.5f);
    GL11.glColor3f(0, 0, 0.5f);
    GL11.glVertex3f(0.0f,0.0f,0.0f);    // lower right vertex
    GL11.glVertex3f( tW,0.0f,0.0f);    // upper right vertex
    GL11.glVertex3f( tW, tW,0.0f);    // lower left vertex
    GL11.glVertex3f(0.0f, tW,0.0f);    // upper left vertex
  
    
    //front
    GL11.glColor3f(0, 0, 0.5f);
    GL11.glVertex3f(0.0f,0.0f, tW);    // lower right vertex
    GL11.glVertex3f(0.0f, tW, tW);    // upper right vertex
    GL11.glVertex3f( tW, tW, tW);    // lower left vertex
    GL11.glVertex3f( tW,0.0f, tW);    // upper left vertex
    
    //top
    GL11.glVertex3f(0.0f, tW, tW);    // lower right vertex
    GL11.glVertex3f(0.0f, tW,0.0f);    // upper right vertex
    GL11.glVertex3f( tW, tW,0.0f);    // lower left vertex
    GL11.glVertex3f( tW, tW, tW);    // upper left vertex
    GL11.glEnd();
    
  }
}
