package rajesh.example.com.childtracker;



import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Users extends ListActivity {


    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    private static final String READ_USER = "http://familytracker.netau.net/prac.php";

    private static final String TAG_SUCCESS = "success";

    private static final String TAG_POSTS = "posts";

    private static final String TAG_USERNAME = "username";
    private static final String TAG_MOBILE = "mobile";

    private JSONArray mComments = null;
    // manages all of our comments in a list.
    private ArrayList<HashMap<String, String>> mCommentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // note that use read_comments.xml instead of our single_post.xml
        setContentView(R.layout.activity_users);


    }


    public void request(View view) {
        Intent intent = new Intent(this,Request.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // loading the comments via AsyncTask
        new LoadComments().execute();
    }



    /**
     * Retrieves recent post data from the server.
     */
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
                String mobile = c.getString(TAG_MOBILE);

                String username = c.getString(TAG_USERNAME);

                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();


                map.put(TAG_MOBILE, mobile);
                map.put(TAG_USERNAME, username);

                mCommentList.add(map);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateList() {

        ListAdapter adapter = new SimpleAdapter(this, mCommentList,
                R.layout.single_user, new String[] { TAG_MOBILE,
                TAG_USERNAME }, new int[] { R.id.mobile,
                R.id.username });


        setListAdapter(adapter);


        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                String user= String.valueOf(parent.getItemAtPosition(position));
                String[] gi=user.split("=");
                String str=gi[1];
                str = str.substring(0, str.length()-8);

                Toast.makeText(Users.this, str, Toast.LENGTH_LONG).show();
                Intent i = new Intent(Users.this, MapsActivity.class);
                i.putExtra("user", str);
                startActivity(i);



            }
        });
    }



    public class LoadComments extends AsyncTask<Void, Void, Boolean> {
        SharedPreferences sp;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Users.this);
            pDialog.setMessage("Loading Users...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            sp= getSharedPreferences("UserInformation", Context.MODE_PRIVATE);
            String username=sp.getString("username","");

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
            updateList();
        }
    }
}
