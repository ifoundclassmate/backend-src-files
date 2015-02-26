package backend;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.JSONObject;

public class course{
	
	//cache for getting course id
	public static ArrayList<cPair> cidCache= new ArrayList<cPair>();
	private String courseId;
//	private String courseName;
	private String subject;
	private String catalog_number;
//	private String location;
//	private String term;
	private String section;
	private int userid;
	private String secType;
	
	public course(int usr,String sbj, String cn,String sec,String st){
		this.subject = sbj;
		this.catalog_number = cn;
		if(subject != "")this.courseId = this.findCourseId(sbj, cn);
	//	this.courseName = name;
	//	this.location = loc;
	//	this.term = t;
		this.section = sec;	
		this.userid = usr;
		this.secType = st;
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
*/	
	public String getSection(){
		return this.section;
	}
	
	public void setCourseId(String id){
		this.courseId = id;
	}
	
	//check for same courses 
	
	public ArrayList<String> checkSameCourses(int userid, int fuserid){
		ArrayList<String> sameCourses = new ArrayList<String>();
		ArrayList<String> mycourse;
		ArrayList<String> friendCourse;
		mycourse = this.retriveClass(userid);
		friendCourse = this.retriveClass(fuserid);
		if(mycourse.size() == 1){
			sameCourses.add("1");
			return sameCourses;
		}
		for(int i = 0; i < mycourse.size(); i+=3){
			for(int j = 0; j < friendCourse.size(); j+=3){
				if(mycourse.get(i).equals(friendCourse.get(j)) &&
						mycourse.get(i+1).equals(friendCourse.get(j+1)) &&
						mycourse.get(i+2).equals(friendCourse.get(j+2)) ){
					sameCourses.add(mycourse.get(i));
					sameCourses.add(mycourse.get(i+1));
					sameCourses.add(mycourse.get(i+2));
				//	sameCourses.add(mycourse.get(i+3));
					break;
				}
			}
		}
		return sameCourses;
	}
	
	
public String findCourseId(String sb, String cata){
		
		String retv = "";
		URL url;
    	HttpURLConnection connection = null;
    	InputStream is = null;
    	String courseId;
    	String courseName;
    	String subject;
    	String catalog_number;
    //	course c = new course(-1, "", "","","");
    	
    	try{
            url = new URL("https://api.uwaterloo.ca/v2/courses/" + sb+ "/" + cata + ".json?key=d580b4f96aa6be2d53e8eb2aeb576c8e");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            is = connection.getInputStream();
            BufferedReader theReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String str = "";
            String reply;
            
            while ((reply = theReader.readLine()) != null){
                    
                    str += reply;
            }
            JSONObject obj = new JSONObject(str);    
            JSONObject res = obj.getJSONObject("data");

            courseId = (String) res.get("course_id");
        //    catalog_number = (String) res.get("catalog_number");

            retv = courseId;
    //        retv += catalog_number;
    	}catch (Exception e) {
            e.printStackTrace();
    	}
		return retv;
	}
/*	
	public String findsAndC(String cid){
		
		String retv = "";
		
		for(int i = 0; i < cidCache.size(); i++){
			if(cidCache.get(i).getCid().equals(cid)){
				retv = cidCache.get(i).getSubject() + '\n';
				retv += cidCache.get(i).getCatalog() + '\n';
				break;
			}
		}
		
		URL url;
    	HttpURLConnection connection = null;
    	InputStream is = null;
    //	JSONParser parser = new JSONParser();
    	String courseId;
    	String courseName;
    	String subject;
    	String catalog_number;
    	course c = new course(-1,"", "", "","");
    	
    	try{
            url = new URL("https://api.uwaterloo.ca/v2/courses/" + cid+ ".json?key=d580b4f96aa6be2d53e8eb2aeb576c8e");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            is = connection.getInputStream();
            BufferedReader theReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String str = "";
            String reply;
            
            while ((reply = theReader.readLine()) != null){
                    
                    str += reply;
            }
            JSONObject obj = new JSONObject(str);    
            JSONObject res = obj.getJSONObject("data");

            subject = (String) res.get("subject");
            catalog_number = (String) res.get("catalog_number");

            retv = subject + '\n';
            retv += catalog_number;
    	}catch (Exception e) {
            e.printStackTrace();
    	}
		return retv;
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
		String sec;
		String st;
		
	//	boolean duplicate = false;
		try{
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ifoundclassmate");
			c.setAutoCommit(false);
			System.out.println("Opened database successsfully");
			
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM COURSE "
					+ "WHERE USERID = " + u + ";");
			while (rs.next()){
		//		id = rs.getInt("userid");
		//		ci = rs.getString("courseid");
		//		cn = rs.getString("coursename");
		//		l = rs.getString("location");
		//		te = rs.getString("term");
		//		sec = rs.getInt("sec");
				sb = rs.getString("subject");
				cata = rs.getString("catalog");
				sec = rs.getString("section");
				st = rs.getString("typesec");
//				System.out.println("courseid = " + ci);
			//	course temp = new course(id,ci,sb,cata);
				sb = sb.replaceAll(" ", "");
				String combined = st + " " + sec ;
				courseList.add(sb);
				courseList.add(cata);
//				courseList.add(sec);
				courseList.add(combined);
				//courseList.add(st);
				//courseList.add(sec);
				
				
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e ){
			System.out.println(e.getClass().getName() + e.getMessage() );
			courseList.clear();
			courseList.add("1");
			return (courseList);
		}
		
		return (courseList);
		
	}
	
	// -2 means something wrong
	// -1 means duplicate
	//  0 means insertion failed;
	//  1 means success
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
			ResultSet rs = stmt.executeQuery("SELECT USERID FROM COURSE"
					+" WHERE COURSEID = " 
					+ "'" + this.courseId + "'" + " AND SECTION = " + "'" +
					this.section + "'" + " ;");
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
			System.out.println("inserting a course into database");
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
			String sql = "INSERT INTO COURSE (USERID,COURSEID,SECTION,"
					+ "TYPESEC,SUBJECT,CATALOG)"+
					"VALUES (" + this.userid+ ", " + "'" + this.courseId + "'" +
					" , " + "'" + this.section +"'"+ " , " + "'" + this.secType
					+ "'" + " , " + "'" + this.subject + "'"  + " , " + "'" + 
					this.catalog_number + "'" + " );" ;
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
	
	public int removeClass(){
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ifoundclassmate");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			
			stmt = c.createStatement();
			String sql = "DELETE FROM COURSE WHERE USERID = " + this.userid + 
					"AND COURSEID = " + "'" + this.courseId + "' AND SECTION = "+
					"'"+this.section + "'" + " ;";
			stmt.executeUpdate(sql);
			c.commit();
			//rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.out.println(e.getClass().getName()+ ":" + e.getMessage());
			return 0;
		}
		
		//removed correctly 
		return 1;
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

