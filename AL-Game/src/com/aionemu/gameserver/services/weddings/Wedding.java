package com.aionemu.gameserver.services.weddings;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import java.sql.Timestamp;

/**
 * @author Alex
 */
public class Wedding {

    private final int playerId;
    private int partnerId;
    private Timestamp lastOnline;
    private Timestamp timeTeleport;
    private Timestamp timeWedding;
    private int worldId;
    private String text;
    private Timestamp dataWedding;
    private String partnerText;
    private String partnerName;
    private boolean update = false;
    private final boolean partner;
    private Player p;

    public Wedding(int player_id, int partner_id, Timestamp lastOnline, int lastWorldId, Timestamp timeTeleport, Timestamp timeWedding, String text, String partnerText, Timestamp dataWedding, String partnerName, boolean partner) {
        this.playerId = player_id;
        this.partnerId = partner_id;
        this.lastOnline = lastOnline;
        this.worldId = lastWorldId;
        this.timeTeleport = timeTeleport;
        this.timeWedding = timeWedding;
        this.text = text;
        this.partnerText = partnerText;
        this.dataWedding = dataWedding;
        this.partnerName = partnerName;
        this.partner = partner;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getPartnerWorldId() {
        return worldId;
    }

    public void setPartnerWorldId(int id) {
        this.worldId = id;
    }

    public Timestamp getPartnerLastOnline() {
        return this.lastOnline;
    }

    public void setPartnerLastOnline(Timestamp time) {
        this.lastOnline = time;
    }

    public Timestamp getTimeTp() {
        return timeTeleport;
    }

    public Timestamp getTimeWedding() {
        return timeWedding;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public String getText() {
        return text;
    }

    public Timestamp getDataWedding() {
        return dataWedding;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public void setTimeWedding(Timestamp time) {
        this.timeWedding = time;
        this.update = true;
    }

    public void setTimeTp(Timestamp time) {
        this.timeTeleport = time;
        this.update = true;
    }

    public void setText(String text) {
        this.text = text;
        this.update = true;
    }

    public void setDataWedding(Timestamp dw) {
        this.dataWedding = dw;
    }

    public void setPartnerText(String t) {
        this.partnerText = t;
        this.update = true;
    }

    public String getPartnerText() {
        return this.partnerText;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String name) {
        this.partnerName = name;
    }

    public boolean isUpdate() {
        return update;
    }

    public boolean isPartner() {
        return partner;
    }

    public boolean isOnline() {
        return p != null;
    }

    public void setPartner(Player player) {
        this.p = player;
    }

    public Player getPartner() {
        return p;
    }
}
