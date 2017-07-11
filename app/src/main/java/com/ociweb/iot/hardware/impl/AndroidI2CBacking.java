package com.ociweb.iot.hardware.impl;

import android.util.Log;
import android.util.SparseArray;

import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManagerService;
import com.ociweb.pronghorn.iot.i2c.I2CBacking;

import java.io.IOException;

/**
 * Created by mizumi on 6/1/17.
 */
public class AndroidI2CBacking implements I2CBacking {

    private final byte[] EMPTY = new byte[0];

    private PeripheralManagerService pms = new PeripheralManagerService();
    private SparseArray<I2cDevice> devices = new SparseArray<>(256);
    private String busName = null;

    private boolean configureDevice(byte address) {
        if (busName == null) {
            throw new IllegalStateException("I2C backing has not been configured!");
        }

        if (devices.get(address) == null) {
            try {
                devices.put(address, pms.openI2cDevice(busName, address));
            } catch (IOException e) {
                Log.e(AndroidI2CBacking.class.getSimpleName(), e.getMessage(), e);
                return false;
            }
        }

        return true;
    }

    @Override
    public AndroidI2CBacking configure(byte bus) {
        if (busName == null) {
            busName = "I2C" + bus;
        } else {
            throw new IllegalStateException("I2C backing has already been configured!");
        }

        return this;
    }

    @Override
    public byte[] read(byte address, byte[] target, int bufferSize) {
        if (!configureDevice(address)) {
            return EMPTY;
        }

        try {
            devices.get(address).read(target, bufferSize);
        } catch (IOException e) {
            Log.e(AndroidI2CBacking.class.getSimpleName(), e.getMessage(), e);
            return EMPTY;
        }

        return target;
    }

    @Override
    public boolean write(byte address, byte[] message, int length) {
        if (!configureDevice(address)) {
            return false;
        }

        try {
            devices.get(address).write(message, length);
        } catch (IOException e) {
            Log.e(AndroidI2CBacking.class.getSimpleName(), e.getMessage(), e);
            return false;
        }

        return true;
    }
}
