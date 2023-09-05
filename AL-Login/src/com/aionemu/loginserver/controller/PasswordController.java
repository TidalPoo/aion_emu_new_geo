/*
 * AionLight Project
 *
 */
package com.aionemu.loginserver.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 *
 * @author Alex
 */
public class PasswordController {

    public static void encodePass(String name, String password) {
        try {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader("./config/aionlight/LightPassword.txt"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (line.equals("Логин: " + name + " | Пароль: " + password) || line.startsWith("#")) {
                        return;
                    }
                }
            } catch (IOException e) {
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                    }
                }
            }
        } catch (Exception e) {
        }
        PrintWriter file = null;
        try {
            file = new PrintWriter(new OutputStreamWriter(new FileOutputStream("./config/aionlight/LightPassword.txt", true)));
        } catch (FileNotFoundException e) {
            return;
        }
        file.println("Логин: " + name + " | Пароль: " + password);
        file.close();
    }
}
