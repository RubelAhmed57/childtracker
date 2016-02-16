package rajesh.example.com.childtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

public class SubmitLatLong extends AppCompatActivity {

    private GoogleMap mMap;
    map Map;
    double Lat;
    double Long;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_lat_long);

        Intent intent = getIntent();
        String user = intent.getExtras().getString("user");
        Toast.makeText(SubmitLatLong.this, user, Toast.LENGTH_LONG).show();
    }
    public void mapping(View view) {
        Map= new map(SubmitLatLong.this);
        Lat= Map.getLatitude();
        Long=Map.getLongitude();
        Toast.makeText(SubmitLatLong.this, "lati=" + Lat + "  long= " + Long, Toast.LENGTH_LONG).show();

    }



}
