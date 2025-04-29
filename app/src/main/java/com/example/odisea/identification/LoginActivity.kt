package com.example.odisea.identification

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.odisea.MainActivity
import com.example.odisea.R
import com.example.odisea.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val PREFS_NAME = "MyAppPrefs"
    private val PREF_SOCIO_ID = "socio_id"
    private val PREF_SOCIO_NOMBRE = "socio_nombre" // Nueva clave para el nombre del socio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.et_username)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val tvEmailError = findViewById<TextView>(R.id.tv_email_error)
        val tvPasswordError = findViewById<TextView>(R.id.tv_password_error)
        val loginButton = findViewById<Button>(R.id.btn_login)
        val goToRegisterButton = findViewById<Button>(R.id.go_to_register_button)

        // Botón para iniciar sesión
        loginButton.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validar campos vacíos
            if (email.isEmpty()) {
                tvEmailError.visibility = android.view.View.VISIBLE
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                tvPasswordError.visibility = android.view.View.VISIBLE
                return@setOnClickListener
            }

            // Llamar al endpoint para validar credenciales
            RetrofitClient.apiService.validarCredenciales(email, password).enqueue(object : Callback<Map<String, Any>> {
                override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                    if (response.isSuccessful && response.body() != null) {
                        val socioId = response.body()?.get("socio_id") as Int
                        val socioNombre = response.body()?.get("nombre") as? String ?: "Usuario"

                        // Guardar el ID y el nombre del socio en SharedPreferences
                        saveSocioInfo(socioId, socioNombre)

                        // Redirigir a la actividad principal
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        tvPasswordError.text = "Correo o contraseña incorrectos"
                        tvPasswordError.visibility = android.view.View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    tvPasswordError.text = "Error al conectar con el servidor"
                    tvPasswordError.visibility = android.view.View.VISIBLE
                    t.printStackTrace()
                }
            })
        }

        // Botón para abrir la página de registro en el navegador
        goToRegisterButton.setOnClickListener {
            // URL de la página de registro
            val registrationUrl = "https://www.ejemplo.com/register" // Cambia esta URL por la real
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(registrationUrl))
            startActivity(intent)
        }
    }

    /**
     * Guarda el ID y el nombre del socio en SharedPreferences.
     */
    private fun saveSocioInfo(socioId: Int, socioNombre: String) {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(PREF_SOCIO_ID, socioId)
        editor.putString(PREF_SOCIO_NOMBRE, socioNombre) // Guardar el nombre del socio
        editor.apply()
    }
}