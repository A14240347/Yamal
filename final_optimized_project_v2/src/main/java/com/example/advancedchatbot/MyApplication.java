package com.example.advancedchatbot;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.example.advancedchatbot.database.AppDatabase;
import com.example.advancedchatbot.network.RetrofitClient;
import com.example.advancedchatbot.utils.NetworkMonitor;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MyApplication extends Application implements LifecycleObserver {

    private static MyApplication instance;
    private AppDatabase appDatabase;
    private RetrofitClient retrofitClient;
    private FirebaseAuth firebaseAuth;
    private NetworkMonitor networkMonitor;

    @Override
    public void onCreate() {
        super.onCreate();

        // Inicialización de instancia de la aplicación
        instance = this;

        // Inicializar Firebase y sus servicios
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        configureFirebaseAuth();
        configureFirebaseCrashlytics();
        configureFirebaseMessaging();

        // Inicializar Room Database
        appDatabase = AppDatabase.getInstance(this);

        // Inicializar Retrofit
        retrofitClient = new RetrofitClient();

        // Configurar monitor de red
        configureNetworkMonitor();

        // Registrar el observador del ciclo de vida de la aplicación
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    // Método para manejar la autenticación de Firebase
    private void configureFirebaseAuth() {
        firebaseAuth.addAuthStateListener(firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // Usuario autenticado, manejar lógica de usuario conectado
            } else {
                // Usuario no autenticado, redirigir a la pantalla de inicio de sesión
            }
        });
    }

    // Configuración avanzada de Firebase Crashlytics
    private void configureFirebaseCrashlytics() {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG);
        // Añadir datos adicionales o configurar comportamiento personalizado de Crashlytics
        FirebaseCrashlytics.getInstance().setCustomKey("App Launched", System.currentTimeMillis());
    }

    // Configuración avanzada de Firebase Messaging
    private void configureFirebaseMessaging() {
        FirebaseMessaging.getInstance().subscribeToTopic("global")
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(this, "Error en la suscripción a FCM", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Configuración avanzada de monitoreo de red
    private void configureNetworkMonitor() {
        networkMonitor = new NetworkMonitor();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();

        connectivityManager.registerNetworkCallback(networkRequest, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                networkMonitor.setNetworkAvailable(true);
                Toast.makeText(MyApplication.this, "Conexión a Internet restaurada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                networkMonitor.setNetworkAvailable(false);
                Toast.makeText(MyApplication.this, "Sin conexión a Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para habilitar MultiDex
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    // Obtener instancia de la aplicación
    public static MyApplication getInstance() {
        return instance;
    }

    // Acceso a la base de datos
    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    // Acceso a Retrofit
    public RetrofitClient getRetrofitClient() {
        return retrofitClient;
    }

    // Acceso al monitor de red
    public NetworkMonitor getNetworkMonitor() {
        return networkMonitor;
    }

    // Método para cerrar y limpiar recursos al terminar
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (appDatabase != null) {
            appDatabase.closeDatabase();
        }
    }
}