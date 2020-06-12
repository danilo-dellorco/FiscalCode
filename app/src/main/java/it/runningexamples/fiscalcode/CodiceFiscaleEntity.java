package it.runningexamples.fiscalcode;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CodiceFiscaleEntity implements Parcelable {
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

    protected CodiceFiscaleEntity(Parcel in) {
        finalFiscalCode = in.readString();
        nome = in.readString();
        cognome = in.readString();
        comune = in.readString();
        data = in.readString();
        genere = in.readString();
        preferito = in.readInt();
    }

    public static final Creator<CodiceFiscaleEntity> CREATOR = new Creator<CodiceFiscaleEntity>() {
        @Override
        public CodiceFiscaleEntity createFromParcel(Parcel in) {
            return new CodiceFiscaleEntity(in);
        }

        @Override
        public CodiceFiscaleEntity[] newArray(int size) {
            return new CodiceFiscaleEntity[size];
        }
    };

    public String getNome(){
        return nome;
    }
    public String getCognome(){
        return cognome;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(finalFiscalCode);
        dest.writeString(nome);
        dest.writeString(cognome);
        dest.writeString(comune);
        dest.writeString(data);
        dest.writeString(genere);
        dest.writeInt(preferito);
    }
}
