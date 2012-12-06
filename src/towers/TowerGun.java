package towers;

import glmodel.GLModel;

public class TowerGun extends Tower {

	public TowerGun(float w) {
		super(w);
		//Set shootingSpeed and shootingRadius of tower
		shootingSpeed=600;
		shootingRadius = 60;
		cost=200;
		try {
			m_Obj = new GLModel("towerGun.obj");
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}  
	}
}
