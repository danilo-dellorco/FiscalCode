package it.runningexamples.fiscalcode;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CodiceFiscale {
    private String nome, cognome;
    Comune comuneNascita;
    int year, day, month;
    private Date birthday;
    private char gender;
    private static Map monthCode = new HashMap<Integer, Character>() {{
        put(1, 'A');
        put(2, 'B');
        put(3, 'C');
        put(4, 'D');
        put(5, 'E');
        put(6, 'H');
        put(7, 'L');
        put(8, 'M');
        put(9, 'P');
        put(10, 'R');
        put(11, 'S');
        put(12, 'T');
    }};



    public CodiceFiscale(String nome, String cognome, Date birthDay, char gender, Comune comuneNascita) {
        this.nome = nome.toUpperCase(); // aggiungere eliminazione spazi bianchi
        this.cognome = cognome.toUpperCase();
        this.birthday = birthDay;
        this.comuneNascita = comuneNascita;
        this.gender = gender;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getDataNascita() {
        return String.format("%d/%d/%d", day, month, year);
    }

    public String calculateCF() {
        String surnameCode = getSurnameCF();
        String nameCode = getNameCF();
        return surnameCode + nameCode;
    }

    private String getNameCF() {

        nome = nome.replaceAll(" ", "").toUpperCase();
        String nameCons = "";
        String nameVows = "";

        nameCons = nome.replaceAll("[AEIOU]", "");
        nameVows = nome.replaceAll("[BCDFGHJKLMNPQRSTVWXYZ]", "");

        String result = "";

        if (!nome.isEmpty()) {
            if (nameCons.length() >= 4) {
                result = nameCons.substring(0, 1) + nameCons.substring(2, 4);
            } else if (nameCons.length() == 3) {
                result = nameCons;
            } else if (nameCons.length() == 2) {
                if (nameVows.isEmpty()) {
                    result = nameCons + "X";
                } else {
                    result = nameCons + nameVows.substring(0, 1);
                }
            } else if (nameCons.length() == 1) {
                if (nameVows.isEmpty()) {
                    result = nameCons + "XX";
                } else {
                    if (nameVows.length() >= 2) {
                        result = nameCons + nameVows.substring(0, 2);
                    } else if (nameVows.length() == 1) {
                        result = nameCons + nameVows + "X";
                    }
                }
            } else {
                if (nameVows.length() >= 3) {
                    result = nameVows.substring(0, 3);
                } else if (nameVows.length() == 2) {
                    result = nameVows + "X";
                } else if (nameVows.length() == 1) {
                    result = nameVows + "XX";
                }
            }
        } else {
            result = "XXX";
        }

        return result;
    }

    private String getSurnameCF() {
        cognome = cognome.replaceAll(" ", "").toUpperCase();

        String surnameCons = "";
        String surnameVows = "";

        surnameCons = cognome.replaceAll("[AEIOU]", "");
        surnameVows = cognome.replaceAll("[BCDFGHJKLMNPQRSTVWXYZ]", "");

        String result = "";

        if (cognome.isEmpty()) {
            result = "XXX";
        } else {
            if (surnameCons.length() >= 3) {
                result = surnameCons.substring(0, 3);
            } else if (surnameCons.length() == 2) {
                if (!surnameVows.isEmpty()) {
                    result = surnameCons + surnameVows.substring(0, 1);
                } else {
                    result = surnameCons + "X";
                }
            } else if (surnameCons.length() == 1) {
                if (!surnameVows.isEmpty()) {
                    if (surnameVows.length() >= 2) {
                        result = surnameCons + surnameVows.substring(0, 2);
                    } else if (surnameVows.length() == 1) {
                        result = surnameCons + surnameVows + "X";
                    }
                } else {
                    result = surnameCons + "XX";
                }
            } else {// no consonants
                if (surnameVows.length() >= 3) {
                    result = surnameVows.substring(0, 3);
                } else if (surnameVows.length() == 2) {
                    result = surnameVows + "X";
                } else if (surnameVows.length() == 1) {
                    result = surnameVows + "XX";
                } else {
                    result = "XXX";
                }
            }
        }

        return result;

    }

    private String getEncodedBirthday() {

        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");  // Get day into two digit
        day = Integer.parseInt(dayFormat.format(this.birthday));

        SimpleDateFormat monthFormat = new SimpleDateFormat("M");  // Get month into one digit
        month = Integer.parseInt(monthFormat.format(this.birthday));

        SimpleDateFormat yearFormat = new SimpleDateFormat("yy");  // Get last two digit of year
        year = Integer.parseInt(yearFormat.format(this.birthday));

        // Return the string coded with female having +40 to day code
        return String.valueOf(year) + monthCode.get(month) + (this.gender.equals(Gender.MALE) ? String.format("%02d", day) : String.format("%02d", day + 40));
    }
}
}
