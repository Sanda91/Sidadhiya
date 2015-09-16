package com.project.sidadiya;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

//import com.androidexample.gcm.R;
import com.google.android.gcm.GCMRegistrar;
import com.project.sidadiya.MainActivity;
import com.project.sidadiya.ServiceHandler;
import com.project.sidadiya.SingleContactActivity;

public class MainActivity extends ListActivity {
	
		
	 	private ProgressDialog pDialog;
	 
	    // URL to get contacts JSON
	    private static String url = "http://mudalali.sidadhiya.lk/results.json";
	 
	 // JSON Node names
	/*    private static final String TAG_ITEM = "contacts";
	    private static final String TAG_NAME = "id";
	    private static final String TAG_IMAGE = "name";
	    private static final String TAG_PRICE = "email";
	    private static final String TAG_VALID = "address";
	    private static final String TAG_DESCRIPTION = "gender";
	    private static final String TAG_CATEGORY = "phone";
	    private static final String TAG_SUB_CATEGORY = "mobile";
	    private static final String TAG_VENDOR = "home";
	    private static final String TAG_LATITUDE = "office";*/
	    
	 // JSON Node names
	    private static final String TAG_ITEM = "item";
	    static final String TAG_NAME = "name";
	    private static final String TAG_IMAGE = "image";
	    private static final String TAG_PRICE = "price";
	    private static final String TAG_VALID = "valid_till";
	    private static final String TAG_DESCRIPTION = "description";
	    private static final String TAG_CATEGORY = "category";
	    //private static final String TAG_SUB_CATEGORY = "sub_category";
	    private static final String TAG_VENDOR = "vendor";
	    private static final String TAG_LATITUDE = "latitude";
	    private static final String TAG_LONGITUDE = "longitude";
	    
	    // contacts JSONArray
	    JSONArray item = null;
	 
    // Hashmap for ListView
	    ArrayList<HashMap<String, String>> contactList;
	    
	// label to display gcm messages
	    TextView lblMessage;
	    Controller aController;
	    
	//HashSet To Store Locations
	    static ArrayList<String> latLang=new ArrayList<String>();
	    static final HashMap<String,ArrayList<String>> locValue=new HashMap<String,ArrayList<String>>();
	
	// Asyntask
	    AsyncTask<Void, Void, Void> mRegisterTask;
	
		public static String name;
		public static String email;

	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		//Get Global Controller Class object (see application tag in AndroidManifest.xml)
		aController = (Controller) getApplicationContext();
		
		
		// Check if Internet present
		/*if (!aController.isConnectingToInternet()) {
			
			// Internet Connection is not present
			aController.showAlertDialog(MainActivity.this,
					"Internet Connection Error",
					"Please connect to Internet connection", false);
			// stop executing code by return
			return;
		}*/
		
		// Getting name, email from intent
		Intent i = getIntent();
		
		name = i.getStringExtra("name");
		email = i.getStringExtra("email");		
		
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest permissions was properly set 
		GCMRegistrar.checkManifest(this);

		lblMessage = (TextView) findViewById(R.id.lblMessage);
		
		// Register custom Broadcast receiver to show messages on activity
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				Config.DISPLAY_MESSAGE_ACTION));
		
		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(this);

		// Check if regid already presents
		if (regId.equals("")) {
			
		// Register with GCM			
		GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);
		
		} else {
			
			// Device is already registered on GCM Server
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				
				// Skips registration.				
				Toast.makeText(getApplicationContext(), "Already registered with GCM Server", Toast.LENGTH_LONG).show();
			
			} else {
				
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						
						// Register on our server
						// On server creates a new user
						aController.register(context, name, email, regId);
						
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				
				// execute AsyncTask
				mRegisterTask.execute(null, null, null);
			}
		}
	
	 //Put the contacts to an ArrayList
		contactList = new ArrayList<HashMap<String, String>>();
		 
        ListView lv = getListView();
        
     // Listview on item click listener
 		lv.setOnItemClickListener(new OnItemClickListener() {

 			@Override
 			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
 				// getting values from selected ListItem
 				String name = ((TextView) view.findViewById(R.id.name))
 						.getText().toString();
 				String cost = ((TextView) view.findViewById(R.id.email))
 						.getText().toString();
 				String valid = ((TextView) view.findViewById(R.id.mobile))
 						.getText().toString();
 				

 				// Starting single contact activity
 				Intent in = new Intent(getApplicationContext(),SingleContactActivity.class);
 				in.putExtra(TAG_NAME, name);
 				in.putExtra(TAG_PRICE, cost);
 				in.putExtra(TAG_VALID, valid);
 				in.putExtra(TAG_DESCRIPTION,contactList.get(position).get(TAG_DESCRIPTION).toString());
 				in.putExtra(TAG_LATITUDE, contactList.get(position).get(TAG_LATITUDE).toString());
 				in.putExtra(TAG_LONGITUDE,contactList.get(position).get(TAG_LONGITUDE).toString());
 				in.putExtra(TAG_VENDOR, contactList.get(position).get(TAG_VENDOR).toString());
 				in.putExtra(TAG_IMAGE, contactList.get(position).get(TAG_IMAGE).toString());
 				startActivity(in);

 			}
 		});

     		// Calling async task to get json
     		new GetContacts().execute();
	}		

	
	
	
	// Create a broadcast receiver to get message and show on screen 
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);
			
			// Waking up mobile if it is sleeping
			aController.acquireWakeLock(getApplicationContext());
			
			// Display message on the screen
			//lblMessage.append(newMessage + "\n");			
			
			Toast.makeText(getApplicationContext(), "Got Message: " + newMessage, Toast.LENGTH_LONG).show();
			
			// Releasing wake lock
			aController.releaseWakeLock();
		}
	};
	
	@Override
	protected void onDestroy() {
		// Cancel AsyncTask
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			// Unregister Broadcast Receiver
			unregisterReceiver(mHandleMessageReceiver);
			
			//Clear internal resources.
			GCMRegistrar.onDestroy(this);
			
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}
	
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetContacts extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@SuppressLint("WorldWriteableFiles")
		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					
					// Getting JSON Array node
					item = jsonObj.getJSONArray(TAG_ITEM);
					
					String str = jsonObj.toString();
					@SuppressWarnings("deprecation")
					SharedPreferences sharedPref = getSharedPreferences( "appData", Context.MODE_WORLD_WRITEABLE );
					@SuppressWarnings("deprecation")
					SharedPreferences.Editor prefEditor = getSharedPreferences( "appData", Context.MODE_WORLD_WRITEABLE ).edit();
					prefEditor.putString( "json", str );
					prefEditor.commit();
					
					if(!isNetworkAvailable()){
						String strJson = sharedPref.getString("json", str);
						if(strJson != null) 
							try{
							JSONObject jsonData = new JSONObject(strJson);
							item=jsonData.getJSONArray(TAG_ITEM);
							
							for (int i = 0; i < item.length(); i++) {
								JSONObject c = item.getJSONObject(i);
								
								String name = c.getString(TAG_NAME);
								String image = c.getString(TAG_IMAGE);
								String price = c.getString(TAG_PRICE);
								String valid = c.getString(TAG_VALID);
								String description = c.getString(TAG_DESCRIPTION);

								// Phone node is JSON Object
								String category = c.getString(TAG_CATEGORY);
								//String sub_category = c.getString(TAG_SUB_CATEGORY);
								String vendor = c.getString(TAG_VENDOR);
								String latitude = c.getString(TAG_LATITUDE);
								String longitude = c.getString(TAG_LONGITUDE);

								// tmp hashmap for single contact
								HashMap<String, String> contact = new HashMap<String, String>();

								// adding each child node to HashMap key => value
								contact.put(TAG_NAME, name);
								contact.put(TAG_IMAGE, "http://mudalali.sidadhiya.lk/ad_images/"+image);
								contact.put(TAG_PRICE,"Price: Rs."+ price);
								contact.put(TAG_CATEGORY,"Category: "+category);
								contact.put(TAG_VALID,"Valid Till: "+valid);
								contact.put(TAG_DESCRIPTION,"Description: "+ description);
								contact.put(TAG_LATITUDE,latitude);
								contact.put(TAG_LONGITUDE, longitude);
								contact.put(TAG_VENDOR, vendor);
								
								// adding contact to contact list
								contactList.add(contact);
							}
							
							}
						catch(JSONException e) {
							e.printStackTrace();
						}
					}
					
					else
					if(isNetworkAvailable()){
					// looping through All Contacts
					for (int i = 0; i < item.length(); i++) {
						JSONObject c = item.getJSONObject(i);
						
						//Intent in = getIntent();
						
						//ArrayList<String> categories=new ArrayList<String>();
						//categories=in.getStringArrayListExtra("CHECKED_LIST");
						//for(int j=0;j<categories.size();j++){
							
						//if(categories.get(i)==c.getString(TAG_CATEGORY)){
						String name = c.getString(TAG_NAME);
						String image = c.getString(TAG_IMAGE);
						String price = c.getString(TAG_PRICE);
						String valid = c.getString(TAG_VALID);
						String description = c.getString(TAG_DESCRIPTION);

						// Phone node is JSON Object
						String category = c.getString(TAG_CATEGORY);
						//String sub_category = c.getString(TAG_SUB_CATEGORY);
						String vendor = c.getString(TAG_VENDOR);
						String latitude = c.getString(TAG_LATITUDE);
						String longitude = c.getString(TAG_LONGITUDE);

						// tmp hashmap for single contact
						HashMap<String, String> contact = new HashMap<String, String>();
						
						latLang.add(latitude);
						latLang.add(longitude);
						locValue.put(vendor, latLang);

						// adding each child node to HashMap key => value
						contact.put(TAG_NAME, name);
						contact.put(TAG_IMAGE, "http://mudalali.sidadhiya.lk/ad_images/"+image);
						contact.put(TAG_PRICE,"Price: Rs."+ price);
						contact.put(TAG_CATEGORY,"Category:"+category);
						contact.put(TAG_VALID,"Valid Till:"+valid);
						contact.put(TAG_DESCRIPTION,"Description:"+ description);
						contact.put(TAG_LATITUDE,latitude);
						contact.put(TAG_LONGITUDE, longitude);
						contact.put(TAG_VENDOR,"By:"+ vendor);
						
						// adding contact to contact list
						contactList.add(contact);
						latLang=new ArrayList<String>();
						 }
						//}
					// }	
					} 
					
				}
					catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}
			
		

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
	        
			if (pDialog.isShowing())
				pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
		
			ListAdapter adapter = new SimpleAdapter(
					MainActivity.this, contactList,
					R.layout.list_item, new String[] {TAG_NAME, TAG_PRICE,
							TAG_VALID}, new int[] {R.id.name,
							R.id.email, R.id.mobile});

			setListAdapter(adapter);
		}
		
		private boolean isNetworkAvailable() {
		    ConnectivityManager connectivityManager 
		          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
		}
	
		

	}
	
	
	
	
	

}
