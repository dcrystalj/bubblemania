package objects;
import org.lwjgl.opengl.GL11;
import window.BaseWindow;

public class Cube extends Model3D
{
  public Cube(float w) {
		super(w);
	}

public float cW=10;
  
  public void render3D()
  {
    // model view stack 
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    
    // save current matrix
    GL11.glPushMatrix();

    // TRANSLATE 
    GL11.glTranslatef(m_nX, m_nY, m_nZ);

    // ROTATE and SCALE
//    GL11.glTranslatef(cW/2, cW/2, cW/2);
    GL11.glTranslatef(WIDTH/2-deltax, WIDTH/2-deltay, WIDTH/2-deltaz);
    if (m_rZ!=0)
      GL11.glRotatef(m_rZ, 0, 0, 1);
    if (m_rY!=0)
      GL11.glRotatef(m_rY, 0, 1, 0);
    if (m_rX!=0)
      GL11.glRotatef(m_rX, 1, 0, 0);
    if (m_sX!=1 || m_sY!=1 || m_sZ!=1)
      GL11.glScalef(m_sX, m_sY, m_sZ);
//    GL11.glTranslatef(-cW/2, -cW/2, -cW/2);    
    GL11.glTranslatef(-WIDTH/2+deltax, -WIDTH/2+deltay, -WIDTH/2+deltaz);
    renderModel();
    
    // discard current matrix
    GL11.glPopMatrix();
  }
  
  private void renderModel()
  {
    GL11.glBegin(GL11.GL_QUADS); // draw independent triangles
    GL11.glColor3f(1, 0, 0);
    
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
    GL11.glColor3f(0.9f, 0, 0);
    GL11.glVertex3f(0.0f,0.0f,0.0f);    // lower right vertex
    GL11.glVertex3f(0.0f,0.0f, cW);    // lower left vertex
    GL11.glVertex3f( cW, 0.0f, cW);    // upper left vertex
    GL11.glVertex3f( cW,0.0f,0.0f);    // upper right vertex
    
    //back
    GL11.glColor3f(0.8f, 0, 0);
    GL11.glVertex3f(0.0f,0.0f,0.0f);    // lower right vertex
    GL11.glVertex3f( cW,0.0f,0.0f);    // upper right vertex
    GL11.glVertex3f( cW, cW,0.0f);    // lower left vertex
    GL11.glVertex3f(0.0f, cW,0.0f);    // upper left vertex
  
    
    //front
    GL11.glColor3f(0.8f, 0, 0);
    GL11.glVertex3f(0.0f,0.0f, cW);    // lower right vertex
    GL11.glVertex3f(0.0f, cW, cW);    // upper right vertex
    GL11.glVertex3f( cW, cW, cW);    // lower left vertex
    GL11.glVertex3f( cW,0.0f, cW);    // upper left vertex
    
    
    //top
    GL11.glColor3f(1, 0, 0);
    GL11.glVertex3f(0.0f, cW, cW);    // lower right vertex
    GL11.glVertex3f(0.0f, cW,0.0f);    // upper right vertex
    GL11.glVertex3f( cW, cW,0.0f);    // lower left vertex
    GL11.glVertex3f( cW, cW, cW);    // upper left vertex
    GL11.glEnd();
    
  }
}
