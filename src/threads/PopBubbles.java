package threads;

import towers.Tower;

public class PopBubbles implements Runnable {
	private volatile boolean running=true;
	Tower t;
	Thread thr;
	
	public PopBubbles(Tower t){
		this.t=t;
	}
	public void startThread(){
		running=true;
		thr=new Thread(this);
		thr.start();
	}
	public void kill() {
		running=false;
		if(thr!=null)
			thr.stop();	//Not gentliest form of destruction, but only one working
		//We access variables that don't need to be locked, so this is ok, but it is, sadly, deprecated...
	}

	public void run()
	{
		//Check every 10ms if there is bubble and start shooting
		while(running){
			if(t.popBubble())
				break;
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				running=false;
			}
		}
		//Then shoots according to its interval
		while (running) {
			try {
				Thread.sleep(t.shootingSpeed);
				t.popBubble();
			} catch (Exception e) {
				running=false;
			}
		}
	}
}
