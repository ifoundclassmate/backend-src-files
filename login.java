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
	private static int currentId = 1;
	private ArrayList<String> friendList = new ArrayList<String>();
	private static ArrayList<pair> records = new ArrayList<pair>();
	
		
	public login(String u, String p){
		if(!u.equals("")){
			this.username = u;
			this.password = p;
			this.allocateUserId();
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
			while (rs.next()){
				id  = Integer.parseInt(rs.getString("userid"));
				usern = rs.getString("username");
				System.out.println("userid = " + id);
				System.out.println("username = " + usern);
				records.add(new pair(usern,id));
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
	
	private void addFriend(login user){
		this.friendList.add(Integer.toString(user.getUserId()));
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
	
	private void allocateUserId(){
		for(pair p: records){
			if(p.getName().equals(this.username)){
				//match found, return id;
				this.userid = p.getId();
				return;
			}
		}
		
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
			String sql = "INSERT INTO USERS (USERID,USERNAME,PASSWORD) "+
					"VALUES (" + this.userid  + ", " + "'" +this.username + "'"
					+" , " + "'" +this.password + "' " + ");" ;
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
