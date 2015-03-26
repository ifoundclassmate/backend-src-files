package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import backend.group.pair;

public class meeting{
	private int meetingId = -1;
	private int month;
	private int date;
	private int hour;
	private int min;
	private int year;
	private int groupid;
	private String groupname;
	static int currentId;
	private static ArrayList<pair> records = new ArrayList<pair>();

	
	public meeting(String groupname, int year, int month, int date, int hour, int min){
		this.month = month;
		this.date = date;
		this.hour = hour;
		this.min = min;
		this.groupname = groupname;
		this.year = year;
		group tempGroup = new group("",groupname,"");
		if(groupname != ""){
			if(tempGroup.getGroupId() != -1){
				this.groupid = tempGroup.getGroupId();

			}
		}
	}
	
	public ArrayList<String> retreiveMeeting(String groupname){
		ArrayList<String> retv = new ArrayList<String>();
		Connection c = null;
		Statement stmt = null;

		String gn;
		int meetingid;
		int groupid;
		int year;
		int month;
		int date;
		int hour;
		int min;
		
		try{
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ifoundclassmate");
			c.setAutoCommit(false);
			System.out.println("Opened database successsfully");
			
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Meeting" + ";");
			while (rs.next()){
				groupid = rs.getInt("groupid");
				meetingid =rs.getInt("meetingid");
				year =rs.getInt("year");
				month =rs.getInt("month");
				date =rs.getInt("date");
				hour =rs.getInt("hour");
				min =rs.getInt("min");
				
				group tempGroup = new group("","","");
				gn = tempGroup.getGroupName(groupid);
				gn = gn.replaceAll(" ","");
				gn = gn.replaceAll("_"," ");
				if(gn.equals(groupname)){
					retv.add(Integer.toString(meetingid));
					retv.add(gn);
					retv.add(Integer.toString(year));
					retv.add(Integer.toString(month));
					retv.add(Integer.toString(date));
					retv.add(Integer.toString(hour));
					retv.add(Integer.toString(min));
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
			ResultSet rs = stmt.executeQuery("SELECT * FROM MEETING" + ";");
			if(rs != null) currentId++;
			while (rs.next()){
				gi = rs.getInt("groupid");
				gn = rs.getString("meetingid");
				
				records.add(new pair(groupid,meetingId));
				currentId++;
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e ){
			System.out.println(e.getClass().getName() + e.getMessage() );
		}
	}
	
	public boolean checkMeetingExist(){
		
		for(pair p: records){
			if(p.getGroupId() == this.groupid){
				//match found, return id;
				this.meetingId = p.getMeetingId();
				return true;
			}
		}
		return false;
	}
	
	public void allocateGroupId(){
		
		
		//no match, need to allocate new id and insert to database
		records.add(new pair(groupid,currentId));
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
			String sql = "INSERT INTO MEETING (MEETINGID,GROUPID,YEAR,MONTH,DATE,HOUR,MIN) "+
					"VALUES (" + this.meetingId  + ", " + "'" +this.groupid + "'"
					+" , " + "'" + this.year + "' " + "'" + this.month + "' " +
					"'" + this.date + "' " +  "'" + this.hour + "' " + "'" + 
					this.min + "' " + ");" ;
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
		private int groupid;
		private int meetingid;
		
		public pair(int x, int y){
			this.groupid = x;
			this.meetingid = y;
		}
		
		public int getGroupId(){
			return this.groupid;
		}
		
		public int getMeetingId(){
			return this.meetingid;
		}
		
	}
	
}