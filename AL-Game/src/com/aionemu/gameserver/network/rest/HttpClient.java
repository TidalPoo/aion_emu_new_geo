package com.aionemu.gameserver.network.rest;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;

public class HttpClient {
    private final Client geoClient;
    private static HttpClient INSTANCE;

    private HttpClient() {
        geoClient = ClientBuilder.newClient();
    }

    public static HttpClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HttpClient();
        }
        return INSTANCE;
    }

    public Client getGeoClient() {
        return geoClient;
    }

}
