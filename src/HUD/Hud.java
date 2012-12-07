package HUD;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import main.GameState;
import main.Refactored;

import objects.Terrain;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import text.Bitmap;
import towers.TowerGun;
import towers.TripleGun;

//Head Up Display
public class Hud {
	public static Bitmap t_money, t_lives, t_1item, t_2item, t_3item, t_lvl, t_gameover, t_choose, t_finishedlvl, t_bubblesThisLvl, p_pic1, t_towerprice;
	public static int[] menuitemsx= {240,760};
	public static int[] menuitemsy= {580,500,480,400,380,300};
	
	 public static void renderFrameMainMenu(){
		  startHUD();
		  
		  //draw rectangle
		  GL11.glBegin(GL11.GL_QUADS);
		  GL11.glColor3f(0.3f, 0.2f, 0.4f);
		  for (int i = 0; i < menuitemsy.length-2; i+=2) {
			  GL11.glVertex2f(menuitemsx[0], menuitemsy[i]);
			  GL11.glVertex2f(menuitemsx[0], menuitemsy[i+1]);
			  GL11.glVertex2f(menuitemsx[1], menuitemsy[i+1]);
			  GL11.glVertex2f(menuitemsx[1], menuitemsy[i]);
		 }
		  GL11.glEnd();
		  
		  GL11.glColor3f(0.1f, 0.3f, 0.3f);
		  t_finishedlvl.charPos[0] = (int)(GameState.WIDTH-t_1item.textWidth("BuBBle MaNiA", 85)/2);
		  t_finishedlvl.charPos[1] = menuitemsy[0]+20;
		  t_finishedlvl.renderString("BuBBle MaNiA",85);
		  GL11.glColor3f(0.9f, 0.9f, 0.9f);
		  t_1item.renderString("START GAME",80);
		  t_2item.renderString("EXIT",80);

		  endHUD();
		  
	  }
	   
	  public static void renderFrameGameOver() {
		  startHUD();
		  
		  //draw rectangle
		  GL11.glBegin(GL11.GL_QUADS);
		  GL11.glColor3f(0.3f, 0.2f, 0.4f);
		  for (int i = 0; i < menuitemsy.length-2; i+=2) {
			  GL11.glVertex2f(menuitemsx[0], menuitemsy[i]);
			  GL11.glVertex2f(menuitemsx[0], menuitemsy[i+1]);
			  GL11.glVertex2f(menuitemsx[1], menuitemsy[i+1]);
			  GL11.glVertex2f(menuitemsx[1], menuitemsy[i]);
		 }
		  GL11.glEnd();
		  
		  GL11.glColor3f(0.1f, 0.3f, 0.3f);
		  t_finishedlvl.charPos[0] = (int)(GameState.WIDTH-t_1item.textWidth("GAME OVER", 85)/2);
		  t_finishedlvl.charPos[1] = menuitemsy[0]+20;
		  t_finishedlvl.renderString("GAME OVER",85);
		  GL11.glColor3f(0.9f, 0.9f, 0.9f);
		  t_1item.renderString("  RESTART",80);
		  t_2item.renderString("EXIT",80);
		  
		  endHUD();
		  Refactored.angle+=0.01;
	}

	public static void renderFrameLvlDone() {
		startHUD();
		  //draw rectangles
		  glEnable (GL_BLEND);
		  glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		  
		  GL11.glBegin(GL11.GL_QUADS);
		  GL11.glColor4f(0.3f, 0.2f, 0.4f,0.5f);
		  int i=2;
		  GL11.glVertex2f(menuitemsx[0], menuitemsy[i]);
		  GL11.glVertex2f(menuitemsx[0], menuitemsy[i+1]);
		  GL11.glVertex2f(menuitemsx[1], menuitemsy[i+1]);
		  GL11.glVertex2f(menuitemsx[1], menuitemsy[i]);
		  i+=2;
		  GL11.glVertex2f(menuitemsx[0], menuitemsy[i]);
		  GL11.glVertex2f(menuitemsx[0], menuitemsy[i+1]);
		  GL11.glVertex2f(menuitemsx[1], menuitemsy[i+1]);
		  GL11.glVertex2f(menuitemsx[1], menuitemsy[i]);
		  GL11.glEnd();
		  
		  //draw texts
		  GL11.glColor3f(0.9f, 0.9f, 0.9f);
		  t_finishedlvl.charPos[0] = (int)(GameState.WIDTH-t_1item.textWidth("Congratulations, you have finished lvl 8", 35)/2);
		  t_finishedlvl.charPos[1] = menuitemsy[1]+20;
		  t_finishedlvl.renderString("Congratulations, you have finished level "+GameState.lvl,35);
		  t_2item.renderString(" Buy",65);
		  t_3item.renderString("Next",80);

		  endHUD();
	}
	public static void renderFrameBuy() {
		  startHUD();  
		  int w=1024;
		  int picHeight=150;
		  int margin=25;
		  int pass=200;
		  int yFirstImage=500;
		  t_choose = new Bitmap();
		  t_choose.charPos[0] = (int)(w-t_1item.textWidth("Choose tower", 20)-margin);
		  t_choose.charPos[1] = yFirstImage+picHeight+20;  
		  t_choose.renderString("Choose tower", 20);
		  
		  //price label
		  t_towerprice = new Bitmap();
		  t_towerprice.charPos[0]  =(int)(w-t_1item.textWidth("500$", 20)-margin);
		  t_towerprice.charPos[1] = yFirstImage-picHeight-20;  
		  t_towerprice.renderString("500$", 20);
		  
		  t_towerprice.charPos[0]  =(int)(w-t_1item.textWidth("200$", 20)-margin);
		  t_towerprice.charPos[1] = yFirstImage;  
		  t_towerprice.renderString("200$", 20);
		  
		  //draw images
		  Bitmap.renderPic(1,w-pass+margin, w-margin, yFirstImage, yFirstImage+picHeight);
		  Bitmap.renderPic(2,w-pass+margin, w-margin, yFirstImage-picHeight-20, yFirstImage-20);
		  
		  //draw rectangle
		  glEnable (GL_BLEND);
		  glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		  
		  GL11.glBegin(GL11.GL_QUADS);
		  //right panel
		  GL11.glColor4f(0.0f, 0.0f, 0.0f,0.8f);
		  GL11.glVertex2f(w-pass, 0);
		  GL11.glVertex2f(w, 0);
		  GL11.glVertex2f(w, 1000);
		  GL11.glVertex2f(w-pass, 1000);
		  GL11.glEnd();


		  glDisable(GL_BLEND);
		  GL11.glColor4f(1.0f, 0.0f, 0.0f,1f);
		  t_choose.charPos[0] = (int)(w/2-t_1item.textWidth("Next level", 30)/2);
		  t_choose.charPos[1] = 20;  
		  t_choose.renderString("Next level", 30);
		  //next level button
		  GL11.glBegin(GL11.GL_QUADS);
		  GL11.glColor4f(1.0f, 0.0f, 0.0f,0.5f);
		  GL11.glVertex2f(menuitemsx[0], 10);
		  GL11.glVertex2f(menuitemsx[1], 10);
		  GL11.glVertex2f(menuitemsx[1], 60);
		  GL11.glVertex2f(menuitemsx[0], 60);
		  GL11.glEnd();
		  glDisable(GL_BLEND);
		  
		  endHUD();
		
	}
	public static void startHUD() {
		if(GameState.lighting)
			GL11.glDisable(GL11.GL_LIGHTING);
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
	    if(GameState.lighting)
	    	GL11.glEnable(GL11.GL_LIGHTING);
	  }
	  public static boolean isInRectangle(int xL,int xR,int yT,int yB){	//We send xLeft, xRight, yTop and yBottom to function

		  if(Mouse.getX()>=xL && Mouse.getX()<=xR){ //right x position
        		if(Mouse.getY()>=yB && Mouse.getY()<=yT){ //second button
        			return true;
        		}
		  }
		  return false;
        		
	  }
}
