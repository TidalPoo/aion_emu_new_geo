/*
 * SAO project
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
@XmlType(name = "npcs")
public class ResultNpcTemplate {

    @XmlAttribute(name = "npc_id")
    protected int id;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "rus_name")
    protected String rusname;
    @XmlAttribute(name = "title")
    protected String title;
    @XmlAttribute(name = "gender")
    protected String gender;
    @XmlAttribute(name = "pc_type")
    protected String pc_type;

    /**
     * @return the location id
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return the world id
     */
    public String getName() {
        return this.name;
    }

    public String getRusname() {
        return rusname;
    }

    public String getTitle() {
        return title;
    }

    public String getGender() {
        return gender;
    }

    public String getPcType() {
        return pc_type;
    }

    public boolean isFemale() {
        return gender.equals("female");
    }

    public boolean isMale() {
        return gender.equals("male");
    }

    public boolean isNoGender() {
        return gender.equals("no_gender");
    }
}
