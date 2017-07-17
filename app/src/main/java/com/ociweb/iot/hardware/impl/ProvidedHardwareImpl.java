package com.ociweb.iot.hardware.impl;

import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
import com.ociweb.iot.hardware.HardwareImpl;
import com.ociweb.iot.hardware.HardwarePlatformType;
import com.ociweb.iot.hardware.I2CConnection;
import com.ociweb.iot.hardware.I2CIODevice;
import com.ociweb.iot.maker.Hardware;
import com.ociweb.iot.maker.Port;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;

import java.io.IOException;

/**
 * Created by mizumi on 6/1/17.
 */

public class ProvidedHardwareImpl extends HardwareImpl {

    private PeripheralManagerService pms = new PeripheralManagerService();

    public ProvidedHardwareImpl(GraphManager gm) {
        super(gm, new String[]{}, new AndroidI2CBacking());
    }

    // TODO: Are these two methods the only place we need to configure?
    @Override
    public Hardware connect(I2CIODevice t){
        super.connect(t);
        ((AndroidI2CBacking) i2cBacking).configure(t.getI2CConnection().address);
        return this;
    }

    @Override
    public Hardware connect(I2CIODevice t, int customRateMS){
        super.connect(t, customRateMS);
        ((AndroidI2CBacking) i2cBacking).configure(t.getI2CConnection().address);
        return this;
    }

    @Override
    public HardwarePlatformType getPlatformType() {
        return HardwarePlatformType.ANDROID;
    }

    @Override
    public int read(Port port) {
        try {
            // TODO: Validate these configurations.
            Gpio gpio = pms.openGpio(String.valueOf(port.port));
            gpio.setDirection(Gpio.DIRECTION_IN);
            gpio.setActiveType(Gpio.ACTIVE_LOW);

            // TODO: Is this correct?
            return gpio.getValue() ? 0 : 1;
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return 0;
    }

    @Override
    public void write(Port port, int value) {
        try {
            // TODO: Validate these configurations.
            Gpio gpio = pms.openGpio(String.valueOf(port.port));
            gpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
            gpio.setActiveType(Gpio.ACTIVE_LOW);

            // TODO: Is this correct?
            gpio.setValue(value == 0);
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }
    }
}
