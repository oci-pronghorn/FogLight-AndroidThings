package objectcomputing.com.thingstest;

import android.app.Activity;
import android.os.Bundle;

import com.ociweb.iot.IoTApp;
import com.ociweb.iot.maker.FogRuntime;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Pronghorn
        FogRuntime.run(new IoTApp());
    }
}
