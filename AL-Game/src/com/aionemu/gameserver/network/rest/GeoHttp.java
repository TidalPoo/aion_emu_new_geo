package com.aionemu.gameserver.network.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

public class GeoHttp {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String target = "https://aion-phys.syntax-gc.org";

    public static float getFirstCollision(int worldId, float x, float y, float z) {
        WebTarget webTarget = HttpClient.getInstance().getGeoClient().target(target)
                .path("geo").path("getFirstCollision")
                .queryParam("worldId", worldId)
                .queryParam("x", x)
                .queryParam("y", y)
                .queryParam("z", z);

        Response response = webTarget.request().get();

        if (response.getStatus() == 200) {
            JsonNode jsonData;
            try {
                jsonData = mapper.readTree(response.readEntity(String.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            String collideObjName = jsonData.get("objName").asText();
            float collideX = jsonData.get("x").floatValue();
            float collideY = jsonData.get("y").floatValue();
            float collideZ = jsonData.get("z").floatValue();
            return collideZ;
        } else {
            System.out.println("Something wrong!!! with uri: " + webTarget.getUri() + " and response status: " + response.getStatus());
        }
        return 0;
    }

    public static float getFirstZ(int worldId, float x, float y) {
        WebTarget webTarget = HttpClient.getInstance().getGeoClient().target(target)
                .path("geo").path("getFirstZ")
                .queryParam("worldId", worldId)
                .queryParam("x", x)
                .queryParam("y", y);

        Response response = webTarget.request().get();

        if (response.getStatus() == 200) {
            JsonNode jsonData;
            try {
                jsonData = mapper.readTree(response.readEntity(String.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            String collideObjName = jsonData.get("objName").asText();
            float collideX = jsonData.get("x").floatValue();
            float collideY = jsonData.get("y").floatValue();
            float collideZ = jsonData.get("z").floatValue();
            return collideZ;
        } else {
            System.out.println("Something wrong!!! with uri: " + webTarget.getUri() + " and response status: " + response.getStatus());
        }
        return 0;
    }

    public static boolean canSee(int worldId, float x, float y, float z, float tx, float ty, float tz, float length) {
        WebTarget webTarget = HttpClient.getInstance().getGeoClient().target(target)
                .path("geo").path("canSee")
                .queryParam("worldId", worldId)
                .queryParam("x", x)
                .queryParam("y", y)
                .queryParam("z", z)
                .queryParam("tx", tx)
                .queryParam("ty", ty)
                .queryParam("tz", tz)
                .queryParam("length", length);

        Response response = webTarget.request().get();

        if (response.getStatus() == 200) {
            JsonNode jsonData;
            try {
                jsonData = mapper.readTree(response.readEntity(String.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            return jsonData.get("canSee").asBoolean();
        } else {
            System.out.println("Something wrong!!! with uri: " + webTarget.getUri() + " and response status: " + response.getStatus());
        }
        return false;
    }
}
