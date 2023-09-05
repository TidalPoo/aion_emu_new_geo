/*
 * AionLight project
 */
package com.aionemu.gameserver.model.gminterface;

import com.aionemu.gameserver.model.templates.gminterface.ResultItemTemplate;

/**
 *
 * @author Alex
 */
public class ItemTemp {

    protected ResultItemTemplate template;

    public ItemTemp() {
    }

    public ItemTemp(ResultItemTemplate template) {
        this.template = template;
    }

    public int getId() {
        return template.getId();
    }

    public String getName() {
        return template.getName();
    }

    public String getRusname() {
        return template.getRusname();
    }

    public ResultItemTemplate getTemplate() {
        return template;
    }
}
