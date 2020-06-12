package it.runningexamples.fiscalcode;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CodiceFiscaleDAO {
    @Query("SELECT * FROM CodiceFiscaleEntity")
    List<CodiceFiscaleEntity> getAll();

    @Query("SELECT * FROM CodiceFiscaleEntity WHERE preferito == 1")
    List<CodiceFiscaleEntity>  getFavourites();

    @Insert
    void saveNewCode(CodiceFiscaleEntity codice);

    @Delete
    void deleteCode(CodiceFiscaleEntity codice);

    @Query("SELECT count(*) FROM CodiceFiscaleEntity")
    int getDbSize();
}
