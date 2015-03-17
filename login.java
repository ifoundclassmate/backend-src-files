package backend;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class login{
	
	private String username;
	private int userid;
	private String password;
	private String email;
	private static int currentId = 1;
	private ArrayList<String> friendList = new ArrayList<String>();
	private static ArrayList<pair> records = new ArrayList<pair>();
	
		
	public login(String command,String u, String e, String p){
		
		this.username = u;
		this.password = p;
		this.email = e;
		
		if(command.equals("login")){
			this.checkUserExist();
			this.setupFriend();
		}else if(command.equals("ac")){
			if(!this.checkUserExist()){
				this.allocateUserId();
				this.setupFriend();
			}
		}
	}
	
	public void setupFriend(){
		Connection c = null;
		Statement stmt = null;
		String usern;
		int myid = 0;
		int friendId = 0;
		try{
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ifoundclassmate");
			c.setAutoCommit(false);
			System.out.println("Opened database successsfully");
			
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM FRIEND " + "WHERE MYID =" 
			+ this.userid + ";");
			while (rs.next()){
				myid  = rs.getInt("myid");
				friendId = rs.getInt("friendid");
				System.out.println("userid = " + myid);
				System.out.println("fid = " + friendId);
				this.friendList.add(Integer.toString(friendId));
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e ){
			System.out.println(e.getClass().getName() + e.getMessage() );
		}
	}
	
	
	public void setupRecords(){
		
		Connection c = null;
		Statement stmt = null;
		String usern;
		int id = 0;
		try{
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ifoundclassmate");
			c.setAutoCommit(false);
			System.out.println("Opened database successsfully");
			
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM USERS" + ";");
			if(rs != null) currentId++;
			while (rs.next()){
				id  = Integer.parseInt(rs.getString("userid"));
				usern = rs.getString("username");
				usern = usern.replaceAll(" ","");
				System.out.println("userid = " + id);
				System.out.println("username = " + usern);
				records.add(new pair(usern,id));
				System.out.println("currentId = " + currentId);
				currentId++;
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e ){
			System.out.println(e.getClass().getName() + e.getMessage() );
		}
	}
	
	private void encode(){
		
	}
	
	private void decode(){
		
	}
	
	// -1 means already friend
	// 0 means add friend
	// 1 means success
	
	public int addFriend(String fn){
		String fid = "-1";
		for(int i = 0; i < records.size(); i++){
			if(records.get(i).getName().equals(fn)){
				fid = Integer.toString(records.get(i).getId());
				break;
			}
		}
		if( fid == "-1") return -1;
		System.out.println("fid: " + fid );
		if(this.friendList.contains(fid)) return -1;
		this.friendList.add(fid);
		Connection c = null;
		Statement stmt = null;
		try{
			System.out.println("add a friend");
			System.out.println(this.userid);
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ifoundclassmate");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "INSERT INTO FRIEND (MYID,FRIENDID) "+
					"VALUES (" + this.userid  + ", " + fid + " );" ;
			stmt.executeUpdate(sql);
			
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e ){
			System.out.println(e.getClass().getName() + e.getMessage() );
			this.friendList.remove(fid);
			return 0;
		}
		return 1;
		
	}
	
		// -1 means no friend
		// 0 means fail remove friend
		// 1 means success
		
	public int removeFriend(String fn){
		
		String fid = "-1";
		for(int i = 0; i < records.size(); i++){
			if(records.get(i).getName().equals(fn)){
				fid = Integer.toString(records.get(i).getId());
				break;
			}
		}
		if( fid == "-1") return -1;
		
		if(!this.friendList.contains(fid)) return -1;
		this.friendList.remove(fid);
		Connection c = null;
		Statement stmt = null;	
		try{
			System.out.println("removing a friend");
			System.out.println(this.userid);
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ifoundclassmate");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "DELETE FROM FRIEND WHERE MYID = " + this.userid + 
					"AND FRIENDID = " + fid + " ;";
			stmt.executeUpdate(sql);
				
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e ){
			System.out.println(e.getClass().getName() + e.getMessage() );
			this.friendList.add(fid);
			return 0;
		}
		return 1;
			
	}
	
	public ArrayList<String> getFriend(){
		return this.friendList;
	}
	
	
	// -1 means account does not exist
	// 0 means password is incorrect
	// 1 means password is correct
	
	public int authenticate(){
		Connection c = null;
		Statement stmt = null;
		int id;
		String pin = "";
		boolean found = false;
		try{
			System.out.println("authenticate");
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ifoundclassmate");
			c.setAutoCommit(false);
			System.out.println("Opened database successsfully");
			
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM USERS "
					+ "WHERE USERID = " +  this.getUserId() + ";");
			while (rs.next()){
				id  = rs.getInt("userid");
				pin = rs.getString("password");
				pin = pin.replaceAll("\\s+$", "");
				System.out.println("userid = " + id);
				if(id == this.getUserId()){
					found = true;
					System.out.println("User found");
					break;
				}
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e ){
			System.out.println(e.getClass().getName() + e.getMessage() );
			return -1;
		}
		
		if(!found) return -1;
		System.out.println(pin);
		System.out.println(this.password);
		if(pin.equals( this.password)){
			System.out.println("correct");
			return 1;
		}else{
			System.out.println("incorrect");
			return 0;
		}
	}
	
	public String getusername(){
		return this.username;
	}
	
	public boolean checkUserExist(){
		
		for(pair p: records){
			if(p.getName().equals(this.username)){
				//match found, return id;
				this.userid = p.getId();
				return true;
			}
		}
		return false;
	}
	
	private void allocateUserId(){
		
		
		//no match, need to allocate new id and insert to database
		records.add(new pair(this.username,currentId));
		this.userid = currentId;
		currentId++;
		Connection c = null;
		Statement stmt = null;
		try{
			System.out.println("setup");
			System.out.println(this.userid);
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ifoundclassmate");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "INSERT INTO USERS (USERID,USERNAME,EMAIL,PASSWORD) "+
					"VALUES (" + this.userid  + ", " + "'" +this.username + "'"
					+" , " + "'" + this.email + "'" +" , " + "'" +this.password + "' " + ");" ;
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
	public int getUserId(){
		return this.userid;
	}
	
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
