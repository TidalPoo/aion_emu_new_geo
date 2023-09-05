/*
 * AionLight project
 */
package com.aionemu.gameserver.services.newWords;

/**
 *
 * @author Alex
 */
public class Words {

    //хранение слов в массиве для замены в контексте 
    private static String[] wordsContext;
    //хранение целых слов в массиве для замены в тексте 
    private static String[] wordsText;

    //все слова для контекста
    public static String[] getWordsInContext() {
        return wordsContext;
    }

    //обнуление и загрузка слов для контекста
    public static void setWordsInContext(String[] words) {
        Words.wordsContext = words;
    }

    //все слова для текста
    public static String[] getWordsInText() {
        return wordsText;
    }

    //обнуление и загрузка слов для текста
    public static void setWordsInText(String[] words) {
        Words.wordsText = words;
    }

    //кол-во слов для контекста
    public static int sizeContext() {
        return wordsContext.length;
    }

    //кол-во слов для текста
    public static int sizeText() {
        return wordsText.length;
    }
}
