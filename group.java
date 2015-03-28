package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class group{
	
	private int groupid;
	private String groupname;
	private String description;
	static int currentId = 1;
	private static ArrayList<pair> records = new ArrayList<pair>();
	
	public group(String command, String groupname, String description){
		
		this.groupname = groupname.replaceAll(" ", "_");
		this.description = description.replaceAll(" ", "_");
/*		if(command.equals("cg")){
			if(!checkGroupExist()){
				this.allocateGroupId();
			}
		}
*/		

	}
	
	
	public ArrayList<String> retreiveGroup(int userid){
		ArrayList<String> retv = new ArrayList<String>();
		ArrayList<Integer> groupids = new ArrayList<Integer>();
		Connection c = null;
		Statement stmt = null;

		String gn;
		String description;
		int id = 0;
		int groupid;
		
		try{
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ifoundclassmate");
			c.setAutoCommit(false);
			System.out.println("Opened database successsfully");
			
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM GROUPMEMBER" + ";");
			while (rs.next()){
				groupid = rs.getInt("groupid");
				id = rs.getInt("userid");
				if(id == userid){
					groupids.add(groupid);
				}
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e ){
			System.out.println(e.getClass().getName() + e.getMessage() );
		}
		
		
		
		try{
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ifoundclassmate");
			c.setAutoCommit(false);
			System.out.println("Opened database successsfully");
			
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM GROUPS" + ";");
			while (rs.next()){
				id = rs.getInt("groupid");
				if(groupids.contains(id)){
					gn = rs.getString("groupname");
					gn = gn.replaceAll(" ","");
					gn = gn.replaceAll("_", " ");
					description = rs.getString("description");
					description = description.replaceAll(" ","");
					description = description.replaceAll("_", " ");
					retv.add(gn);
					retv.add(description);
				}
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e ){
			System.out.println(e.getClass().getName() + e.getMessage() );
		}
		
		return retv;
	}
	
	public String getGroupName(int groupid){
		for(pair p: records){
			if(p.getId() == groupid){
				//match found, return id;
				return p.getName();
			}
		}
		return "";
	}
	
	public int addUserToGroup(String username, String groupname){
		
		login templ = new login("",username,"","");
		int userid = templ.findUserId(username);
		if(userid  == -1){
			return -1;
		}
		if(!checkGroupExist()){
			return -2;
		}
		
		Connection c = null;
		Statement stmt = null;
		try{
			System.out.println("add a user to a group");
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ifoundclassmate");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "INSERT INTO GROUPMEMBER (USERID,GROUPID) "+
					"VALUES (" + "'" + userid + "'" + " , " + 
					"'" + this.groupid +"'"+  " );" ;
			stmt.executeUpdate(sql);
			
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e ){
			System.out.println(e.getClass().getName() + e.getMessage());
			return 0;
		}
		
		return 1;
	}
	
	
	public int removeUserFromGroup(String username,String groupname){
		
		
		return 1;
	}
	
	public void setupRecords(){
		
		Connection c = null;
		Statement stmt = null;
		int gi;
		String gn;
		int id = 0;
		try{
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ifoundclassmate");
			c.setAutoCommit(false);
			System.out.println("Opened database successsfully");
			
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM GROUPS" + ";");
			if(rs != null) currentId++;
			while (rs.next()){
				gi = rs.getInt("groupid");
				gn = rs.getString("groupname");
				gn = gn.replaceAll(" ", "");
//				gn = gn.replaceAll("_", " ");
				System.out.println("groupname: "+ gn);
				
				records.add(new pair(gn,gi));
				currentId++;
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e ){
			System.out.println(e.getClass().getName() + e.getMessage() );
		}
	}
	
	public boolean checkGroupExist(){
		
		for(pair p: records){
			System.out.println("groupname: "+ p.getName());
			if(p.getName().equals(this.groupname)){
				//match found, return id;
				this.groupid = p.getId();
				return true;
			}
		}
		return false;
	}
	
	public int getGroupId(){
		for(pair p: records){
			if(p.getName().equals(this.groupname)){
				//match found, return id;
				return p.getId();
			}
		}
		return -1;
	}
	
	public void allocateGroupId(){
		
		
		//no match, need to allocate new id and insert to database
		records.add(new pair(this.groupname,currentId));
		this.groupid = currentId;
		currentId++;
		Connection c = null;
		Statement stmt = null;
		try{
			System.out.println("group setup");
			System.out.println(this.groupid);
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ifoundclassmate");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "INSERT INTO GROUPS (GROUPID,GROUPNAME,DESCRIPTION) "+
					"VALUES (" + this.groupid  + ", " + "'" +this.groupname + "'"
					+" , " + "'" + this.description + "' " + ");" ;
			stmt.executeUpdate(sql);
			
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e ){
			System.out.println(e.getClass().getName() + e.getMessage() );
			return;
		}
		return;
	}
	
	class pair{
		private String name;
		private int id;
		
		public pair(String x, int y){
			this.name = x;
			this.id = y;
		}
		
		public String getName(){
			return this.name;
		}
		
		public int getId(){
			return this.id;
		}
		
	}
}