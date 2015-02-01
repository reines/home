package com.furnaghan.home.component.meter.currentcost.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.furnaghan.home.component.meter.currentcost.api.Reading;
import io.dropwizard.jackson.FuzzyEnumModule;
import io.dropwizard.jackson.GuavaExtrasModule;
import io.dropwizard.jackson.LogbackModule;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.string.StringDecoder;

import java.nio.charset.StandardCharsets;

public class CC128Decoder extends StringDecoder {

    private static final ObjectMapper XML = newXmlMapper();

    private static ObjectMapper newXmlMapper() {
        final ObjectMapper mapper = new XmlMapper();
        mapper.registerModule(new GuavaModule());
        mapper.registerModule(new LogbackModule());
        mapper.registerModule(new GuavaExtrasModule());
        mapper.registerModule(new JodaModule());
        mapper.registerModule(new AfterburnerModule());
        mapper.registerModule(new FuzzyEnumModule());
        return mapper;
    }

    public CC128Decoder() {
        super(StandardCharsets.UTF_8);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        final String xml = (String) super.decode(ctx, channel, msg);
        return XML.readValue(xml, Reading.class);
    }
}
