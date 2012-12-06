package threads;

import towers.TripleGun;
import main.GameState;

public class ThreadPopBubblesTripleGun extends Thread {
	
		@Override
		public void run()
		{
			while (GameState.running) {
				try {
					for(TripleGun t : GameState.tripleGuns){
						t.popBubble();
					}
					Thread.sleep(TripleGun.shootingSpeed);
				} catch (Exception e) {
				}
			}
		}
}
