package com.example.advancedchatbot.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.advancedchatbot.database.AppDatabase;
import com.example.advancedchatbot.database.dao.MessageDao;
import com.example.advancedchatbot.database.entities.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatRepository {

    private final MessageDao messageDao;
    private final ExecutorService executorService;

    // Caché de mensajes en memoria para optimizar acceso
    private final List<Message> cachedMessages = new ArrayList<>();

    // LiveData para estados de carga y error
    private final MutableLiveData<Boolean> loadingState = new MutableLiveData<>();
    private final MutableLiveData<String> errorState = new MutableLiveData<>();

    public ChatRepository(AppDatabase appDatabase) {
        this.messageDao = appDatabase.messageDao();
        this.executorService = Executors.newFixedThreadPool(3); // Tres hilos para operaciones en segundo plano
    }

    // Método para observar el estado de carga
    public LiveData<Boolean> getLoadingState() {
        return loadingState;
    }

    // Método para observar el estado de error
    public LiveData<String> getErrorState() {
        return errorState;
    }

    // Cargar mensajes de la conversación y actualizar LiveData
    public LiveData<List<Message>> getMessagesForConversation(int conversationId) {
        MutableLiveData<List<Message>> messagesLiveData = new MutableLiveData<>();
        loadingState.setValue(true);
        
        executorService.execute(() -> {
            try {
                List<Message> messages = messageDao.getMessagesByConversationId(conversationId);
                cachedMessages.clear();
                cachedMessages.addAll(messages);
                messagesLiveData.postValue(messages);
                loadingState.postValue(false);
            } catch (Exception e) {
                e.printStackTrace();
                errorState.postValue("Error al cargar mensajes: " + e.getMessage());
                loadingState.postValue(false);
            }
        });
        return messagesLiveData;
    }

    // Insertar un mensaje y actualizar el caché
    public void insertMessage(Message message) {
        executorService.execute(() -> {
            try {
                messageDao.insertMessage(message);
                cachedMessages.add(message); // Añadir al caché
            } catch (Exception e) {
                e.printStackTrace();
                errorState.postValue("Error al enviar mensaje: " + e.getMessage());
            }
        });
    }

    // Método para filtrar mensajes desde el caché local por palabra clave
    public LiveData<List<Message>> filterMessages(String keyword) {
        MutableLiveData<List<Message>> filteredMessagesLiveData = new MutableLiveData<>();
        List<Message> filteredMessages = new ArrayList<>();
        
        for (Message message : cachedMessages) {
            if (message.getText() != null && message.getText().contains(keyword)) {
                filteredMessages.add(message);
            }
        }
        
        filteredMessagesLiveData.setValue(filteredMessages);
        return filteredMessagesLiveData;
    }

    // Método para obtener el número de mensajes en la conversación (usando Transformations)
    public LiveData<Integer> getMessageCount(int conversationId) {
        return Transformations.map(getMessagesForConversation(conversationId), List::size);
    }

    // Marcar todos los mensajes de una conversación como leídos
    public void markMessagesAsRead(int conversationId) {
        executorService.execute(() -> {
            try {
                messageDao.markMessagesAsRead(conversationId);
                for (Message message : cachedMessages) {
                    if (message.getConversationId() == conversationId) {
                        message.setRead(true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                errorState.postValue("Error al marcar mensajes como leídos: " + e.getMessage());
            }
        });
    }

    // Obtener el último mensaje en la conversación
    public LiveData<Message> getLastMessage(int conversationId) {
        MutableLiveData<Message> lastMessageLiveData = new MutableLiveData<>();
        
        executorService.execute(() -> {
            try {
                Message lastMessage = messageDao.getLastMessageInConversation(conversationId);
                lastMessageLiveData.postValue(lastMessage);
            } catch (Exception e) {
                e.printStackTrace();
                errorState.postValue("Error al obtener el último mensaje: " + e.getMessage());
            }
        });
        
        return lastMessageLiveData;
    }

    // Método para cerrar el repositorio y liberar recursos
    public void close() {
        executorService.shutdown();
    }
}