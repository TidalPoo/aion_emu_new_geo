/*
 * SAO Project
 */
package com.aionemu.gameserver.services.weddings;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.cache.HTMLCache;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.HTMLConfig;
import com.aionemu.gameserver.configs.main.WeddingsConfig;
import com.aionemu.gameserver.dao.WeddingDAO;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TOLL_INFO;
import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_ACCOUNT_TOLL_INFO;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.ColorUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.world.World;
import java.sql.Timestamp;
import java.util.Iterator;
import org.apache.commons.lang.time.DurationFormatUtils;

/**
 * @author: Alex
 *
 */
public class WeddingService {

    // Время в часах
    private final long timeWedding = 24;// Откат на свадьбу
    private final long timeTeleport = 1;// Откат на телепорт к партнеру
    private final String[] SuitItems = WeddingsConfig.WEDDINGS_SUITS.split(",");//110900135;// Костюм для свадьбы

    public static WeddingService getInstance() {
        return SingletonHolder.instance;
    }
    private final long marryToll = 300;
    private final long unMarryToll = 300;

    public boolean decToll(Player p, long cnt) {
        if (cnt == 0) {
            return true;
        }
        long rfinal = (p.getPlayerAccount().getToll() - cnt);
        if (LoginServer.getInstance().sendPacket(new SM_ACCOUNT_TOLL_INFO(rfinal, p.getAcountName()))) {
            p.getPlayerAccount().setToll(rfinal);
            PacketSendUtility.sendPacket(p, new SM_TOLL_INFO(rfinal));
            return true;
        }
        return false;
    }

    private boolean WeddingsItem(Player player1, Player player) {
        if (WeddingsConfig.WEDDINGS_SUIT_ENABLE) {
            for (String suit : SuitItems) {
                int suitId = Integer.parseInt(suit);
                if (player.getEquipment().getEquippedItemsByItemId(suitId).isEmpty()) {
                    PacketSendUtility.sendMessage(player, "Необходимо надеть [item:" + suitId + "].Если у Вас нет необходимого костюма, загляните в игровой магазин.");
                    return false;
                }
                if (player1.getEquipment().getEquippedItemsByItemId(suitId).isEmpty()) {
                    PacketSendUtility.sendMessage(player, "У Вашего партнера нет необходимого костюма.Необходимо надеть [item:" + suitId + "].");
                    PacketSendUtility.sendMessage(player1, "Для вступления в брак, необходимо надеть [item:" + suitId + "].Если у Вас нет необходимого костюма, загляните в игровой магазин.");
                    return false;
                }
            }
        }
        return true;
    }

    public void text(Player player, String text) {
        player.getWedding().setText(text);
        DAOManager.getDAO(WeddingDAO.class).update(player.getWedding());
    }

    private static class SingletonHolder {

        protected static final WeddingService instance = new WeddingService();
    }

    public WeddingService() {
    }

    public void marry(Player player, Player partner) {
        if (!decToll(player, marryToll)) {
            PacketSendUtility.sendMessage(player, "Не удалось списать Game Point за свадьбу");
            return;
        }
        Wedding wedding = new Wedding(player.getObjectId(), partner.getObjectId(), null, partner.getWorldId(), null,
                new Timestamp(System.currentTimeMillis() + (timeWedding * 1000 * 60 * 60)), "Привет любимка!:3", "Привет любимка!:3", new Timestamp(System.currentTimeMillis()), partner.getName(), false);
        wedding.setPartner(partner);
        player.setWedding(wedding);
        Wedding weddingPartner = new Wedding(partner.getObjectId(), player.getObjectId(), null, player.getWorldId(), null,
                new Timestamp(System.currentTimeMillis() + (timeWedding * 1000 * 60 * 60)), "Привет любимка!:3", "Привет любимка!:3", new Timestamp(System.currentTimeMillis()), player.getName(), true);
        weddingPartner.setPartner(player);
        partner.setWedding(weddingPartner);
        DAOManager.getDAO(WeddingDAO.class).insertWedding(wedding);
        player.setPlayerSearch(null);
        player.setNewName(null);
        partner.setPlayerSearch(null);
        partner.setNewName(null);
        respawn(player);
        respawn(partner);
        if (HTMLConfig.ENABLE_HTML_WELCOME) {
            showInfoWedding(player);
            showInfoWedding(partner);
        }
        String message = "Свадьба " + player.getName() + " с " + partner.getName() + "!";
        Iterator<Player> iter = World.getInstance().getPlayersIterator();
        while (iter.hasNext()) {
            PacketSendUtility.sendYellowMessageOnCenter(iter.next(), message);
        }
    }

    private void respawn(Player player) {
        PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
        World.getInstance().despawn(player);
        World.getInstance().spawn(player);
    }

    public void unmarry(Player player) {
        if (!decToll(player, unMarryToll)) {
            PacketSendUtility.sendMessage(player, "Не удалось списать Game Point за развод");
            return;
        }
        int partnerId = player.getWedding().getPartnerId();
        String name = player.getWedding().getPartnerName();
        DAOManager.getDAO(WeddingDAO.class).removeWedding(player.getObjectId(), partnerId);
        DAOManager.getDAO(WeddingDAO.class).insertToLog(player.getWedding());
        player.setWedding(null);
        player.setPlayerSearch(null);
        player.setNewName(null);
        respawn(player);
        if (player.getWedding() != null && player.getWedding().isOnline()) {
            Player partner = player.getWedding().getPartner();
            partner.setWedding(null);
            partner.setPlayerSearch(null);
            partner.setNewName(null);
            respawn(partner);
        }

        String message = player.getName() + " разводится с " + name + "!";
        Iterator<Player> iter = World.getInstance().getPlayersIterator();
        while (iter.hasNext()) {
            PacketSendUtility.sendYellowMessageOnCenter(iter.next(), message);
        }
    }

    public boolean canMarry(Player player, String partnerName) {
        Player partner = World.getInstance().findPlayer(partnerName);
        if (partner == null) {
            PacketSendUtility.sendMessage(player, (new StringBuilder()).append("Персонаж ").append(partnerName).append(" не в онлайне.").toString());
            return false;
        } else if (!timeWedding(player)) {
            PacketSendUtility.sendMessage(player, "Свадьба доступна раз в " + timeWedding + " часа!\n Осталось времени: " + DurationFormatUtils.formatDuration(player.getWedding().getTimeWedding().getTime() - System.currentTimeMillis(), "HHч mm мин") + "");
            return false;
        } else if (!timeWedding(partner)) {
            PacketSendUtility.sendMessage(player, "У Вашего партнера откат на свадьбу " + timeWedding + " часа!\n Осталось: " + DurationFormatUtils.formatDuration(player.getWedding().getTimeWedding().getTime() - System.currentTimeMillis(), "HHч mm мин") + "");
            return false;
        } else if (player.isMarried()) {
            PacketSendUtility.sendMessage(player, "Вы уже обручены!");
            return false;
        } else if (partner.isMarried()) {
            PacketSendUtility.sendMessage(player, (new StringBuilder()).append(partnerName).append(" уже обручены!").toString());
            return false;
        } else if (player.getRace() != partner.getRace()) {
            PacketSendUtility.sendMessage(player, "Вы не можете обручится с персонажем другой расы!");
            return false;
        } else if (player == partner) {
            PacketSendUtility.sendMessage(player, "Нельзя обручится с самим собой!");
            return false;
        } else if (player.getGender() == partner.getGender() && !player.getGender().isFemale()) {
            PacketSendUtility.sendMessage(player, "Нельзя совершать мужские однополые свадьбы!");
            return false;
        } else if (player.getPlayerAccount().getToll() < marryToll) {
            PacketSendUtility.sendMessage(player, "Для вступления в брак необходимо " + marryToll + " GP");
            return false;
        } else {
            return WeddingsItem(partner, player);
        }
    }

    public void showInfo(Player player) {
        StringBuilder sb = new StringBuilder();

    }

    public void showInfoWedding(Player player) {
        //PacketSendUtility.sendMessage(player, "Информация о вашем семейном положении появилась в окне голосования!");
        //HTMLService.showHTML(player, getHTML(textInfo(player)));
    }

    public String getHTML(String message) {
        String context = HTMLCache.getInstance().getHTML("new_weddings.xhtml");
        context = context.replace("%message%", message == null ? " " : message);
        return context;
    }

    public boolean isMarriedTo(Player player, Player partner) {
        return (player.getWedding() != null && player.getWedding().getPartnerId() == partner.getObjectId());
    }

    public void summon(Player player) {
        if (!player.isMarried()) {
            PacketSendUtility.sendMessage(player, "Вы не обручены!");
            return;
        }
        String name = player.getWedding().getPartnerName();
        Player partner = World.getInstance().findPlayer(name);
        if (partner == null) {
            PacketSendUtility.sendMessage(player, "Ваш партнер не в онлайне.");
            return;
        }
        if (partner.isInPrison()) {
            PacketSendUtility.sendMessage(player, "Ваш партнер в тюрьме.");
            return;
        }
        if (!timeTp(player)) {
            PacketSendUtility.sendMessage(player, "Призвать партнера можно раз в " + timeTeleport + " час!\nОсталось времени: " + DurationFormatUtils.formatDuration(player.getWedding().getTimeTp().getTime() - System.currentTimeMillis(), "HHч mmмин") + "");
            return;
        }
        PacketSendUtility.sendMessage(player, "Ожидание ответа от " + name + " ...");
        RequestResponseHandler rh = new RequestResponseHandler(player) {
            @Override
            public void denyRequest(Creature player, Player partner) {
                PacketSendUtility.sendMessage((Player) player, partner.getName() + " отказывается от телепортации к Вам.");
            }

            @Override
            public void acceptRequest(Creature player, Player partner) {
                teleport(partner, (Player) player);
                ((Player) player).getWedding().setTimeTp(new Timestamp(System.currentTimeMillis() + (timeTeleport * 1000 * 60 * 60)));
            }
        };
        partner.getResponseRequester().putRequest(902247, rh);
        PacketSendUtility.sendPacket(partner, new SM_QUESTION_WINDOW(902247, 0, 0, player.getName() + " просит вас прийти на помощь. Телепортироваться?"));
    }

    public void teleport(Player player) {
        if (!player.isMarried()) {
            PacketSendUtility.sendMessage(player, "Вы не обручены!");
            return;
        }
        String name = player.getWedding().getPartnerName();
        Player partner = World.getInstance().findPlayer(name);
        if (partner == null) {
            PacketSendUtility.sendMessage(player, "Ваш партнер не в онлайне.");
            return;
        }
        if (partner.isInPrison()) {
            PacketSendUtility.sendMessage(player, "Ваш партнер находится в тюрьме. Телепортация невозможна!");
            return;
        }
        if (timeTp(player)) {
            TeleportService2.teleportTo(player, partner.getWorldId(), partner.getInstanceId(), partner.getX(), partner.getY(), partner.getZ(), partner.getHeading());
            player.getWedding().setTimeTp(new Timestamp(System.currentTimeMillis() + (timeTeleport * 1000 * 60 * 60)));
        } else {
            PacketSendUtility.sendMessage(player, "Вы можете совершать телепорт только раз в " + timeTeleport + " час!\n У Вас осталось: " + DurationFormatUtils.formatDuration(player.getWedding().getTimeTp().getTime() - System.currentTimeMillis(), "mmмин") + "");
            return;
        }
        teleport(player, partner);
    }

    public boolean timeTp(Player admin) {
        if (!admin.isMarried() || admin.getWedding().getTimeTp() == null) {
            return true;
        }
        return (admin.getWedding().getTimeTp().getTime() - System.currentTimeMillis()) / 1000 <= 0;
    }

    public boolean timeWedding(Player admin) {
        if (!admin.isMarried() || admin.getWedding().getTimeWedding() == null) {
            return true;
        }
        return (admin.getWedding().getTimeWedding().getTime() - System.currentTimeMillis()) / 1000 <= 0;
    }

    public void teleport(Player fromPlayer, Player toPlayer) {
        TeleportService2.teleportTo(fromPlayer, toPlayer.getWorldId(), toPlayer.getInstanceId(), toPlayer.getX(), toPlayer.getY(), toPlayer.getZ(), toPlayer.getHeading());
    }

    public void marry(Player player, String... params) {
        if (params.length < 2) {
            PacketSendUtility.sendMessage(player, "Syntax: .wedding marry ник персонажа");
            return;
        }
        final String partnerName = CustomConfig.ENABLE_CONVERT_NAME ? Util.convertName(params[1]) : params[1];

        if (!canMarry(player, partnerName)) {
            return;
        }
        RequestResponseHandler rh = new RequestResponseHandler(player) {
            @Override
            public void denyRequest(Creature player, Player partner) {
            }

            @Override
            public void acceptRequest(Creature player, Player partner) {
                startMarryTask((Player) player, partnerName);
            }
        };
        player.getResponseRequester().putRequest(902247, rh);
        PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(902247, 0, 0, "Вы действительно желаете обручится с персонажем " + partnerName + " ?"));
    }

    public void unMarry(Player player) {
        if (!player.isMarried()) {
            PacketSendUtility.sendMessage(player, "Вы не обручены!");
            return;
        }

        if (player.getPlayerAccount().getToll() < unMarryToll) {
            PacketSendUtility.sendMessage(player, "Для развода требуется " + unMarryToll + " GP");
            return;
        }

        RequestResponseHandler rh = new RequestResponseHandler(player) {
            @Override
            public void denyRequest(Creature player, Player partner) {
            }

            @Override
            public void acceptRequest(Creature player, Player partner) {
                unmarry(partner);
            }
        };
        player.getResponseRequester().putRequest(902247, rh);
        PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(902247, 0, 0, "Вы действительно желаете расторгнуть брак с персонажем " + player.getWedding().getPartnerName() + "  ?"));
    }

    public void startMarryTask(Player player, String partnerName) {
        Player partner = World.getInstance().findPlayer(partnerName);
        PacketSendUtility.sendMessage(player, "Ожидание ответа от " + partnerName + " ...");

        RequestResponseHandler rh = new RequestResponseHandler(player) {
            @Override
            public void denyRequest(Creature player, Player partner) {
                PacketSendUtility.sendMessage((Player) player, partner.getName() + " отказывается от свадьбы с вами.");
            }

            @Override
            public void acceptRequest(Creature player, Player partner) {
                Player p = (Player) player;
                marry(p, partner);
            }
        };
        partner.getResponseRequester().putRequest(902247, rh);
        PacketSendUtility.sendPacket(partner, new SM_QUESTION_WINDOW(902247, 0, 0, player.getName() + " предлагает вам обручиться. Вы согласны?"));
    }

    public boolean WeddingCommand(Player player, String text) {
        if (player.getWedding().isOnline()) {
            Player partner = player.getWedding().getPartner();
            PacketSendUtility.sendPacket(partner, new SM_MESSAGE(player, text, ChatType.WHISPER));
            PacketSendUtility.sendMessage(player, "Сообщение отправлено вашей половинке");
        } else {
            PacketSendUtility.sendMessage(player, "Ваш партнер не в сети!");
        }
        return true;
    }

    public void getRenamePartner(Player player) {
        if (player.isMarried()) {
            if (player.getWedding().isOnline()) {
                Player partner = player.getWedding().getPartner();
                partner.getWedding().setPartnerName(player.getName());
                PacketSendUtility.sendPacket(partner, new SM_PLAYER_INFO(partner, false));
            }
        }
    }

    public void onEnterWorld(Player player) {
        //DAOManager.getDAO(WeddingDAO.class).loadPartner(player);
        if (player.isMarried()) {
            if (player.getWedding().isOnline()) {
                Player partner = player.getWedding().getPartner();
                //partner.getWedding().setPartner(player);
                PacketSendUtility.sendMessage(partner, "\ue020" + ColorUtil.convertTextToColor(player.getName() + " входит в игру", "128 0 128") + " \ue020");
                PacketSendUtility.sendMessage(player, "\ue020" + ColorUtil.convertTextToColor(partner.getName() + " в сети ", "128 0 128") + "\ue020");
            }
            String text = player.getWedding().getPartnerText();
            if (text != null && !text.isEmpty()) {
                PacketSendUtility.sendYellowMessageOnCenter(player, "\ue020" + ColorUtil.convertTextToColor(player.getWedding().getPartnerName(), "0 255 0") + ": " + ColorUtil.convertTextToColor(text, "128 0 128"));
            }
        }
    }

    public void dofinal(Player player) {
        if (player.isMarried()) {
            if (player.getWedding().isOnline()) {
                Player partner = player.getWedding().getPartner();
                partner.getWedding().setPartner(null);
                PacketSendUtility.sendMessage(partner, "\ue020" + ColorUtil.convertTextToColor(player.getName() + " выходит из игры", "128 0 128"));
            }
            if (player.getWedding().isUpdate()) {
                DAOManager.getDAO(WeddingDAO.class).update(player.getWedding());
            }
        }
    }

    public static String getRealWeddingsName(String name) {
        int index = name.indexOf('\ue020');
        if (index == -1) {
            return name;
        }
        String s = name.substring(index + 1).intern();
        return name.replaceAll("\ue020" + s, "");
    }
}
