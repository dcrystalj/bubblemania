package main;

import objects.Tower;

public class ThreadPopBubbles extends Thread {
	@Override
	public void run()
	{
		while (GameState.running) {
			try {
				for(Tower t : GameState.towers){
					t.popBubble();
				}
				Thread.sleep(600);
			} catch (Exception e) {
			}
		}
	}
}
