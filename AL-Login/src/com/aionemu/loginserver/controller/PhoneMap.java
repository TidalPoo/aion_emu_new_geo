/*aion-light.ru*/
package com.aionemu.loginserver.controller;

/**
 *
 * @author Alex
 */
public class PhoneMap {

    private String name;
    private String password;
    private String phone;
    private int id;

    public PhoneMap(int id, String name, String password, String phone) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String setName(String name) {
        return this.name = name;
    }

    public String setPhone(String phone) {
        return this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public int setId(int id) {
        return this.id = id;
    }

    public String setPassword(String password) {
        return this.password = password;
    }
}
