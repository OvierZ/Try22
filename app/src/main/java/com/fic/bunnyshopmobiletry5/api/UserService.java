package com.fic.bunnyshopmobiletry5.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class UserService {

    private static final String PREFERENCES_NAME = "UserPreferences";
    private static final String USER_KEY = "User";
    private static SharedPreferences sharedPreferences = null;

    // Constructor que recibe el contexto
    public UserService(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    // Método para obtener los datos del usuario
    public static String getData() {
        String userData = sharedPreferences.getString(USER_KEY, null);
        Log.d("Shared_Preferences", userData != null ? userData : "No data found");
        return userData;
    }

    // Método para guardar los datos del usuario
    public void saveData(String userData) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_KEY, userData);
        editor.apply();
        Log.d("Shared_Preferences", "Data saved: " + userData);
    }

    // Método para eliminar los datos del usuario
    public void clearData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USER_KEY);
        editor.apply();
        Log.d("Shared_Preferences", "Data cleared");
    }
}
