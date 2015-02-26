package backend;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

//import org.json.simple.JSONObject;




class preprocess{
	
	public preprocess(){
	//	courseSetup();
		userSetup();
	}
	
	
	
	private void userSetup(){
		login l = new login("","");
		l.setupRecords();
		//l.setupFriend();
	}
	
	private void courseSetup(){
		System.out.println("Start preprocessing ...");
		
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
            url = new URL("https://api.uwaterloo.ca/v2/courses/cs.json?key=d580b4f96aa6be2d53e8eb2aeb576c8e");
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
            //	System.out.println(str);
                   // Object obj = parser.parse(reply);
                    JSONObject obj = new JSONObject(str);
                    
                    
                    for(int i = 0; i < obj.getJSONArray("data").length(); i++){
                    
                    	JSONObject res = obj.getJSONArray("data").getJSONObject(i);                 
                    	courseId = (String) res.get("course_id");
                    	courseName = (String) res.get("title");
                    	subject = (String) res.get("subject");
                    	catalog_number = (String) res.get("catalog_number");
 //                   	System.out.println(courseId);
 //                   	System.out.println(courseName);
 //                  	System.out.println(subject);
 //                   	System.out.println(catalog_number);
                    	c.addToCache(subject,catalog_number,courseId);
                    }
    	}catch (Exception e) {
            e.printStackTrace();
    	}
	}
}