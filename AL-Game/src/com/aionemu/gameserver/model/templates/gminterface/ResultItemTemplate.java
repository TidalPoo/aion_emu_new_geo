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
@XmlType(name = "items")
public class ResultItemTemplate {

    @XmlAttribute(name = "item_id")
    protected int id;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "rus_name")
    protected String rusname;

    /**
     * @return the itemid
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return the Name
     */
    public String getName() {
        return this.name;
    }

    public String getRusname() {
        return rusname;
    }
}
