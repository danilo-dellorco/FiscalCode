package it.runningexamples.fiscalcode.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CodiceFiscaleEntity.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String DB_NAME = "CF_DB";
    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
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
