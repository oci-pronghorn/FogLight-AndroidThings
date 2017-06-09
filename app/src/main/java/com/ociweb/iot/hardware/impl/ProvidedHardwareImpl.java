package com.ociweb.iot.hardware.impl;

import com.ociweb.iot.hardware.HardwareImpl;
import com.ociweb.iot.hardware.HardwarePlatformType;
import com.ociweb.iot.maker.Port;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;

/**
 * Created by mizumi on 6/1/17.
 */

public class ProvidedHardwareImpl extends HardwareImpl {

    public ProvidedHardwareImpl(GraphManager gm) {
        super(gm, new AndroidI2CBacking());
    }

    @Override
    public HardwarePlatformType getPlatformType() {
        return null;
    }

    @Override
    public int read(Port port) {
        return 0;
    }

    @Override
    public void write(Port port, int value) {

    }
}
