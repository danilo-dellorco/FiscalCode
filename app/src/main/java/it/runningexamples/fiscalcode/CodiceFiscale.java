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
        String codiceNome;
        int numeroConsonanti = 0;
        nome = StringUtilis.eliminaSpaziBianchi(nome).toUpperCase();

        if(nome.length() >= 3){
            numeroConsonanti = StringUtilis.getNumConsonanti(nome);

            if(numeroConsonanti >= 4){
                codiceNome = StringUtilis.getConsonanteI(nome, 1) + StringUtilis.getConsonanteI(nome, 3) + StringUtilis.getConsonanteI(nome, 4);
            }
            else if(numeroConsonanti >= 3){

                codiceNome = StringUtilis.getPrimeConsonanti(nome, 3);
            }
            else{
                codiceNome = StringUtilis.getPrimeConsonanti(nome, numeroConsonanti);
                codiceNome += StringUtilis.getPrimeVocali(nome, 3 - numeroConsonanti);
            }
        }
        else{
            int numeroCaratteri = nome.length();
            codiceNome = nome + StringUtilis.nXChar(3 - numeroCaratteri);
        }


        return codiceNome;
    }
}
