package com.aionemu.gameserver.network.telnet.commands;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.telnet.TelnetCommand;
import com.aionemu.gameserver.network.telnet.TelnetCommandHolder;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Amiko.Yuki
 */
public class TelnetSay implements TelnetCommandHolder {

    private Set<TelnetCommand> _commands = new LinkedHashSet<>();

    public TelnetSay() {
        _commands.add(new TelnetCommand("announce", "ann") {
            @Override
            public String getUsage() {
                return "announce <text>";
            }

            @Override
            public String handle(String[] args) {
                if (args.length == 0) {
                    return null;
                }

                final String _message = args[0];
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
                    @Override
                    public void visit(Player player) {
                        PacketSendUtility.sendMessage(player, _message);
                    }
                });

                return "Announcement sent.\n";
            }
        });

        _commands.add(new TelnetCommand("message", "msg") {
            @Override
            public String getUsage() {
                return "message <player> <text>";
            }

            @Override
            public String handle(String[] args) {
                if (args.length < 2) {
                    return null;
                }

                Player player = World.getInstance().findPlayer(args[0]);
                if (player == null) {
                    return "Player not found.\n";
                }

                if (!args[1].isEmpty()) {
                    PacketSendUtility.sendMessage(player, args[1]);
                }

                return "Message sent.\n";
            }

        });
    }

    @Override
    public Set<TelnetCommand> getCommands() {
        return _commands;
    }
}
