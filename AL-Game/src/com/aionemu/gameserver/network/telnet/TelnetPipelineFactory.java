package com.aionemu.gameserver.network.telnet;

import com.aionemu.gameserver.configs.network.NetworkConfig;
import java.nio.charset.Charset;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import static org.jboss.netty.channel.Channels.pipeline;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

/**
 * @author Amiko.Yuki
 */
public class TelnetPipelineFactory implements ChannelPipelineFactory {

    private final ChannelHandler handler;

    public TelnetPipelineFactory(ChannelHandler handler) {
        this.handler = handler;
    }

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        // Create a default pipeline implementation.
        ChannelPipeline pipeline = pipeline();

        // Add the text line codec combination first,
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new StringDecoder(Charset.forName(NetworkConfig.TELNET_DEFAULT_ENCODING)));
        pipeline.addLast("encoder", new StringEncoder(Charset.forName(NetworkConfig.TELNET_DEFAULT_ENCODING)));

        // and then business logic.
        pipeline.addLast("handler", handler);

        return pipeline;
    }
}