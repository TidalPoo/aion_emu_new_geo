/*
 * AionLight project
 */
package com.aionemu.gameserver.model.gminterface;

import com.aionemu.gameserver.model.templates.gminterface.ResultNpcTemplate;

/**
 *
 * @author Alex
 */
public class NpcTemp {

    protected ResultNpcTemplate template;

    public NpcTemp() {
    }

    public NpcTemp(ResultNpcTemplate template) {
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

    public String getTitle() {
        return template.getTitle();
    }

    public ResultNpcTemplate getTemplate() {
        return template;
    }
}
