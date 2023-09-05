package com.aionemu.gameserver.network.telnet.commands;

import com.aionemu.gameserver.network.telnet.TelnetCommand;
import com.aionemu.gameserver.network.telnet.TelnetCommandHolder;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.gametime.GameTime;
import com.aionemu.gameserver.utils.gametime.GameTimeManager;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Amiko.Yuki
 */
public class TelnetStatus implements TelnetCommandHolder {

    private Set<TelnetCommand> _commands = new LinkedHashSet<>();

    public TelnetStatus() {
        _commands.add(new TelnetCommand("status", "s") {

            @Override
            public String getUsage() {
                return "status";
            }

            @Override
            public String handle(String[] args) {
                StringBuilder sb = new StringBuilder();
                /*   int[] stats = new int[5];
                 for (WorldMap map : World.getInstance().getAllWorldMap()) {
                 stats[0]++;
                
                 for (WorldMapInstance inst : map.getInstances()) {
                 stats[1]++;
                 int[] s = inst.getStats();
                 stats[3] += s[0]; //active region
                 stats[4] += s[1]; //deactive region
                 stats[2] += s[0] + s[1]; //counts region
                 }
                 }
                
                 sb.append("Server Status: ").append("\n");
                 sb.append("Players: ................. ").append(World.getInstance().getPlayersCount()).append("/").append(NetworkConfig.MAX_ONLINE_PLAYERS).append("\n");
                 sb.append("AionObjects: ................. ").append(World.getInstance().getAionObjectCount()).append("\n");
                 sb.append("VisibleObjects: ................. ").append(World.getInstance().getVisibleObjectCount()).append("\n");
                 sb.append("Npcs: .................... ").append(World.getInstance().getNpcsCount()).append("\n");
                 sb.append("World Maps: ................. ").append(stats[0]).append("\n");
                 sb.append("     Instance: ............. ").append(stats[1]).append("\n");
                 sb.append("     Regions: ........... ").append(stats[2]).append("\n");
                 sb.append("            Active: ........... ").append(stats[3]).append("\n");
                 sb.append("            Deactive: ............... ").append(stats[4]).append("\n");
                 sb.append("Game Time: ............... ").append(getGameTime()).append("\n");
                 sb.append("Real Time: ............... ").append(getCurrentTime()).append("\n");
                 sb.append("Start Time: .............. ").append(getStartTime()).append("\n");
                 sb.append("Uptime: .................. ").append(getUptime()).append("\n");
                 sb.append("Shutdown: ................ ").append(Util.formatTime(Shutdown.getInstance().getSeconds())).append("/").append(Shutdown.getInstance().getMode()).append("\n");
                 sb.append("Threads: ................. ").append(Thread.activeCount()).append("\n");
                 sb.append("RAM Used: ................ ").append(StatsUtils.getMemUsedMb()).append("\n");*/
                return sb.toString();
            }

        });
    }

    @Override
    public Set<TelnetCommand> getCommands() {
        return _commands;
    }

    public static String getGameTime() {
        StringBuilder sb = new StringBuilder();
        GameTime gt = GameTimeManager.getGameTime();
        sb.append(gt.getHour()).append(":").append(gt.getMinute()).append(" ");
        sb.append(gt.getDay()).append("-").append(gt.getMonth()).append("-").append(gt.getYear());
        return sb.toString();
    }

    public static String getUptime() {
        return Util.formatTime((int) ManagementFactory.getRuntimeMXBean().getUptime() / 1000);
    }

    public static String getStartTime() {
        return new Date(ManagementFactory.getRuntimeMXBean().getStartTime()).toString();
    }

    public static String getCurrentTime() {
        return new Date().toString();
    }
}
