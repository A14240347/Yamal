package com.example.advancedchatbot.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.advancedchatbot.database.dao.MessageDao;
import com.example.advancedchatbot.database.entities.Message;
import com.example.advancedchatbot.database.converters.DateConverter;

// Definición de la base de datos Room con exportación de esquema para control de versiones
@Database(entities = {Message.class}, version = 4, exportSchema = true)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    // DAO para interactuar con las tablas
    public abstract MessageDao messageDao();

    // Singleton para la instancia de la base de datos
    private static volatile AppDatabase INSTANCE;

    /**
     * Método para obtener la instancia de la base de datos.
     * Utiliza un patrón singleton para evitar múltiples instancias.
     */
    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "chat_database")
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Migración de la versión 1 a la versión 2:
     * Agrega una columna "timestamp" a la tabla "messages" para registrar el tiempo de creación.
     */
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE messages ADD COLUMN timestamp INTEGER DEFAULT 0 NOT NULL");
        }
    };

    /**
     * Migración de la versión 2 a la versión 3:
     * Agrega una columna "isRead" para rastrear si el mensaje ha sido leído.
     */
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE messages ADD COLUMN isRead INTEGER DEFAULT 0 NOT NULL");
        }
    };

    /**
     * Migración de la versión 3 a la versión 4:
     * Modifica el tipo de la columna "timestamp" para almacenar el valor como un entero largo.
     * Crea un índice en "timestamp" para optimizar las consultas de fecha.
     */
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE messages_temp (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "text TEXT, isFromBot INTEGER NOT NULL, timestamp INTEGER, isRead INTEGER DEFAULT 0 NOT NULL)");
            database.execSQL("INSERT INTO messages_temp (id, text, isFromBot, timestamp, isRead) " +
                    "SELECT id, text, isFromBot, timestamp, isRead FROM messages");
            database.execSQL("DROP TABLE messages");
            database.execSQL("ALTER TABLE messages_temp RENAME TO messages");
            database.execSQL("CREATE INDEX index_messages_timestamp ON messages (timestamp)");
        }
    };

    /**
     * Cierra la instancia de la base de datos.
     * Este método libera recursos y permite que se cree una nueva instancia cuando sea necesario.
     */
    public void closeDatabase() {
        if (INSTANCE != null && INSTANCE.isOpen()) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }
}