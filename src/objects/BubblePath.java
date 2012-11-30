package objects;

import java.util.LinkedList;

public class BubblePath {
	LinkedList<Point> path;	//list of point on path for bubbles
	public BubblePath(Point start, Point end){
		//Generate simple path for bubbles
		path=new LinkedList<Point>();
		path.add(start);
		path.add(new Point(start.x+200,start.y,start.z));
		path.add(new Point(start.x+200,start.y+200,start.z));
		path.add(end);
	}
	public Point get(int index){	//Returns point on path
		return path.get(index);
	}
}
