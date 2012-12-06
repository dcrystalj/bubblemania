package threads;



import main.GameState;
import main.Refactored;

import org.lwjgl.input.Mouse;

import towers.Tower;
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
		Mouse.setCursorPosition(222, 672);


		while (move) {
//			System.out.println(Mouse.getX()+","+Mouse.getY());
			//QWe move tower according to mouse
			Refactored.newTower.setPosition(Mouse.getX()-222, 0, 672-Mouse.getY());
			if(Mouse.isButtonDown(1))
				move=false;
		}
		//We finnaly buy tower on desired position, towerGun
		if(GameState.buildTower==1){
			GameState.money-=Refactored.newTower.cost;
			GameState.towerGuns.add((TowerGun)Refactored.newTower);
			Refactored.newTower=null;
		}
		//Else we have triple gun
		else if(GameState.buildTower==2){
			GameState.money-=Refactored.newTower.cost;
			GameState.tripleGuns.add((TripleGun)Refactored.newTower);
			Refactored.newTower=null;
		}
			
		Mouse.setGrabbed(false);

	}

}
