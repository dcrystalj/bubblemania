package towers;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glEnable;
import glmodel.GLModel;
import main.*;	//Import of main package
import objects.Bubble;
import objects.Model3D;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import threads.PopBubbles;


//Class for saving object tower
public class Tower extends Model3D {
	
	public int shootingSpeed=1000;
	public float shootingRadius;
	public static int cost;
	public float cW=WIDTH;
	public boolean shooting=true;
	public PopBubbles shoot=null;
	GLModel m_Obj = null;
	public boolean building=false;
	
	public Tower(float w) {
		super(w);
	}

	@Override
	public synchronized void render3D()
	  {
		  if(show){
			// model view stack 
			    GL11.glMatrixMode(GL11.GL_MODELVIEW);
			    
			    // save current matrix
			    GL11.glPushMatrix();

			    // TRANSLATE 
			    GL11.glTranslatef(m_nX, m_nY, m_nZ);

			    // ROTATE and SCALE
			    GL11.glTranslatef(WIDTH/2-deltax, WIDTH/2-deltay, WIDTH/2-deltaz);
			    if (m_rZ!=0)
			      GL11.glRotatef(m_rZ, 0, 0, 1);
			    if (m_rY!=0)
			    	GL11.glRotatef(m_rY, 0, 1, 0);
			    if (m_rX!=0)
			      GL11.glRotatef(m_rX, 1, 0, 0);
			    if (m_sX!=1 || m_sY!=1 || m_sZ!=1)
			      GL11.glScalef(m_sX, m_sY, m_sZ);
			    
			    GL11.glTranslatef(-WIDTH/2+deltax, -WIDTH/2+deltay, -WIDTH/2+deltaz);
//			    GL11.glColor3f(0.7f,0.7f,0.7f);		//BLACK COLOR for radius of tower
			    //Draw .obj tower
			    GL11.glColor3f(1f,1f,1f);
			    glEnable(GL_CULL_FACE);
			    glEnable(GL11.GL_TEXTURE_2D);
			    m_Obj.render();
			    
//			    shooting range of tower
			    if(GameState.lighting)
			    	GL11.glDisable(GL11.GL_LIGHTING);
			    GL11.glBegin(GL11.GL_LINE_LOOP);
			    GL11.glColor3f(0,0,0);		//BLACK COLOR for radius of tower
			    float DEG2RAD = 3.14159f/180.f;
			    for (int i=0; i<360; i++)
			    {
			       float degInRad = i*DEG2RAD;
			       GL11.glVertex3f((float)Math.cos(degInRad)*shootingRadius+cW/2, 1, (float)Math.sin(degInRad)*shootingRadius+cW/2);
			       GL11.glVertex3f((float)Math.cos(degInRad)*(shootingRadius-3)+cW/2, 1, (float)Math.sin(degInRad)*(shootingRadius-3)+cW/2);
			    }
			    GL11.glEnd();
			    
			    //Draw red circle above tower to know which tower we are building
			    if(building){
			    	GL11.glBegin(GL11.GL_LINE_LOOP);
			    	GL11.glColor3f(1,0,0);		//BLACK COLOR for radius of tower
			    	for (int i=0; i<360; i++)
			    	{
			    		float degInRad = i*DEG2RAD;
			    		GL11.glVertex3f((float)Math.cos(degInRad)*5+cW/2, 20, (float)Math.sin(degInRad)*5+cW/2);
			    		GL11.glVertex3f((float)Math.cos(degInRad)*2+cW/2, 20, (float)Math.sin(degInRad)*2+cW/2);
			    	}
			    	GL11.glEnd();
			    }
			    GL11.glPopMatrix();
			    if(GameState.lighting)
			    	GL11.glEnable(GL11.GL_LIGHTING);
		  }
	  }
	public boolean popBubble(){	//Check whether there is a bubble in his radius and pops it, and returns whether it pops bloon
		for (Bubble b : GameState.bubbles){
			if(b.show){	//If not popped yet or not gone out of visible area
				float x = b.m_nX-this.m_nX;
				float y = b.m_nY-this.m_nY;
				float z = b.m_nZ-this.m_nZ;
				float dToBubble=(float)Math.sqrt(x*x + y*y + z*z)-b.radius; //distance from tower to bubble
				if(dToBubble<=shootingRadius){
					rotateOnShoot(b);
					b.show=false;	//If we popped one bubble we quit function
					GameState.money+=10; //For every bloon we add money
					GameState.poppedBubbles++;
					return true;
				}
			}
		}
		return false;
		
		
	}
	public void rotateOnShoot(Bubble b){
		//We don't consider height, y coordinate
		Vector2f toBubble=new Vector2f(b.m_nX-this.m_nX, b.m_nZ-this.m_nZ);
		toBubble.normalise();
		//Calculating rotation with polar coordinates tan function
		this.m_rY=(float)Math.toDegrees(Math.atan2(toBubble.y, -toBubble.x))+90; 
		
	}
	
}
