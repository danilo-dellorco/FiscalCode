package it.runningexamples.fiscalcode;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CodiceFiscaleEntity.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "CF_DB";
    private static volatile AppDatabase instance;

    static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    public AppDatabase() {
    }

    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                DB_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }

    public abstract CodiceFiscaleDAO codiceFiscaleDAO();

}
