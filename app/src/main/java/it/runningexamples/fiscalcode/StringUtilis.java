package it.runningexamples.fiscalcode;

import android.util.Log;

public class StringUtilis {

    private static final String VOCALI = "AEIOU";

    public static int getNumConsonanti(String string) {
        int consonanti = 0;
        string = string.toUpperCase();
        for (int i = 0; i < string.length(); ++i) {
            char ch = string.charAt(i);
            if (!VOCALI.contains(Character.toString(ch))) {
                ++consonanti;
            }
        }
        return consonanti;
    }

    public static String getConsonanti(String string) {
        String consonanti = "";
        int nConsonanti = 0;
        char ch;
        for (int i = 0; i <= string.length(); ++i) {

            ch = string.charAt(i);

            if (!VOCALI.contains(Character.toString(ch))) {
                nConsonanti++;
                if (nConsonanti <= 3) {
                    consonanti = consonanti + ch;
                }
            }
            Log.d("TAG", consonanti);
            if (nConsonanti == 3) return consonanti;
        }
        return consonanti;
    }

    public static String eliminaSpaziBianchi(String string) {
        return string.replaceAll("\\s+", "");
    }

    public static String getConsonanteI(String string, int i) {
        int contatoreConsonanti = 0;
        for (int j = 0; j < string.length(); j++) {
            if (!isVocale(string.charAt(j))) {
                contatoreConsonanti++;
                if (contatoreConsonanti == i) {
                    return Character.toString(string.charAt(j));
                }
            }
        }
        return null;
    }

    public static boolean isVocale(char character) {
        return VOCALI.contains(Character.toString(character));
    }

    public static String getPrimeConsonanti(String string, int numero) {
        String consonanti = "";
        for (int i = 0; i < string.length(); i++) {
            if (!isVocale(string.charAt(i))) {
                if (consonanti.length() < numero) {
                    consonanti += Character.toString(string.charAt(i));
                }
            }
        }
        return consonanti;
    }

    public static String getPrimeVocali(String string, int numero) {
        String vocali = "";
        for (int i = 0; i < string.length(); i++) {
            if (isVocale(string.charAt(i))) {
                if (vocali.length() < numero) {
                    vocali += Character.toString(string.charAt(i));
                }
            }
        }
        return vocali;

    }

    public static String nXChar(int n){
        String risultato = "";
        for(int i = 0; i < n; i++){
            risultato += "X";
        }
        return risultato;
    }

}
