package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class course{
	
	//cache for getting course id
	public static ArrayList<cPair> cidCache= new ArrayList<cPair>();
	private String courseId;
//	private String courseName;
	private String subject;
	private String catalog_number;
//	private String location;
//	private String term;
//	private int section;
	private int userid;
	
	public course(int usr,String id, String sbj, String cn){
		this.subject = sbj;
		this.catalog_number = cn;
		this.courseId = id;
	//	this.courseName = name;
	//	this.location = loc;
	//	this.term = t;
	//	this.section = sec;	
		this.userid = usr;
	}
	
	
	
	public String getId(){
		return this.courseId;
	}
	
/*	public String getName(){
		return this.courseName;
	}
*/	
/*	public String getLocation(){
		return this.location;
	}
	
	public String term(){
		return this.term;
	}
	
	public int getSection(){
		return this.section;
	}
*/	
	/*
	 * -2 means there is error during check for duplicate
	 * -1 means there is a duplicate course in the database
	 * 0 means insertion failed.
	 * 1 means successfully inserted
	 */
	
	public ArrayList<String> retriveClass(int u){
		ArrayList<String> courseList = new ArrayList<String>();
		Connection c = null;
		Statement stmt = null;
		int id;
		String ci;
	//	String cn;
//		String l;
//		String te;
		String sb;
		String cata;
//		int sec;
		
	//	boolean duplicate = false;
		try{
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ifoundclassmate");
			c.setAutoCommit(false);
			System.out.println("Opened database successsfully");
			
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM COURSE"
					+ "WHERE USERID = " + u + ";");
			while (rs.next()){
				id = rs.getInt("userid");
				ci = rs.getString("courseid");
		//		cn = rs.getString("coursename");
		//		l = rs.getString("location");
		//		te = rs.getString("term");
		//		sec = rs.getInt("sec");
				sb = rs.getString("subject");
				cata = rs.getString("catalog_number");
				
			//	course temp = new course(id,ci,sb,cata);
				courseList.add(ci);
				System.out.println("userid = " + id);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e ){
			System.out.println(e.getClass().getName() + e.getMessage() );
			return null;
		}
		
		return (courseList);
		
	}
	
	public int addClass(){
		Connection c = null;
		Statement stmt = null;
		int id;
		boolean duplicate = false;
		try{
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ifoundclassmate");
			c.setAutoCommit(false);
			System.out.println("Opened database successsfully");
			
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COURSEID FROM COURSE"
					+ "WHERE USERID = " + this.userid + ";");
			while (rs.next()){
				id  = rs.getInt("userid");
				System.out.println("userid = " + id);
				if(id == this.userid){
					duplicate = true;
					System.out.println("Duplicate in database, abort");
					break;
				}
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e ){
			System.out.println(e.getClass().getName() + e.getMessage() );
			return -2;
		}
		//return error code if duplicate exists
		if(duplicate) return -1;
		
		try{
			for(int i = 0; i < cidCache.size() ; i++){
				if(cidCache.get(i).getSubject() == this.subject &&
						cidCache.get(i).getCatalog() == this.catalog_number){
					this.courseId = cidCache.get(i).getCid();
				}
					
			}
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ifoundclassmate");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "INSERT INTO COURSE (USERID,ID)"+
					"VALUES (" + this.userid+ ", " + this.courseId + ";" ;
			stmt.executeUpdate(sql);
			
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.out.println(e.getClass().getName()+ ": " + e.getMessage());
			return 0;
			
		}
		System.out.println("Recoreds created successfully");
		//always can add class for now
		return 1;
	}
	
	public boolean removeClass(){
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ifoundclassmate");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			
			stmt = c.createStatement();
			String sql = "DELETE FROM COURSE WHERE USERID = " + this.userid + 
					"AND COURSEID = " + this.courseId + ";";
			stmt.executeUpdate(sql);
			c.commit();
			//rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.out.println(e.getClass().getName()+ ":" + e.getMessage());
			return false;
		}
		
		//removed correctly 
		return true;
	}
	
	public void addToCache(String s, String c, String id){
		cidCache.add(new cPair(s,c,id));
	}
	
	
	
	class cPair{
		String sbj;
		String cata_n;
		String cid;
		
		public cPair(String x, String y, String z){
			this.sbj = x;
			this.cata_n = y;
			this.cid = z;
		}
		
		public String getSubject(){
			return this.sbj;
		}
		
		public String getCatalog(){
			return this.cata_n;
		}
		
		public String getCid(){
			return this.cid;
		}
		
	}
	
}

