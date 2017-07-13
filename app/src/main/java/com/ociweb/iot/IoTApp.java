/**
 * blinkerChannel is a CommandChannel created to transport data. 
 * Data is published to the channel. When  the blinkerChannel is
 * subscribed to the channel, the published data can also be accessed 
 * by playload.writeBoolean()from the channel.
 * <p>
 * Lambda expressions are introduced in Java 8 to facilitate functional 
 * programming. A Lambda expression is usually written using syntax 
 * (argument) -> (body). 
 * <p>
 */
package com.ociweb.iot;

import static com.ociweb.iot.grove.AnalogDigitalTwig.LED;
import static com.ociweb.iot.maker.Port.D5;

import com.ociweb.gl.api.MessageReader;
import com.ociweb.gl.api.PubSubListener;
import com.ociweb.gl.api.PubSubWritable;
import com.ociweb.gl.api.StartupListener;
import com.ociweb.iot.maker.FogCommandChannel;
import com.ociweb.iot.maker.FogRuntime;
import com.ociweb.iot.maker.Hardware;
import com.ociweb.iot.maker.FogApp;
import com.ociweb.iot.maker.Port;
import com.ociweb.pronghorn.pipe.BlobWriter;

public class IoTApp implements FogApp {

    private static final String TOPIC = "light";
    private static final int PAUSE = 500;
    public static final Port LED_PORT = D5;

    public static void main( String[] args) {
        FogRuntime.run(new IoTApp());
    }

    @Override
    public void declareConnections(Hardware c) {
        c.connect(LED, D5);

    }

    @Override
    public void declareBehavior(FogRuntime runtime) {

        final FogCommandChannel blinkerChannel = runtime.newCommandChannel(DYNAMIC_MESSAGING);
        runtime.addPubSubListener(new PubSubListener() {
            @Override
            public boolean message(CharSequence topic, MessageReader payload) {

                boolean value = payload.readBoolean();
                blinkerChannel.setValueAndBlock(LED_PORT, value, PAUSE);
                boolean ignored = blinkerChannel.publishTopic(TOPIC, new PubSubWritable() {
                    @Override
                    public void write(BlobWriter w) {
                        w.writeBoolean(!value);
                    }
                });
                return true;

            }
        }).addSubscription(TOPIC);

        final FogCommandChannel startupChannel = runtime.newCommandChannel(DYNAMIC_MESSAGING);
        runtime.addStartupListener(
                new StartupListener() {
                    @Override
                    public void startup() {
                        boolean ignored = startupChannel.publishTopic(TOPIC, new PubSubWritable() {
                            @Override
                            public void write(BlobWriter w) {
                                w.writeBoolean(true);
                            }
                        });
                    }
                });
    }
}