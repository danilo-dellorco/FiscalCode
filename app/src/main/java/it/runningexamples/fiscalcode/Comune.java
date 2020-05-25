package it.runningexamples.fiscalcode;

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

    public void setName(String name) {
        this.name = name;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
