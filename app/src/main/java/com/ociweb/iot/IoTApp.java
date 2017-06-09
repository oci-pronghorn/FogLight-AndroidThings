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

import static com.ociweb.iot.grove.GroveTwig.LED;
import static com.ociweb.iot.maker.Port.D4;

import com.ociweb.gl.api.GreenCommandChannel;
import com.ociweb.gl.api.MessageReader;
import com.ociweb.gl.api.PubSubListener;
import com.ociweb.gl.api.PubSubWritable;
import com.ociweb.gl.api.PubSubWriter;
import com.ociweb.gl.api.StartupListener;
import com.ociweb.iot.maker.CommandChannel;
import com.ociweb.iot.maker.DeviceRuntime;
import com.ociweb.iot.maker.Hardware;
import com.ociweb.iot.maker.IoTSetup;
import com.ociweb.iot.maker.Port;

public class IoTApp implements IoTSetup {

    private static final String TOPIC = "light";
    private static final int PAUSE = 500;
    public static final Port LED_PORT = D4;

    public static void main( String[] args) {
        DeviceRuntime.run(new IoTApp());
    }

    @Override
    public void declareConnections(Hardware c) {
        c.connect(LED, D4);
    }

    @Override
    public void declareBehavior(DeviceRuntime runtime) {

        final CommandChannel blinkerChannel = runtime.newCommandChannel(GreenCommandChannel.DYNAMIC_MESSAGING);
        runtime.addPubSubListener(new PubSubListener() {
            @Override
            public boolean message(CharSequence topic, MessageReader payload) {

                boolean value = payload.readBoolean();
                blinkerChannel.setValueAndBlock(LED_PORT, value ? 1 : 0, PAUSE);
                boolean ignored = blinkerChannel.openTopic(TOPIC, new PubSubWritable() {
                    @Override
                    public void write(PubSubWriter w) {
                        w.writeBoolean(!value);
                        w.publish();

                    }
                });
                return true;

            }
        }).addSubscription(TOPIC);

        final CommandChannel startupChannel = runtime.newCommandChannel(GreenCommandChannel.DYNAMIC_MESSAGING);
        runtime.addStartupListener(
                new StartupListener() {
                    @Override
                    public void startup() {
                        boolean ignored = startupChannel.openTopic(TOPIC, new PubSubWritable() {
                            @Override
                            public void write(PubSubWriter w) {
                                w.writeBoolean(true);
                                w.publish();
                            }
                        });
                    }
                });
    }
}
