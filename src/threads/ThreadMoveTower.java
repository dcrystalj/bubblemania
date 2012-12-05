package threads;



import main.GameState;
import main.Refactored;
import objects.Tower;

import org.lwjgl.input.Mouse;

public class ThreadMoveTower extends Thread {
	@Override
	public void run()
	{
		boolean move=true;
//		Refactored.newTower=new Tower(10);
		Refactored.newTower=new Tower(10);
		Mouse.setCursorPosition(222, 672);


		while (move) {
			System.out.println("in");

			Refactored.newTower.setPosition(Mouse.getX()-222, 0, 672-Mouse.getY());
			if(Mouse.isButtonDown(1))
				move=false;
		}
		System.out.println("done");
		if(GameState.money>200){
			GameState.money-=200;
			GameState.towers.add(Refactored.newTower);
			Refactored.newTower=null;
		}
		Mouse.setGrabbed(false);

	}

}
