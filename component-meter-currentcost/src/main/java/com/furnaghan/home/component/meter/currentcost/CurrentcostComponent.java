package com.furnaghan.home.component.meter.currentcost;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.meter.MeterType;
import com.furnaghan.home.component.meter.currentcost.api.Reading;
import com.furnaghan.home.component.meter.currentcost.api.Sensor;
import com.furnaghan.home.component.meter.currentcost.codec.CC128Decoder;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.jamierf.rxtx.RXTXLoader;
import de.uniluebeck.itm.nettyrxtx.RXTXChannelConfig;
import de.uniluebeck.itm.nettyrxtx.RXTXChannelFactory;
import de.uniluebeck.itm.nettyrxtx.RXTXDeviceAddress;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executors;

public class CurrentcostComponent extends Component<MeterType.Listener> implements MeterType {
    private static final int BAUD_RATE = 57600;
    private static final int MAX_FRAME_SIZE = 8192;

    private static final RXTXChannelConfig.Stopbits STOPBITS = RXTXChannelConfig.Stopbits.STOPBITS_1;
    private static final RXTXChannelConfig.Databits DATABITS = RXTXChannelConfig.Databits.DATABITS_8;
    private static final RXTXChannelConfig.Paritybit PARITYBIT = RXTXChannelConfig.Paritybit.NONE;

    private static final Logger LOG = LoggerFactory.getLogger(CurrentcostComponent.class);

    private static void loadDriver() {
        try {
            RXTXLoader.load();
        } catch (IOException e) {
            LOG.error("Failed to load RXTX driver", e);
            throw Throwables.propagate(e);
        }
    }

    public CurrentcostComponent(final CurrentcostConfiguration configuration) {
        loadDriver();

        final RXTXDeviceAddress address = new RXTXDeviceAddress(configuration.getPort());
        final ClientBootstrap bootstrap = new ClientBootstrap(new RXTXChannelFactory(Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(() -> {
            final ChannelPipeline pipeline = Channels.pipeline();

            pipeline.addLast("framer", new DelimiterBasedFrameDecoder(MAX_FRAME_SIZE, Delimiters.lineDelimiter()));
            pipeline.addLast("decoder", new CC128Decoder());
            pipeline.addLast("handler", new SimpleChannelHandler() {
                @Override
                public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
                    final Reading reading = (Reading) e.getMessage();
                    receiveChannel("ch1", reading.getChannel1());
                    receiveChannel("ch2", reading.getChannel2());
                    receiveChannel("ch3", reading.getChannel3());
                }

                @Override
                public void exceptionCaught(final ChannelHandlerContext ctx, final ExceptionEvent e) throws Exception {
                    LOG.warn("Unknown exception", e.getCause());
                }
            });

            return pipeline;
        });

        bootstrap.setOption("baudrate", BAUD_RATE);
        bootstrap.setOption("stopbits", STOPBITS);
        bootstrap.setOption("databits", DATABITS);
        bootstrap.setOption("paritybit", PARITYBIT);

        final ChannelFuture result = bootstrap.connect(address).awaitUninterruptibly();
        if (!result.isSuccess()) {
            throw Throwables.propagate(result.getCause());
        }

        LOG.info("Connected to {}", address.getDeviceAddress());
    }

    private void receiveChannel(final String name, final Optional<Sensor> channel) {
        if (!channel.isPresent()) {
            return;
        }

        trigger((listener) -> listener.receive(name, channel.get().getWatts()));
    }
}
