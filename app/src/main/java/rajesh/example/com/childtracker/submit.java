package rajesh.example.com.childtracker;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Rajesh on 2/15/2016.
 */
public class submit extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public submit(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String dataString = intent.getDataString();
        Toast.makeText(submit.this, dataString, Toast.LENGTH_LONG).show();
    }
}
