package threads;



import main.GameState;
import main.Refactored;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;

import towers.TowerGun;
import towers.TripleGun;

public class ThreadMoveTower extends Thread {
	@Override
	public void run()
	{
		boolean move=true;
//		Refactored.newTower=new Tower(10);
		if(GameState.buildTower==1)
			Refactored.newTower=new TowerGun(10);
		else 
			Refactored.newTower=new TripleGun(10);
		//For drawing marking circle above
		Refactored.newTower.building=true;
		try {
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		Mouse.setCursorPosition(222+230, 672-230);

		while (move) {
			//Hide cursor
			Mouse.setGrabbed(true);
			int posX=Mouse.getX()-222;
			int posZ=672-Mouse.getY();
			if(GameState.isPlaceFree(posX,0,posZ)){
				//We move tower according to mouse, starting in center
				Refactored.newTower.setPosition(posX, 0, posZ);
			}
			//We build it with right button on mouse
			if(Mouse.isButtonDown(1))
				move=false;
		}
		
		GameState.towers.add(Refactored.newTower);
		Refactored.newTower.building=false;
		Refactored.newTower=null;
		
		//We bought tower on desired position, towerGun
		if(GameState.buildTower==1){
			GameState.money-=200;
			
		}
		//Else we have triple gun
		else if(GameState.buildTower==2){
			GameState.money-=500;
		}
			
		Mouse.setGrabbed(false);

	}

}
