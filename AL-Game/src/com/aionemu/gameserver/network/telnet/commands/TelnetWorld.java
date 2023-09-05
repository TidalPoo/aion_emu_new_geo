package com.aionemu.gameserver.network.telnet.commands;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.telnet.TelnetCommand;
import com.aionemu.gameserver.network.telnet.TelnetCommandHolder;
import com.aionemu.gameserver.world.World;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Amiko.Yuki
 */
public class TelnetWorld implements TelnetCommandHolder {

    private Set<TelnetCommand> _commands = new LinkedHashSet<>();

    public TelnetWorld() {
        _commands.add(new TelnetCommand("find") {

            @Override
            public String getUsage() {
                return "find <name>";
            }

            @Override
            public String handle(String[] args) {
                if (args.length == 0) {
                    return null;
                }

                Iterator<Player> itr = World.getInstance().getPlayersIterator();
                StringBuilder sb = new StringBuilder();
                int count = 0;
                Player player;
                Pattern pattern = Pattern.compile(args[0] + "\\S+", Pattern.CASE_INSENSITIVE);
                while (itr.hasNext()) {
                    player = itr.next();

                    if (pattern.matcher(player.getName()).matches()) {
                        count++;
                        sb.append(player).append("\n");
                    }
                }

                if (count == 0) {
                    sb.append("Player not found.").append("\n");
                } else {
                    sb.append("=================================================\n");
                    sb.append("Found: ").append(count).append(" players.").append("\n");
                }

                return sb.toString();
            }

        });
        _commands.add(new TelnetCommand("whois", "who") {

            @Override
            public String getUsage() {
                return "whois <name>";
            }

            @Override
            public String handle(String[] args) {
                if (args.length == 0) {
                    return null;
                }

                Player player = World.getInstance().findPlayer(args[0]);
                if (player == null) {
                    return "Player not found.\n";
                }

                StringBuilder sb = new StringBuilder();

                sb.append("Name: .................... ").append(player.getName()).append("\n");
                sb.append("ID: ...................... ").append(player.getObjectId()).append("\n");
                sb.append("Account Name: ............ ").append(player.getAcountName()).append("\n");
                sb.append("IP: ...................... ").append(player.getClientConnection().getIP()).append("\n");
                sb.append("Level: ................... ").append(player.getLevel()).append("\n");
                sb.append("Map Id: ................ ").append(player.getPosition().getMapId()).append("\n");
                sb.append("Map Instance Id: ................ ").append(player.getPosition().getInstanceId()).append("\n");
                sb.append("Pos: ................ ").append(player.getPosition().getPoint()).append("\n");

                if (player.getLegion() != null) {
                    sb.append("Legion id: .................... ").append(player.getLegion().getLegionId()).append("\n");
                    sb.append("Legion name: .................... ").append(player.getLegion().getLegionName()).append("\n");
                }

                sb.append(player.toString()).append("\n");

                return sb.toString();
            }

        });

    }

    @Override
    public Set<TelnetCommand> getCommands() {
        return _commands;
    }
}
