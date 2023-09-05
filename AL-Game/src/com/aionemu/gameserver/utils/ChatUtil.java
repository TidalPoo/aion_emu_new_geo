/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.utils;

import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author antness
 * @modified Alex - added charname, where, kvalue, color getRealAdminName() and
 * fix position in map
 */
public class ChatUtil {

    public static String position(String label, WorldPosition pos) {
        return position(label, pos.getMapId(), pos.getX(), pos.getY(), pos.getZ());
    }

    public static String position(String label, long worldId, float x, float y, float z) {
        return String.format("[pos:%s;%d %f %f %f 0]", label, worldId, x, y, z);
    }

    public static String item(long itemId) {
        return String.format("[item: %d]", itemId);
    }

    public static String recipe(long recipeId) {
        return String.format("[recipe: %d]", recipeId);
    }

    public static String charname(String name) {
        return String.format("[charname:%s;R G B]", name);
    }

    public static String quest(int questId) {
        return String.format("[quest: %d]", questId);
    }

    public static String where(String name, long npcId) {
        return String.format("[where:%s;%d]", name, npcId);
    }

    public static String color(String message, int R, int G, int B) {
        return String.format("[color:%s;%d %d %d]", message, R, G, B);
    }

    public static String color(String message, String color) {
        ColorEnum ce = ColorEnum.getFor(color);
        if (ce == null) {
            return "";
        }
        return String.format("[color:%s;%s]", message, ce.getRgb());
    }

    public static String kvalue(String title, String message) {
        return String.format("[kvalue:%s;%s;str]", title, message);
    }

    public static String tooltip(int textIdTo, int textIdMouse) {
        return String.format("[tooltip:%d;%d]", textIdTo, textIdMouse);
    }

    public static String webLinkForum() {
        //Форум
        return "[web:http://forum.mmo-sao.ru/;uVTymudTuim6rl3REq5h20VRg5mfKVnkcIKURP7j+0JFUYOZnylZ5HCClET+4/tCRVGDmZ8pWeRwgpRE/uP7QiSOn+0fFzVAirjRrMZhAFY=]";
    }

    public static String webLinkForumMmoSaoRu() {
        //Форум
        return "[web:http://forum.mmo-sao.ru/;COFWzhq5DihKhOz5Yp/gldTSxz8EsRSyMhta2XMWcujwQPhQY/hOMHr5L9JDmmVORVGDmZ8pWeRwgpRE/uP7Qi0pIKfOWOwbSFXH5cbuCKo=]";
    }

    public static String webLinkSite() {
        //сайт
        return "[web:http://mmo-sao.ru/;Ukot7KUxRYub9XGG4oHQKEVRg5mfKVnkcIKURP7j+0JFUYOZnylZ5HCClET+4/tCRVGDmZ8pWeRwgpRE/uP7QiM6x/Fv6ewdwcXkjNCzSAM=]";
    }

    public static String webLinkMmoSaoRu() {
        //сайт
        return "[web:http://mmo-sao.ru/;BoJ5TdVuJRByCTkqLcIdWIWcw7RUbo/km41ax6mzzYZFUYOZnylZ5HCClET+4/tCRVGDmZ8pWeRwgpRE/uP7QsF5+7bZelCS152qojN8VN8=]";
    }

    public static String webLinkReplenishBalanceAccount() {
        //<Пополнить баланс аккаунта>
        return "[web:http://mmo-sao.ru/balance/;wELeF7id6q8OM1TkGmzmoLBSseHnJIE5LKfT/qzcSrW+yQjocjeoZI2D12ensSpIYkiqJX6FR+oB+QXMLMx2+uIOwCA4Rrxk/rdYO4KYSys=]";
    }

    public static String webLinkAddFunds() {
        //Пополнить счет
        return "[web:http://mmo-sao.ru/balance/;wELeF7id6q8OM1TkGmzmoAotwD1VfjWtA69kKOAERo9FUYOZnylZ5HCClET+4/tCRVGDmZ8pWeRwgpRE/uP7Qpf+udaSM3NpD5sB1qO8KWI=]";
    }

    public static String webLinkReplenishBalance() {
        //Пополнение баланса
        return "[web:http://mmo-sao.ru/balance/;wELeF7id6q8OM1TkGmzmoEAyI2UFsL0yPXk3eZth7E9599dg5W383YftBC8PqvN/RVGDmZ8pWeRwgpRE/uP7QnBV3HMjGMCSyuVRzh3Ip5E=]";
    }

    public static String webLinkSAO() {
        //SAO
        return "[web:http://mmo-sao.ru/;u43PV2z5utHkGmi7bG59JkVRg5mfKVnkcIKURP7j+0JFUYOZnylZ5HCClET+4/tCRVGDmZ8pWeRwgpRE/uP7QhCqO+2fNdgaKe/1n9cr7UM=]";
    }

    public static String webLinkServerAionOnline() {
        return "[web:http://mmo-sao.ru/;dXxNl8UpyOt/lMBtr/HP/mkVoCdaod6UCdSKT0s6srgtOkdmUAIJwoZFmpT1avDvRVGDmZ8pWeRwgpRE/uP7QiTn/pEtjsyitn7uMjLTcPc=]";
    }

    public static String webLinkAionDatabaseNet() {
        return "[web:http://aiondatabase.net/ru/;C76521ZRO0R9xu3dTTCFrKf/MLUBOPTbSAiKpc/kx8GWDZ83uLNBnpkSriew5uHJRVGDmZ8pWeRwgpRE/uP7QgErpdjhBH5vNR2QWV4yr64=]";
    }

    public static String webLinkAIDB() {
        return "[web:http://aidb.ru/;UyI8kuRmE2Lb5mAr/oVYWS1r1tTnmFXWppJYSxUUU25FUYOZnylZ5HCClET+4/tCRVGDmZ8pWeRwgpRE/uP7Qi0pIKfOWOwbSFXH5cbuCKo=]";
    }

    public static String getCountToText(String count) {
        String c = count;
        //todo
        return c;
    }

    public static String getRealAdminName(String name) {
        int index = name.lastIndexOf(' ');
        if (index == -1) {
            return name;
        }
        return name.substring(index + 1);
    }
}
