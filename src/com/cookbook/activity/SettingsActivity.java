package com.cookbook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cookbook.R;
import com.cookbook.adapter.CookbookDBAdapter;

public class SettingsActivity extends FragmentActivity {
	private CookbookDBAdapter mDbHelper;
	private static String measurementSetting = "Metric";
	private static String sortby = "Alphabetically";
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        mDbHelper = new CookbookDBAdapter(this);
        Spinner spinner = (Spinner) findViewById(R.id.measurementSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.measurementsArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new measurementsSelectedListener());
        
        Spinner sorter = (Spinner) findViewById(R.id.sortSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this, R.array.sort_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sorter.setAdapter(adapter2);
        sorter.setOnItemSelectedListener(new sortSelectedListener());
        
	}
	
	/*protected void onStart() {};
    
    protected void onRestart() {};

    protected void onResume() {
    	mDbHelper.open();
    };

    protected void onPause() {
    	mDbHelper.close();
    };

    protected void onStop() {};

    protected void onDestroy() {};*/
	
	public class measurementsSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    	setMeasurementSetting(parent.getItemAtPosition(pos).toString());
	    }

	    public void onNothingSelected(AdapterView parent) {}
	}

	public class sortSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    	setSortSetting(parent.getItemAtPosition(pos).toString());
	    }

	    public void onNothingSelected(AdapterView parent) {}
	}
	
	// Getter and setter methods for measurementSetting
	private void setMeasurementSetting(String setting) { measurementSetting = setting; }
	private String getMeasurementSetting() { return measurementSetting; }
	
	// Getter and setter methods for measurementSetting
	private void setSortSetting(String setting) { 
		sortby = setting;
		if(sortby.equals("Alphabetically")) setResult(0);
		else if(sortby.equals("By Category")) setResult(1);
		else if(sortby.equals("By Cooking Duration")) setResult(2);
		else if(sortby.equals("By Occasion")) setResult(3);
		else if(sortby.equals("By Region")) setResult(4);
		else if(sortby.equals("By Rating")) setResult(5);
		else setResult(6);
	}
	private String getSortSetting() { return sortby; }
	
    //Important method for action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    
    //Important method for action bar, item selected listenener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	case R.id.search:
            // app icon in action bar clicked; go home
            Intent intent1 = new Intent(this, SearchNameActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);
            return true;        
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent2 = new Intent(this, CookbookActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
