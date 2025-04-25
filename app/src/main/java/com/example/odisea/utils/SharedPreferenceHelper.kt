package com.example.odisea.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceHelper(context: Context) {

    private val PREFS_NAME = "MyAppPrefs"
    private val PREF_SOCIO_ID = "socio_id"

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Obtener el ID del socio
    fun getSocioId(): Int {
        return sharedPreferences.getInt(PREF_SOCIO_ID, -1) // Retorna -1 si no hay socio_id guardado
    }

    // Limpiar el ID del socio (por ejemplo, al cerrar sesión)
    fun clearSocioId() {
        val editor = sharedPreferences.edit()
        editor.remove(PREF_SOCIO_ID)
        editor.apply()
    }
}