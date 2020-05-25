package it.runningexamples.fiscalcode;

public class StringUtilis {

    private static final String VOCALI = "AEIOU";

    public static int getNumConsonanti(String string) {
        int consonanti = 0;
        string = string.toUpperCase();
        for (int i = 0; i < string.length(); ++i) {
            char ch = string.charAt(i);
            if (!VOCALI.contains(Character.toString(ch))){
                ++consonanti;
            }
        }
        return consonanti;
    }

    public static String getConsonanti(String string){
        String consonanti = "";
        int nConsonanti = 0;
        char ch;
        for (int i = 0; i<=string.length(); i++){
            ch = string.charAt(i);
            if (!VOCALI.contains(Character.toString(ch))){
                nConsonanti++;
                if (nConsonanti <= 3){
                    consonanti = consonanti + ch;
                }
            }
        }
        return consonanti;
    }

}
