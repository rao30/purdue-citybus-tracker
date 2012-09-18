/** 
 * ***********************************************
 * File		   - ASPNETpost.java
 * Description - Automates posting of MyRideWEB form.
 *               ASP scraping algorithm was inspired
 *               from an example on Stackoverflow
 * Author      - A. Arun Goud (DiodeDroid)
 * Date        - 2012/01/21 (First created)
 * 				 2012/01/25 (v1.0 released)
 * 				 2012/06/01 (v1.1 released)
 * email	   - microbuff@hotmail.com
 * ***********************************************
 */
package com.weebly.microbuff.purduecitybustracker;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import android.util.Log;

public class ASPNETpost {
	String viewstate;
	String eventvalidation;
	String stopCode;
	String quickCode;
	//private boolean D = false;
	
	public String getStopCode() {
		return stopCode;
	}
	
	public String getQuickCode() {
		return quickCode;
	}
	
	/** For state persistence, hidden variables of the form "_ARGUMENT" are 
	 * returned to the server to help it track the state of the form controls.
	 * First the user submits the busstop query and then from the returned
	 * list of buses, the stop code is extracted. This will be used by HTMLHandler()
	 * object to extract the schedule info page. */
	public void searchStopCode(String stopName) {
		//Pull up the page, extract out the hidden input variables __VIEWSTATE, __EVENTVALIDATION		
		URL url;
		try {
			url = new URL("http://myride.gocitybus.com/public/laf/web/Default.aspx");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
			//This reads the page line-by-line and extracts out all the values from hidden input fields 
			//if(D) Log.d("MyApp","Sending");
			getViewstate(conn);  
			//Now re-open the URL to actually submit the POST data 
			conn = (HttpURLConnection) url.openConnection();             
			conn.setRequestMethod("POST");
			//conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			conn.setDoOutput(true); 
			conn.setDoInput(true); 
			DataOutputStream out = new DataOutputStream(conn.getOutputStream()); 
			String postValues = URLEncoder.encode("__EVENTTARGET", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"); 
			postValues += "&" + URLEncoder.encode("__EVENTARGUMENT", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8");
			postValues += "&" + URLEncoder.encode("__VIEWSTATE", "UTF-8") + "=" + URLEncoder.encode(viewstate, "UTF-8");
			postValues += "&" + URLEncoder.encode("__VIEWSTATEENCRYPTED", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8");
			postValues += "&" + URLEncoder.encode("__EVENTVALIDATION", "UTF-8") + "=" + URLEncoder.encode(eventvalidation, "UTF-8");
			postValues += "&" + URLEncoder.encode("_ctl0:cphMainContent:lookup:txtSearch", "UTF-8") + "=" + URLEncoder.encode(stopName, "UTF-8");
			postValues += "&" + URLEncoder.encode("_ctl0:cphMainContent:lookup:btnSearch", "UTF-8") + "=" + URLEncoder.encode("GO", "UTF-8");
			
			//if(D) Log.d("MyApp","Sending="+postValues);
			
			out.writeBytes(postValues); 
			out.flush(); 
			//out.close();
				
			String line;
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			Pattern pattern = Pattern.compile("ViewStopNew.aspx\\?sp=(.*?)&amp;pt");
			Pattern patternBUS = Pattern.compile("href=\"http://myride\\.gocitybus\\.com/public/laf/web/ViewStopNew\\.aspx\\?sp=(.*?)&amp;pt");
			while ((line = rd.readLine()) != null) {     
				//The page it prints out is the page it was redirected to when logged in through the browser     
				//if(D) Log.d("MyApp",line+"\n");
				Matcher matcherBUS = patternBUS.matcher(line);
				if (matcherBUS.find()) {     
					stopCode = matcherBUS.group(1); 
					//if(D) Log.d("MyApp","StopCode="+stopCode);
					break;
				} 
				
				if (line.indexOf("href=\"ViewStopNew.aspx?sp=") > 0) { 
					Matcher matcher = pattern.matcher(line);
					if (matcher.find()) {     
						stopCode = matcher.group(1); 
						quickCode = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
						//if(D) Log.d("MyApp","StopCode="+stopCode+"\nQuickCode="+quickCode);
						break;
					} 
					//stopCode = line.substring(line.indexOf("href=\"ViewStopNew.aspx?sp=") + 6, line.indexOf("\" "));
					//quickCode = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
					
				}
			} 
			rd.close();
			out.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	
	public void getViewstate(HttpURLConnection conn) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			// get the viewstate parameter                 
			String aLine;                 
			while ((aLine = in.readLine()) != null) {                     
				// System.out.println(aLine);                     
				if (aLine.lastIndexOf("id=\"__VIEWSTATE\"") > 0) {   
					//if(D) Log.d("MyApp","Viewstate="+aLine.substring(aLine.lastIndexOf("value=\"") + 7, aLine.lastIndexOf("\" ")));
					viewstate = aLine.substring(aLine.lastIndexOf("value=\"") + 7, aLine.lastIndexOf("\" "));
					                     
				}  
				if (aLine.lastIndexOf("id=\"__EVENTVALIDATION\"") > 0) { 
					//if(D) Log.d("MyApp","Validation="+aLine.substring(aLine.lastIndexOf("value=\"") + 7, aLine.lastIndexOf("\" ")));
					eventvalidation = aLine.substring(aLine.lastIndexOf("value=\"") + 7, aLine.lastIndexOf("\" "));                     
				} 
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}