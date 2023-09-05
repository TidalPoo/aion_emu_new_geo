/*
 * SAO Project
 */
package com.aionemu.gameserver.model.gminterface;

import com.aionemu.gameserver.model.templates.gminterface.ResultSkillTemplate;

/**
 *
 * @author Alex
 */
public class SkillTemp {

    protected ResultSkillTemplate template;

    public SkillTemp() {
    }

    public SkillTemp(ResultSkillTemplate template) {
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

    public String getMessage() {
        return template.getMessage();
    }

    public ResultSkillTemplate getTemplate() {
        return template;
    }
}
