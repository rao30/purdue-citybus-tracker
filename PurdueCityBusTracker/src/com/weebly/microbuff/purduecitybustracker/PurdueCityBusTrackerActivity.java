/** 
 * ***********************************************
 * File		   - PurdueCityBusTrackerActivity.java
 * Description - Main activity for the app
 * Author      - A. Arun Goud (DiodeDroid)
 * Date        - 2012/01/21 (First created)
 * 				 2012/01/25 (v1.0 released)
 * 				 2012/06/01 (v1.1 released)
 * email	   - microbuff@hotmail.com
 * ***********************************************
 */
package com.weebly.microbuff.purduecitybustracker;

import java.net.URL;
import java.util.Map;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
//import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PurdueCityBusTrackerActivity extends Activity {
	private UpdateTask update;
	private boolean updateRunning = false;
	
	private String search = "";
	private String busstop = "Stop";
	private String busname = "";
	private String bustime = "";
	
	private String stopColor;
	private String scheduleColor;
	private String shortcutColor;
	private String btnColor;
	private String backgroundColor;
	
	private boolean showImage = true;
	private boolean useDefaults = true;
	private boolean onStart = true;
	//private boolean D = false;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);       
      
        setEnterPressListener();
        setSearchButtonClickListener();
        setShortcutClickListener();
		
		readColorPrefs();	
		
		/** Restore saved state */
        if (savedInstanceState != null) {
        	search = savedInstanceState.getString("search");
        	Button buttonSearch = (Button)findViewById(R.id.searchbutton);
        	
        	if (!search.equalsIgnoreCase("")) {
        		//if(D) Log.d("MyApp","Search="+search);
        		//busStopsearch.setText(search);
        		//buttonSearch.performClick();
        		busstop = savedInstanceState.getString("stopname");
        		busname = savedInstanceState.getString("busname");
        		bustime = savedInstanceState.getString("bustime");
        		
        	}
        	
        	if (savedInstanceState.getString("stopname").equalsIgnoreCase("Updating..."))
        		buttonSearch.performClick();
        }
        
        onStart = true;
        setColorBtnsTextBackground(search,busstop,busname,bustime);
    }
    
    private void setSearchButtonClickListener() {
    	Button buttonSearch = (Button)findViewById(R.id.searchbutton);
    	buttonSearch.getBackground().setColorFilter(0xFF575757, Mode.MULTIPLY);
    	final EditText busStopsearch = (EditText) findViewById(R.id.searchbox);
    	//final TextView busStop = (TextView) findViewById(R.id.busstop);
    	//final TextView busName = (TextView) findViewById(R.id.busname);
    	//final TextView busTime = (TextView) findViewById(R.id.bustime);
    	
    	buttonSearch.setOnClickListener(new View.OnClickListener() {
    		
			public void onClick(View v) {
    			/**HTMLHandler pctHTMLHandler = new HTMLHandler();
    			String strPCBurl = "";
    			try {
    				
    				XMLReader xr = XMLReaderFactory.createXMLReader("org.ccil.cowan.tagsoup.Parser");
    				ASPNETpost busstopSearch = new ASPNETpost();
    				busstopSearch.searchStopCode(busStopsearch.getText().toString());
    				strPCBurl = "http://myride.gocitybus.com/public/laf/web/ViewStopNew.aspx?sp="+busstopSearch.getStopCode();
    				URL sourceUrl = new URL(strPCBurl);

    				xr.setContentHandler(pctHTMLHandler);
    				//if(D) Log.d("MyApp","Calling SAX parser ");
    				InputSource pcthtml = new InputSource(sourceUrl.openStream());
    				pcthtml.setEncoding("utf-8");
    				xr.parse(pcthtml); 
    				
    			} catch (Exception e) {
    				if(D) Log.d("MyApp","HTML Parsing Exception = " + e);
    				e.printStackTrace();
    			} 
   	
    			busStop.setText(pctHTMLHandler.getbusStop());
    			busName.setText(pctHTMLHandler.getbusNameList());
				busTime.setText(pctHTMLHandler.getbusTimeList());
    			if (pctHTMLHandler.getNumResults()==0) {	
    				Toast.makeText(PurdueCityBusTrackerActivity.this, "There are no buses running at this time.", Toast.LENGTH_LONG).show();
    			}*/
				
				/** Runs search in background AsyncTask */
				if(!busStopsearch.getText().toString().equalsIgnoreCase("")) {
					update = new UpdateTask();
					update.execute(busStopsearch.getText().toString());
					updateRunning = true;
				}
				else {
					Toast toast = Toast.makeText(PurdueCityBusTrackerActivity.this, "Please enter a search term.", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
					toast.show();
				}
			}
    	});
    }
    
    /** Listeners for the 4 shortcut buttons */
    private void setShortcutClickListener() {
    	final Button buttonSearch = (Button)findViewById(R.id.searchbutton);
    	final EditText busStopsearch = (EditText) findViewById(R.id.searchbox);
    	final Button buttonShortcut1 = (Button)findViewById(R.id.shortcut1);
    	final Button buttonShortcut2 = (Button)findViewById(R.id.shortcut2);
    	final Button buttonShortcut3 = (Button)findViewById(R.id.shortcut3);
    	final Button buttonShortcut4 = (Button)findViewById(R.id.shortcut4);
    	
    	buttonShortcut1.setOnClickListener(new View.OnClickListener() {
    		
			public void onClick(View v) {
				busStopsearch.setText(buttonShortcut1.getTag().toString());
				buttonSearch.performClick();
			}
    	});
    	
    	buttonShortcut2.setOnClickListener(new View.OnClickListener() {
    		
			public void onClick(View v) {
				busStopsearch.setText(buttonShortcut2.getTag().toString());
				buttonSearch.performClick();
			}
    	});
    	
    	buttonShortcut3.setOnClickListener(new View.OnClickListener() {
    		
			public void onClick(View v) {
				busStopsearch.setText(buttonShortcut3.getTag().toString());
				buttonSearch.performClick();
			}
    	});
    	
    	buttonShortcut4.setOnClickListener(new View.OnClickListener() {
    		
			public void onClick(View v) {
				busStopsearch.setText(buttonShortcut4.getTag().toString());
				buttonSearch.performClick();
			}
    	});
    	
    }
    
    /** Listens for enter key press and executes search */
    private void setEnterPressListener() { 

    	final EditText textSearch = (EditText)findViewById(R.id.searchbox);

    	textSearch.setOnKeyListener(new View.OnKeyListener() {          
    		public boolean onKey(View v, int keyCode, KeyEvent event) {  
    	    	Button buttonSearch = (Button)findViewById(R.id.searchbutton);
    			if (keyCode == KeyEvent.KEYCODE_ENTER) {                 
    				// perform search   
    				buttonSearch.performClick();
    				return true;             
    			}             
    			return false;         
    		}     
    	}); 
    			
    } 
    
    /** Menu layout in /res/menu/citybus_menu.xml */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater mi = getMenuInflater();
    	mi.inflate(R.menu.citybus_menu,menu);
    	return true;
    }
    
    /** What to do when menu options are selected */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	switch(item.getItemId()) {
    		case R.id.save:		saveSearch();
    							break;
    		case R.id.shortcut:	shortcutChooser();
    							break;
    		case R.id.busstops: Intent i = new Intent(PurdueCityBusTrackerActivity.this,OpenURLActivity.class);											
								i.putExtra("URL","http://www.gocitybus.com/");
								PurdueCityBusTrackerActivity.this.startActivity(i);
    							break;
    		case R.id.preferences:  // initialColor is the initially-selected color to be shown in the rectangle on the left of the arrow.
    								// for example, 0xff000000 is black, 0xff0000ff is blue. Please be aware of the initial 0xff which is the alpha.
    								/**int initialColor = 0xff0000ff;
    								AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, initialColor, new OnAmbilWarnaListener() {
    									public void onOk(AmbilWarnaDialog dialog, int color) {
    										// color is the color selected by the user.
    									}
    			                
    									public void onCancel(AmbilWarnaDialog dialog) {
    										// cancel was selected by the user
    									}
    									
    								});
    								
    								dialog.show();*/
    								Intent showprefs = new Intent(PurdueCityBusTrackerActivity.this, SetPrefsActivity.class);         
    								startActivity(showprefs);
    								
									break;
    		case R.id.about: 	showAbout();
    							break;
    		case R.id.exit:		finish();
    							android.os.Process.killProcess(android.os.Process.myPid());
    	
    	}

    	return true;
    }
    
    /** Save shortcut name into shared preferences */
    public void saveSearch() {
    	final EditText busStopsearchsave = (EditText) findViewById(R.id.searchbox);
    	AlertDialog.Builder saveSearch = new AlertDialog.Builder(this);  
    	saveSearch.setTitle("Save Search"); 
    	saveSearch.setMessage("Enter a shortcut name for this search keyword");  
		// Set an EditText view to get user input  
		final EditText inputsaveSearch = new EditText(this);
		inputsaveSearch.setText(busStopsearchsave.getText().toString());
		inputsaveSearch.setSingleLine();
		saveSearch.setView(inputsaveSearch);  
		saveSearch.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			 
			public void onClick(DialogInterface dialog, int whichButton) { 
				Editable saveName = inputsaveSearch.getText();  // Do something with value!
				if (!saveName.toString().equalsIgnoreCase("")) {
					 SharedPreferences savedPref = getApplicationContext().getSharedPreferences("Shortcuts", MODE_PRIVATE);
					 SharedPreferences.Editor savedPrefEditor = savedPref.edit();
					 if(!savedPref.contains(saveName.toString())) {
						 savedPrefEditor.putString(saveName.toString(), busStopsearchsave.getText().toString());
						 savedPrefEditor.commit();
						 //if(D) Log.d("SPref","bm="+bookmarkPref.getString(bookmark.toString(), webview.getUrl()));
						 Toast toast = Toast.makeText(PurdueCityBusTrackerActivity.this, "Search term "+"\""+saveName.toString()+"\" has been saved.", Toast.LENGTH_SHORT);
						 toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
						 toast.show();
						 
					 }
					 else {
						 Toast toast = Toast.makeText(PurdueCityBusTrackerActivity.this, "Search term "+"\""+saveName.toString()+"\" already exists.", Toast.LENGTH_SHORT);
						 toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
						 toast.show();
					 }
				}
				else {
					Toast toast = Toast.makeText(PurdueCityBusTrackerActivity.this, "Invalid name.", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
					toast.show();
				}

			} 
		});  
		saveSearch.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {  
			public void onClick(DialogInterface dialog, int whichButton) {      
				// Do nothing
			} 
		});   
		saveSearch.show(); 
		
    }
    
    /** Lets user choose shortcut to be displayed or removed from main activity */
    public void shortcutChooser() {
    	
    	final Button buttonShortcut1 = (Button)findViewById(R.id.shortcut1);
    	final Button buttonShortcut2 = (Button)findViewById(R.id.shortcut2);
    	final Button buttonShortcut3 = (Button)findViewById(R.id.shortcut3);
    	final Button buttonShortcut4 = (Button)findViewById(R.id.shortcut4);
    	
    	SharedPreferences shortcutPref = getApplicationContext().getSharedPreferences("Shortcuts", MODE_PRIVATE);
		final SharedPreferences.Editor shortcutEditor = shortcutPref.edit();
		final Map<String, ?> items = shortcutPref.getAll();
		
	   	final String[] mBusstopList = new String[items.keySet().size()];
	   	int count = 0;
		for(String s : items.keySet()){
			mBusstopList[count] = s;
			count++;		    
		}
		    
		final SharedPreferences checkedPref = getApplicationContext().getSharedPreferences("CheckedShortcuts", MODE_PRIVATE);
		final SharedPreferences.Editor checkedEditor = checkedPref.edit();
		final Map<String, ?> checkeditems = checkedPref.getAll();
		
		//final String[] chosenSC = new String[4];
		//final String[] chosenKey = new String[4];
		final boolean[] isChecked = new boolean[count];
		
		if(checkeditems.keySet().size()>0){
			for(int i=0;i<count;i++){
				if(checkeditems.containsKey(mBusstopList[i]))
					isChecked[i]=true;
				else
					isChecked[i]=false;				
			}
		}
		
    	AlertDialog.Builder builder = new Builder(this);    
    	builder.setTitle("Shortcuts");  
    	
    	builder.setMultiChoiceItems(mBusstopList, isChecked, new DialogInterface.OnMultiChoiceClickListener(){
    		
    		public void onClick(DialogInterface dialog, int which, boolean status){ 
    			if (mBusstopList.length>0) {
    				//chosenSC[0] = items.get(mBusstopList[which]).toString();
    				//chosenKey[0] = mBusstopList[which].toString();
    				//if(D) Log.d("File","key="+chosenKey[0]+" checked="+status+" file="+chosenSC[0]);
    				
    			}
    		}       
    	}); 

    	builder.setPositiveButton("Display", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				int numChecked = 0;
				buttonShortcut1.setVisibility(View.GONE);
				buttonShortcut2.setVisibility(View.GONE);
				buttonShortcut3.setVisibility(View.GONE);
				buttonShortcut4.setVisibility(View.GONE);
				if (mBusstopList.length>0) {
					//if(D) Log.d("File","key="+mFileList[0]+" file="+chosenFile);
					checkedEditor.clear();
					//boolean commitneeded = false;
					for(int i=0;i<isChecked.length;i++) {
						if(isChecked[i]) {
							numChecked++;
							if(numChecked==1) {
								buttonShortcut1.setText(mBusstopList[i].toString());
								buttonShortcut1.setTag(items.get(mBusstopList[i]).toString());
								buttonShortcut1.setVisibility(View.VISIBLE);
								buttonShortcut1.getBackground().setColorFilter(0xFF575757, Mode.MULTIPLY);
								checkedEditor.putString(mBusstopList[i].toString(), items.get(mBusstopList[i]).toString());
								//if(D) Log.d("MyApp","Saved1="+mBusstopList[i].toString());
								//commitneeded = true;
							}
							else if(numChecked==2) {
								buttonShortcut2.setText(mBusstopList[i].toString());
								buttonShortcut2.setTag(items.get(mBusstopList[i]).toString());
								buttonShortcut2.setVisibility(View.VISIBLE);
								buttonShortcut2.getBackground().setColorFilter(0xFF575757, Mode.MULTIPLY);
								checkedEditor.putString(mBusstopList[i].toString(), items.get(mBusstopList[i]).toString());
								//if(D) Log.d("MyApp","Saved2="+mBusstopList[i].toString());
								//commitneeded = true;
							}
							else if(numChecked==3) {
								buttonShortcut3.setText(mBusstopList[i].toString());
								buttonShortcut3.setTag(items.get(mBusstopList[i]).toString());
								buttonShortcut3.setVisibility(View.VISIBLE);
								buttonShortcut3.getBackground().setColorFilter(0xFF575757, Mode.MULTIPLY);
								checkedEditor.putString(mBusstopList[i].toString(), items.get(mBusstopList[i]).toString());
								//if(D) Log.d("MyApp","Saved3="+mBusstopList[i].toString());
								//commitneeded = true;
							}
							else if(numChecked==4) {
								buttonShortcut4.setText(mBusstopList[i].toString());
								buttonShortcut4.setTag(items.get(mBusstopList[i]).toString());
								buttonShortcut4.setVisibility(View.VISIBLE);
								buttonShortcut4.getBackground().setColorFilter(0xFF575757, Mode.MULTIPLY);
								checkedEditor.putString(mBusstopList[i].toString(), items.get(mBusstopList[i]).toString());
								//if(D) Log.d("MyApp","Saved4="+mBusstopList[i].toString());
								//commitneeded = true;
								break;
							}
						}
					}
					//if(commitneeded)
						checkedEditor.commit();
					
				}
				
	    		readColorPrefs();
	    		setColorBtnsTextBackground(search,busstop,busname,bustime);
			}
		});
    	
    	/**builder.setNeutralButton("Import", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (mBusstopList.length>0) {
					final File mPath = new File(Environment.getExternalStorageDirectory()+"/ScholarDroid/");
					File targetFile = new File(mPath,"Bookmarks.txt");
					StringBuilder textToWrite = new StringBuilder();
					for(String s : items.keySet()){
						textToWrite.append(s+"\t"+items.get(s)+"\n");
					}
					try {
						FileWriter bookmarkFile = new FileWriter(targetFile);
						//for(String s : items.keySet()){
							//bookmarkFile.append(s+"\t"+items.get(s)+"\n");		    
						//}
						bookmarkFile.append(textToWrite.toString());
						bookmarkFile.flush();
						bookmarkFile.close();
						Toast.makeText(PurdueCityBusTrackerActivity.this, "Bookmarks have been exported to Bookmarks.txt", Toast.LENGTH_LONG).show();
						
					}
					catch(Exception e) {}
				}
			}
		});*/
    	
    	builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (mBusstopList.length>0) {
					boolean lookinChecked = false;
					Map<String, ?> itemsChecked = checkedPref.getAll();
					for(int i=0;i<isChecked.length;i++) {
						if(isChecked[i]) {
							shortcutEditor.remove(mBusstopList[i].toString());
							if(itemsChecked.containsKey(mBusstopList[i].toString())) {
								lookinChecked = true;
								checkedEditor.remove(mBusstopList[i].toString());
							}
						}
					}
					shortcutEditor.commit();
					if(lookinChecked)
						checkedEditor.commit();
					
				}
			}
		});
    	
    	builder.show();
    	return;
    	
    }
    
    /** Save state so that it can be restored (on screen rotations or when user leaves 
     * and later returns to this app) */
    protected void onSaveInstanceState(Bundle savedInstanceState) {
    	
    	final EditText busStopsearch = (EditText) findViewById(R.id.searchbox);
    	TextView busStop = (TextView) findViewById(R.id.busstop);
    	TextView busName = (TextView) findViewById(R.id.busname);
    	TextView busTime = (TextView) findViewById(R.id.bustime);
    	
    	savedInstanceState.putString("search", busStopsearch.getText().toString());
    	savedInstanceState.putString("stopname", busStop.getText().toString());
    	savedInstanceState.putString("busname", busName.getText().toString());
    	savedInstanceState.putString("bustime", busTime.getText().toString());
    	super.onSaveInstanceState(savedInstanceState);
    	
    }
    
    /** AsyncTask which carries out search function asynchronously */
    private class UpdateTask extends AsyncTask<String, Void, String> {

    	TextView busStopUT;
    	TextView busNameUT;
    	TextView busTimeUT;
    	HTMLHandler pctHTMLHandler;
    	String spcode;
    	int results;
    	
    	protected void onPreExecute() {
        	busStopUT = (TextView) findViewById(R.id.busstop);
        	busNameUT = (TextView) findViewById(R.id.busname);
        	busTimeUT = (TextView) findViewById(R.id.bustime);
        	Spannable summary;
    		summary = new SpannableString("Updating..."); 
			summary.setSpan(new ForegroundColorSpan(Color.parseColor(stopColor)), 0, summary.length(), 0 );
        	busStopUT.setText(summary);
        	busNameUT.setText("");
        	busTimeUT.setText("");
    	}
    	
    	/** Carries out automated posting of MyRideWEB ASP.NET form.
    	 * The user entered query (busstop name) is passed to ASPNETpost() object. This retrieves
    	 * the webpage containing list of names of buses arriving at that stop and returns the stop code
    	 * for the top-most bus in the list. This is used to generate a new query to pull the page 
    	 * containing the departure schedules. (Only the top-most bus name and its code needs to be 
    	 * extracted because all subsequent buses in the list direct to the same schedule page)
    	 * TagSoup is then used within the HTMLHandler() object to extract the schedule info from the page. */
    	protected String doInBackground(String... query) {          
  			pctHTMLHandler = new HTMLHandler();
			String strPCBurl = "";
			try {
				
				XMLReader xr = XMLReaderFactory.createXMLReader("org.ccil.cowan.tagsoup.Parser");
				ASPNETpost busstopSearch = new ASPNETpost();
				busstopSearch.searchStopCode(query[0]);
				spcode = busstopSearch.getStopCode();
				
				strPCBurl = "http://myride.gocitybus.com/public/laf/web/ViewStopNew.aspx?sp="+spcode;
				URL sourceUrl = new URL(strPCBurl);

				xr.setContentHandler(pctHTMLHandler);
				//if(D) Log.d("MyApp","Calling SAX parser ");
				InputSource pcthtml = new InputSource(sourceUrl.openStream());
				pcthtml.setEncoding("utf-8");
				xr.parse(pcthtml); 
				results = pctHTMLHandler.getNumResults();
				updateRunning = false;
				
			} catch (Exception e) {
				//if(D) Log.d("MyApp","HTML Parsing Exception = " + e);
				e.printStackTrace();
			} 
			return null;
			
    	} 
    	
    	/** Displays the schedule info */
    	protected void onPostExecute(String result) {
    		Spannable summary;
    		summary = new SpannableString(pctHTMLHandler.getbusStop()); 
			summary.setSpan(new ForegroundColorSpan(Color.parseColor(stopColor)), 0, summary.length(), 0 );
			busStopUT.setText(summary);
			//String extra = "\nExtra\nExtra\nExtra\nExtra\nExtra\nExtra\nExtra\nExtra\nExtra\nExtra\nExtra\nExtra\nExtra\nExtra\nExtra\nExtra\nExtra";
			//extra = "Hello\nThere";
			summary = new SpannableString(pctHTMLHandler.getbusNameList()+"\n"); 
			//if(D) Log.d("MyApp","Post="+scheduleColor);
			summary.setSpan(new ForegroundColorSpan(Color.parseColor(scheduleColor)), 0, summary.length(), 0 );
			busNameUT.setText(summary);
			
			summary = new SpannableString(pctHTMLHandler.getbusTimeList()+"\n"); 
			summary.setSpan(new ForegroundColorSpan(Color.parseColor(scheduleColor)), 0, summary.length(), 0 );
			busTimeUT.setText(summary);
			if (results==0) {
				Toast toast = Toast.makeText(PurdueCityBusTrackerActivity.this, "There are no buses arriving at this time/bus stop.", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
				toast.show();
			} 
    	}  
    }
    
    /** About screen */
    public void showAbout() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(PurdueCityBusTrackerActivity.this);
    	builder.setTitle("About Purdue CityBus Tracker");
    	builder.setMessage("Purdue CityBus Tracker is a free app that lets you track " +
    			"Lafayette/West Lafayette CityBus departure schedules at the click of a button.\n\n"+
    			"Version - 1.1\nDiodeDroid, 2012\nmicrobuff@hotmail.com\n\n"+
    			"Uses TagSoup 1.2.1 and AmbilWarna libraries licensed under the Apache License, Version 2.0");
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
    	builder.setNegativeButton("AL v2.0", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent i = new Intent(PurdueCityBusTrackerActivity.this,OpenURLActivity.class);											
				i.putExtra("URL","http://www.apache.org/licenses/LICENSE-2.0.html");
				PurdueCityBusTrackerActivity.this.startActivity(i);
			}
		});
    	builder.create().show();
    }
    
    @Override 
    protected void onPause() {   	
    	super.onPause();
    	if(updateRunning) {
    		//if(D) Log.d("MyApp","Cancelling...");
    		update.cancel(true);
    	}
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	//if(D) Log.d("MyApp","Stop="+busstop);
    	if(!onStart) {
    		readColorPrefs();
    		//if(D) Log.d("MyApp","Start Stop="+busstop);
    		setColorBtnsTextBackground(search,busstop,busname,bustime);
    	}
    	onStart = false;
    }
    
    /** Preferences set by users through preferences menu */
    public void readColorPrefs() {
		SharedPreferences appPref = PreferenceManager.getDefaultSharedPreferences(this);
		//final Map<String, ?> colorValues = appPref.getAll();
		stopColor = appPref.getString("stopColor","#3c76a8");
		scheduleColor = appPref.getString("scheduleColor","#575757");
		//if(D) Log.d("MyApp","Schedule="+scheduleColor);
		shortcutColor = appPref.getString("shortcutColor","#3c76a8");
		btnColor = appPref.getString("btnColor","#575757");
		backgroundColor = appPref.getString("backgroundColor","#000000");
		showImage = appPref.getBoolean("showImage",true);
		useDefaults = appPref.getBoolean("useDefaults",true);
		
		if(useDefaults) {
			stopColor = "#3c76a8";
			scheduleColor = "#575757";
			shortcutColor = "#3c76a8";
			btnColor = "#575757";
			backgroundColor = "#000000";
			showImage = true;			
		}
		
    	
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.rellayout);
		if(showImage) {
			Drawable background = getResources().getDrawable(R.drawable.purdue11); 
			layout.setBackgroundDrawable(background); 
		}
		else {
			layout.setBackgroundResource(0);
			layout.setBackgroundColor(Color.parseColor(backgroundColor));
		}
    }
    
    /** Assigns user chosen preferences to the controls */
    public void setColorBtnsTextBackground(String search, String busstop, String busname, String bustime) {
        
		SharedPreferences checkedPref = getApplicationContext().getSharedPreferences("CheckedShortcuts", MODE_PRIVATE);
		Map<String, ?> checkeditems = checkedPref.getAll();
		
		
		Spannable summary;
		
		//if(D) Log.d("MyApp","Size="+checkeditems.size());
		if(checkeditems.size()>0) {
	    	final Button buttonShortcut1 = (Button)findViewById(R.id.shortcut1);
	    	final Button buttonShortcut2 = (Button)findViewById(R.id.shortcut2);
	    	final Button buttonShortcut3 = (Button)findViewById(R.id.shortcut3);
	    	final Button buttonShortcut4 = (Button)findViewById(R.id.shortcut4);
	    	
		   	int count = 0;
		   	final String[] shortcutName = new String[checkeditems.keySet().size()];
			for(String s : checkeditems.keySet()){
				shortcutName[count] = s;
				//if(D) Log.d("MyApp","SavedPref="+s);
				count++;		    
			}
			
			
			summary = new SpannableString(shortcutName[0].toString()); 
			summary.setSpan(new ForegroundColorSpan(Color.parseColor(shortcutColor)), 0, summary.length(), 0 ); 
	    	
	    	//buttonShortcut1.setText(shortcutName[0].toString());
	    	buttonShortcut1.setText(summary);
	    	buttonShortcut1.setTag(checkeditems.get(shortcutName[0]).toString());
	    	buttonShortcut1.setVisibility(View.VISIBLE);
	    	buttonShortcut1.getBackground().setColorFilter(Color.parseColor(btnColor), Mode.MULTIPLY); 
	    	
	    	if(count>1) {
	    		summary = new SpannableString(shortcutName[1].toString()); 
				summary.setSpan(new ForegroundColorSpan(Color.parseColor(shortcutColor)), 0, summary.length(), 0 );
				
	    		buttonShortcut2.setText(summary);
		    	buttonShortcut2.setTag(checkeditems.get(shortcutName[1]).toString());
		    	buttonShortcut2.setVisibility(View.VISIBLE);
		    	buttonShortcut2.getBackground().setColorFilter(Color.parseColor(btnColor), Mode.MULTIPLY);
	    	}
	    	
	    	if(count>2) {
	    		summary = new SpannableString(shortcutName[2].toString()); 
				summary.setSpan(new ForegroundColorSpan(Color.parseColor(shortcutColor)), 0, summary.length(), 0 );
	    		buttonShortcut3.setText(summary);
		    	buttonShortcut3.setTag(checkeditems.get(shortcutName[2]).toString());
		    	buttonShortcut3.setVisibility(View.VISIBLE);
		    	buttonShortcut3.getBackground().setColorFilter(Color.parseColor(btnColor), Mode.MULTIPLY);
	    	}
	    	
	    	if(count>3) {
	    		summary = new SpannableString(shortcutName[3].toString()); 
				summary.setSpan(new ForegroundColorSpan(Color.parseColor(shortcutColor)), 0, summary.length(), 0 );
	    		buttonShortcut4.setText(summary);
		    	buttonShortcut4.setTag(checkeditems.get(shortcutName[3]).toString());
		    	buttonShortcut4.setVisibility(View.VISIBLE);
		    	buttonShortcut4.getBackground().setColorFilter(Color.parseColor(btnColor), Mode.MULTIPLY);
	    	}
	    	
		}
		
		Button buttonSearchUT = (Button)findViewById(R.id.searchbutton);
		EditText busSearchUT = (EditText) findViewById(R.id.searchbox);
    	TextView busStopUT = (TextView) findViewById(R.id.busstop);
    	TextView busNameUT = (TextView) findViewById(R.id.busname);
    	TextView busTimeUT = (TextView) findViewById(R.id.bustime);
    	TextView busRouteUT = (TextView) findViewById(R.id.busroute);
    	TextView busDepartureUT = (TextView) findViewById(R.id.busdeparture);
    	
    	summary = new SpannableString(buttonSearchUT.getText().toString()); 
		summary.setSpan(new ForegroundColorSpan(Color.parseColor(shortcutColor)), 0, summary.length(), 0 );
		buttonSearchUT.setText(summary);
    	buttonSearchUT.getBackground().setColorFilter(Color.parseColor(btnColor), Mode.MULTIPLY);
    	
    	if(onStart)
    		summary = new SpannableString(search); 
    	else
    		summary = new SpannableString(busSearchUT.getText());
		summary.setSpan(new ForegroundColorSpan(Color.parseColor(shortcutColor)), 0, summary.length(), 0 );
		busSearchUT.setText(summary);
		
		if(onStart)
			summary = new SpannableString(busstop);
		else
			summary = new SpannableString(busStopUT.getText());
		summary.setSpan(new ForegroundColorSpan(Color.parseColor(stopColor)), 0, summary.length(), 0 );
		busStopUT.setText(summary);
		
		if(onStart)
			summary = new SpannableString(busname); 
		else
			summary = new SpannableString(busNameUT.getText());
		summary.setSpan(new ForegroundColorSpan(Color.parseColor(scheduleColor)), 0, summary.length(), 0 );
		busNameUT.setText(summary);
		
		if(onStart)
			summary = new SpannableString(bustime);
		else
			summary = new SpannableString(busTimeUT.getText());
		summary.setSpan(new ForegroundColorSpan(Color.parseColor(scheduleColor)), 0, summary.length(), 0 );
		busTimeUT.setText(summary);
		
		summary = new SpannableString(busRouteUT.getText()); 
		summary.setSpan(new ForegroundColorSpan(Color.parseColor(scheduleColor)), 0, summary.length(), 0 );
		busRouteUT.setText(summary);
		
		summary = new SpannableString(busDepartureUT.getText()); 
		summary.setSpan(new ForegroundColorSpan(Color.parseColor(scheduleColor)), 0, summary.length(), 0 );
		busDepartureUT.setText(summary);
		
		
    	
    }
}