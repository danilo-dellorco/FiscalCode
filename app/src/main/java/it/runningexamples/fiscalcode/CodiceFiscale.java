package it.runningexamples.fiscalcode;

import android.util.Log;

public class CodiceFiscale {
    private String nome, cognome, comuneNascita;
    private int giornoNascita, meseNascita, annoNascita;
    private char sesso;

    public CodiceFiscale(String nome, String cognome, int giornoNascita, int meseNascita, int annoNascita, char sesso, String comuneNascita){
        this.nome = nome.toUpperCase(); // aggiungere eliminazione spazi bianchi
        this.cognome = cognome.toUpperCase();
        this.annoNascita = annoNascita;
        this.meseNascita = meseNascita;
        this.giornoNascita = giornoNascita;
        this.comuneNascita = comuneNascita;
        this.sesso = sesso;
    }

    public String getNome(){
        return nome;
    }

    public String getCognome(){
        return cognome;
    }

    public String getDataNascita(){
        return String.format("%d/%d/%d", giornoNascita, meseNascita, annoNascita);
    }

    public String getComuneNascita(){
        return comuneNascita;
    }

    public String calculateCF(){
        String surnameCode = getSurnameCF();
        String nameCode = getNameCF();
        return surnameCode+nameCode;
    }

    private String getNameCF(){

        nome = nome.replaceAll(" ", "").toUpperCase();
        String nameCons = "";
        String nameVows = "";

        nameCons = nome.replaceAll("[AEIOU]", "");
        nameVows = nome.replaceAll("[BCDFGHJKLMNPQRSTVWXYZ]", "");

        String result = "";

        if(!nome.isEmpty()){
            if(nameCons.length() >= 4){
                result = nameCons.substring(0, 1) + nameCons.substring(2, 4);
            }else if(nameCons.length() == 3){
                result = nameCons;
            }else if(nameCons.length() == 2){
                if(nameVows.isEmpty()){
                    result = nameCons + "X";
                }else{
                    result = nameCons + nameVows.substring(0, 1);
                }
            }else if(nameCons.length() == 1){
                if(nameVows.isEmpty()){
                    result = nameCons + "XX";
                }else{
                    if(nameVows.length() >= 2){
                        result = nameCons + nameVows.substring(0, 2);
                    }else if(nameVows.length() == 1){
                        result = nameCons + nameVows + "X";
                    }
                }
            }else{
                if(nameVows.length() >= 3){
                    result = nameVows.substring(0, 3);
                }else if(nameVows.length() == 2){
                    result = nameVows + "X";
                }else if(nameVows.length() == 1){
                    result = nameVows + "XX";
                }
            }
        }else{
            result = "XXX";
        }

        return result;
    }

    private String getSurnameCF(){
        cognome = cognome.replaceAll(" ", "").toUpperCase();

        String surnameCons = "";
        String surnameVows = "";

        surnameCons = cognome.replaceAll("[AEIOU]", "");
        surnameVows = cognome.replaceAll("[BCDFGHJKLMNPQRSTVWXYZ]", "");

        String result = "";

        if(cognome.isEmpty()){
            result = "XXX";
        }else{
            if(surnameCons.length() >= 3){
                result = surnameCons.substring(0, 3);
            }else if(surnameCons.length() == 2){
                if(!surnameVows.isEmpty()){
                    result = surnameCons + surnameVows.substring(0, 1);
                }else{
                    result = surnameCons + "X";
                }
            }else if(surnameCons.length() == 1){
                if(!surnameVows.isEmpty()){
                    if(surnameVows.length() >= 2){
                        result = surnameCons + surnameVows.substring(0, 2);
                    }else if(surnameVows.length() == 1){
                        result = surnameCons + surnameVows + "X";
                    }
                }else{
                    result = surnameCons + "XX";
                }
            }else{// no consonants
                if(surnameVows.length() >= 3){
                    result = surnameVows.substring(0, 3);
                }else if(surnameVows.length() == 2){
                    result = surnameVows + "X";
                }else if(surnameVows.length() == 1){
                    result = surnameVows + "XX";
                }else{
                    result = "XXX";
                }
            }
        }

        return result;

    }

//    public static String getBirthDateYearCF(Calendar birthDate){
//        int year = birthDate.get(Calendar.YEAR);
//
//        String result = Integer.toString(year);
//
//        return result.substring(result.length() - 2, result.length());
//    }
}
