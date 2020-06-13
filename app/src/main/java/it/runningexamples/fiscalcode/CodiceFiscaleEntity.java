package it.runningexamples.fiscalcode;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    @ColumnInfo(name = "personale")
    public int personale;

    public CodiceFiscaleEntity(){}
    private static Comune comuneNascita;
    private static Stato statoNascita;
    private static Date birthday;
    private static int day;
    private static int year;
    private static int month;



    private static Map monthCode = new HashMap<Integer, Character>() {{
        put(1, 'A');
        put(2, 'B');
        put(3, 'C');
        put(4, 'D');
        put(5, 'E');
        put(6, 'H');
        put(7, 'L');
        put(8, 'M');
        put(9, 'P');
        put(10, 'R');
        put(11, 'S');
        put(12, 'T');
    }};

    private static Map evenCode = new HashMap<Character, Integer>() {{
        put('0', 0);
        put('1', 1);
        put('2', 2);
        put('3', 3);
        put('4', 4);
        put('5', 5);
        put('6', 6);
        put('7', 7);
        put('8', 8);
        put('9', 9);
        put('A', 0);
        put('B', 1);
        put('C', 2);
        put('D', 3);
        put('E', 4);
        put('F', 5);
        put('G', 6);
        put('H', 7);
        put('I', 8);
        put('J', 9);
        put('K', 10);
        put('L', 11);
        put('M', 12);
        put('N', 13);
        put('O', 14);
        put('P', 15);
        put('Q', 16);
        put('R', 17);
        put('S', 18);
        put('T', 19);
        put('U', 20);
        put('V', 21);
        put('W', 22);
        put('X', 23);
        put('Y', 24);
        put('Z', 25);
    }};

    private static Map remainderCode = new HashMap<Integer, Character>() {{
        put(0, 'A');
        put(1, 'B');
        put(2, 'C');
        put(3, 'D');
        put(4, 'E');
        put(5, 'F');
        put(6, 'G');
        put(7, 'H');
        put(8, 'I');
        put(9, 'J');
        put(10, 'K');
        put(11, 'L');
        put(12, 'M');
        put(13, 'N');
        put(14, 'O');
        put(15, 'P');
        put(16, 'Q');
        put(17, 'R');
        put(18, 'S');
        put(19, 'T');
        put(20, 'U');
        put(21, 'V');
        put(22, 'W');
        put(23, 'X');
        put(24, 'Y');
        put(25, 'Z');
    }};

    private static Map oddCode = new HashMap<Character, Integer>() {{
        put('0', 1);
        put('1', 0);
        put('2', 5);
        put('3', 7);
        put('4', 9);
        put('5', 13);
        put('6', 15);
        put('7', 17);
        put('8', 19);
        put('9', 21);
        put('A', 1);
        put('B', 0);
        put('C', 5);
        put('D', 7);
        put('E', 9);
        put('F', 13);
        put('G', 15);
        put('H', 17);
        put('I', 19);
        put('J', 21);
        put('K', 2);
        put('L', 4);
        put('M', 18);
        put('N', 20);
        put('O', 11);
        put('P', 3);
        put('Q', 6);
        put('R', 8);
        put('S', 12);
        put('T', 14);
        put('U', 16);
        put('V', 10);
        put('W', 22);
        put('X', 25);
        put('Y', 24);
        put('Z', 23);
    }};



    CodiceFiscaleEntity(String nome, String cognome, Date birthDay, String gender, @Nullable Comune comuneNascita, @Nullable Stato stato, int personale) {
        this.nome = nome;
        this.cognome = cognome;
        this.birthday = birthDay;
        this.genere = gender;
        this.personale = personale;
        if (comuneNascita != null){
            this.comuneNascita = comuneNascita;
            this.comune = comuneNascita.getName();
        }
        if (stato != null){
            this.statoNascita = stato;
        }
    }

    protected CodiceFiscaleEntity(Parcel in) {
        finalFiscalCode = in.readString();
        nome = in.readString();
        cognome = in.readString();
        comune = in.readString();
        data = in.readString();
        genere = in.readString();
        personale = in.readInt();
        //todo stato estero
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

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }
//
    public String getDataNascita() {
        return String.format("%d/%d/%d", day, month, year);
    }

    public String getGenere() {
        return genere;
    }

//    public String getComune() {return String.format("%s (%s)", comuneNascita.getName(), comuneNascita.getProv());}
public String getComuneName() {
    return comune;
    }


    public String getFinalFiscalCode(){
        return finalFiscalCode;
    }
    public String getStatoNascita(){
        return statoNascita.getName();
    }

    public String calculateCF() {
        String surnameCode = getSurnameCF();
        String nameCode = getNameCF();
        String birthdayCode = getBirthdayCF();
        String code = (statoNascita == null) ? comuneNascita.getCode() : statoNascita.getCode();
        String result = surnameCode + nameCode + birthdayCode + code;
        finalFiscalCode = result + checkCharacter(result);
        return finalFiscalCode;
    }

    private String checkCharacter(String result) {

        char[] fiscalCodeCharArray = result.toCharArray();  // Get array of character

        int sum = 0;

        for (int i = 0; i < fiscalCodeCharArray.length; i++) {  // Sum all character code
            Character character = fiscalCodeCharArray[i];
            if ((i+1) % 2 == 0) {
                sum = sum + (int) evenCode.get(character);
            } else {
                sum = sum + (int) oddCode.get(character);
            }
        }

        return (remainderCode.get(sum % 26)).toString();
    }

    private String getNameCF() {

        nome = nome.replaceAll(" ", "").toUpperCase();
        String nameCons = "";
        String nameVows = "";

        nameCons = nome.replaceAll("[AEIOU]", "");
        nameVows = nome.replaceAll("[BCDFGHJKLMNPQRSTVWXYZ]", "");

        String result = "";

        if (!nome.isEmpty()) {
            if (nameCons.length() >= 4) {
                result = nameCons.substring(0, 1) + nameCons.substring(2, 4);
            } else if (nameCons.length() == 3) {
                result = nameCons;
            } else if (nameCons.length() == 2) {
                if (nameVows.isEmpty()) {
                    result = nameCons + "X";
                } else {
                    result = nameCons + nameVows.substring(0, 1);
                }
            } else if (nameCons.length() == 1) {
                if (nameVows.isEmpty()) {
                    result = nameCons + "XX";
                } else {
                    if (nameVows.length() >= 2) {
                        result = nameCons + nameVows.substring(0, 2);
                    } else if (nameVows.length() == 1) {
                        result = nameCons + nameVows + "X";
                    }
                }
            } else {
                if (nameVows.length() >= 3) {
                    result = nameVows.substring(0, 3);
                } else if (nameVows.length() == 2) {
                    result = nameVows + "X";
                } else if (nameVows.length() == 1) {
                    result = nameVows + "XX";
                }
            }
        } else {
            result = "XXX";
        }

        return result;
    }

    private String getSurnameCF() {
        cognome = cognome.replaceAll(" ", "").toUpperCase();

        String surnameCons = "";
        String surnameVows = "";

        surnameCons = cognome.replaceAll("[AEIOU]", "");
        surnameVows = cognome.replaceAll("[BCDFGHJKLMNPQRSTVWXYZ]", "");

        String result = "";

        if (cognome.isEmpty()) {
            result = "XXX";
        } else {
            if (surnameCons.length() >= 3) {
                result = surnameCons.substring(0, 3);
            } else if (surnameCons.length() == 2) {
                if (!surnameVows.isEmpty()) {
                    result = surnameCons + surnameVows.substring(0, 1);
                } else {
                    result = surnameCons + "X";
                }
            } else if (surnameCons.length() == 1) {
                if (!surnameVows.isEmpty()) {
                    if (surnameVows.length() >= 2) {
                        result = surnameCons + surnameVows.substring(0, 2);
                    } else if (surnameVows.length() == 1) {
                        result = surnameCons + surnameVows + "X";
                    }
                } else {
                    result = surnameCons + "XX";
                }
            } else {// no consonants
                if (surnameVows.length() >= 3) {
                    result = surnameVows.substring(0, 3);
                } else if (surnameVows.length() == 2) {
                    result = surnameVows + "X";
                } else if (surnameVows.length() == 1) {
                    result = surnameVows + "XX";
                } else {
                    result = "XXX";
                }
            }
        }

        return result;

    }

    private String getBirthdayCF() {
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");  // Get day into two digit
        day = Integer.parseInt(dayFormat.format(this.birthday));

        SimpleDateFormat monthFormat = new SimpleDateFormat("M");  // Get month into one digit
        month = Integer.parseInt(monthFormat.format(this.birthday));

        SimpleDateFormat yearFormat = new SimpleDateFormat("YY");  // Get last two digit of year
        year = Integer.parseInt(yearFormat.format(this.birthday));

        // Return the string coded with female having +40 to day code
        if (genere.equals("F")){
            return String.valueOf(year)+monthCode.get(month) + String.format("%02d", day + 40);
        }
        return String.format("%02d", year) + monthCode.get(month) +String.format("%02d", day);
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
        dest.writeInt(personale);
    }
}

