/*
 * SAO Project
 */
package com.aionemu.gameserver.cardinal;

/**
 * @author Alex
 */
public enum Language {
    /*Windows-1250 для языков Центральной Европы, которые используют латинское написание букв 
     (польский, чешский, словацкий, венгерский, словенский, хорватский, румынский и албанский)
     Windows-1251 для кириллических алфавитов
     Windows-1252 для западных языков
     Windows-1253 для греческого языка
     Windows-1254 для турецкого языка
     Windows-1255 для иврита
     Windows-1256 для арабского языка
     Windows-1257 для балтийских языков
     Windows-1258 для вьетнамского языка
     Shift JIS для японского языка (Microsoft CP932)
     EUC-KR для корейского языка (Microsoft CP949)
     ISO-2022 и EUC для китайской письменности
     Кодировки UTF-8, UTF-16 и UTF-32 набора символов Юникод*/

    russian("1251"),
    english("1250, 1252, 1253, 1254, 1255, 1256, 1257, 1258"),
    none("");
    private final String encoding;

    Language(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return encoding;
    }

    public static Language getLanguageCode(int encoding) {
        for (Language lc : Language.values()) {
            if (lc.getEncoding().contains(encoding + "")) {
                return lc;
            }
        }
        return Language.none;
    }
}
