/** 
 * ***********************************************
 * File		   - HTMLHandler.java
 * Description - Handler to extract schedule info
 * Author      - A. Arun Goud (DiodeDroid)
 * Date        - 2012/01/21 (First created)
 * 				 2012/01/25 (v1.0 released)
 * 				 2012/06/01 (v1.1 released)
 * email	   - microbuff@hotmail.com
 * ***********************************************
 */
package com.weebly.microbuff.purduecitybustracker;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//import android.util.Log;

//import android.util.Log;

public class HTMLHandler extends DefaultHandler {

	StringBuilder builder = new StringBuilder();
	static String currenttext = "";

	int numresults = 0;
	static int level = 0;
	static final int inBUSSTOP = 1;
	static final int outBUSSTOP = 2;
	static final int inBUSNAME = 3;
	static final int outBUSNAME = 4;
	static final int inBUSTIME = 5;
	static final int outBUSTIME = outBUSSTOP;
	//private boolean D = false;

	private String busStop = "";
	private String busNameList = "";
	private String busTimeList = "";
	
	public String getbusStop() {
		return busStop;
	}
	
	public String getbusNameList() {
		return busNameList;
	}
	
	public String getbusTimeList() {
		return busTimeList;
	}
	
	public int getNumResults() {
		return numresults;
	}
	
// inBUSSTOP->outBUSSTOP->inBUSNAME->outBUSNAME->inBUSTIME->outBUSTIME
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		if (localName.equals("span")&&(attributes.getLength()==1)) {
			
			currenttext = attributes.getValue(0);
			if (currenttext.equalsIgnoreCase("_ctl0_cphMainContent_lookupResults_lblName")) {
				level = inBUSSTOP;
				//if(D) Log.d("MyApp","In parser");
				builder.setLength(0);
			} 
			else if ((level==outBUSSTOP)&&(currenttext.contains("_lblRouteName"))) {
				level = inBUSNAME;
				builder.setLength(0);
			} 
			else if ((level==outBUSNAME)&&currenttext.contains("_lblDepartureTime")) {
				level = inBUSTIME;
				builder.setLength(0);
			} 

		}
		
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (localName.equalsIgnoreCase("span")) {
		
			if (level==inBUSSTOP) {
				level = outBUSSTOP;
				//if(D) Log.d("MyApp","Stop="+builder.toString());
				busStop = builder.toString();
			} 
			else if (level==inBUSNAME) {
				level = outBUSNAME;
				//if(D) Log.d("MyApp","Name="+builder.toString());
				busNameList = busNameList + builder.toString()+"\n";
			}
			else if (level==inBUSTIME) {
				level = outBUSTIME;
				//if(D) Log.d("MyApp","Time="+builder.toString());
				busTimeList = busTimeList + builder.toString()+"\n";
				numresults++;
			}
			
		}

	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		currenttext = new String(ch, start, length);
		builder.append(currenttext);
	}

}
