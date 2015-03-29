package backend;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

class groupSocket{
	
	public groupSocket() throws IOException{

		String groupname;
		String description;
		String command;
		String username;
		ServerSocket welcomeSocket = new ServerSocket(3457);
		String returnSentence = "";
		System.out.println("waiting for connection");
		while(true){
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(
					connectionSocket.getInputStream()));
			System.out.println("group connection established");
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			command = inFromClient.readLine();


			System.out.println("command: " + command);
			int retv = 0;
			//int ret = l.authenticate();
			//System.out.println("ret: " + ret);
			//returnSentence += Integer.toString(ret) + '\n';
			
			if(command.equals("cg")){
				groupname = inFromClient.readLine();
				description = inFromClient.readLine();
				group g = new group("",groupname,description);
				System.out.println("groupname: " + groupname);
				System.out.println("description: " + description);
				boolean tempRetv = g.checkGroupExist();
				if(!tempRetv) {
					g.allocateGroupId();
					retv = 1;
				}
				returnSentence = Integer.toString(retv) + '\n';
			}else if(command.equals("autg")){
				groupname = inFromClient.readLine();
				username  = inFromClient.readLine();
				group g = new group("",groupname,"");
				boolean tempRetv = g.checkGroupExist();
				if(!tempRetv){
					returnSentence = Integer.toString(retv) + '\n';
					outToClient.writeBytes(returnSentence);
					returnSentence = "";
					continue;
				}
				retv = g.addUserToGroup(username, groupname);
				returnSentence = Integer.toString(1) + '\n';
			}else if(command.equals("rmg")){
				username  = inFromClient.readLine();
				login templ = new login("",username,"","");
				if(!templ.checkUserExist()){
					returnSentence = "-1\n";
					outToClient.writeBytes(returnSentence);
					returnSentence = "";
					continue;
				}
				int userid = templ.getUserId();
				group g = new group("","","");
				returnSentence += "1\n";
				ArrayList<String> retv2 = g.retreiveGroup(userid);
				returnSentence += Integer.toString(retv2.size()/2) + '\n';
				for(int i = 0; i < retv2.size(); i++){
					returnSentence += retv2.get(i) + '\n';
				}
				returnSentence += "end\n";
				
			}else if(command.equals("am")){
				groupname = inFromClient.readLine();
				String year = inFromClient.readLine();
				String month = inFromClient.readLine();
				String date = inFromClient.readLine();
				String hour = inFromClient.readLine();
				String min = inFromClient.readLine();
				meeting m = new meeting(groupname,Integer.parseInt(year),
						Integer.parseInt(month),Integer.parseInt(date),
						Integer.parseInt(hour)
						,Integer.parseInt(min));
				if(m.checkMeetingExist()){
					returnSentence = Integer.toString(retv) + '\n';
					outToClient.writeBytes(returnSentence);
					returnSentence = "";
					continue;
				}
				m.allocateMeetingId();
				
				returnSentence = "1\n";
				
			}else if(command.equals("rmm")){
				groupname = inFromClient.readLine();
				meeting m = new meeting(groupname,-1,-1,-1,-1,-1);
		
				ArrayList<String> retv2 = m.retreiveMeeting(groupname);
				returnSentence += Integer.toString(retv2.size()/7) + '\n';
				for(int i = 0; i < retv2.size(); i++){
					returnSentence += retv2.get(i) + '\n';
				}
				returnSentence += "end\n";
			}else if(command.equals("rg")){

				
			}
			
			outToClient.writeBytes(returnSentence);
			returnSentence = "";
		}
	}
}