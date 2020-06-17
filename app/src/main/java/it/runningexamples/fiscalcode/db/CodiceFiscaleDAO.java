package it.runningexamples.fiscalcode.db;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@SuppressWarnings("ALL")
@Dao
public interface CodiceFiscaleDAO {
    @Query("SELECT * FROM CodiceFiscaleEntity")
    List<CodiceFiscaleEntity> getAll();

    @Query("SELECT count(*) FROM CodiceFiscaleEntity WHERE finalFiscalCode IN (:code)")
    int getCode(String code);

    @Query("SELECT * FROM CodiceFiscaleEntity WHERE personale == 1")
    CodiceFiscaleEntity  getPersonalCode();

    @Insert
    void saveNewCode(CodiceFiscaleEntity codice);

    @Delete
    void deleteCode(CodiceFiscaleEntity codice);

    @Query("SELECT count(*) FROM CodiceFiscaleEntity")
    int getDbSize();

    @Query("UPDATE CodiceFiscaleEntity SET personale = 1  WHERE finalFiscalCode IN (:code)")
    int setPersonal(String code);

    @Query("UPDATE CodiceFiscaleEntity SET personale = 0  WHERE finalFiscalCode IN (:code)")
    int removePersonal(String code);

    @Query("DELETE FROM CodiceFiscaleEntity")
    void deleteAll();
}
