package com.cookbook.activity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItem;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.cookbook.*;
import com.cookbook.Recipe;
import com.cookbook.RecipeList;
import com.cookbook.Utility;
import com.cookbook.readFile;
import com.cookbook.adapter.CookbookDBAdapter;
import com.cookbook.facebook.BaseDialogListener;
import com.cookbook.facebook.UpdateStatusResultDialog;
import com.facebook.android.FacebookError;

public class ViewRecipeActivity extends FragmentActivity
{
	private CookbookDBAdapter mDBHelper;

	private Cursor recipe;
	private Cursor ingredients;

	private long recipeID = -1;
	private boolean online;
	private String recipeName;
	private String method;
	private String mealType;
	private String duration;
	private long durationLong;
	private long durationHours;
	private long durationMinutes;
	private String timeOfYear;
	private String region;
	private float ratingVal;
	private String serves;
	private String friendID;
	
	private boolean success = true;

	Resources myResources;
	readFile rd;
	/** List of Bookmarks */
	protected RecipeList bookmarks = new RecipeList();

	public static final String HACK_ICON_URL = "http://www.facebookmobileweb.com/hackbook/img/facebook_icon_large.png";

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);	
        setContentView(R.layout.view_recipe);
        setUpTabs();

        mDBHelper = new CookbookDBAdapter(this);
        mDBHelper.open();

        try
        {
        	Bundle b = getIntent().getExtras();
        	recipeID = b.getLong("recipeID");
        	online   = b.getBoolean("online");
            if (online) {
            	recipeName = b.getString("recipeName");
            	method     = b.getString("method");
            	mealType   = b.getString("mealType");
            	duration   = b.getString("duration");
            }
            else recipe = mDBHelper.fetchRecipe(recipeID);
        }
        catch (RuntimeException e)
        {
        	Toast.makeText(getApplicationContext(), "Error getting RecipeID", Toast.LENGTH_SHORT).show();
        	success = false;
        }

        myResources = getResources();
        /*
         * Read the bookmarks list.
         */
        FileInputStream fos;
		//
        
        if (!online)
        {
        	try
        	{
        		fos = openFileInput("bookmarks");
        		rd = new readFile();
        		bookmarks.fetchFromIDs(rd.readIDs(fos), mDBHelper);
        		try
        		{
        			fos.close();
        		}
        		catch (IOException e)
        		{
        			Toast.makeText(getApplicationContext(), "Bookmarks error 1", Toast.LENGTH_SHORT).show();
        		}
        	}
        	catch (FileNotFoundException e)
        	{
        		Toast.makeText(getApplicationContext(), "Bookmarks error 2", Toast.LENGTH_SHORT).show();
        		e.printStackTrace();
        	}
        }

        Button shareFB = (Button) findViewById(R.id.ShareToFacebook);
        Button shareTwitter = (Button) findViewById(R.id.ShareToTwitter);
        CheckBox bookmark = (CheckBox) findViewById(R.id.bookmark);
        RatingBar rating = (RatingBar) findViewById(R.id.rtbProductRating);
        Button editButton = (Button) findViewById(R.id.editButton);
        Button deleteButton = (Button) findViewById(R.id.deleteButton);

        if (online) {
    		int i;
    		// Get online ingredients
    	}
        else {
        	if (recipe.getCount() == 0)
            {
            	success = false;
            	Toast.makeText(getApplicationContext(), "No recipe found", Toast.LENGTH_SHORT).show();
            	setLabels("No recipe found", "No method", "None", "0", "None", "None", "None");
            }
            else
            {
            	ingredients = mDBHelper.fetchRecipeIngredient(recipeID);
            	mDBHelper.close();
            	recipeName = recipe.getString(1);
                method = recipe.getString(2);
                mealType = recipe.getString(3);
                duration = recipe.getString(4);

                durationLong = Long.parseLong(duration);
                durationHours = durationLong / 60;
                durationMinutes = durationLong % 60;

                if (durationHours == 0)
                    duration = durationMinutes + " minutes";
                 else
                    duration = durationHours + " hours, " + durationMinutes + " minutes";

                timeOfYear = recipe.getString(5);
                region = recipe.getString(6);
                String rt = recipe.getString(7);
                serves = recipe.getString(8);
                recipe.close();
            
                if (rt != null)
                {
                	ratingVal = Float.valueOf((rt)).floatValue();
                	rating.setRating(ratingVal);
                }
                
                setLabels(recipeName, method, mealType, duration, timeOfYear, region, serves);
            }
        }

        if (success) 
        {
        	if (!online) {
        		setIngredients(ingredients);
            	ingredients.close();
        	}
        	setMethod(method);
        }
        else
        {
        	ImageView mainImage = (ImageView)this.findViewById(R.id.imageView1);
        	mainImage.setImageResource(R.drawable.error);
        }

        if (bookmarks.contains(recipeID))
        {
        	bookmark.setChecked(true);
        }

        /**
         * Facebook Share Button
         */
        shareFB.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
            	//Post on current user's wall
            	Bundle params = new Bundle();
            	params.putString("name", recipeName);
            	//params.putString("caption", mealType);
            	params.putString("description", mealType);
            	params.putString("picture", HACK_ICON_URL);
                Utility.mFacebook.dialog(v.getContext(), "feed", params, new PostDialogListener());
            }
        });
        
        shareTwitter.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
            	Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            	sharingIntent.setType("text/plain");
            	sharingIntent.setPackage("com.twitter.android");
            	sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I'm cooking " + recipeName + " with #Cookbook Gamma for #Android");
            	sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
            	startActivity(Intent.createChooser(sharingIntent, "Shared with"));
            }
        });
        
        editButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
            	Intent i = new Intent(getBaseContext(), EditRecipeActivity.class);
            	i.putExtra("currentRecipeID", recipeID);
            	startActivity(i);
            }
        });
        
        deleteButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
            	mDBHelper.open();
            	mDBHelper.deleteRecipe(recipeID);
            	mDBHelper.deleteRecipeIngedients(recipeID);
            	mDBHelper.close();
            	Toast.makeText(getApplicationContext(), "Recipe deleted", Toast.LENGTH_SHORT).show();
            	finish();
            }
        });
        
        /** 
         * BOOKMARK BUTTON 
         * */       
        bookmark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{

				/**
				 * if isChecked
				 * 		add it to the bookmark list and save the list.
				 * else
				 * 		remove from the list(loaded from file) and save it
				 */

				if (success)
				{
					if (isChecked)
					{
						bookmarks.addRecipe(new Recipe(recipeName, "", method, recipeID, mealType, 0, timeOfYear, region, 0f));
					}
					else
					{
						bookmarks.removeRecipe(recipeName);
					}
					FileOutputStream ros;
					try
					{
						ros = openFileOutput("bookmarks", Context.MODE_PRIVATE);
						rd.writeIDs(bookmarks,ros);
					}
					catch (FileNotFoundException e)
					{
						e.printStackTrace();
					}
				}
			}
        });
        
        /**
         * RATING BUTTON
         */
        
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
        	
        	public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
        	{
        		mDBHelper.open();
        		mDBHelper.updateRecipe(recipeID, rating);
        		mDBHelper.close();
        	}
        });
    }

    protected void onResume()
    {
    	if (!online) mDBHelper.open();
    	super.onResume();
    };

    protected void onPause()
    {
    	if (!online) {
    		ingredients.close();
        	recipe.close();
        	mDBHelper.close();
    	}
    	super.onPause();
    };

    protected void onStop()
    {
    	if (!online) {
    		ingredients.close();
        	recipe.close();
        	mDBHelper.close();
    	}
    	super.onStop();
    };

    protected void onDestroy()
    {
    	if (!online) {
    		ingredients.close();
        	recipe.close();
        	mDBHelper.close();
    	}
    	super.onDestroy();
    };
    
    // Display a list of ingredients and quantities from a cursor
    private void setIngredients(Cursor ingredients)
    {
    	int count = ingredients.getCount();

    	LayoutParams myTableRowParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    	LayoutParams myTextViewParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

    	TableLayout table = (TableLayout)findViewById(R.id.tableLayout3);
    	table.setStretchAllColumns(true);
    	
        for (int i = 0; i < count; i++)
        {
           TableRow table_row = new TableRow(this);
           TextView ingredientName = new TextView(this);
           TextView quantity = new TextView(this);
           
           int amount = Integer.parseInt(ingredients.getString(3));
           String unitString = ingredients.getString(4);
           
           table_row.setLayoutParams(myTableRowParams);

           ingredientName.setText(ingredients.getString(2));
           quantity.setText(amount + " " + displayUnits(unitString, amount));
           
           ingredientName.setLayoutParams(myTextViewParams);
           quantity.setLayoutParams(myTextViewParams);
           
           ingredientName.setGravity(Gravity.LEFT);
           quantity.setGravity(Gravity.RIGHT);
           
           table_row.addView(ingredientName);
           table_row.addView(quantity);

           table.addView(table_row);

           if (i != count - 1)
        	   ingredients.moveToNext();
        }
    }
    
    // Convert a unit from the database unit to a nice format for display
    private String displayUnits(String databaseFormat, int quantity)
    {
    	System.out.println("databaseFormat: " + databaseFormat);
    	System.out.println("quantity: " + quantity);
    	if (databaseFormat.equals("Gram(s) (g)"))
    			return "g";

    	if (databaseFormat.equals("Kilogram(s) (kg)"))
    			return "kg";

    	if (databaseFormat.equals("Ounce(s) (oz)"))
    			return "oz";

    	if (databaseFormat.equals("Pound(s) (lb)"))
    			return "lb";

    	if (databaseFormat.equals("Millilitre(s) (ml)"))
    			return "ml";

    	if (databaseFormat.equals("Litre(s) (l)"))
    			return "l";
    	
    	if (databaseFormat.equals("Unit(s) (clove/lemon etc..)"))
    		return "";

    	if (databaseFormat.equals("Gallon(s)"))
    	{
    		if (quantity == 1)
    			return "gallon";
    		else
    			return "gallons";
    	}

    	if (databaseFormat.equals("Teaspoon(s)"))
    	{
    		if (quantity == 1)
    			return "teaspoon";
    		else
    			return "teaspoons";
    	}
    	if (databaseFormat.equals("Tablespoon(s)"))
    	{
    		if (quantity == 1)
    			return "tablespoon";
    		else
    			return "tablespoons";
    	}
    	if (databaseFormat.equals("Cup(s)"))
    	{
    		if (quantity == 1)
    			return "cup";
    		else
    			return "cups";
    	}
    	if (databaseFormat.equals("Pinch"))
    	{
    		if (quantity == 1)
    			return "pinch";
    		else
    			return "pinches";
    	}
    	
    	return "Unrecognised unit";
    }
    
    // Separate the steps of a method and display them
    private void setMethod(String method)
    {
    	TableLayout methodTable = (TableLayout)findViewById(R.id.methodTable);
    	
    	LayoutParams myTextViewParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1f);
    	LayoutParams myTableRowParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    	
    	String[] steps = method.split("\\$");
    	int numberOfSteps = steps.length;

    	for (int i = 0; i < numberOfSteps; i++)
    	{
    		TableRow table_row = new TableRow(this);
    		table_row.setLayoutParams(myTableRowParams);

    		TextView step = new TextView(this);
    		step.setLayoutParams(myTextViewParams);
    		step.setText(i + 1 + ". " + steps[i]);
    		step.setTextSize(18);
    		step.setGravity(Gravity.CENTER);
    		step.setPadding(0, 0, 0, 25);
    		table_row.addView(step);
    		methodTable.addView(table_row);
    	}
    }

    // Initialise tab host and give tabs labels
	private void setUpTabs()
    {
        TabHost th = (TabHost)findViewById(R.id.tabHost);
        th.setup();

        TabSpec ts = th.newTabSpec("tag1");
        ts.setContent(R.id.tab1Summary);
        ts.setIndicator("Summary");
        th.addTab(ts);

        ts = th.newTabSpec("tag2");
        ts.setContent(R.id.tab2Ingredients);
        ts.setIndicator("Ingredients");
        th.addTab(ts);
        
        ts = th.newTabSpec("tag3");
        ts.setContent(R.id.tab3Method);
        ts.setIndicator("Method");
        th.addTab(ts);
    }
    
    // Print recipe fields to TextViews
    private void setLabels(String recipeName, String method, String mealType, String duration2, String timeOfYear, String region, String serves)
    {
        TextView txtRecipeName = (TextView)this.findViewById(R.id.viewRecipeNameTab1);
        txtRecipeName.setText(recipeName);
        
        TextView txtRecipeName2 = (TextView)this.findViewById(R.id.viewRecipeNameTab2);
        txtRecipeName2.setText(recipeName);
        
        TextView txtRecipeName3 = (TextView)this.findViewById(R.id.viewRecipeNameTab3);
        txtRecipeName3.setText(recipeName);

        TextView txtMealType = (TextView)this.findViewById(R.id.viewMealType);
        txtMealType.setText(mealType);
        
        TextView txtDuration = (TextView)this.findViewById(R.id.viewDuration);
        txtDuration.setText(duration2);

        TextView txtTimeOfYear = (TextView)this.findViewById(R.id.viewTimeOfYear);
        if (timeOfYear.length() == 0)
        {
        	txtTimeOfYear.setVisibility(View.GONE);
        	TextView txtTimeOfYearLabel = (TextView)this.findViewById(R.id.timeOfYear);
        	txtTimeOfYearLabel.setVisibility(View.GONE);
        }
        else
        	txtTimeOfYear.setText(timeOfYear);
        
        TextView txtRegion = (TextView)this.findViewById(R.id.viewRegion);
        if (region.length() == 0)
        {
        	txtRegion.setVisibility(View.GONE);
        	TextView txtRegionLabel = (TextView)this.findViewById(R.id.region);
        	txtRegionLabel.setVisibility(View.GONE);
        }
        else
        	txtRegion.setText(region);
        
//        TextView txtServes = (TextView)this.findViewById(R.id.viewServes);
//        txtServes.setText(serves);
    }
    
    public class PostDialogListener extends BaseDialogListener
    {
        public void onComplete(Bundle values)
        {
            final String postId = values.getString("post_id");
            if (postId != null)
            {
                new UpdateStatusResultDialog(ViewRecipeActivity.this, "Update Status executed", values).show();
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), "No wall post made", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        public void onFacebookError(FacebookError error)
        {
            Toast.makeText(getApplicationContext(), "Facebook Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }

        public void onCancel()
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Update status cancelled", Toast.LENGTH_SHORT);
            toast.show();
        }
    }   
    
	//means when you click the logo it goes home
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    switch (item.getItemId())
	    {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, CookbookActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}