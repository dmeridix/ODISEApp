package com.example.odisea.identification

import android.content.Intent
import android.content.SharedPreferences
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

    // Clave para SharedPreferences
    private val PREFS_NAME = "MyAppPrefs"
    private val PREF_SOCIO_ID = "socio_id"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.btn_login)
        val goToRegisterButton = findViewById<Button>(R.id.go_to_register_button)
        val etEmail = findViewById<EditText>(R.id.et_username)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val tvEmailError = findViewById<TextView>(R.id.tv_email_error)
        val tvPasswordError = findViewById<TextView>(R.id.tv_password_error)

        // Botón para iniciar sesión
        loginButton.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Limpiar mensajes de error previos
            tvEmailError.visibility = android.view.View.GONE
            tvPasswordError.visibility = android.view.View.GONE

            var hasError = false

            if (email.isEmpty()) {
                tvEmailError.visibility = android.view.View.VISIBLE
                hasError = true
            }

            if (password.isEmpty()) {
                tvPasswordError.visibility = android.view.View.VISIBLE
                hasError = true
            }

            if (hasError) {
                return@setOnClickListener
            }

            // Llamar al endpoint para validar las credenciales
            val apiService = RetrofitClient.apiService
            apiService.buscarSocio(email, password).enqueue(object : Callback<Map<String, Any>> {
                override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                    if (response.isSuccessful && response.body() != null) {
                        // Inicio de sesión exitoso
                        val socioId = response.body()?.get("socio_id") as Int

                        // Guardar el ID del socio en SharedPreferences
                        saveSocioId(socioId)

                        // Redirigir a MainActivity
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        // Mostrar mensaje de error si las credenciales son inválidas
                        tvPasswordError.text = "Correo o contraseña incorrectos"
                        tvPasswordError.visibility = android.view.View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    // Manejar errores de conexión
                    tvPasswordError.text = "Error al conectar con el servidor"
                    tvPasswordError.visibility = android.view.View.VISIBLE
                    t.printStackTrace()
                }
            })
        }

        // Botón para ir al registro
        goToRegisterButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    // Método para guardar el ID del socio en SharedPreferences
    private fun saveSocioId(socioId: Int) {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(PREF_SOCIO_ID, socioId)
        editor.apply()
    }
}