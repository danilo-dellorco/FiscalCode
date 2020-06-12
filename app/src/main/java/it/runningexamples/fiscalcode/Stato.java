package it.runningexamples.fiscalcode;

public class Stato {
    private String nome;
    private String code;

    public Stato(String nome, String code) {
        this.nome = nome;
        this.code = code;
    }

    public String getName() {
        return nome;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.format("%s", this.nome);
    }
}
