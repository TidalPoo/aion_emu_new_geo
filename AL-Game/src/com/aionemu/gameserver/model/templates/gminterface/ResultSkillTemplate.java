/*
 * AionLight project
 */
package com.aionemu.gameserver.model.templates.gminterface;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Alex
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "skills")
public class ResultSkillTemplate {

    @XmlAttribute(name = "skill_id")
    protected int id;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "rus_name")
    protected String rusname;
    @XmlAttribute(name = "message")
    protected String message;

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getRusname() {
        return rusname;
    }

    public String getMessage() {
        return message;
    }
}
