/*
 * SAO Project
 */
package com.aionemu.chatserver.network.aion.clientpackets;

import com.aionemu.chatserver.network.aion.AbstractClientPacket;
import com.aionemu.chatserver.network.netty.handler.ClientChannelHandler;
import com.aionemu.chatserver.service.BroadcastService;
import java.nio.charset.Charset;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author pixfid
 */
public class CM_CHAT_CHANNEL_CREATE extends AbstractClientPacket {

    private String _channelName;
    private String _channelPassword;
    private int _channelIndex;
    private final BroadcastService broadcastService;
    /*public CM_CHAT_CHANNEL_CREATE(int opCode)
     {
     super(opCode);
     }*/

    public CM_CHAT_CHANNEL_CREATE(ChannelBuffer channelBuffer, ClientChannelHandler gameChannelHandler, BroadcastService broadcastService) {
        super(channelBuffer, gameChannelHandler, 0x18);
        this.broadcastService = broadcastService;
    }

    @Override
    protected void readImpl() {
        readC();
        readD();
        readH();
        _channelIndex = readH();
        _channelName = readS();
        _channelPassword = new String(readB(readH() * 2), Charset.forName("UTF-16LE"));
    }

    @Override
    protected void runImpl() {
        // ChatChannels.addPrivateChannel(_channelIndex, _channelName, _channelPassword, getChatClient());
    }
}
