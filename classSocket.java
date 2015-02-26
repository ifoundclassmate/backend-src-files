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
		String courseid = "";
		String section = "";
		String subject = "";
		String catalog = "";
		String sectionType = "";
		ServerSocket welcomeSocket = new ServerSocket(3456);
		String returnSentence = "";
		System.out.println("class waiting for connection");
		int retv = 0;
		while(true){
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(
					connectionSocket.getInputStream()));
			System.out.println("class connection established");
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			command = inFromClient.readLine();
			userid = inFromClient.readLine();
			
			System.out.println("command: " + command);
			System.out.println("userid: " + userid);
			
			
			if(command.equals("ac")){
				System.out.println("adding a course");
				String temp = inFromClient.readLine();
				subject = temp.split(" ")[0];
				catalog = temp.split(" ")[1];
				temp  = inFromClient.readLine();
				sectionType = temp.split(" ")[0];
				section = temp.split(" ")[1];
				course c = new course(Integer.parseInt(userid), subject,catalog,
						section,sectionType);

				System.out.println("subject name: " + subject);
				System.out.println("catalog name: " + catalog);
				System.out.println("section: " + section);
				System.out.println("section type : " + sectionType);
				retv = c.addClass();
				returnSentence = Integer.toString(retv) + '\n';
			}else if(command.equals("dc")){
				System.out.println("removing a course");
				String temp = inFromClient.readLine();
				subject = temp.split(" ")[0];
				catalog = temp.split(" ")[1];
				temp  = inFromClient.readLine();
				sectionType = temp.split(" ")[0];
				section = temp.split(" ")[1];
				course c = new course(Integer.parseInt(userid), subject,catalog,
						section,sectionType);

				System.out.println("subject name: " + subject);
				System.out.println("catalog name: " + catalog);
				System.out.println("section: " + section);
				System.out.println("section type : " + sectionType);
				retv = c.removeClass();
				returnSentence = Integer.toString(retv) + '\n';
			}else if(command.equals("rmc")){
				System.out.println("retrieve courses");
				ArrayList<String> cids;
				course c = new course(Integer.parseInt(userid), courseid, "dc", "dc",
						section);
				cids = c.retriveClass(Integer.parseInt(userid));
				if(cids.size() == 1) returnSentence = "0\n";
				else returnSentence = "1\n";
				returnSentence += Integer.toString(cids.size()/3) + '\n';
				for(int i = 0; i < cids.size(); i++){
					returnSentence += cids.get(i) + '\n';
				}
				returnSentence += "end\n";
			}else if(command.equals("rfc")){
				String friendid = inFromClient.readLine();
				ArrayList<String> cids;
				course c = new course(Integer.parseInt(userid), courseid, "dc", "dc",
						section);
				cids = c.retriveClass(Integer.parseInt(friendid));
				if(cids.size() == 1) returnSentence = "0\n";
				else returnSentence = "1\n";
				returnSentence += Integer.toString(cids.size()/3) + '\n';
				for(int i = 0; i < cids.size(); i++){
					returnSentence += cids.get(i) + '\n';
				}
				returnSentence += "end\n";
			}else if(command.equals("cfsc")){
				
				//check for same courses between two friends.
				String friendid = inFromClient.readLine();
				course c = new course(Integer.parseInt(userid), courseid, "dc", "dc",
						section);
				ArrayList<String> ret;
				ret = c.checkSameCourses(Integer.parseInt(userid),
											Integer.parseInt(friendid));
				if(ret.size() == 1) returnSentence = "0\n";
				else returnSentence = "1\n";
				returnSentence += Integer.toString(ret.size()/3) + '\n';
				for(int i = 0; i < ret.size(); i++){
					returnSentence += ret.get(i) + '\n';
				}
				returnSentence += "end\n";
			}
			outToClient.writeBytes(returnSentence);
			returnSentence = "";
		}
			
	}
}