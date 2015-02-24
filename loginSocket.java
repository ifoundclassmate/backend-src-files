package backend;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

class loginSocket{
	
	public loginSocket() throws IOException{
		String userid;
		String username;
		String password;
		ServerSocket welcomeSocket = new ServerSocket(3455);
		String returnSentence = "";
		System.out.println("waiting for connection");
		while(true){
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(
					connectionSocket.getInputStream()));
			System.out.println("connection established");
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			userid = inFromClient.readLine();
			username = inFromClient.readLine();
			password = inFromClient.readLine();
			System.out.println("userid: " + userid);
			System.out.println("username: " + username);
			System.out.println("password: " + password);
			
			login l = new login(username,password);
			int ret = l.authenticate();
			System.out.println("ret: " + ret);
			returnSentence += Integer.toString(ret) + '\n';
			
			if(ret == 1){
				System.out.println("useridInSocket: " + l.getUserId());
				returnSentence += Integer.toString(l.getUserId()) + '\n';
				l.setupFriend();
				ArrayList<String> fids;
				returnSentence += "fids\n" ;
				fids = l.getFriend();
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