package objects;

import java.util.LinkedList;
import org.lwjgl.util.vector.Vector3f;

public class BubblePath {
	LinkedList<Vector3f> path;	//list of point on path for bubbles
	public BubblePath(Vector3f start, Vector3f end){
		//Generate simple path for bubbles
		path=new LinkedList<Vector3f>();
		path.add(new Vector3f(start.x+250,start.y,start.z));
		path.add(new Vector3f(start.x+250,start.y,start.z+250));
		path.add(new Vector3f(end.x,end.y,start.z+250));
	}
	public Vector3f get(int index){	//Returns point on path
		return path.get(index);
	}
}
