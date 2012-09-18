/** 
 * ***********************************************
 * File		   - SetPrefsActivity.java
 * Description - Preferences activity
 * Author      - A. Arun Goud (DiodeDroid)
 * Date        - 2012/01/21 (First created)
 * 				 2012/01/25 (v1.0 released)
 * 				 2012/06/01 (v1.1 released)
 * email	   - microbuff@hotmail.com
 * ***********************************************
 */
package com.weebly.microbuff.purduecitybustracker;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;

import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class SetPrefsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	private Preference stopColorPref;
	private Preference scheduleColorPref;
	private Preference shortcutColorPref;
	private Preference btnColorPref;
	private Preference backgroundColorPref;

	//private CheckBoxPreference showImagePref;
	//private CheckBoxPreference useDefaultsPref;
	
	int scheduleColor;
	int shortcutColor;
	int backgroundColor;
	int initialColor;
	int selectedColor;
	OnPreferenceClickListener mColorchangeListener;

	
	//private boolean D = false;
	
	/** Populate the preferences options with pre user-defined values */
	@Override    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		addPreferencesFromResource(R.xml.preferences);
		
		stopColorPref = findPreference("stopColor");
		scheduleColorPref = findPreference("scheduleColor"); 
		shortcutColorPref = findPreference("shortcutColor");
		btnColorPref = findPreference("btnColor");
		backgroundColorPref = findPreference("backgroundColor");
		
		mColorchangeListener = new OnPreferenceClickListener() {     
			    
			public boolean onPreferenceClick(Preference preference) {         
				final String key = preference.getKey();          
				showAmbilWarnaDialog(key);					
				return true;
				//return false; // we didn't handle the click     
			} 
		}; 
				
		stopColorPref.setOnPreferenceClickListener(mColorchangeListener);
		scheduleColorPref.setOnPreferenceClickListener(mColorchangeListener);
		shortcutColorPref.setOnPreferenceClickListener(mColorchangeListener);
		btnColorPref.setOnPreferenceClickListener(mColorchangeListener);
		backgroundColorPref.setOnPreferenceClickListener(mColorchangeListener);
		

		//showImagePref = (CheckBoxPreference)getPreferenceScreen().findPreference("showImage");
		//useDefaultsPref = (CheckBoxPreference)getPreferenceScreen().findPreference("useDefaults");
		
		//if(D) Log.d("MyApp","Changed...");
	}
	
	
	/** Whenever the user changes an option, it'll be displayed as a summary */
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {     
		//if(D) Log.d("MyApp","Changed="+key);
		String strselectedColor;
		Spannable summary;
		
		if (key.equalsIgnoreCase("stopColor")) { 
			//if(D) Log.d("MyApp","Changed stop");
			strselectedColor = sharedPreferences.getString("stopColor","#3c76a8");
			summary = new SpannableString(strselectedColor); 
			summary.setSpan(new ForegroundColorSpan(Color.parseColor(summary.toString())), 0, summary.length(), 0 ); 
			stopColorPref.setSummary(summary);
						
		}
		else if (key.equalsIgnoreCase("scheduleColor")) { 
			//if(D) Log.d("MyApp","Changed schedule");
			strselectedColor = sharedPreferences.getString("scheduleColor","#575757");
			summary = new SpannableString(strselectedColor); 
			summary.setSpan(new ForegroundColorSpan(Color.parseColor(summary.toString())), 0, summary.length(), 0 ); 
			scheduleColorPref.setSummary(summary); 			
		} 
		else if (key.equalsIgnoreCase("shortcutColor")) {         			       
			strselectedColor = sharedPreferences.getString("shortcutColor","#3c76a8");
			summary = new SpannableString(strselectedColor); 
			summary.setSpan(new ForegroundColorSpan(Color.parseColor(summary.toString())), 0, summary.length(), 0 ); 
			shortcutColorPref.setSummary(summary); 			
		}
		else if (key.equalsIgnoreCase("btnColor")) { 
			strselectedColor = sharedPreferences.getString("btnColor","#575757");
			summary = new SpannableString(strselectedColor); 
			summary.setSpan(new ForegroundColorSpan(Color.parseColor(summary.toString())), 0, summary.length(), 0 ); 
			btnColorPref.setSummary(summary);  			
		}
		else if (key.equalsIgnoreCase("backgroundColor")) {         			       
			strselectedColor = sharedPreferences.getString("backgroundColor","#000000");
			summary = new SpannableString(strselectedColor); 
			summary.setSpan(new ForegroundColorSpan(Color.parseColor(summary.toString())), 0, summary.length(), 0 ); 
			backgroundColorPref.setSummary(summary);  			
		}

	} 
	
	public void showAmbilWarnaDialog(String key) {
		final Preference changePref = findPreference(key);
		final String keyChange = key;
		int initialColor;
		SharedPreferences appPref = PreferenceManager.getDefaultSharedPreferences(this);
		final SharedPreferences.Editor appPrefEditor = appPref.edit();
		
		initialColor = Color.parseColor(changePref.getSummary().toString());
		
		AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, initialColor, new OnAmbilWarnaListener() {
			public void onOk(AmbilWarnaDialog dialog, int selectedColor) {
				String strselectedColor = "#"+colortoString(selectedColor);
				//Spannable summary = new SpannableString(strselectedColor); 
				//summary.setSpan(new ForegroundColorSpan(selectedColor), 0, summary.length(), 0 ); 
				//changePref.setSummary(summary);
				appPrefEditor.putString(keyChange, strselectedColor);
				appPrefEditor.commit();
 				
			}     
			public void onCancel(AmbilWarnaDialog dialog) {
				// cancel was selected by the user
			}
			
		});			
		dialog.show();
	}
	
    @Override 
    public boolean onPrepareOptionsMenu (Menu menu) { 
    	menu.clear();
    	super.onCreateOptionsMenu(menu);
    	MenuInflater mi = getMenuInflater();
    	mi.inflate(R.menu.preference_menu,menu);     
    	return true; 
    } 
    
    /** What to do when menu options are selected. */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

    	switch(item.getItemId()) {
							
    		case R.id.return_search:	SetPrefsActivity.this.finish();
    									break;
    									
    		case R.id.startover:	SharedPreferences appPref = PreferenceManager.getDefaultSharedPreferences(this);
    								SharedPreferences.Editor appPrefEditor = appPref.edit();
    								appPrefEditor.putString("stopColor","#3c76a8");
    								appPrefEditor.putString("scheduleColor","#575757");
    								appPrefEditor.putString("shortcutColor","#3c76a8");
    								appPrefEditor.putString("btnColor","#575757");
    								appPrefEditor.putString("backgroundColor","#000000");
    								appPrefEditor.commit();
    								break;
    	
    	}

    	return true;
    }
    
	/** When brought to the foreground, display selected values as summary */
    @Override     
    protected void onResume() {         
    	super.onResume();          
    	// Setup the initial values
    	SharedPreferences appPref = PreferenceManager.getDefaultSharedPreferences(this);
		//final Map<String, ?> colorValues = appPref.getAll();
		
		Spannable summary;
		
		summary = new SpannableString(appPref.getString("stopColor","#3c76a8")); 
		summary.setSpan(new ForegroundColorSpan(Color.parseColor(summary.toString())), 0, summary.length(), 0 ); 
    	stopColorPref.setSummary(summary);
    	
		summary = new SpannableString(appPref.getString("scheduleColor","#575757")); 
		summary.setSpan(new ForegroundColorSpan(Color.parseColor(summary.toString())), 0, summary.length(), 0 ); 
    	scheduleColorPref.setSummary(summary);
    	
		summary = new SpannableString(appPref.getString("shortcutColor","#3c76a8")); 
		summary.setSpan(new ForegroundColorSpan(Color.parseColor(summary.toString())), 0, summary.length(), 0 ); 
    	shortcutColorPref.setSummary(summary);
    	
    	summary = new SpannableString(appPref.getString("btnColor","#575757")); 
		summary.setSpan(new ForegroundColorSpan(Color.parseColor(summary.toString())), 0, summary.length(), 0 ); 
    	btnColorPref.setSummary(summary);
    	
		summary = new SpannableString(appPref.getString("backgroundColor","#000000")); 
		summary.setSpan(new ForegroundColorSpan(Color.parseColor(summary.toString())), 0, summary.length(), 0 ); 
    	backgroundColorPref.setSummary(summary);

    	
    	// Set up a listener whenever a key changes                     
    	getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);     
    }      
    
    @Override     
    protected void onPause() {         
    	super.onPause();          
    	// Unregister the listener whenever a key changes                     
    	getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);         
    	 
    }

    
    static String colortoString(int color){     
    	return Integer.toHexString(Color.rgb(Color.red(color), Color.green(color), Color.blue(color))); 
    } 
    
 
}