package backend;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

class friendSocket{
	
	public friendSocket() throws IOException{
		String userid;
		String friendname;
		String username;
		String password;
		String command;
		ServerSocket welcomeSocket = new ServerSocket(3454);
		String returnSentence = "";
		System.out.println("waiting for connection");
		while(true){
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(
					connectionSocket.getInputStream()));
			System.out.println("connection established");
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			command = inFromClient.readLine();
			username = inFromClient.readLine();
			password = inFromClient.readLine();
			friendname = inFromClient.readLine();

			System.out.println("command: " + command);
			System.out.println("username: " + username);
			System.out.println("friendname: " + friendname);

			
			login l = new login("login",username,"",password);
			int retv = 0;
			//int ret = l.authenticate();
			//System.out.println("ret: " + ret);
			//returnSentence += Integer.toString(ret) + '\n';
			
			if(command.equals("af")){
				retv = l.addFriend(friendname);
				returnSentence = Integer.toString(retv) + '\n';
			}else if(command.equals("rf")){
				retv = l.removeFriend(friendname);
				returnSentence = Integer.toString(retv) + '\n';
			}else if(command.equals("rmf")){
				ArrayList<String> fids;
				fids = l.getFriend();
				returnSentence += fids.size() + "\n" ;
				//send friend list when user login
				for(int i = 0; i < fids.size(); i++){
					returnSentence += fids.get(i) + '\n';
				}
			}
			
			
			
			outToClient.writeBytes(returnSentence);
			returnSentence = "";
		}
	}
}