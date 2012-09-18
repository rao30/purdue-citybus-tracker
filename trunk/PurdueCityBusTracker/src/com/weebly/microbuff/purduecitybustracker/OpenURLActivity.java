/** 
 * ***********************************************
 * File		   - OpenURLActivity.java
 * Description - Handles opening of webpages using WebView
 *               Borrowed from Scholar Droid
 * Author      - A. Arun Goud (DiodeDroid)
 * Date        - 2012/01/21 (First created)
 * 				 2012/01/25 (v1.0 released)
 * 				 2012/06/01 (v1.1 released)
 * email	   - microbuff@hotmail.com
 * ***********************************************
 */
package com.weebly.microbuff.purduecitybustracker;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
//import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions; 
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class OpenURLActivity extends Activity {
	private String strGSurl = "";
	private WebView webview;
	/**static final int onlyLink = 1;
	static final int freePDF = 2;
	static final int book = 3;
	int type;*/
	private String downloadlink = "";
	//private String downloadPath = "";
	//private boolean D = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        
        //SharedPreferences appPref = PreferenceManager.getDefaultSharedPreferences(this);
        //downloadPath = appPref.getString("downloadPath", "/ScholarDroid/");
        
        webview = (WebView)findViewById(R.id.webview);    
        webview.getSettings().setJavaScriptEnabled(true);
        
        /** Removed zoom controls since it crashes the app when the user clicks on 
         * back key before the controls have disappeared from the screen */
        //webview.getSettings().setSupportZoom(true); 
        //webview.getSettings().setBuiltInZoomControls(true);
        //webview.getSettings().setDisplayZoomControls(false);
        
        webview.setWebViewClient(new PCBWebViewClient());
        webview.getSettings().setGeolocationEnabled(true);
        webview.setWebChromeClient(new WebChromeClient() {  
        	public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {     
        		callback.invoke(origin, true, false);  
        	} 
        });
        
        /** Look for links that the user clicked */
        webview.setDownloadListener(new DownloadListener() {         
        	public void onDownloadStart(String url, String userAgent,                 
        			String contentDisposition, String mimetype,                 
        			long contentLength) { 
        			downloadlink = url;
        			AlertDialog.Builder alert = new AlertDialog.Builder(OpenURLActivity.this);  
					alert.setTitle("File Access"); 
					alert.setMessage("How do you want to open this link?"); 
					//final EditText input = new EditText(OpenURLActivity.this);
					//input.setText("temporary.pdf");
					//input.setSingleLine();
					//alert.setView(input); 
					alert.setPositiveButton("View", new DialogInterface.OnClickListener() { 
						public void onClick(DialogInterface dialog, int whichButton) { 
								
		        			String linkquery;
							try {
								linkquery = URLEncoder.encode(downloadlink,"utf-8");
								//type = onlyLink;
			        			webview.loadUrl("http://docs.google.com/viewer?url="+linkquery);			        			
			        			
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								Toast.makeText(OpenURLActivity.this, "Problem loading URL.", Toast.LENGTH_LONG).show();
							}
						} 
					}); 
					/**alert.setNeutralButton("Download", new DialogInterface.OnClickListener() {  
						public void onClick(DialogInterface dialog, int whichButton) {
							Editable filename = input.getText();
							DownloadFile downloadFile = new DownloadFile(OpenURLActivity.this, downloadPath); 
							downloadFile.execute(downloadlink,filename.toString()); 
						} 
					});*/
					alert.setNegativeButton("Browser", new DialogInterface.OnClickListener() {  
						public void onClick(DialogInterface dialog, int whichButton) {
							try {
								Intent browserIntent = new Intent();
								ComponentName comp = new ComponentName("com.android.browser","com.android.browser.BrowserActivity");
								browserIntent.setComponent(comp);
								browserIntent.setAction(Intent.ACTION_VIEW);
								browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
								browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								browserIntent.setData(Uri.parse(webview.getUrl()));
								startActivity(browserIntent);

							}
							catch(Exception e) {
								Toast.makeText(OpenURLActivity.this, "Problem loading URL.", Toast.LENGTH_LONG).show();
							}
						} 
					});   
					alert.show(); 
					


        	}     
        }); 
        
        /** Get URL that was passed in */
        Intent i = getIntent();
        Bundle extras = i.getExtras();

        strGSurl = extras.getString("URL");
        webview.loadUrl(strGSurl);

    }
    
    /** Clicking links in webview will start a new browser. So the following is needed. */
    private class PCBWebViewClient extends WebViewClient {    
    	@Override    
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {        
    		view.loadUrl(url); 
    		return true;    
    	}
    }
    
    /** When back physical key is pressed */
    @Override       
    public boolean onKeyDown(int keyCode, KeyEvent event) {           
    	if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {                              
    		webview.goBack();               
    		return true;           
    	}           
    	return super.onKeyDown(keyCode, event);      
    }
    
  /**  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater mi = getMenuInflater();
    	mi.inflate(R.menu.webview_menu,menu);
    	return true;
    }*/
    
    /** Save to SD card menu option is only enabled if it's a PDF file and not a webpage.
     * Layout is defined in /res/menu/webview_menu.xml */
    @Override 
    public boolean onPrepareOptionsMenu (Menu menu) { 
    	menu.clear();
    	super.onCreateOptionsMenu(menu);
    	MenuInflater mi = getMenuInflater();
    	mi.inflate(R.menu.webview_menu,menu);     
    	return true; 
    } 
    
    /** What to do when menu options are selected. */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

    	switch(item.getItemId()) {
							
    		case R.id.return_search:	OpenURLActivity.this.finish();
    									break;
    		case R.id.google_maps:		webview.loadUrl("http://maps.google.com/");
    									break;
    	
    	}

    	return true;
    }
    
    /** Following is based on discussions on Stackoverflow. */
    @Override
    protected void onPause() {
    	super.onPause();
    	webview.clearHistory();
    	webview.clearCache(false);
    	callHiddenWebViewMethod("onPause");
    	
    }
    
    @Override     
    protected void onDestroy() {         
    	super.onDestroy();                  
    	webview.destroy();
    	webview = null;
    } 
    
    /** Webview's page load operation is not really stopped when back key is pressed. 
     * If a download was initiated, it'll continue in the background.
     * Uses reflection to call hidden onPause method */
    private void callHiddenWebViewMethod(String name) {     
    	if( webview != null ) {         
    		try {             
    			Method method = WebView.class.getMethod(name);             
    			method.invoke(webview);
    			//if(D) Log.d("MyApp","Stopping webview");
    		} 
    		catch (NoSuchMethodException e) {             
    			//if(D) Log.d("MyApp", "No such method: " + name + " " +  e);         
    		} 
    		catch (IllegalAccessException e) {             
    			//if(D) Log.d("MyApp", "Illegal Access: " + name + " " +  e);         
    		} 
    		catch (InvocationTargetException e) {             
    			//if(D) Log.d("MyApp", "Invocation Target Exception: " + name + " " +  e);         
    		}     
    	} 
    } 
    
    
}







