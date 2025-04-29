package com.example.odisea.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceHelper(context: Context) {
    private val PREFS_NAME = "MyAppPrefs"
    private val PREF_SOCIO_ID = "socio_id"
    private val PREF_SOCIO_NOMBRE = "socio_nombre"
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Guardar el ID del socio
    fun saveSocioId(socioId: Int) {
        sharedPreferences.edit().putInt(PREF_SOCIO_ID, socioId).apply()
    }

    // Obtener el ID del socio
    fun getSocioId(): Int {
        return sharedPreferences.getInt(PREF_SOCIO_ID, -1) // Retorna -1 si no hay socio_id guardado
    }

    // Guardar el nombre del socio
    fun saveSocioNombre(socioNombre: String) {
        sharedPreferences.edit().putString(PREF_SOCIO_NOMBRE, socioNombre).apply()
    }

    // Obtener el nombre del socio
    fun getSocioNombre(): String? {
        return sharedPreferences.getString(PREF_SOCIO_NOMBRE, null)
    }

    // Limpiar los datos del socio (por ejemplo, al cerrar sesión)
    fun clearSocioData() {
        sharedPreferences.edit().remove(PREF_SOCIO_ID).remove(PREF_SOCIO_NOMBRE).apply()
    }
}