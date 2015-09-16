package com.project.sidadiya;

import java.util.Calendar;

//import com.androidexample.gcm.R;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
 
@SuppressWarnings("deprecation")
public class TabListView extends TabActivity {
    // TabSpec Names
  
	public static String name;
	public static String email;

     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_view);
        
        setupAlarm(1800);
        
        Intent i = getIntent();
		
		name = i.getStringExtra("name");
		email = i.getStringExtra("email");	
         
        TabHost tabHost = getTabHost();
         
        //Deals Tab
        TabSpec dealsList = tabHost.newTabSpec("Deals");
        dealsList.setIndicator("Deals List");
        // Tab Icon
        Intent inboxIntent = new Intent(this, MainActivity.class);
        inboxIntent.putExtra("name", name);
        inboxIntent.putExtra("email", email);
        // Tab Content
        dealsList.setContent(inboxIntent);
         
        //Maps Tab
        TabSpec dealsAround = tabHost.newTabSpec("Location");
        dealsAround.setIndicator("Deals Around");
        Intent outboxIntent = new Intent(this, AndroidGPSTrackingActivity.class);
        dealsAround.setContent(outboxIntent);
        
        //Deals Tab
        TabSpec categorySelect = tabHost.newTabSpec("Categories");
        categorySelect.setIndicator("Select Category");
        Intent catOutIntent =new Intent(this,CategorySelect.class);
        categorySelect.setContent(catOutIntent);
        
      
         
        // Adding all TabSpec to TabHost
        tabHost.addTab(dealsList); // Adding Deals List Tab
        tabHost.addTab(dealsAround); // Adding Deals Around Tab
        tabHost.addTab(categorySelect);//Adding Category Select Tab

    }
    
    private void setupAlarm(int seconds) {
		  AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		  Intent intent = new Intent(getBaseContext(), OnAlarmReceive.class);
		  PendingIntent pendingIntent = PendingIntent.getBroadcast(
		     TabListView.this, 0, intent,
		     PendingIntent.FLAG_UPDATE_CURRENT);
		 
		 // Log.d(Globals.TAG, "Setup the alarm");
		 
		  // Getting current time and add the seconds in it
		  Calendar cal = Calendar.getInstance();
		  cal.add(Calendar.SECOND, seconds);
		 
		  alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
		 
		  // Finish the currently running activity
		  //TabListView.this.finish();
		}
}