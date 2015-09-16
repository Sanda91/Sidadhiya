package com.project.sidadiya;
 

import com.project.sidadiya.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



//import com.androidexample.gcm.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
 
public class AndroidGPSTrackingActivity extends Activity implements OnItemSelectedListener {
     
    Button btnShowLocation;
    // Google Map
    private GoogleMap googleMap;
    private double latitude;
    private double longitude;
    private EditText radius;
    private Button submit;
    
    
    public static int radiusValue=1000;
    public static Circle circle;
 
    // GPSTracker class
    GPSTracker gps;
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_location);
                   	               
        try {
            // Loading map
            initializeMap();
            // create marker
            // create class object
            gps = new GPSTracker(AndroidGPSTrackingActivity.this);

            // check if GPS enabled     
            if(gps.canGetLocation()){
                 
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                 
                // \n is for new line
                //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();    
            }else{
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }
              
            
            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Your Location");
             
            // adding marker
            googleMap.addMarker(marker);
            
            //Move Camera With Animation
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(latitude, longitude)).zoom(15).build();
            populateMap();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            
           
            submit=(Button)findViewById(R.id.radiusSubmit);
            radius=(EditText)findViewById(R.id.radiusText);
            
            submit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                	//String name = txtName.getText().toString();
                	if(radius.getText().toString().matches("")){
                		Toast.makeText(getApplicationContext(),"Please Enter a Value Before Submitting", Toast.LENGTH_SHORT).show();
                	}
                	else{
                	radiusValue=Integer.parseInt(radius.getText().toString());
                	 CircleOptions circleOptions = new CircleOptions()
         	        .center(new LatLng(latitude, longitude))
         	        .radius(radiusValue)
         	        .strokeWidth(2)
         	        .strokeColor(Color.RED)
         	        .fillColor(Color.parseColor("#50F57856"));
         	        // Supported formats are: #RRGGBB #AARRGGBB
         	        //   #AA is the alpha, or amount of transparency
                	 circle.remove();
                	 populateMap();
                	 MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Your Location");
                	 googleMap.addMarker(marker);
                     circle=googleMap.addCircle(circleOptions);
                	}
                	
                }
            });
            
            CircleOptions circleOptions = new CircleOptions()
	        .center(new LatLng(latitude, longitude))
	        .radius(radiusValue)
	        .strokeWidth(2)
	        .strokeColor(Color.RED)
	        .fillColor(Color.parseColor("#50F57856"));
	        // Supported formats are: #RRGGBB #AARRGGBB
	        //   #AA is the alpha, or amount of transparency

            circle = googleMap.addCircle(circleOptions);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
	protected void onResume() {
		super.onResume();
		initializeMap();
	}

	/**
	 * function to load map If map is not created it will create it for you
	 * */
	private void initializeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
			
			
		}
	}
	
	private void populateMap(){
		HashMap <String,ArrayList<String>>  latLang=MainActivity.locValue;
		ArrayList<String> value=new ArrayList<String>();
		double latitude=0;
		double longitude=0;
			 Iterator<?> it = latLang.entrySet().iterator();
			    while (it.hasNext()) {
			        @SuppressWarnings("rawtypes")
					Map.Entry pairs = (Map.Entry)it.next();
			        String key=pairs.getKey().toString();
			        value=latLang.get(key);
			        latitude=Double.parseDouble(value.get(0));
			        longitude=Double.parseDouble(value.get(1));
			        
			        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(key);
			        // GREEN color icon
			        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

			        // adding marker
		            googleMap.addMarker(marker);
			        it.remove(); // avoids a ConcurrentModificationException
			    }
			    
			    
		
	}
	

		 
		  // get the selected dropdown list value
	/*public double addListenerOnButton() {
		 
			spinner1 = (Spinner) findViewById(R.id.spinner1);
			String value=spinner1.getSelectedItem().toString();
			value=value.replaceAll("[^\\d.]", "");
			value=value.trim();
			
			return Double.parseDouble(value);
		  }*/
	
	
	
	/*@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// On selecting a spinner item
		String item = parent.getItemAtPosition(position).toString();
		
		// Showing selected spinner item
		Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
		item=item.replaceAll("[^\\d.]", "");
		item=item.trim();
		area=Double.parseDouble(item);
		populateMap();

	}*/

	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

     
}