package com.aionemu.gameserver.model.templates.survey;

import javolution.util.FastMap;

/**
 * @author KID
 */
public class SurveyItem {

    public int ownerId;
    public int uniqueId;
    public int itemId;
    public long count;
    public FastMap<Integer, Long> items;
    public int multiCount;

    public String html, radio, title;
    public SurveyActionType actionType;
    public Object action;
}
