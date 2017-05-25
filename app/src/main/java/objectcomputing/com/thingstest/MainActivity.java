package objectcomputing.com.thingstest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This service lets us talk to hardware.
        PeripheralManagerService service = new PeripheralManagerService();

        // Log some resources.
        Log.d(TAG, "Available GPIO: " + service.getGpioList());
        Log.d(TAG, "Available I2C Bus: " + service.getI2cBusList());
        Log.d(TAG, "Available I2C Devices: " + service.getI2sDeviceList());

        // LCD Display control? Why not.
        try {
            I2cDevice rgbDisplay = service.openI2cDevice("I2C6", 1);

            rgbDisplay.write(new byte[0], 0);
            rgbDisplay.read(new byte[0], 0);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
