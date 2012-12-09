package objects;
import org.lwjgl.opengl.GL11;

import text.Bitmap;

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

    renderModel();

    // discard current matrix
  }
  
  private void renderModel()
  {
	GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
	GL11.glEnable(GL11.GL_TEXTURE_2D);
	GL11.glDisable(GL11.GL_DEPTH_TEST);
	GL11.glDisable(GL11.GL_LIGHTING);
	GL11.glDisable(GL11.GL_BLEND);
	GL11.glColor4f(1,1,1,1);
	
	GL11.glBindTexture(GL11.GL_TEXTURE_2D, Bitmap.font.get(9));
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_S,GL11.GL_CLAMP); 
	GL11.glTexParameteri( 
			GL11.GL_TEXTURE_2D, 
			GL11.GL_TEXTURE_WRAP_T, 
			GL11.GL_CLAMP 
	); 
    GL11.glBegin(GL11.GL_QUADS); // draw independent triangles

    //right
    GL11.glNormal3f(-1, 0, 0);
    GL11.glTexCoord2f(0, 0); GL11.glVertex3f(2*tW,-tW,-tW);    // lower right vertex
    GL11.glTexCoord2f(1, 0);GL11.glVertex3f(2*tW,-tW,2*tW);    // upper right vertex
    GL11.glTexCoord2f(1, 1);GL11.glVertex3f(2*tW,2*tW,2*tW);    // upper left vertex
    GL11.glTexCoord2f(0, 1);GL11.glVertex3f(2*tW,2*tW,-tW);    // lower left vertex
    GL11.glEnd();
    
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, Bitmap.font.get(8));
    GL11.glBegin(GL11.GL_QUADS); // draw independent triangles
    //left
    GL11.glNormal3f(1, 0, 0);
    GL11.glTexCoord2f(0, 0);GL11.glVertex3f(-tW,-tW,-tW);    // lower right vertex
    GL11.glTexCoord2f(1, 0);GL11.glVertex3f(-tW,2*tW,-tW);    // lower left vertex
    GL11.glTexCoord2f(1, 1);GL11.glVertex3f(-tW,2*tW,2*tW);    // upper left vertex
    GL11.glTexCoord2f(0, 1);GL11.glVertex3f(-tW,-tW,2*tW);    // upper right vertex
    GL11.glEnd();
    
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, Bitmap.font.get(7));
    GL11.glBegin(GL11.GL_QUADS); // draw independent triangles
    //back
    GL11.glNormal3f(0, 0, 1);
    GL11.glTexCoord2f(0, 0);GL11.glVertex3f(-tW,-tW,-tW);    // lower right vertex
    GL11.glTexCoord2f(1, 0);GL11.glVertex3f(2*tW,-tW,-tW);    // upper right vertex
    GL11.glTexCoord2f(1, 1);GL11.glVertex3f(2*tW,2*tW,-tW);    // lower left vertex
    GL11.glTexCoord2f(0, 1);GL11.glVertex3f(-tW,2*tW,-tW);    // upper left vertex
    GL11.glEnd();
    
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, Bitmap.font.get(5));
    GL11.glBegin(GL11.GL_QUADS); // draw independent triangles
    
    //front
    GL11.glNormal3f(0, 0, -1);
    GL11.glTexCoord2f(0, 0);GL11.glVertex3f(-tW,-tW,2*tW);    // lower right vertex
    GL11.glTexCoord2f(1, 0);GL11.glVertex3f(-tW,2*tW,2*tW);    // upper right vertex
    GL11.glTexCoord2f(1, 1);GL11.glVertex3f(2*tW,2*tW,2*tW);    // lower left vertex
    GL11.glTexCoord2f(0, 1);GL11.glVertex3f(2*tW,-tW,2*tW);    // upper left vertex
    GL11.glEnd();
    
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, Bitmap.font.get(10));
    GL11.glBegin(GL11.GL_QUADS); // draw independent triangles
    //top
    
    GL11.glNormal3f(0, -1, 0);
    GL11.glTexCoord2f(0, 0);GL11.glVertex3f(-tW,2*tW,2*tW);    // lower right vertex
    GL11.glTexCoord2f(1, 0);GL11.glVertex3f(-tW,2*tW,-tW);    // upper right vertex
    GL11.glTexCoord2f(1, 1);GL11.glVertex3f(2*tW,2*tW,-tW);    // lower left vertex
    GL11.glTexCoord2f(0, 1);GL11.glVertex3f(2*tW,2*tW,2*tW);    // upper left vertex
    GL11.glEnd();

    //bottom
    GL11.glNormal3f(0, 1, 0);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, Bitmap.font.get(6));
    GL11.glBegin(GL11.GL_QUADS);
    GL11.glColor3f(1, 1, 1f);
    GL11.glTexCoord2f(0, 0);  	GL11.glVertex3f(-tW,-tW,-tW);    // lower right vertex
    GL11.glTexCoord2f(4, 0);	GL11.glVertex3f(-tW,-tW,2*tW);    // lower left vertex
    GL11.glTexCoord2f(4, 4);	GL11.glVertex3f(2*tW,-tW,2*tW);    // upper left vertex
    GL11.glTexCoord2f(0, 4);	GL11.glVertex3f(2*tW,-tW,-tW);    // upper right vertex
    GL11.glEnd();
    
    //bottom

    GL11.glColor4f(1,1,1,0.2f);
    GL11.glNormal3f(0, 1, 0);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, Bitmap.font.get(3));
    GL11.glBegin(GL11.GL_QUADS);
    GL11.glTexCoord2f(0, 0);  	GL11.glVertex3f(-tW,0,-tW);    // lower right vertex
    GL11.glTexCoord2f(4, 0);	GL11.glVertex3f(-tW,0,2*tW);    // lower left vertex
    GL11.glTexCoord2f(4, 4);	GL11.glVertex3f(2*tW,0,2*tW);    // upper left vertex
    GL11.glTexCoord2f(0, 4);	GL11.glVertex3f(2*tW,0,-tW);    // upper right vertex
    GL11.glEnd();
    
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    GL11.glPopAttrib();
    GL11.glEnable(GL11.GL_LIGHTING);
  }
}
