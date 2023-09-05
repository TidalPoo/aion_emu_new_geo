package com.aionemu.gameserver.network.telnet.commands;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.network.telnet.TelnetCommand;
import com.aionemu.gameserver.network.telnet.TelnetCommandHolder;
import com.aionemu.gameserver.services.PunishmentService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.world.World;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Amiko.Yuki
 */
public class TelnetBan implements TelnetCommandHolder {

    private Set<TelnetCommand> _commands = new LinkedHashSet<>();

    public TelnetBan() {
        _commands.add(new TelnetCommand("kick") {
            @Override
            public String getUsage() {
                return "kick <name>";
            }

            @Override
            public String handle(String[] args) {
                if (args.length == 0 || args[0].isEmpty()) {
                    return null;
                }

                Player player = World.getInstance().findPlayer(args[0]);
                if (player != null) {
                    AionConnection connection = player.getClientConnection();
                    if (connection != null) {
                        connection.close(true);
                        return "Player kicked.\n";
                    }
                }
                return "Player not found.\n";
            }
        });

        _commands.add(new TelnetCommand("kickobj") {
            @Override
            public String getUsage() {
                return "kickobj <playerObjId>";
            }

            @Override
            public String handle(String[] args) {
                if (args.length == 0 || args[0].isEmpty()) {
                    return null;
                }

                Player player = World.getInstance().findPlayer(Integer.valueOf(args[0]));
                if (player != null) {
                    AionConnection connection = player.getClientConnection();
                    if (connection != null) {
                        connection.close(true);
                        return "Player kicked.\n";
                    }
                }
                return "Player not found.\n";
            }
        });

        _commands.add(new TelnetCommand("chatban") {
            @Override
            public String getUsage() {
                return "chatban <playerName> <period(min)> <reason>";
            }

            @Override
            public String handle(String[] args) {
                if (args.length == 0 || args[0].isEmpty() || args.length < 1) {
                    return null;
                }

                Player target = World.getInstance().findPlayer(args[0]);

                long time = -1;
                if (args.length >= 2) {
                    try {
                        time = Integer.valueOf(args[1]) * 60;
                        time += System.currentTimeMillis() / 1000;
                    } catch (Exception ex) {
                        return "chatban <playerName> <period(min)> <reason>";
                    }
                }

                if (time < 0) {
                    PacketSendUtility.sendMessage(target, "Your chat banned. Have to wait infinite time.");
                } else {
                    String leftTime = Util.formatTime((int) (time - (System.currentTimeMillis() / 1000)));
                    PacketSendUtility.sendMessage(target, "Your chat banned. Have to wait " + leftTime);
                }

                if (args.length == 3) {
                    PacketSendUtility.sendMessage(target, "Reason for ban chat: " + args[2]);
                }

                //target.setChatBanExpireTime(time);
                PunishmentService.setGAG(target, true, time, null);
                return "Player " + args[0] + " chat banned.";
            }
        });

        _commands.add(new TelnetCommand("ban") {
            @Override
            public String getUsage() {
                return "ban <player name> <account | ip | full> <time in minutes>";
            }

            @Override
            public String handle(String[] args) {
                if (args.length == 0 || args[0].isEmpty() || args.length < 3) {
                    return null;
                }

                // We need to get player's account ID
                String name = Util.convertName(args[0]);
                int accountId = 0;

                // First, try to find player in the World
                Player player = World.getInstance().findPlayer(name);
                if (player != null) {
                    accountId = player.getClientConnection().getAccount().getId();
                }

                // Second, try to get account ID of offline player from database
                if (accountId == 0) {
                    accountId = DAOManager.getDAO(PlayerDAO.class).getAccountIdByName(name);
                }

                // Third, fail
                if (accountId == 0) {
                    return "Player " + name + " was not found!";
                }

                byte type = 3; // Default: full
                if (args.length > 1) {
                    // Smart Matching
                    String stype = args[1].toLowerCase();
                    if (("account").startsWith(stype)) {
                        type = 1;
                    } else if (("ip").startsWith(stype)) {
                        type = 2;
                    } else if (("full").startsWith(stype)) {
                        type = 3;
                    } else if (("mac").startsWith(stype)) {
                        type = 4;
                    } else {
                        return "ban <player name> <account | ip | full> <time in minutes>";
                    }
                }

                int time = 0; // Default: infinity
                if (args.length > 2) {
                    try {
                        time = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        return "ban <player name> <account | ip | full> <time in minutes>";
                    }
                }
                LoginServer.getInstance().sendBanPacket(type, accountId, "", time, 0);
                return "Player " + name + " banned.";
            }
        });
    }

    @Override
    public Set<TelnetCommand> getCommands() {
        return _commands;
    }
}
