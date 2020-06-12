package it.runningexamples.fiscalcode;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CodiceFiscaleEntity {
    @PrimaryKey
    @NonNull
    public String finalFiscalCode;

    @ColumnInfo(name = "nome")
    public String nome;

    @ColumnInfo(name = "cognome")
    public String cognome;

    @ColumnInfo(name = "comune")
    public String comune;

    @ColumnInfo(name = "data")
    public String data;

    @ColumnInfo(name = "genere")
    public String genere;

    @ColumnInfo(name = "preferito")
    public int preferito;

    public CodiceFiscaleEntity(String finalFiscalCode, String nome, String cognome, String comune, String data, String genere) {
        this.finalFiscalCode = finalFiscalCode;
        this.nome = nome;
        this.cognome = cognome;
        this.comune = comune;
        this.data = data;
        this.genere = genere;
        this.preferito = 0;
    }

}
