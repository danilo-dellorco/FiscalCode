package it.runningexamples.fiscalcode;

public class CodiceFiscale {
    private String nome, cognome, comuneNascita;
    private int giornoNascita, meseNascita, annoNascita;
    private char sesso;

    public CodiceFiscale(String nome, String cognome, int giornoNascita, int meseNascita, int annoNascita, char sesso, String comuneNascita){
        this.nome = nome;
        this.cognome = cognome;
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
}
