package com.cookbook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cookbook.R;
import com.cookbook.adapter.CookbookDBAdapter;

public class SettingsActivity extends Activity {
	private CookbookDBAdapter mDbHelper;
	private static String measurementSetting = "Metric";
	
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
	
	// Getter and setter methods for measurementSetting
	private void setMeasurementSetting(String setting) { measurementSetting = setting; }
	private String getMeasurementSetting() { return measurementSetting; }
}
