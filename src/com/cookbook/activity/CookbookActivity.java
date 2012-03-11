package com.cookbook.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.*;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cookbook.*;
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
	static final int SETTINGS_RESULT = 0;
	private String sortby = "recipeName";
	
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
        
        /*
         * setOnItemClickListener
         */
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	Intent intent;
            	switch (position) {
	            	case 0:
	            		intent = new Intent(v.getContext(), MyRecipesActivity.class);
	                	intent.putExtra("sortby", sortby);
	                	startActivityForResult(intent, 0);
	                	break;
	            	case 1:
	            		String query = "SELECT name, current_location, uid, pic_square FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1=me()) AND is_app_user ORDER BY name";
	            		Bundle params = new Bundle();
                        params.putString("method", "fql.query");
                        params.putString("query", query);
	            		Utility.mAsyncRunner.request(null, params, new FriendsRequestListener());
	                	break;
	            	case 2:
	            		intent = new Intent(v.getContext(), DiscoverActivity.class);
	                	intent.putExtra("sortby", sortby);
	                	startActivityForResult(intent, 0);
	                	break;
	            	case 3:
	            		intent = new Intent(v.getContext(), SettingsActivity.class);
	                	startActivityForResult(intent, SETTINGS_RESULT);
	            		
	                	break;
	            	default:
	            		break;
            	}
            }
        });
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(requestCode == SETTINGS_RESULT){
        	if(resultCode == 0){
        		sortby = "recipeName";
        	}else if(resultCode == 1){
        		sortby = "typeOfMeal";
        	}else if(resultCode == 2){
        		sortby = "duration";
        	}else if(resultCode == 3){
        		sortby = "season";
        	}else if(resultCode == 4){
        		sortby = "region";
        	}else if(resultCode == 5){
        		sortby = "rating";
        	}
        	
        }
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
        	case R.id.filter:
                // app icon in action bar clicked; go home
                Intent intent2 = new Intent(this, FilterActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                return true;      
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent3 = new Intent(this, CookbookActivity.class);
                intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}