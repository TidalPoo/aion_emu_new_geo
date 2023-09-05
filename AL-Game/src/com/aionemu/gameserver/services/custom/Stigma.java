package com.aionemu.gameserver.services.custom;

import com.aionemu.gameserver.cardinal.AddItemType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 *
 * @author Alex
 */
public class Stigma {

    public static void Add(Player player) {
        int[] items = null;
        switch (player.getPlayerClass()) {
            case GLADIATOR:
                items = new int[]{140000720, 140000722, 140000222, 140000535, 140000688, 140000712, 140000726, 140000710, 140000717, 140000718, 140000715, 140000713, 140000530, 140000691, 140000714, 140000023, 140000007, 140000008, 140000709, 140000539, 140000716, 140000707, 140000705, 140000011, 140000723, 140000540, 140000394, 140000719, 140000724, 140000016, 140001001, 140000708, 140000172, 140000173};
                PacketSendUtility.sendMessage(player, "Вы получили набор стигм для гладиатора");
                break;
            case GUNNER:
                items = new int[]{140000980, 140000986, 140000950, 140000927, 140000933, 140000959, 140000967, 140000981, 140000920, 140000940, 140000966, 140000993, 140000928, 140000985, 140001000, 140000954, 140000912, 140000926, 140000973, 140000919};
                PacketSendUtility.sendMessage(player, "Вы получили набор стигм для снайпера");
                break;
            case BARD:
                items = new int[]{140000862, 140000863, 140000858, 140000897, 140000898, 140000906, 140000872, 140000878, 140000859, 140000877, 140000861, 140000883, 140000905, 140000867, 140000868, 140000873, 140000890, 140000857};
                PacketSendUtility.sendMessage(player, "Вы получили набор стигм для музыканта");
                break;
            case TEMPLAR:
                items = new int[]{140000174, 140000016, 140000175, 140000027, 140000706, 140000821, 140000559, 140000192, 140000807, 140000563, 140000044, 140000820, 140000562, 140000558, 140000373, 140000028, 140000819, 140000812, 140000813, 140000825, 140000814, 140000715, 140000693, 140000823, 140000815, 140000238, 140000354, 140000237, 140000818, 140000817, 140000816, 140000822, 140000824};
                PacketSendUtility.sendMessage(player, "Вы получили набор стигм для стражника");
                break;
            case ASSASSIN:
                items = new int[]{140000179, 140000178, 140000605, 140000586, 140000200, 140000199, 140000213, 140000850, 140000077, 140000253, 140000078, 140000780, 140000600, 140000696, 140000087, 140000606, 140000608, 140000607, 140000081, 140000781, 140000695, 140000503, 140000198, 140000088, 140000782, 140000596, 140000786, 140000783, 140000785, 140000400, 140000779, 140000777, 140000778, 140000776};
                PacketSendUtility.sendMessage(player, "Вы получили набор стигм для убийцы");
                break;
            case RANGER:
                items = new int[]{140000177, 140000176, 140000239, 140000585, 140000570, 140000344, 140000343, 140000063, 140000754, 140000853, 140000852, 140000770, 140000769, 140000057, 140000056, 140000702, 140000764, 140000763, 140000049, 140000577, 140000582, 140000581, 140000074, 140000694, 140000197, 140000196, 140000578, 140000195, 140000048, 140000069, 140000245, 140000768, 140000767, 140000766, 140000762, 140000761, 140000765, 140000771, 140000772, 140000247, 140000368};
                PacketSendUtility.sendMessage(player, "Вы получили набор стигм для лучника");
                break;
            case SORCERER:
                items = new int[]{140000184, 140000185, 140000144, 140000142, 140000141, 140000738, 140000851, 140000733, 140000732, 140000669, 140000729, 140000739, 140000139, 140000135, 140000131, 140000692, 140000390, 140000662, 140000855, 140000736, 140000130, 140000512, 140000511, 140000670, 140000291, 140000735, 140000731, 140000293, 140000728, 140000737, 140000465, 140000742, 140000295, 140000741};
                PacketSendUtility.sendMessage(player, "Вы получили набор стигм для волшебника");
                break;
            case SPIRIT_MASTER:
                items = new int[]{140000186, 140000187, 140000750, 140000743, 140000152, 140000159, 140000214, 140000149, 140000148, 140000687, 140000212, 140000167, 140000701, 140000211, 140000156, 140000686, 140000681, 140000677, 140000752, 140000685, 140000703, 140000746, 140000163, 140000751, 140000303, 140000493, 140000306, 140000753, 140000749, 140000748, 140000747, 140000488, 140000486, 140000302};
                PacketSendUtility.sendMessage(player, "Вы получили набор стигм для заклинателя");
                break;
            case CLERIC:
                items = new int[]{140000181, 140000180, 140000098, 140000099, 140000101, 140000627, 140000826, 140000104, 140000699, 140000841, 140000504, 140000633, 140000631, 140000102, 140000704, 140000832, 140000831, 140000840, 140000108, 140000842, 140000836, 140000269, 140000849, 140000845, 140000442, 140000848, 140000847, 140000844, 140000843, 140000846, 140000839, 140000837, 140000838};
                PacketSendUtility.sendMessage(player, "Вы получили набор стигм для целителя");
                break;
            case CHANTER:
                items = new int[]{140000854, 140000653, 140000125, 140000793, 140000788, 140000123, 140000796, 140000801, 140000116, 140000787, 140000112, 140000802, 140000800, 140000700, 140000506, 140000654, 140000652, 140000655, 140000794, 140000183, 140000182, 140000117, 140000806, 140000799, 140000282, 140000797, 140000805, 140000286, 140000798, 140000803, 140000856, 140000804};
                PacketSendUtility.sendMessage(player, "Вы получили набор стигм для чантера");
                break;
        }
        if (items != null) {
            for (int itemId : items) {
                ItemService.addItem(player, itemId, 1, AddItemType.EVENT, null);
            }
        }
    }

    public static void Teleport(Player player) {
        TeleportService2.teleportToCapital(player);
    }
}