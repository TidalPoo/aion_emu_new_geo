package com.aionemu.gameserver.network.telnet;

import com.aionemu.gameserver.configs.network.NetworkConfig;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * @author Amiko.Yuki
 */
public class TelnetServer {

    public TelnetServer() {
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newFixedThreadPool(1), Executors.newFixedThreadPool(1), 1));

        TelnetServerHandler handler = new TelnetServerHandler();
        bootstrap.setPipelineFactory(new TelnetPipelineFactory(handler));

        bootstrap.bind(new InetSocketAddress(NetworkConfig.TELNET_HOSTNAME.equals("*") ? null : NetworkConfig.TELNET_HOSTNAME, NetworkConfig.TELNET_PORT));
    }
}
