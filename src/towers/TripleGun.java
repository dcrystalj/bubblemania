package towers;

import threads.PopBubbles;
import glmodel.GLModel;

public class TripleGun extends Tower {

	public TripleGun(float w) {
		super(w);
		//Set shootingSpeed and shootingRadius of tower
		shootingSpeed=250;
		shootingRadius = 40;
		cost=100;
		shoot=new PopBubbles(this);
		try {
			m_Obj = new GLModel("tripleGun.obj");
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}  
	}
}
