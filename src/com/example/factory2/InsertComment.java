package com.example.factory2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.util.Log;

public class InsertComment {
	
	public void insert(String text2, String image_path) throws IOException {
		// Create data variable for sent values to server  
        
        String data = URLEncoder.encode("firstname", "UTF-8") 
                     + "=" + URLEncoder.encode(text2, "UTF-8"); 

        data += "&" + URLEncoder.encode("lastname", "UTF-8") + "="
                    + URLEncoder.encode("images/"+image_path, "UTF-8"); 

        data += "&" + URLEncoder.encode("age", "UTF-8") 
                    + "=" + URLEncoder.encode("", "UTF-8");
        String text = "";
        BufferedReader reader=null;
        try
        { 
			// Defined URL  where to send data
	        URL url = new URL("http://niceloop-factory.net63.net/insertComment.php");
	         
	        // Send POST data request
	
	        URLConnection conn = url.openConnection(); 
	        conn.setDoOutput(true); 
	        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
	        wr.write( data ); 
	        wr.flush(); 
	        
	        // Get the server response 
            
	          reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	          StringBuilder sb = new StringBuilder();
	          String line = null;
	          
	          // Read Server Response
	          while((line = reader.readLine()) != null)
	              {
	                     // Append server response in string
	                     sb.append(line + "\n");
	              }
	              
	              
	              text = sb.toString();
	              Log.e("insertComment", text);
        }catch(Exception ex)
        {
            Log.e("insertComment", ex.toString());
        }
	}
}
