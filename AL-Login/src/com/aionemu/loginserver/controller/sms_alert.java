package com.aionemu.loginserver.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 * @author Alex
 */
public class sms_alert {

    private static String text = "Я лайт хули";
    private final String byteHandId = "6190";
    private final String bytehandKey = "7BA5654A67BE273D";
    private static String bytehandFrom = "Aion-Light";
    private final String encodeCharset = "UTF-8";
    private static String phone = "380630535439";

    public static void setSign(String sign) {
        bytehandFrom = sign;
    }

    public static String getSign() {
        return bytehandFrom;
    }

    public static String Text(String texts) {
        return text = texts;
    }

    public static String getText() {
        return text;
    }

    public static String setPhone(String phon) {
        return phone = phon;
    }

    public static String getPhone() {
        return phone;
    }

    public boolean sendSms(final String to, final String text) {
        try {
            new URL("http://bytehand.com:3800/send?id=" + byteHandId + "&key=" + bytehandKey + "&to=" + URLEncoder.encode(to, encodeCharset) + "&from=" + URLEncoder.encode(bytehandFrom, encodeCharset) + "&text=" + URLEncoder.encode(text, encodeCharset)).getContent();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(final String[] args) throws MalformedURLException, IOException {
        new sms_alert().sendSms(getPhone(), getText());
    }
}
