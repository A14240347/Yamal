package com.example.advancedchatbot.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.room.OnConflictStrategy;

import com.example.advancedchatbot.database.entities.Message;

import java.util.List;

@Dao
public interface MessageDao {

    // Inserta un solo mensaje en la base de datos; reemplaza en caso de conflicto
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(Message message);

    // Inserta una lista de mensajes de una sola vez; reemplaza en caso de conflicto
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessages(List<Message> messages);

    // Actualiza un mensaje existente
    @Update
    void updateMessage(Message message);

    // Elimina un mensaje específico
    @Delete
    void deleteMessage(Message message);

    // Elimina todos los mensajes en la base de datos
    @Query("DELETE FROM messages")
    void deleteAllMessages();

    // Obtiene todos los mensajes, ordenados por timestamp en orden ascendente
    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    List<Message> getAllMessages();

    // Obtiene los mensajes recientes, limitando la cantidad a un número específico
    @Query("SELECT * FROM messages ORDER BY timestamp DESC LIMIT :limit")
    List<Message> getRecentMessages(int limit);

    // Cuenta el número total de mensajes en la base de datos
    @Query("SELECT COUNT(*) FROM messages")
    int getMessageCount();

    // Obtiene todos los mensajes de un tipo específico (mensajes del bot o del usuario)
    @Query("SELECT * FROM messages WHERE isFromBot = :isFromBot ORDER BY timestamp ASC")
    List<Message> getMessagesByType(boolean isFromBot);

    // Recupera todos los mensajes con contenido de texto que contenga una palabra clave
    @Query("SELECT * FROM messages WHERE text LIKE '%' || :keyword || '%' ORDER BY timestamp ASC")
    List<Message> searchMessagesByKeyword(String keyword);

    // Actualiza el texto de un mensaje dado su ID
    @Query("UPDATE messages SET text = :newText WHERE id = :messageId")
    void updateMessageText(int messageId, String newText);

    // Elimina todos los mensajes que sean más antiguos que una fecha específica
    @Query("DELETE FROM messages WHERE timestamp < :timestamp")
    void deleteMessagesOlderThan(long timestamp);

    // Transacción: Inserta y obtiene el ID del mensaje insertado
    @Transaction
    @Insert
    long insertAndGetId(Message message);

    // Recupera los mensajes entre un rango de fechas específicas
    @Query("SELECT * FROM messages WHERE timestamp BETWEEN :startDate AND :endDate ORDER BY timestamp ASC")
    List<Message> getMessagesBetweenDates(long startDate, long endDate);

    // Transacción: Inserta múltiples mensajes y devuelve una lista de sus IDs
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAndGetIds(List<Message> messages);

    // Recupera el mensaje más reciente del usuario o del bot
    @Query("SELECT * FROM messages WHERE isFromBot = :isFromBot ORDER BY timestamp DESC LIMIT 1")
    Message getLastMessageByType(boolean isFromBot);

    // Actualiza el campo de timestamp de un mensaje dado su ID
    @Query("UPDATE messages SET timestamp = :newTimestamp WHERE id = :messageId")
    void updateMessageTimestamp(int messageId, long newTimestamp);

    // Elimina mensajes por IDs específicos
    @Query("DELETE FROM messages WHERE id IN (:ids)")
    void deleteMessagesByIds(List<Integer> ids);

    // Verifica si un mensaje con un ID específico existe
    @Query("SELECT COUNT(id) FROM messages WHERE id = :messageId")
    int checkMessageExists(int messageId);
}