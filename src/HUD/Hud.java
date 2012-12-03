package HUD;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import main.GameState;

import org.lwjgl.opengl.GL11;

import text.BitmapText;

//Head Up Display
public class Hud {
	public static BitmapText t_money, t_lives, t_startgame, t_exit, t_lvl, t_gameover, t_finishedlvl, t_bubblesThisLvl;
	public static int[] menuitemsx= {240,760};
	public static int[] menuitemsy= {580,500,480,400};
	
	 public static void renderFrameMainMenu(){
		  startHUD();
		  
		  //draw rectangle
		  GL11.glBegin(GL11.GL_QUADS);
		  GL11.glColor3f(0.3f, 0.2f, 0.4f);
		  for (int i = 0; i < menuitemsy.length; i+=2) {
			  GL11.glVertex2f(menuitemsx[0], menuitemsy[i]);
			  GL11.glVertex2f(menuitemsx[0], menuitemsy[i+1]);
			  GL11.glVertex2f(menuitemsx[1], menuitemsy[i+1]);
			  GL11.glVertex2f(menuitemsx[1], menuitemsy[i]);
		 }
		  GL11.glEnd();
		  
		  GL11.glColor3f(0.9f, 0.9f, 0.9f);
		  t_startgame.renderString("START GAME",80);
		  t_exit.renderString("EXIT",80);

		  endHUD();
		  
	  }
	   
	  public static void renderFrameGameOver() {
		  startHUD();
		  
		  //draw rectangle
		  GL11.glBegin(GL11.GL_QUADS);
		  GL11.glColor3f(0.3f, 0.2f, 0.4f);
		  for (int i = 0; i < menuitemsy.length; i+=2) {
			  GL11.glVertex2f(menuitemsx[0], menuitemsy[i]);
			  GL11.glVertex2f(menuitemsx[0], menuitemsy[i+1]);
			  GL11.glVertex2f(menuitemsx[1], menuitemsy[i+1]);
			  GL11.glVertex2f(menuitemsx[1], menuitemsy[i]);
		 }
		  GL11.glEnd();
		  
		  GL11.glColor3f(0.9f, 0.9f, 0.9f);
		  t_startgame.renderString("START GAME",80);
		  t_exit.renderString("EXIT",80);

		  endHUD();
		
	}

	public static void renderFrameLvlDone() {
		startHUD();
		  //draw rectangle
		  glEnable (GL_BLEND);
		  glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		  GL11.glBegin(GL11.GL_QUADS);
		  GL11.glColor4f(0.3f, 0.2f, 0.4f,0.5f);
		  int i=2;
			  GL11.glVertex2f(menuitemsx[0], menuitemsy[i]);
			  GL11.glVertex2f(menuitemsx[0], menuitemsy[i+1]);
			  GL11.glVertex2f(menuitemsx[1], menuitemsy[i+1]);
			  GL11.glVertex2f(menuitemsx[1], menuitemsy[i]);
		 
		  GL11.glEnd();
		  
		  GL11.glColor3f(0.9f, 0.9f, 0.9f);
		  t_finishedlvl.renderString("Congratulations, you have finished level "+GameState.lvl,35);
		  t_exit.renderString("Next",80);

		  endHUD();
	}
	
	public static void startHUD() {
	    GL11.glMatrixMode(GL11.GL_PROJECTION);
	    GL11.glPushMatrix();
	    GL11.glLoadIdentity();
	    GL11.glOrtho(0, 1024, 0, 768, -1, 1);
	    GL11.glMatrixMode(GL11.GL_MODELVIEW);
	    GL11.glPushMatrix();
	    GL11.glLoadIdentity();
	  }
	  
	  public static void endHUD() {
	    GL11.glMatrixMode(GL11.GL_PROJECTION);
	    GL11.glPopMatrix();
	    GL11.glMatrixMode(GL11.GL_MODELVIEW);
	    GL11.glPopMatrix();
	  }
}
