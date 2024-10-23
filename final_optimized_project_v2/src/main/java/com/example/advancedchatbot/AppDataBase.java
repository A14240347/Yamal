package com.example.advancedchatbot.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.advancedchatbot.database.dao.MessageDao;
import com.example.advancedchatbot.database.entities.Message;

// Definición de la base de datos Room, con la lista de entidades y su versión
@Database(entities = {Message.class}, version = 2, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {

    // Definición de DAOs (Data Access Objects) para interactuar con las tablas de la base de datos
    public abstract MessageDao messageDao();

    // Singleton para la instancia de la base de datos
    private static volatile AppDatabase INSTANCE;

    /**
     * Método para obtener la instancia de la base de datos. Este patrón singleton previene la
     * apertura de múltiples instancias de la base de datos al mismo tiempo, asegurando que solo
     * una instancia esté en uso.
     */
    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Creación de la instancia de la base de datos con Room y configuración avanzada
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "chat_database")
                            .addMigrations(MIGRATION_1_2)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Ejemplo de una migración avanzada: desde la versión 1 a la versión 2 de la base de datos.
     * Esta migración agrega una columna "timestamp" a la tabla "messages".
     */
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Modifica la tabla "messages" para agregar una nueva columna "timestamp"
            database.execSQL("ALTER TABLE messages ADD COLUMN timestamp INTEGER DEFAULT 0 NOT NULL");
        }
    };

    /**
     * Función para cerrar la base de datos. Esto es útil si deseas liberar recursos
     * cuando la base de datos ya no es necesaria.
     */
    public void closeDatabase() {
        if (INSTANCE != null && INSTANCE.isOpen()) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }
}