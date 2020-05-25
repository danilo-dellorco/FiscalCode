package it.runningexamples.fiscalcode;

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

//    public String calculateCF(){}

    public String calculateNomeCF(){
        String nomeCF = null;
        int nConsonanti;

        if (nome.length()>=3){
            nConsonanti = StringUtilis.getNumConsonanti(nome);
            if (nConsonanti >=3){
                nomeCF = StringUtilis.getConsonanti(nome);
            }
        }
        return nomeCF;
    }
}
