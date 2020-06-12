package it.runningexamples.fiscalcode;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CodiceFiscaleDAO {
    @Query("SELECT * FROM CodiceFiscale")
    List<CodiceFiscale> getAll();

    @Query("SELECT * FROM CodiceFiscale WHERE preferito == 1")
    List<CodiceFiscale>  getFavourites();

    @Insert
    void saveNewCode(CodiceFiscale codice);

    @Delete
    void deleteCode(CodiceFiscale codice);

    @Query("SELECT count(*) FROM CodiceFiscale")
    int getDbSize();
}
