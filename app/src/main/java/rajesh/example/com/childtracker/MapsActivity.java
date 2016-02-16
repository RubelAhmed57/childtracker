package rajesh.example.com.childtracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    CameraPosition cameraPosition;
    MapView mMapView;

double Lt,Lg;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    private static final String READ_USER = "http://familytracker.netau.net/mapactivity.php";

    private static final String TAG_SUCCESS = "success";

    private static final String TAG_POSTS = "posts";

    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    String username;
    String Lat,Long;

    private JSONArray mComments = null;
    // manages all of our comments in a list.
    private ArrayList<HashMap<String, String>> mCommentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        username = intent.getExtras().getString("user");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // loading the comments via AsyncTask
        new LoadComments().execute();
    }


    public void updateJSONdata() {



        mCommentList = new ArrayList<HashMap<String, String>>();

        JSONParser jParser = new JSONParser();

        JSONObject json = jParser.getJSONFromUrl(READ_USER);


        try {


            mComments = json.getJSONArray(TAG_POSTS);

            // looping through all posts according to the json object returned
            for (int i = 0; i < mComments.length(); i++) {
                JSONObject c = mComments.getJSONObject(i);

                // gets the content of each tag
                String latitude = c.getString(TAG_LATITUDE);

                String longitude = c.getString(TAG_LONGITUDE);

                Lt = Double.valueOf(latitude).doubleValue();
                Lg = Double.valueOf(longitude).doubleValue();

                //Toast.makeText(MapsActivity.this,latitude, Toast.LENGTH_LONG).show();
                Log.i("latitude",latitude);
                Log.i("latitude","lati=" + Lt + "  long= " + Lg);

                // creating new HashMap
               // HashMap<String, String> map = new HashMap<String, String>();


                //map.put(TAG_LAT, latitude);
               // map.put(TAG_LONG, longitude);

               // mCommentList.add(map);



               // Lat=map.get(TAG_LAT);
               // Long=map.get(TAG_LONG);
              //  Toast.makeText(MapsActivity.this, Lat, Toast.LENGTH_LONG).show();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public class LoadComments extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapsActivity.this);
            pDialog.setMessage("Loading Map...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));


            Log.d("request!", "starting");
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(READ_USER, "POST",
                    params);


            updateJSONdata();
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            //updateList();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Lt,Lg);
        mMap.addMarker(new MarkerOptions().position(sydney).title("marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Lt,Lg)).zoom(16).build();


       /* googleMap.addMarker(new MarkerOptions().position(
                new LatLng(Lt,Lg)).title("Your Position").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
         cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Lt, Lg)).zoom(16).build();*/

    }
}
