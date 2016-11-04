package model;
import java.io.*;
import java.util.*;

public class GeomApp implements Serializable {
	
	public static final String storeDir = "C:/Users/Dror/Desktop";
	public static final String storeFile = "shachar.dat";
	
	
	private ArrayList<Point> points;
	public GeomApp() {
		points = new ArrayList<Point>();
	}
	
	public void addPoint(Point p) {
		 points.add(p);
	}
	
	public void writePoints() {
		for (Point p: points) {
			System.out.println(p);
		 }
	}
	
	public static void writeApp(GeomApp gapp) throws IOException {
		ObjectOutputStream oos  = new ObjectOutputStream(
			new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(gapp);
	
	}
	
	
	public static void main(String[] args) {
		GeomApp gapp = new GeomApp();
		 gapp.addPoint(new Point(1,2));
		 gapp.addPoint(new Point(4,5));
		 gapp.addPoint(new Point(5,6));
		gapp.writePoints(); 
		try {
			writeApp(gapp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


class Point implements Comparable<Point>, Serializable {
	int x,y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int compareTo(Point o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
