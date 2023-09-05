/*
 * AionLight project
 */
package com.aionemu.gameserver.services.custom;

import com.aionemu.gameserver.ai2.AI2Engine;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.spawnengine.SpawnEngine;

/**
 *
 * @author Alex
 */
public class NewSpawn {

    public static void spawn() {
        spawnEly();
        spawnAsmo();
    }

    public static void ffaPortal(boolean spawn) {
        Npc npc = null;
        Npc npc2 = null;
        if (spawn) {
            // ely
            if (npc == null) {
                SpawnTemplate spawn3 = SpawnEngine.addNewSpawn(110010000, 831073, 1504, 1525, 565, (byte) 95, 0);
                spawn3.setMasterName("FFA");
                SpawnEngine.spawnObject(spawn3, 1);
                AI2Engine.getInstance().setupAI("pvp_portal", (Creature) spawn3.getVisibleObject());
                ((AbstractAI) ((Creature) spawn3.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);
                npc = (Npc) spawn3.getVisibleObject();
            }
            // asmo
            if (npc2 == null) {
                SpawnTemplate spawn4 = SpawnEngine.addNewSpawn(120010000, 831073, 1626, 1421, 193, (byte) 83, 0);
                spawn4.setMasterName("FFA");
                SpawnEngine.spawnObject(spawn4, 1);
                AI2Engine.getInstance().setupAI("pvp_portal", (Creature) spawn4.getVisibleObject());
                ((AbstractAI) ((Creature) spawn4.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);
                npc2 = (Npc) spawn4.getVisibleObject();
            }
        } else {
            if (npc != null) {
                npc.getController().onDelete();
            }
            if (npc2 != null) {
                npc2.getController().onDelete();
            }
        }
    }

    public static void spawnEly() {

        SpawnTemplate spawn = SpawnEngine.addNewSpawn(110010000, 831073, 1533, 1479, 565, (byte) 38, 0);
        spawn.setMasterName("FFA-LEGION");
        SpawnEngine.spawnObject(spawn, 1);
        AI2Engine.getInstance().setupAI("pvp_portal", (Creature) spawn.getVisibleObject());
        ((AbstractAI) ((Creature) spawn.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);

        SpawnTemplate spawn2 = SpawnEngine.addNewSpawn(110010000, 831073, 1505, 1496, 565, (byte) 27, 0);
        spawn2.setMasterName("FFA-GROUP");
        SpawnEngine.spawnObject(spawn2, 1);
        AI2Engine.getInstance().setupAI("pvp_portal", (Creature) spawn2.getVisibleObject());
        ((AbstractAI) ((Creature) spawn2.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);

        SpawnTemplate spawn5 = SpawnEngine.addNewSpawn(110010000, 831073, 1562, 1511, 565, (byte) 59, 0);
        spawn5.setMasterName("\u0414\u043b\u044f \u041d\u043e\u0432\u0438\u0447\u043a\u043e\u0432");
        SpawnEngine.spawnObject(spawn5, 1);
        AI2Engine.getInstance().setupAI("pvp_portal", (Creature) spawn5.getVisibleObject());
        ((AbstractAI) ((Creature) spawn5.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);

        // new 
        SpawnTemplate spawn6 = SpawnEngine.addNewSpawn(110010000, 730840, 1459, 1495, 573, (byte) 59, 0);
        spawn6.setMasterName("\u0410\u043b\u044c\u044f\u043d\u0441");
        SpawnEngine.spawnObject(spawn6, 1);
        // AI2Engine.getInstance().setupAI("pvp_portal", (Creature) spawn6.getVisibleObject());
        // ((NpcAI2) ((Creature) spawn6.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);

        SpawnTemplate spawn7 = SpawnEngine.addNewSpawn(110010000, 730316, 1459, 1526, 573, (byte) 60, 0);
        spawn7.setMasterName("\u0413\u0440\u0443\u043f\u043f\u0430");
        SpawnEngine.spawnObject(spawn7, 1);
        // AI2Engine.getInstance().setupAI("pvp_portal", (Creature) spawn7.getVisibleObject());
        // ((NpcAI2) ((Creature) spawn7.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);
    }

    public static void spawnAsmo() {

        SpawnTemplate spawn = SpawnEngine.addNewSpawn(120010000, 831073, 1602, 1424, 193, (byte) 105, 0);
        spawn.setMasterName("FFA-LEGION");
        SpawnEngine.spawnObject(spawn, 1);
        AI2Engine.getInstance().setupAI("pvp_portal", (Creature) spawn.getVisibleObject());
        ((AbstractAI) ((Creature) spawn.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);

        SpawnTemplate spawn2 = SpawnEngine.addNewSpawn(120010000, 831073, 1613, 1424, 193, (byte) 94, 0);
        spawn2.setMasterName("FFA-GROUP");
        SpawnEngine.spawnObject(spawn2, 1);
        AI2Engine.getInstance().setupAI("pvp_portal", (Creature) spawn2.getVisibleObject());
        ((AbstractAI) ((Creature) spawn2.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);

        SpawnTemplate spawn5 = SpawnEngine.addNewSpawn(120010000, 831073, 1605, 1379, 193, (byte) 17, 0);
        spawn5.setMasterName("\u0414\u043b\u044f \u041d\u043e\u0432\u0438\u0447\u043a\u043e\u0432");
        SpawnEngine.spawnObject(spawn5, 1);
        AI2Engine.getInstance().setupAI("pvp_portal", (Creature) spawn5.getVisibleObject());
        ((AbstractAI) ((Creature) spawn5.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);

        // new 
        SpawnTemplate spawn6 = SpawnEngine.addNewSpawn(120010000, 730840, 1596, 1395, 193, (byte) 29, 0);
        spawn6.setMasterName("\u0410\u043b\u044c\u044f\u043d\u0441");
        SpawnEngine.spawnObject(spawn6, 1);
        //  AI2Engine.getInstance().setupAI("pvp_portal", (Creature) spawn6.getVisibleObject());
        // ((NpcAI2) ((Creature) spawn6.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);

        SpawnTemplate spawn7 = SpawnEngine.addNewSpawn(120010000, 730356, 1596, 1406, 193, (byte) 89, 0);
        spawn7.setMasterName("\u0413\u0440\u0443\u043f\u043f\u0430");
        SpawnEngine.spawnObject(spawn7, 1);
        // AI2Engine.getInstance().setupAI("pvp_portal", (Creature) spawn7.getVisibleObject());
        //  ((NpcAI2) ((Creature) spawn7.getVisibleObject()).getAi2()).setStateIfNot(AIState.IDLE);
    }

    public static VisibleObject spawn(int worldId, int instanceId, int npcId, float x, float y, float z, byte heading, int respTime) {
        SpawnTemplate template = SpawnEngine.addNewSpawn(worldId, npcId, x, y, z, heading, respTime);
        return SpawnEngine.spawnObject(template, instanceId);
    }

    public static void playerZone() {
        int instanceId = NewPlayerZone.getFFAInstanceId();
        int worldId = NewPlayerZone.getFFAInstanceMap().getId();
        // 219358
        spawn(worldId, instanceId, 219358, 339.86957f, 1280.2687f, 153.3091f, (byte) 89, 3600);
        // 231407
        spawn(worldId, instanceId, 231407, 248.22623f, 507.5073f, 112.9149f, (byte) 69, 295);
        spawn(worldId, instanceId, 231407, 246.32051f, 509.537f, 112.84828f, (byte) 79, 295);
        spawn(worldId, instanceId, 231407, 239.02882f, 572.9187f, 108.63457f, (byte) 108, 295);
        spawn(worldId, instanceId, 231407, 251.25314f, 559.3671f, 108.56407f, (byte) 37, 295);
        spawn(worldId, instanceId, 231407, 260.75516f, 590.5112f, 104.67074f, (byte) 34, 295);
        spawn(worldId, instanceId, 231407, 247.87476f, 632.1278f, 105.26678f, (byte) 56, 295);
        spawn(worldId, instanceId, 231407, 249.01004f, 631.16174f, 105.14651f, (byte) 67, 295);
        spawn(worldId, instanceId, 231407, 208.28557f, 625.07404f, 105.91437f, (byte) 44, 295);
        spawn(worldId, instanceId, 231407, 278.47223f, 750.03174f, 99.343506f, (byte) 39, 295);
        spawn(worldId, instanceId, 231407, 280.95206f, 752.3879f, 99.25f, (byte) 98, 295);
        spawn(worldId, instanceId, 231407, 233.75403f, 824.3577f, 102.69971f, (byte) 36, 295);
        spawn(worldId, instanceId, 231407, 226.32095f, 818.4887f, 106.62863f, (byte) 46, 295);
        spawn(worldId, instanceId, 231407, 160.36713f, 861.53107f, 103.45411f, (byte) 118, 295);
        spawn(worldId, instanceId, 231407, 160.85736f, 862.708f, 103.51258f, (byte) 112, 295);
        spawn(worldId, instanceId, 231407, 201.31676f, 919.3044f, 106.12577f, (byte) 9, 295);
        spawn(worldId, instanceId, 231407, 201.2505f, 920.5781f, 106.11429f, (byte) 0, 295);
        spawn(worldId, instanceId, 231407, 201.86142f, 918.4041f, 106.21608f, (byte) 5, 295);
        spawn(worldId, instanceId, 231407, 565.0572f, 256.87003f, 109.16053f, (byte) 112, 295);
        spawn(worldId, instanceId, 231407, 565.78625f, 258.67966f, 109.07775f, (byte) 112, 295);
        spawn(worldId, instanceId, 231407, 607.7578f, 276.31854f, 99.39014f, (byte) 8, 295);
        spawn(worldId, instanceId, 231407, 635.55414f, 272.90118f, 99.375f, (byte) 118, 295);
        spawn(worldId, instanceId, 231407, 656.9862f, 291.91833f, 99.375f, (byte) 20, 295);
        spawn(worldId, instanceId, 231407, 685.8723f, 318.80408f, 99.375f, (byte) 11, 295);
        spawn(worldId, instanceId, 231407, 704.84485f, 344.62173f, 99.375f, (byte) 20, 295);
        spawn(worldId, instanceId, 231407, 707.4178f, 386.25314f, 99.375f, (byte) 32, 295);
        spawn(worldId, instanceId, 231407, 682.0542f, 408.44736f, 100.5f, (byte) 47, 295);
        spawn(worldId, instanceId, 231407, 635.70056f, 421.13635f, 100.537926f, (byte) 57, 295);
        spawn(worldId, instanceId, 231407, 590.9831f, 454.4162f, 105.0f, (byte) 75, 295);
        spawn(worldId, instanceId, 231407, 639.8673f, 500.94073f, 108.16632f, (byte) 11, 295);
        spawn(worldId, instanceId, 231407, 639.21295f, 503.14694f, 107.838264f, (byte) 4, 295);
        spawn(worldId, instanceId, 231407, 657.53033f, 516.799f, 111.04421f, (byte) 71, 295);
        spawn(worldId, instanceId, 231407, 659.15967f, 514.39795f, 110.57184f, (byte) 71, 295);
        spawn(worldId, instanceId, 231407, 765.77856f, 473.68106f, 105.625f, (byte) 71, 295);
        spawn(worldId, instanceId, 231407, 780.87756f, 496.47736f, 106.8242f, (byte) 102, 295);
        spawn(worldId, instanceId, 231407, 779.1236f, 495.22195f, 106.5216f, (byte) 100, 295);
        spawn(worldId, instanceId, 231407, 748.36523f, 483.05353f, 107.60217f, (byte) 115, 295);
        spawn(worldId, instanceId, 231407, 694.48425f, 453.93008f, 106.086f, (byte) 81, 295);
        spawn(worldId, instanceId, 231407, 696.2642f, 452.54623f, 105.81828f, (byte) 25, 295);
        spawn(worldId, instanceId, 231407, 727.23285f, 506.3811f, 111.95206f, (byte) 79, 295);
        spawn(worldId, instanceId, 231407, 722.708f, 511.39978f, 111.464516f, (byte) 80, 295);
        spawn(worldId, instanceId, 231407, 743.94415f, 568.7322f, 112.230484f, (byte) 93, 295);
        spawn(worldId, instanceId, 231407, 745.2345f, 569.2904f, 112.13199f, (byte) 97, 295);
        spawn(worldId, instanceId, 231407, 760.9382f, 552.3475f, 111.910706f, (byte) 82, 295);
        spawn(worldId, instanceId, 231407, 782.16766f, 474.675f, 106.03143f, (byte) 25, 295);
        spawn(worldId, instanceId, 231407, 818.99664f, 559.5988f, 119.158554f, (byte) 88, 295);
        spawn(worldId, instanceId, 231407, 818.8383f, 556.64716f, 119.15793f, (byte) 89, 295);
        spawn(worldId, instanceId, 231407, 826.55273f, 556.11096f, 118.97404f, (byte) 89, 295);
        spawn(worldId, instanceId, 231407, 826.67944f, 559.78577f, 119.054504f, (byte) 89, 295);
        spawn(worldId, instanceId, 231407, 807.8928f, 539.1322f, 118.972534f, (byte) 116, 295);
        spawn(worldId, instanceId, 231407, 831.4906f, 515.08057f, 118.686325f, (byte) 24, 295);
        spawn(worldId, instanceId, 231407, 874.9594f, 509.81f, 119.44684f, (byte) 93, 295);
        spawn(worldId, instanceId, 231407, 874.25586f, 510.68945f, 119.39099f, (byte) 86, 295);
        spawn(worldId, instanceId, 231407, 856.8625f, 491.85516f, 115.29109f, (byte) 107, 295);
        spawn(worldId, instanceId, 231407, 941.5917f, 483.574f, 110.863174f, (byte) 78, 295);
        spawn(worldId, instanceId, 231407, 941.8436f, 480.5475f, 110.1222f, (byte) 86, 295);
        spawn(worldId, instanceId, 231407, 920.4611f, 430.37094f, 103.02818f, (byte) 22, 295);
        spawn(worldId, instanceId, 231407, 923.4227f, 429.0635f, 104.15852f, (byte) 22, 295);
        spawn(worldId, instanceId, 231407, 962.4532f, 394.347f, 108.644196f, (byte) 26, 295);
        spawn(worldId, instanceId, 231407, 1061.3278f, 479.54825f, 117.96967f, (byte) 67, 295);
        spawn(worldId, instanceId, 231407, 1051.9f, 439.76315f, 120.0f, (byte) 106, 295);
        spawn(worldId, instanceId, 231407, 1060.8737f, 421.09885f, 119.63736f, (byte) 45, 295);
        spawn(worldId, instanceId, 231407, 1038.5714f, 360.01367f, 118.69472f, (byte) 21, 295);
        spawn(worldId, instanceId, 231407, 1039.7751f, 360.37354f, 118.79091f, (byte) 32, 295);
        spawn(worldId, instanceId, 231407, 1041.5642f, 360.12042f, 118.754654f, (byte) 32, 295);
        spawn(worldId, instanceId, 231407, 1100.2539f, 367.01807f, 126.63605f, (byte) 81, 295);
        spawn(worldId, instanceId, 231407, 1100.812f, 365.76483f, 127.375854f, (byte) 75, 295);
        spawn(worldId, instanceId, 231407, 1085.0111f, 353.1188f, 134.54782f, (byte) 15, 295);
        spawn(worldId, instanceId, 231407, 1154.9174f, 306.71054f, 134.4241f, (byte) 48, 295);
        spawn(worldId, instanceId, 231407, 1157.6198f, 308.50974f, 134.32504f, (byte) 42, 295);
        spawn(worldId, instanceId, 231407, 1208.8774f, 377.68173f, 138.94516f, (byte) 68, 295);
        spawn(worldId, instanceId, 231407, 1208.2946f, 382.1938f, 138.6313f, (byte) 69, 295);
        spawn(worldId, instanceId, 231407, 1211.4076f, 421.92584f, 140.125f, (byte) 71, 295);
        spawn(worldId, instanceId, 231407, 1210.3997f, 422.54037f, 140.15877f, (byte) 80, 295);
        spawn(worldId, instanceId, 231407, 1188.489f, 475.43887f, 136.43613f, (byte) 76, 295);
        spawn(worldId, instanceId, 231407, 1189.8313f, 474.70654f, 136.60391f, (byte) 76, 295);
        spawn(worldId, instanceId, 231407, 1153.7739f, 489.37604f, 139.69348f, (byte) 74, 295);
        spawn(worldId, instanceId, 231407, 1086.6501f, 502.37973f, 127.07753f, (byte) 58, 295);
        spawn(worldId, instanceId, 231407, 1086.6193f, 500.54764f, 127.35861f, (byte) 52, 295);
        spawn(worldId, instanceId, 231407, 991.31696f, 520.5924f, 101.337975f, (byte) 73, 295);
        spawn(worldId, instanceId, 231407, 990.1393f, 521.40356f, 101.28728f, (byte) 78, 295);
        spawn(worldId, instanceId, 231407, 930.66254f, 506.27454f, 99.33359f, (byte) 77, 295);
        spawn(worldId, instanceId, 231407, 847.3379f, 521.496f, 118.5f, (byte) 105, 295);
        spawn(worldId, instanceId, 231407, 895.84265f, 471.8358f, 100.664154f, (byte) 110, 295);
        spawn(worldId, instanceId, 231407, 891.98126f, 437.79453f, 99.10982f, (byte) 21, 295);
        spawn(worldId, instanceId, 231407, 857.1805f, 370.26657f, 112.46935f, (byte) 12, 295);
        spawn(worldId, instanceId, 231407, 858.3806f, 369.70547f, 112.02673f, (byte) 17, 295);
        spawn(worldId, instanceId, 231407, 890.8991f, 396.01346f, 104.35452f, (byte) 28, 295);
        spawn(worldId, instanceId, 231407, 871.622f, 500.60895f, 115.625f, (byte) 100, 295);
        spawn(worldId, instanceId, 231407, 863.4861f, 552.77924f, 119.01203f, (byte) 75, 295);
        spawn(worldId, instanceId, 231407, 864.5368f, 552.03705f, 119.01203f, (byte) 81, 295);
        spawn(worldId, instanceId, 231407, 866.22235f, 551.46985f, 119.01203f, (byte) 79, 295);
        spawn(worldId, instanceId, 231407, 822.68414f, 544.6062f, 119.21698f, (byte) 86, 295);
        spawn(worldId, instanceId, 231407, 813.8793f, 519.028f, 117.647095f, (byte) 26, 295);
        spawn(worldId, instanceId, 231407, 813.0927f, 484.22968f, 108.49795f, (byte) 63, 295);
        spawn(worldId, instanceId, 231407, 648.8956f, 356.8081f, 102.08207f, (byte) 114, 295);
        spawn(worldId, instanceId, 231407, 648.2215f, 358.0783f, 102.208466f, (byte) 1, 295);
        spawn(worldId, instanceId, 231407, 627.31006f, 582.59534f, 115.18604f, (byte) 92, 295);
        spawn(worldId, instanceId, 231407, 600.83344f, 557.1091f, 110.125f, (byte) 44, 295);
        spawn(worldId, instanceId, 231407, 602.0339f, 556.9299f, 110.18735f, (byte) 112, 295);
        spawn(worldId, instanceId, 231407, 605.0756f, 636.7926f, 117.17697f, (byte) 64, 295);
        spawn(worldId, instanceId, 231407, 604.02966f, 637.5972f, 116.93561f, (byte) 70, 295);
        spawn(worldId, instanceId, 231407, 539.63776f, 625.9429f, 119.91193f, (byte) 15, 295);
        spawn(worldId, instanceId, 231407, 564.67664f, 681.2645f, 119.38977f, (byte) 92, 295);
        spawn(worldId, instanceId, 231407, 560.7324f, 680.7322f, 119.40848f, (byte) 92, 295);
        spawn(worldId, instanceId, 231407, 541.48975f, 757.25397f, 107.26474f, (byte) 119, 295);
        spawn(worldId, instanceId, 231407, 540.88904f, 755.54456f, 107.23613f, (byte) 114, 295);
        spawn(worldId, instanceId, 231407, 569.74115f, 760.46106f, 112.43263f, (byte) 111, 295);
        spawn(worldId, instanceId, 231407, 625.985f, 761.1932f, 120.35085f, (byte) 49, 295);
        spawn(worldId, instanceId, 231407, 626.4218f, 759.374f, 120.57825f, (byte) 66, 295);
        spawn(worldId, instanceId, 231407, 606.99286f, 835.2865f, 114.81294f, (byte) 72, 295);
        spawn(worldId, instanceId, 231407, 605.32043f, 836.04443f, 115.056f, (byte) 79, 295);
        spawn(worldId, instanceId, 231407, 632.16064f, 925.5151f, 117.72814f, (byte) 61, 295);
        spawn(worldId, instanceId, 231407, 630.6042f, 926.2301f, 117.48703f, (byte) 82, 295);
        spawn(worldId, instanceId, 231407, 630.86237f, 924.543f, 117.72814f, (byte) 74, 295);
        spawn(worldId, instanceId, 231407, 584.3024f, 970.9631f, 128.0602f, (byte) 66, 295);
        spawn(worldId, instanceId, 231407, 583.2146f, 972.2431f, 128.1098f, (byte) 73, 295);
        spawn(worldId, instanceId, 231407, 592.97f, 1045.5746f, 137.74625f, (byte) 64, 295);
        spawn(worldId, instanceId, 231407, 592.84467f, 1047.4684f, 137.73059f, (byte) 66, 295);
        spawn(worldId, instanceId, 231407, 618.376f, 1112.5758f, 139.969f, (byte) 49, 295);
        spawn(worldId, instanceId, 231407, 617.4316f, 1111.0765f, 140.057f, (byte) 48, 295);
        spawn(worldId, instanceId, 231407, 654.995f, 1165.6925f, 143.25f, (byte) 64, 295);
        spawn(worldId, instanceId, 231407, 655.3635f, 1164.6598f, 143.25f, (byte) 68, 295);
        spawn(worldId, instanceId, 231407, 654.766f, 1167.3583f, 143.25f, (byte) 67, 295);
        spawn(worldId, instanceId, 231407, 611.45135f, 1188.6443f, 143.61043f, (byte) 56, 295);
        spawn(worldId, instanceId, 231407, 611.1058f, 1189.7516f, 143.98688f, (byte) 80, 295);
        spawn(worldId, instanceId, 231407, 523.54346f, 1228.5139f, 141.42838f, (byte) 96, 295);
        spawn(worldId, instanceId, 231407, 521.7806f, 1228.2001f, 141.91373f, (byte) 93, 295);
        spawn(worldId, instanceId, 231407, 515.917f, 1195.6945f, 139.00519f, (byte) 102, 295);
        spawn(worldId, instanceId, 231407, 526.2191f, 1170.2037f, 137.93964f, (byte) 79, 295);
        spawn(worldId, instanceId, 231407, 493.63943f, 1147.7762f, 135.38354f, (byte) 73, 295);
        spawn(worldId, instanceId, 231407, 492.3853f, 1143.0707f, 134.96066f, (byte) 84, 295);
        spawn(worldId, instanceId, 231407, 424.77054f, 1111.814f, 126.35718f, (byte) 106, 295);
        spawn(worldId, instanceId, 231407, 425.3612f, 1113.2236f, 126.94504f, (byte) 103, 295);
        spawn(worldId, instanceId, 231407, 450.3501f, 1048.1301f, 119.45624f, (byte) 113, 295);
        spawn(worldId, instanceId, 231407, 448.6922f, 1045.2886f, 119.45674f, (byte) 110, 295);
        spawn(worldId, instanceId, 231407, 439.93707f, 1033.362f, 117.526505f, (byte) 93, 295);
        spawn(worldId, instanceId, 231407, 443.23663f, 1069.7577f, 121.469f, (byte) 10, 295);
        spawn(worldId, instanceId, 231407, 434.43707f, 1045.9534f, 119.75f, (byte) 117, 295);
        spawn(worldId, instanceId, 231407, 432.91687f, 1056.4418f, 119.75f, (byte) 1, 295);
        spawn(worldId, instanceId, 231407, 449.6069f, 1051.1644f, 119.52457f, (byte) 8, 295);
        spawn(worldId, instanceId, 231407, 383.82916f, 1186.3467f, 150.25f, (byte) 88, 295);
        spawn(worldId, instanceId, 231407, 378.7581f, 1153.6818f, 146.97931f, (byte) 34, 295);
        spawn(worldId, instanceId, 231407, 255.96034f, 1118.726f, 150.375f, (byte) 80, 295);
        spawn(worldId, instanceId, 231407, 237.58183f, 1134.2327f, 147.06976f, (byte) 70, 295);
        spawn(worldId, instanceId, 231407, 238.41064f, 1133.9778f, 146.96878f, (byte) 88, 295);
        spawn(worldId, instanceId, 231407, 236.11996f, 1133.2617f, 146.93294f, (byte) 88, 295);
        spawn(worldId, instanceId, 231407, 197.25616f, 1102.5542f, 145.0982f, (byte) 113, 295);
        spawn(worldId, instanceId, 231407, 197.1339f, 1100.6722f, 145.05055f, (byte) 108, 295);
        spawn(worldId, instanceId, 231407, 224.95145f, 1052.7466f, 134.35329f, (byte) 12, 295);
        spawn(worldId, instanceId, 231407, 273.8554f, 1078.8912f, 134.03763f, (byte) 62, 295);
        spawn(worldId, instanceId, 231407, 296.3161f, 1041.0465f, 125.36073f, (byte) 82, 295);
        spawn(worldId, instanceId, 231407, 389.84183f, 1003.9915f, 108.38435f, (byte) 3, 295);
        spawn(worldId, instanceId, 231407, 425.3159f, 1011.6154f, 111.55493f, (byte) 6, 295);
        spawn(worldId, instanceId, 231407, 467.7833f, 992.4282f, 122.82179f, (byte) 34, 295);
        spawn(worldId, instanceId, 231407, 468.3571f, 991.3212f, 123.00006f, (byte) 13, 295);
        spawn(worldId, instanceId, 231407, 435.1153f, 1051.387f, 119.75f, (byte) 118, 295);
        spawn(worldId, instanceId, 231407, 439.89392f, 1071.3701f, 121.36864f, (byte) 21, 295);
        spawn(worldId, instanceId, 231407, 441.1279f, 1034.0682f, 117.559845f, (byte) 99, 295);
        spawn(worldId, instanceId, 231407, 507.4673f, 1096.6736f, 128.0075f, (byte) 16, 295);
        // 217237
        spawn(worldId, instanceId, 217237, 424.47897f, 1050.7565f, 119.84506f, (byte) 118, 3600);
        // 232853
        spawn(worldId, instanceId, 232853, 223.56685f, 492.68936f, 112.021416f, (byte) 14, 750);
        spawn(worldId, instanceId, 232853, 219.84756f, 548.69244f, 108.03375f, (byte) 11, 750);
        spawn(worldId, instanceId, 232853, 204.11786f, 677.5343f, 106.720894f, (byte) 25, 750);
        spawn(worldId, instanceId, 232853, 146.41608f, 852.67993f, 99.125f, (byte) 5, 750);
        spawn(worldId, instanceId, 232853, 621.94525f, 380.75464f, 99.375f, (byte) 98, 750);
        spawn(worldId, instanceId, 232853, 610.4293f, 365.36194f, 99.375f, (byte) 112, 750);
        spawn(worldId, instanceId, 232853, 575.3763f, 435.93503f, 104.66399f, (byte) 15, 750);
        spawn(worldId, instanceId, 232853, 672.3643f, 496.0708f, 107.75885f, (byte) 118, 750);
        spawn(worldId, instanceId, 232853, 724.5146f, 509.0041f, 111.90508f, (byte) 86, 750);
        spawn(worldId, instanceId, 232853, 758.2071f, 553.81165f, 111.64029f, (byte) 89, 750);
        spawn(worldId, instanceId, 232853, 822.63495f, 556.8442f, 118.97432f, (byte) 89, 750);
        spawn(worldId, instanceId, 232853, 945.22516f, 446.45282f, 104.17327f, (byte) 54, 750);
        spawn(worldId, instanceId, 232853, 1060.5267f, 481.4645f, 117.514465f, (byte) 94, 750);
        spawn(worldId, instanceId, 232853, 1056.4127f, 443.08246f, 120.10728f, (byte) 75, 750);
        spawn(worldId, instanceId, 232853, 1156.5178f, 307.2873f, 134.4169f, (byte) 43, 750);
        spawn(worldId, instanceId, 232853, 1207.8082f, 379.78278f, 138.88858f, (byte) 69, 750);
        spawn(worldId, instanceId, 232853, 1152.3467f, 490.95267f, 139.39621f, (byte) 73, 750);
        spawn(worldId, instanceId, 232853, 890.3597f, 397.9452f, 104.35452f, (byte) 1, 750);
        spawn(worldId, instanceId, 232853, 822.92584f, 560.8573f, 118.97404f, (byte) 88, 750);
        spawn(worldId, instanceId, 232853, 649.75244f, 358.21188f, 101.92142f, (byte) 5, 750);
        spawn(worldId, instanceId, 232853, 625.1473f, 583.53705f, 115.53362f, (byte) 97, 750);
        spawn(worldId, instanceId, 232853, 541.0544f, 624.7188f, 120.2612f, (byte) 19, 750);
        spawn(worldId, instanceId, 232853, 605.77875f, 834.0717f, 114.78175f, (byte) 78, 750);
        spawn(worldId, instanceId, 232853, 690.6109f, 864.0491f, 126.09714f, (byte) 60, 750);
        spawn(worldId, instanceId, 232853, 685.76807f, 933.3213f, 124.360504f, (byte) 52, 750);
        spawn(worldId, instanceId, 232853, 616.50116f, 1113.0076f, 139.65694f, (byte) 46, 750);
        spawn(worldId, instanceId, 232853, 681.09784f, 1190.7632f, 143.11151f, (byte) 78, 750);
        spawn(worldId, instanceId, 232853, 676.0216f, 1190.7198f, 143.00792f, (byte) 83, 750);
        spawn(worldId, instanceId, 232853, 682.8495f, 1185.8159f, 142.71957f, (byte) 70, 750);
        spawn(worldId, instanceId, 232853, 334.25397f, 1171.8281f, 150.94937f, (byte) 99, 750);
        spawn(worldId, instanceId, 232853, 269.36414f, 1163.4407f, 148.047f, (byte) 72, 750);
        spawn(worldId, instanceId, 232853, 523.6481f, 1044.4282f, 125.74523f, (byte) 105, 750);
        spawn(worldId, instanceId, 232853, 506.86673f, 1097.5984f, 127.85437f, (byte) 10, 750);
    }
}
