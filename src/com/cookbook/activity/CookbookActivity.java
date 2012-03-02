package com.cookbook.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cookbook.*;
import com.cookbook.Utility;
import com.cookbook.adapter.CookbookDBAdapter;
import com.cookbook.adapter.MainImageAdapter;
import com.cookbook.facebook.BaseRequestListener;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionEvents;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;
import com.facebook.android.SessionStore;

public class CookbookActivity extends FragmentActivity {
	/** Called when the activity is first created. */
	//private Facebook mFacebook;
    public static final String APP_ID = "263632013686454";
    private Handler mHandler;
    ProgressDialog dialog;
    private TextView mText;
    private ImageView mUserPic;
	public CookbookDBAdapter mDbHelper;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // Create the Facebook Object using the app id.
        Utility.mFacebook = new Facebook(APP_ID);
        // Instantiate the asynrunner object for asynchronous api calls.
        Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);

        // Restore session if one exists
        SessionStore.restore(Utility.mFacebook, this);
        SessionEvents.addAuthListener(new FbAPIsAuthListener());
        SessionEvents.addLogoutListener(new FbAPIsLogoutListener());  
        
        if (!Utility.mFacebook.isSessionValid()) {
        	Utility.mFacebook.authorize(this, new DialogListener() {
                public void onComplete(Bundle values) {}

                public void onFacebookError(FacebookError error) {}
                public void onError(DialogError e) {}
                public void onCancel() {}
            });
        }
        
        GridView gridview = (GridView) findViewById(R.id.main_gridview);
        gridview.setAdapter(new MainImageAdapter(this));
        
        /*mDbHelper = new CookbookDBAdapter(this);
        mDbHelper.open();
        
        
        mDbHelper.createRecipe("Hamburger","Grill burger until juices run clea$Place burger in bun and add sauces to your own taste.", "Main Course", 15, "", "American", 0);
        mDbHelper.createRecipe("Pasta And Tuna","Bring a pan of water to the boil.$Place pasta in pan and boil until soft.$Use a collander to remove exces water from the pan and serve in a bowl with tunned tuna.", "Lunch", 15, "", "", 0);
        mDbHelper.createRecipe("Fish And Chips","Fry in the oil", "Main Course", 25, "", "England", 0);
        mDbHelper.createRecipe("Hash Browns","Bake in the oven for specified time", "Snack", 15, "", "", 0);
        mDbHelper.createRecipe("Beef Bourguignon","Seal the beef in a pan.$Add the red wine and chopped vegetables.$Boil together on a low heat for an hour and a half", "Main Course", 105, "Winter", "",0);
        mDbHelper.createRecipe("Noodles With Vegetables","Boil until soft.$Add vegetables like carrots or spring onions to taste.", "Starter", 20, "Winter", "China", 0);
        mDbHelper.createRecipe("Sushi","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus venenatis lectus sem, nec iaculis ante. Donec ac magna massa. In sollicitudin congue velit ut gravida. Phasellus lacus sem, euismod blandit mollis et, suscipit ac enim. Phasellus eu quam ut lacus viverra mollis. ", "Starter", 50, "", "Japan",0);
        mDbHelper.createRecipe("Kebab","Lorem ipsum dolor sit amet, consectetur adipiscing elit.$Phasellus venenatis lectus sem, nec iaculis ante.", "Main Course", 40, "", "Turkey",0);
        mDbHelper.createRecipe("Apple Pie","Lorem ipsum dolor sit amet, consectetur adipiscing elit.$Phasellus venenatis lectus sem, nec iaculis ante.", "Dessert", 80, "", "",0);
        mDbHelper.createRecipe("Meatballs","Lorem ipsum dolor sit amet, consectetur adipiscing elit.$Phasellus venenatis lectus sem, nec iaculis ante.", "Lunch", 30, "Winter", "Usa",0);
        mDbHelper.createRecipe("Chocolate Cake","Lorem ipsum dolor sit amet, consectetur adipiscing elit.$Phasellus venenatis lectus sem, nec iaculis ante.", "Dessert", 45, "", "World",0);
        mDbHelper.createRecipe("Soup","Lorem ipsum dolor sit amet, consectetur adipiscing elit.$Phasellus venenatis lectus sem, nec iaculis ante.", "Snack", 15, "Winter", "",0);
        mDbHelper.createRecipe("Veggie Soup","Lorem ipsum dolor sit amet, consectetur adipiscing elit.$Phasellus venenatis lectus sem, nec iaculis ante.", "Snack", 15, "", "",0);
        mDbHelper.createRecipe("Roast Beef","Lorem ipsum dolor sit amet, consectetur adipiscing elit.$Phasellus venenatis lectus sem, nec iaculis ante.", "Main Course", 120, "", "England",0);
        mDbHelper.createRecipe("Tuna Mayonnaise Sandwich","Lorem ipsum dolor sit amet, consectetur adipiscing elit.$Phasellus venenatis lectus sem, nec iaculis ante.", "Snack", 5, "", "England",0);
        mDbHelper.createRecipe("Gingerbread","Lorem ipsum dolor sit amet, consectetur adipiscing elit.$Phasellus venenatis lectus sem, nec iaculis ante.", "Dessert", 70, "", "",0);
        
        mDbHelper.createRecipeIngredient(1,"Burger", 50, "Gram(s) (g)");
        mDbHelper.createRecipeIngredient(1,"Burger Bun", 1, "Unit(s) (clove/lemon etc..)");
        mDbHelper.createRecipeIngredient(2,"Pasta", 300, "Gram(s) (g)");
        mDbHelper.createRecipeIngredient(2,"Tuna", 100, "Gram(s) (g)");
        mDbHelper.createRecipeIngredient(2,"Salt", 1, "Pinch");
        mDbHelper.createRecipeIngredient(3,"Fish", 200, "Gram(s) (g)");
        mDbHelper.createRecipeIngredient(3,"Chips", 100, "Gram(s) (g)");
        mDbHelper.createRecipeIngredient(4,"Hash Browns", 100, "Gram(s) (g)");
        mDbHelper.createRecipeIngredient(5,"Beef", 300, "Gram(s) (g)");
        mDbHelper.createRecipeIngredient(5,"Red Wine", 100, "Millilitre(s) (ml)");
        mDbHelper.createRecipeIngredient(5,"Onion", 3, "Unit(s) (clove/lemon etc..)");
        mDbHelper.createRecipeIngredient(6,"Noodles", 300, "Gram(s) (g)");
        mDbHelper.createRecipeIngredient(6, "Mixed Vegetables", 100, "Gram(s) (g)");
        mDbHelper.createRecipeIngredient(7,"Seaweed", 50, "Gram(s) (g)");
        mDbHelper.createRecipeIngredient(7,"Rice", 300, "Gram(s) (g)");
        mDbHelper.createRecipeIngredient(7,"Cucumber", 2, "Unit(s) (clove/lemon etc..)");
        mDbHelper.createRecipeIngredient(8,"Kebab Meat", 300, "Gram(s) (g)");
        mDbHelper.createRecipeIngredient(8,"Pita Bread", 1, "Unit(s) (clove/lemon etc..)");
        mDbHelper.createRecipeIngredient(9,"Apples", 8, "Unit(s) (clove/lemon etc..)");
        mDbHelper.createRecipeIngredient(9,"Puff Pastry", 300, "Gram(s) (g)");
        mDbHelper.createRecipeIngredient(10,"Meatballs", 300, "Gram(s) (g)");
        mDbHelper.createRecipeIngredient(11,"Flour", 300, "Gram(s) (g)");
        mDbHelper.createRecipeIngredient(11,"Cocoa Powder", 30, "Gram(s) (g)");
        mDbHelper.createRecipeIngredient(11,"Eggs", 3, "Unit(s) (clove/lemon etc..)");
        mDbHelper.createRecipeIngredient(12,"Water", 500, "Millilitre(s) (ml)");
        mDbHelper.createRecipeIngredient(12,"Beef Stock", 30, "Gram(s) (g)");
        mDbHelper.createRecipeIngredient(13,"Water", 500, "Millilitre(s) (ml)");
        mDbHelper.createRecipeIngredient(13,"Onions", 2, "Unit(s) (clove/lemon etc..)");
        mDbHelper.createRecipeIngredient(14,"Beef Joint", 1, "Kilogram(s) (kg)");
        mDbHelper.createRecipeIngredient(14,"Potatoes", 300, "Gram(s) (g)");
        mDbHelper.createRecipeIngredient(15,"Tuna", 100, "Gram(s) (g)");
        mDbHelper.createRecipeIngredient(15,"Mayonnaise", 3, "Tablespoon(s)");
        mDbHelper.createRecipeIngredient(15,"Bread", 2, "Unit(s) (clove/lemon etc..)");
        mDbHelper.createRecipeIngredient(16,"Ginger", 3, "Teaspoon(s)");
        mDbHelper.createRecipeIngredient(16,"Flour", 200, "Gram(s) (g)");
        
        mDbHelper.close();*/
        
        /*
         * setOnItemClickListener
         */
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	Intent intent;
            	switch (position) {
	            	case 0:
	            		intent = new Intent(v.getContext(), MyRecipesActivity.class);
	                	startActivityForResult(intent, 0);
	                	break;
	            	case 1:
	            		String query = "SELECT name, current_location, uid, pic_square FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1=me()) order by name";
	            		Bundle params = new Bundle();
                        params.putString("method", "fql.query");
                        params.putString("query", query);
	            		Utility.mAsyncRunner.request(null, params, new FriendsRequestListener());
	                	break;
	            	case 2:
	            		intent = new Intent(v.getContext(), DiscoverActivity.class);
	                	startActivityForResult(intent, 1);
	                	break;
	            	case 3:
	            		intent = new Intent(v.getContext(), SettingsActivity.class);
	                	startActivityForResult(intent, 3);
	                	break;
	            	case 4:
	            		intent = new Intent(v.getContext(), SearchActivity.class);
	                	startActivityForResult(intent, 4);
	                	break;
	            	default:
	            		break;
            	}
            }
        });
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Utility.mFacebook.authorizeCallback(requestCode, resultCode, data);
    }

    /*
     * Request user name, and picture to show on the main screen.
     */
    public void requestUserData() {
        Bundle params = new Bundle();
        params.putString("fields", "name, picture");
        Utility.mAsyncRunner.request("me", params, new UserRequestListener());
    }

    /*
     * Callback for fetching current user's name, picture, uid.
     */
    public class UserRequestListener extends BaseRequestListener {
        public void onComplete(final String response, final Object state) {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(response);

                final String picURL = jsonObject.getString("picture");
                final String name = jsonObject.getString("name");
                Utility.userUID = jsonObject.getString("id");

                mHandler.post(new Runnable() {
                    public void run() {
                        mText.setText(name);
                        mUserPic.setImageBitmap(Utility.getBitmap(picURL));
                    }
                });

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
    
    /*
     * Callback after friends are fetched via me/friends or fql query.
     */
    public class FriendsRequestListener extends BaseRequestListener {
        public void onComplete(final String response, final Object state) {
            //dialog.dismiss();
            Intent myIntent = new Intent(getApplicationContext(), FriendsList.class);
            myIntent.putExtra("API_RESPONSE", response);
            myIntent.putExtra("METHOD", "fql");
            startActivity(myIntent);
        }

        public void onFacebookError(FacebookError error) {
            //dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Facebook Error: " + error.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
    
    /*
     * The Callback for notifying the application when authorization succeeds or
     * fails.
     */

    public class FbAPIsAuthListener implements AuthListener {
        public void onAuthSucceed() {
            requestUserData();
        }

        public void onAuthFail(String error) {
            mText.setText("Login Failed: " + error);
        }
    }
    
    /*
     * The Callback for notifying the application when log out starts and
     * finishes.
     */
    public class FbAPIsLogoutListener implements LogoutListener {
        public void onLogoutBegin() {
            mText.setText("Logging out...");
        }

        public void onLogoutFinish() {
            mText.setText("You have logged out! ");
            mUserPic.setImageBitmap(null);
        }
    }

}