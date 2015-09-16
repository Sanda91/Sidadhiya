package com.project.sidadiya;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class CategorySelect extends Activity {

	  private CheckBox cat1, cat2, cat3;
	  private Button btnDisplay;
	 
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_select);
	 
			addListenerOncat1();
			addListenerOnButton();
		
		
	  }
	  
	  
	 public void addListenerOncat1() {
		
		cat1 = (CheckBox) findViewById(R.id.cat1);
	 
		cat1.setOnClickListener(new OnClickListener() {
	 
		  @Override
		  public void onClick(View v) {
	                //is cat1 checked?
			if (((CheckBox) v).isChecked()) {
				Toast.makeText(CategorySelect.this,
			 	   "Bro, try Android :)", Toast.LENGTH_LONG).show();
			}
	 
		  }
		});
	 
	  }
	 
	  public void addListenerOnButton() {
	 
		cat1 = (CheckBox) findViewById(R.id.cat1);
		cat2 = (CheckBox) findViewById(R.id.cat2);
		cat3 = (CheckBox) findViewById(R.id.cat3);
		btnDisplay = (Button) findViewById(R.id.btnDisplay);
		
		
		final ArrayList<String> clicked=new ArrayList<String>();
	 
		btnDisplay.setOnClickListener(new OnClickListener() {
	 
	          //Run when button is clicked
		  @Override
		  public void onClick(View v) {
			if(cat1.isChecked()){
				clicked.add("Apparel");
			}
			if(cat2.isChecked()){
				clicked.add("Technology");
			}
			if(cat3.isChecked()){
				clicked.add("Food and Beverages");
			}
			
		
			Intent out=new Intent(getApplicationContext(),MainActivity.class);
			out.putExtra("CHECKED_LIST", clicked);
			startActivity(out);
			/*StringBuffer result = new StringBuffer();
			result.append("IPhone check : ").append(cat1.isChecked());
			result.append("\nAndroid check : ").append(cat2.isChecked());
			result.append("\nWindows Mobile check :").append(cat3.isChecked());
	 
			Toast.makeText(CategorySelect.this, result.toString(),
					Toast.LENGTH_LONG).show();*/
	 
		  }
		});
	 
	  }
	}
