package threads;

import main.GameState;
import towers.Tower;

public class ThreadPopBubblesTowerGun extends Thread {
	@Override
	public void run()
	{
		while (GameState.running) {
			try {
				for(Tower t : GameState.towerGuns){
					t.popBubble();
				}
				Thread.sleep(600);
			} catch (Exception e) {
			}
		}
	}
}
