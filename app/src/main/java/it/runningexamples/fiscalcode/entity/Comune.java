package it.runningexamples.fiscalcode.entity;
/** Rappresenta un comune italiano, restituito da un Parser sul file 'comuni.csv' */

@SuppressWarnings("ALL")
public class Comune {
    private String name;
    private String prov;
    private String code;

    public Comune(String name, String prov, String code) {
        this.name = name;
        this.prov = prov;
        this.code = code;
    }
    public String getName() {
        return name;
    }

    public String getProv() {
        return prov;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // Necessaria per mostrare il nome degli oggetti nell'AutoCompleteTextView
    @Override
    public String toString() {
        return String.format("%s (%s)", this.name, this.prov);
    }
}
