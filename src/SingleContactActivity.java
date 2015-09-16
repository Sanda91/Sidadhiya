package com.project.sidadiya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.androidexample.gcm.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SingleContactActivity  extends Activity {
	
	private static final String TAG_NAME = "name";
	private static final String TAG_IMAGE= "image";
    private static final String TAG_PRICE = "price";
    private static final String TAG_VALID = "valid_till";
    private static final String TAG_DESCRIPTION="description";
    private static final String TAG_LATITUDE="latitude";
    private static final String TAG_LONGITUDE="longitude";
    private static final String TAG_VENDOR="vendor";
	
	 // Google Map
    private GoogleMap googleMap;
    private double lat;
    private double longi;
 
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_contact);
   
        
        // getting intent data
        Intent in = getIntent();
        
        // Get JSON values from previous intent
       
        String name = in.getStringExtra(TAG_NAME);
        String image=in.getStringExtra(TAG_IMAGE);
        String valid = in.getStringExtra(TAG_VALID);
        String price = in.getStringExtra(TAG_PRICE);
        String description=in.getStringExtra(TAG_DESCRIPTION);
        String latitude=in.getStringExtra(TAG_LATITUDE);
        String longitude=in.getStringExtra(TAG_LONGITUDE);
        String vendor=in.getStringExtra(TAG_VENDOR);
        
        
        int loader = R.drawable.logo;
        
        // Imageview to show
        ImageView deal_image = (ImageView) findViewById(R.id.deal_image);
         
        // Image url
        String image_url = image;
         
        // ImageLoader class instance
        ImageLoader imgLoader = new ImageLoader(getApplicationContext());
        
        imgLoader.DisplayImage(image_url, loader, deal_image);
        
        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.name_label);
        TextView lblValid = (TextView) findViewById(R.id.email_label);
        TextView lblPrice = (TextView) findViewById(R.id.mobile_label);
        TextView lblDesc=(TextView) findViewById(R.id.address_label);
        
        lblName.setText(name);
        lblValid.setText(valid);
        lblPrice.setText(price);
        lblDesc.setText(description);
        
        try {
            // Loading map
            initializeMap();
            // create marker
            // create class object
            lat=Double.parseDouble(latitude);
            longi=Double.parseDouble(longitude);
            		
            MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, longi)).title(vendor);
             
            // adding marker
            googleMap.addMarker(marker);
            
            //Move Camera With Animation
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(lat, longi)).zoom(12).build();
     
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

 
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
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.shop_location)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
		
	}	
	

}
