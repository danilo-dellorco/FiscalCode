/** Rappresenta lo stato Estero, restituito da un Parser sul file 'stati_esteri.csv' */
package it.runningexamples.fiscalcode.entity;
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

    // Necessaria per mostrare il nome degli oggetti nell'AutoCompleteTextView
    @Override
    public String toString() {
        return String.format("%s", this.nome); //NON-NLS
    }
}
