package rajesh.example.com.childtracker;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;




public class ChildRegister extends AppCompatActivity implements OnClickListener{

    private EditText user, pass,id,mobile;
    private Button  mRegister;
    String username,password;
    String mobile_no,parent_id;


    private GoogleMap mMap;
    map Map;
    double Lat;
    double Long;

    String latitude;
    String longitude;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //php login script

    //localhost :
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
    // private static final String LOGIN_URL = "http://xxx.xxx.x.x:1234/webservice/register.php";

    //testing on Emulator:
    private static final String LOGIN_URL = "http://familytracker.netau.net/childregister.php";

    //testing from a real server:
    //private static final String LOGIN_URL = "http://www.yourdomain.com/webservice/register.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_child_register);

        user = (EditText)findViewById(R.id.name);
        pass = (EditText)findViewById(R.id.pass);
        id=(EditText)findViewById(R.id.parent_id);
        mobile=(EditText)findViewById(R.id.mobile);


        mRegister = (Button)findViewById(R.id.regi);
        mRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        username = user.getText().toString();
        password = pass.getText().toString();
        parent_id = id.getText().toString();
        mobile_no = mobile.getText().toString();

        Map= new map(ChildRegister.this);
        Lat= Map.getLatitude();
        Long=Map.getLongitude();
        Toast.makeText(ChildRegister.this, "lati=" + Lat + "  long= " + Long, Toast.LENGTH_LONG).show();
        latitude= String.valueOf(Lat);
        longitude = String.valueOf(Long);

        Log.i("Lati",latitude);

        new CreateUser().execute();

    }

    class CreateUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ChildRegister.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("parent_id", parent_id));
                params.add(new BasicNameValuePair("mobile", mobile_no));
                params.add(new BasicNameValuePair("lat", latitude));
                params.add(new BasicNameValuePair("long", longitude));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                // full json response
                Log.d("Login attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("User Created!", json.toString());


                    //Intent i = new Intent(ChildRegister.this, SubmitLatLong.class);
                   // i.putExtra("user", username);
                   // startActivity(i);
                    finish();
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(ChildRegister.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }


}

