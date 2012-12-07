package objects;
import glmodel.GLModel;

import org.lwjgl.opengl.GL11;

public class Obj3D extends Model3D
{
  GLModel m_Obj = null;
  public Obj3D(String p_strFileName)
  {
    try {
      m_Obj = new GLModel(p_strFileName);
    }
    catch(Exception e)
    {
      System.err.println(e.getMessage());
    }    
  }

  public void render3D()
  {
    // model view stack 
    GL11.glMatrixMode(GL11.GL_MODELVIEW);

    // save current matrix
    GL11.glPushMatrix();

    // TRANSLATE 
    GL11.glTranslatef(m_nX, m_nY, m_nZ);
    if (m_rZ!=0)
      GL11.glRotatef(m_rZ, 0, 0, 1);
    if (m_rY!=0)
      GL11.glRotatef(m_rY, 0, 1, 0);
    if (m_rX!=0)
      GL11.glRotatef(m_rX, 1, 0, 0);
    GL11.glScalef(m_sX, m_sY, m_sZ);
    GL11.glColor3f(1,1,1);
    m_Obj.render();
    
    GL11.glPopMatrix();
  }
}
