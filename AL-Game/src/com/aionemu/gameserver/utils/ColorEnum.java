/*
 * AionLight project
 */
package com.aionemu.gameserver.utils;

/**
 *
 * @author Alex
 */
public enum ColorEnum {

    BlueWhite("голубой", "0 255 255"),
    Black("черный", "0 0 0"),
    Blue("синий", "0 0 255"),
    Green("зеленый", "0 128 0"),
    Lime("ярко-зеленый", "0 255 0"),
    Purple("фиолетовый", "128 0 128"),
    Red("красный", "255 0 0"),
    Yellow("желтый", "255 255 0");
    // Fuchsia("фуксин", "255 0 255"),
    // Gray("серый", "128 128 128"),
    // Maroon("темно-бордовый", "128 0 0"),
    // Olive("оливковый", "128 128 0"),
    // Silver("серебряный", "192 192 192"),
    // Teal("серо-зеленый", "0 128 128"),
    // White("белый", "255 255 255");
    private final String name;
    private final String rgb;

    ColorEnum(String name, String rgb) {
        this.name = name;
        this.rgb = rgb;
    }

    public static ColorEnum getFor(String param) {
        for (ColorEnum c : ColorEnum.values()) {
            if (c.getName().equalsIgnoreCase(param)) {
                return c;
            }
        }
        return ColorEnum.Red;
    }

    public static String getRGB(String param) {
        for (ColorEnum c : ColorEnum.values()) {
            if (c.getName().equalsIgnoreCase(param)) {
                return c.getRgb();
            }
        }
        return ColorEnum.Red.getRgb();
    }

    public String getName() {
        return name;
    }

    public String getRgb() {
        return rgb;
    }
}
