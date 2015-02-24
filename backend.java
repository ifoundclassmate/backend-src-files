package backend;

import java.io.IOException;
import java.util.ArrayList;



public class backend{
	
	public static void main(String args[]) throws IOException{
		System.out.println("here");
		backend m = new backend();
		
		//accepting login request
		Thread t1 = new Thread(){
			public void run(){
				try {
					loginSocket ls = new loginSocket();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t1.start();
		System.out.println("here");
		
		//accepting class request
		Thread t2 = new Thread(){
			public void run(){
				try {
					classSocket cs = new classSocket();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t2.start();
		System.out.println("here2");
		while(true){
			
		}
		
	}
	
	public backend(){
		setup();
	}
	
	public void setup(){
		preprocess p = new preprocess();
	}
}