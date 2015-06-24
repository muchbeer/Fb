package muchbeer.king.fb;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private TextView mTextDetail;
        private LoginButton loginButton;
        private CallbackManager callbackManager;

        private AccessTokenTracker mTokenTracker;
        private ProfileTracker mProfileTracker;
        private String naming;

        private Profile profile;
        private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();

                profile = Profile.getCurrentProfile();
               naming  = profile.getFirstName();
                displayWelcomMesage(profile);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        };

        public PlaceholderFragment() {
        }

      public void  onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

          callbackManager = CallbackManager.Factory.create();

          mTokenTracker = new AccessTokenTracker() {
              @Override
              protected void onCurrentAccessTokenChanged(AccessToken OldaccessToken, AccessToken NewaccessToken2) {

              }
          };

          mProfileTracker = new ProfileTracker() {
              @Override
              protected void onCurrentProfileChanged(Profile oldProfile, Profile NewProfile2) {
                    displayWelcomMesage(NewProfile2);
              }
          };

          mTokenTracker.startTracking();
          mProfileTracker.startTracking();
     //     LoginButton loginButton = (LoginButton) view.findViewById(R.id.usersettings_fragment_login_button);
       //   loginButton.registerCallback(callbackManager, new LoginButton.Callback() { ... });


      }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);


            // Other app specific specialization
            return rootView;
        }

        private void displayWelcomMesage(Profile profile) {
            if(profile != null) {
                mTextDetail.setText("Welcome " + naming);
            }
        }
        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            loginButton = (LoginButton) view.findViewById(R.id.login_button);
            loginButton.setReadPermissions("user_friends");
            // If using in a fragment
            loginButton.setFragment(this);
            loginButton.registerCallback(callbackManager, mCallBack);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        @Override
        public void onResume() {
            super.onResume();
          //  Profile profile = Profile.getCurrentProfile();
            displayWelcomMesage(profile);
        }

        @Override
        public void onStop() {
            super.onStop();

        mTokenTracker.stopTracking();
            mProfileTracker.startTracking();
        }
    }


}
