package backend;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;

//import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



class classSocket{
	
	public classSocket() throws IOException{
		String command;
		String userid;
		String courseid;
		ServerSocket welcomeSocket = new ServerSocket(3456);
		String returnSentence = "";
		System.out.println("class waiting for connection");
		while(true){
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(
					connectionSocket.getInputStream()));
			System.out.println("class connection established");
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			command = inFromClient.readLine();
			userid = inFromClient.readLine();
			courseid = inFromClient.readLine();
			System.out.println("command: " + userid);
			System.out.println("courseid: " + courseid);
			System.out.println("userid: " + userid);
			course c = new course(Integer.parseInt(userid), courseid, "dc", "dc");
			if(command == "ac"){
				c.addClass();
			}else if(command  == "dc"){
				c.removeClass();
			}else if(command == "rmc"){
				ArrayList<String> cids;
				cids = c.retriveClass(Integer.parseInt(userid));
				for(int i = 0; i < cids.size(); i++){
					returnSentence += cids.get(i);
				}
			}else if(command == "rfc"){
				String friendid = inFromClient.readLine();
				ArrayList<String> cids;
				cids = c.retriveClass(Integer.parseInt(friendid));
				for(int i = 0; i < cids.size(); i++){
					returnSentence += cids.get(i);
				}
			}
			outToClient.writeBytes(returnSentence);
		}
			
	}
}