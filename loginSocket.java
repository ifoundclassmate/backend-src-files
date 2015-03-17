package backend;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

class loginSocket{
	
	public loginSocket() throws IOException{
		String userid;
		String username;
		String password;
		String email;
		String confirmedPassword;
		String command;
		ServerSocket welcomeSocket = new ServerSocket(3455);
		String returnSentence = "";
		System.out.println("Login Server waiting for connection");
		while(true){
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(
					connectionSocket.getInputStream()));
			System.out.println("Login Server connection established");
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			
			command = inFromClient.readLine();
			
			username = inFromClient.readLine();
			
			
			System.out.println("command: " + command);
			System.out.println("username: " + username);
			
			if(command.equals("login")){
				password = inFromClient.readLine();
	
				System.out.println("password: " + password);
				login l = new login(command,username,"",password);
				int ret = l.authenticate();
				System.out.println("autenticate result: " + ret);
				returnSentence += Integer.toString(ret) + '\n';
				
				if(ret == 1){
					System.out.println("useridInSocket: " + l.getUserId());
					returnSentence += Integer.toString(l.getUserId()) + '\n';
					ArrayList<String> fids;
					returnSentence += "fids\n" ;
					fids = l.getFriend();
					//send friend list when user login
					for(int i = 0; i < fids.size(); i++){
						returnSentence += fids.get(i) + '\n';
					}
				}
				outToClient.writeBytes(returnSentence);
				
			}else if(command.equals("ac")){
				email = inFromClient.readLine();
				password = inFromClient.readLine();
				
				System.out.println("email: " + email);
				System.out.println("password: " + password);
				System.out.println("creating new users");
				login checkExist = new login("checkExist",username,email,password);
				if(checkExist.checkUserExist()){
					returnSentence += "0\n";
				}else{
					login l = new login(command,username,email,password);	
					returnSentence +="1" + '\n' + Integer.toString(l.getUserId()) + '\n';
				}
				outToClient.writeBytes(returnSentence);
			}
			
			
			returnSentence = "";
		}
	}
}