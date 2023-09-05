package com.aionemu.gameserver.network.telnet.commands;

//import com.aionemu.gameserver.Shutdown;
//import com.aionemu.gameserver.data.holder.GMAccessesHolder;
//import com.aionemu.gameserver.data.parser.GMAccessesParser;
import com.aionemu.gameserver.ShutdownHook;
import com.aionemu.gameserver.network.telnet.TelnetCommand;
import com.aionemu.gameserver.network.telnet.TelnetCommandHolder;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Amiko.Yuki
 */
public class TelnetServer implements TelnetCommandHolder {

    private Set<TelnetCommand> _commands = new LinkedHashSet<>();

    public TelnetServer() {
        _commands.add(new TelnetCommand("restart") {
            @Override
            public String getUsage() {
                return "restart <seconds>|now>";
            }

            @Override
            public String handle(String[] args) {
                if (args.length == 0) {
                    return null;
                }

                StringBuilder sb = new StringBuilder();

                if (args[0].equalsIgnoreCase("now")) {
                    sb.append("Server will restart now!\n");
                    // Shutdown.getInstance().schedule(0, Shutdown.RESTART);
                    ShutdownHook.getInstance().doShutdown(0, 0, ShutdownHook.ShutdownMode.RESTART);

                } else if (args[0].contains(":")) {
                    String[] hhmm = args[0].split(":");

                    Calendar date = Calendar.getInstance();
                    Calendar now = Calendar.getInstance();

                    date.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hhmm[0]));
                    date.set(Calendar.MINUTE, hhmm.length > 1 ? Integer.parseInt(hhmm[1]) : 0);
                    date.set(Calendar.SECOND, 0);
                    date.set(Calendar.MILLISECOND, 0);
                    if (date.before(now)) {
                        date.roll(Calendar.DAY_OF_MONTH, true);
                    }

                    int seconds = (int) (date.getTimeInMillis() / 1000L - now.getTimeInMillis() / 1000L);

                    //Shutdown.getInstance().schedule(seconds, Shutdown.RESTART);
                    ShutdownHook.getInstance().doShutdown(seconds, 1, ShutdownHook.ShutdownMode.RESTART);
                    //  sb.append("Server will restart in ").append(Shutdown.getInstance().getSeconds()).append(" seconds!\n");
                    sb.append("Type \"abort\" to abort restart!\n");
                } else {
                    int val = Integer.valueOf(args[0]);
                    ShutdownHook.getInstance().doShutdown(val, 1, ShutdownHook.ShutdownMode.RESTART);
                    //sb.append("Server will restart in ").append(Shutdown.getInstance().getSeconds()).append(" seconds!\n");
                    sb.append("Type \"abort\" to abort restart!\n");
                }

                return sb.toString();
            }
        });

        _commands.add(new TelnetCommand("shutdown") {
            @Override
            public String getUsage() {
                return "shutdown <seconds>|now|<hh:mm>";
            }

            @Override
            public String handle(String[] args) {
                if (args.length == 0) {
                    return null;
                }

                StringBuilder sb = new StringBuilder();

                if (args[0].equalsIgnoreCase("now")) {
                    sb.append("Server will shutdown now!\n");
                    // Shutdown.getInstance().schedule(0, Shutdown.SHUTDOWN);
                    ShutdownHook.getInstance().doShutdown(0, 1, ShutdownHook.ShutdownMode.SHUTDOWN);
                } else if (args[0].contains(":")) {
                    String[] hhmm = args[0].split(":");

                    Calendar date = Calendar.getInstance();
                    Calendar now = Calendar.getInstance();

                    date.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hhmm[0]));
                    date.set(Calendar.MINUTE, hhmm.length > 1 ? Integer.parseInt(hhmm[1]) : 0);
                    date.set(Calendar.SECOND, 0);
                    date.set(Calendar.MILLISECOND, 0);
                    if (date.before(now)) {
                        date.roll(Calendar.DAY_OF_MONTH, true);
                    }

                    int seconds = (int) (date.getTimeInMillis() / 1000L - now.getTimeInMillis() / 1000L);

                    //Shutdown.getInstance().schedule(seconds, Shutdown.SHUTDOWN);
                    ShutdownHook.getInstance().doShutdown(seconds, 1, ShutdownHook.ShutdownMode.SHUTDOWN);
                    //  sb.append("Server will shutdown in ").append(Shutdown.getInstance().getSeconds()).append(" seconds!\n");
                    sb.append("Type \"abort\" to abort shutdown!\n");
                } else {
                    int val = Integer.valueOf(args[0]);
                    //Shutdown.getInstance().schedule(val, Shutdown.SHUTDOWN);
                    ShutdownHook.getInstance().doShutdown(val, 1, ShutdownHook.ShutdownMode.SHUTDOWN);
                    //sb.append("Server will shutdown in ").append(Shutdown.getInstance().getSeconds()).append(" seconds!\n");
                    sb.append("Type \"abort\" to abort shutdown!\n");
                }

                return sb.toString();
            }
        });

        _commands.add(new TelnetCommand("abort") {

            @Override
            public String getUsage() {
                return "abort";
            }

            @Override
            public String handle(String[] args) {
                //TODO fix it!
                // Shutdown.getInstance().cancel();
                return "Aborted.\n";
            }

        });

        _commands.add(new TelnetCommand("reload") {
            @Override
            public String getUsage() {
                return "reload gmaccess";
            }

            @Override
            public String handle(String[] args) {
                if (args.length == 0) {
                    return null;
                }

                StringBuilder sb = new StringBuilder();

                if (args[0].equalsIgnoreCase("gmaccess")) {
                    try {
                        //   GMAccessesParser.getInstance().reload();
                        //   if (GMAccessesHolder.getInstance().size() > 0) {
                        //       sb.append("GMAccesses (").append(GMAccessesHolder.getInstance().size()).append(") reloaded successfuly!");
                        //      } else {
                        //        sb.append("GMAccesses reload failed! Keeping last version ...");
                        //    }
                    } catch (Exception e) {
                        return sb.append("Error GMAccess reload!\n").toString();
                    }
                }

                return sb.toString();
            }
        });
    }

    @Override
    public Set<TelnetCommand> getCommands() {
        return _commands;
    }
}
