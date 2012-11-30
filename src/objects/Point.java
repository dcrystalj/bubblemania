package objects;

public class Point {
	float x, y, z;

	public Point(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public void normalize(){
		float len=(float)Math.sqrt(x*x+y*y+z*z);
		this.x=x/len;
		this.y=y/len;
		this.z=z/len;
	}
	public float sum(){
		return x+y+z;
	}
}
