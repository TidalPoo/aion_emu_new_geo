package com.aionemu.gameserver.network.telnet.commands;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.CacheConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.DropConfig;
import com.aionemu.gameserver.configs.main.EnchantsConfig;
import com.aionemu.gameserver.configs.main.FallDamageConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.GroupConfig;
import com.aionemu.gameserver.configs.main.LegionConfig;
import com.aionemu.gameserver.configs.main.PeriodicSaveConfig;
import com.aionemu.gameserver.configs.main.PricesConfig;
import com.aionemu.gameserver.configs.main.RateConfig;
import com.aionemu.gameserver.configs.main.ShutdownConfig;
import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.configs.network.IPConfig;
import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.network.telnet.TelnetCommand;
import com.aionemu.gameserver.network.telnet.TelnetCommandHolder;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Amiko.Yuki
 */
public class TelnetConfig implements TelnetCommandHolder {

    private Set<TelnetCommand> _commands = new LinkedHashSet<>();

    public TelnetConfig() {
        _commands.add(new TelnetCommand("config", "cfg") {
            @Override
            public String getUsage() {
                return "config <set | show> <config name> <property> <new value>";
            }

            @Override
            public String handle(String[] args) {
                if (args.length == 0 || args[0].isEmpty()) {
                    return null;
                }

                String command = "";
                if (args.length == 3) {
                    //show
                    command = args[0];
                    if (!"show".equalsIgnoreCase(command)) {
                        return "config <set | show> <config name> <property> <new value>";
                    }
                } else if (args.length == 4) {
                    //set
                    command = args[0];
                    if (!"set".equalsIgnoreCase(command)) {
                        return "config <set | show> <config name> <property> <new value>";
                    }
                } else {
                    return "config <set | show> <config name> <property> <new value>";
                }

                Class<?> classToMofify = null;
                String className = args[1];

                if ("admin".equalsIgnoreCase(className)) {
                    classToMofify = AdminConfig.class;
                } else if ("cache".equalsIgnoreCase(className)) {
                    classToMofify = CacheConfig.class;
                } else if ("custom".equalsIgnoreCase(className)) {
                    classToMofify = CustomConfig.class;
                } else if ("group".equalsIgnoreCase(className)) {
                    classToMofify = GroupConfig.class;
                } else if ("gs".equalsIgnoreCase(className)) {
                    classToMofify = GSConfig.class;
                } else if ("legion".equalsIgnoreCase(className)) {
                    classToMofify = LegionConfig.class;
                } else if ("ps".equalsIgnoreCase(className)) {
                    classToMofify = PeriodicSaveConfig.class;
                } else if ("rate".equalsIgnoreCase(className)) {
                    classToMofify = RateConfig.class;
                } else if ("shutdown".equalsIgnoreCase(className)) {
                    classToMofify = ShutdownConfig.class;
                } /*else if ("task".equalsIgnoreCase(className)) {
                 classToMofify = TaskManagerConfig.class;
                 }*/ else if ("ip".equalsIgnoreCase(className)) {
                    classToMofify = IPConfig.class;
                } else if ("network".equalsIgnoreCase(className)) {
                    classToMofify = NetworkConfig.class;
                } else if ("enchants".equalsIgnoreCase(className)) {
                    classToMofify = EnchantsConfig.class;
                } else if ("fd".equalsIgnoreCase(className)) {
                    classToMofify = FallDamageConfig.class;
                } /*else if ("nm".equalsIgnoreCase(className)) {
                 classToMofify = NpcMovementConfig.class;
                 }*/ else if ("prices".equalsIgnoreCase(className)) {
                    classToMofify = PricesConfig.class;
                } else if ("siege".equalsIgnoreCase(className)) {
                    classToMofify = SiegeConfig.class;
                } else if ("drop".equalsIgnoreCase(className)) {
                    classToMofify = DropConfig.class;
                } /*else if ("event".equalsIgnoreCase(className)) {
                 classToMofify = EventConfig.class;
                 }*/

                if (command.equalsIgnoreCase("show")) {
                    String fieldName = args[2];
                    Field someField;
                    try {
                        someField = classToMofify.getDeclaredField(fieldName.toUpperCase());
                        return "Current value is " + someField.get(null);
                    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                        return "Something really bad happend :)";
                    }
                } else if (command.equalsIgnoreCase("set")) {
                    String fieldName = args[2];
                    String newValue = args[3];
                    if (classToMofify != null) {
                        Field someField;
                        try {
                            someField = classToMofify.getDeclaredField(fieldName.toUpperCase());
                            Class<?> classType = someField.getType();
                            if (classType == String.class) {
                                someField.set(null, newValue);
                            } else if (classType == int.class || classType == Integer.class) {
                                someField.set(null, Integer.parseInt(newValue));
                            } else if (classType == Boolean.class || classType == boolean.class) {
                                someField.set(null, Boolean.valueOf(newValue));
                            } else if (classType == Float.class || classType == float.class) {
                                someField.set(null, Float.valueOf(newValue));
                            }
                            return "Property changed";
                        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                            return "Something really bad happend :)";
                        }
                    }
                }
                return null;
            }
        });
    }

    @Override
    public Set<TelnetCommand> getCommands() {
        return _commands;
    }
}
