package com.example.odisea.identification

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.odisea.R

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Botón para redirigir al registro en la página web
        val registerButton = findViewById<Button>(R.id.btn_register_final)
        registerButton.setOnClickListener {
            // URL de la página de registro (cámbiala por la URL real más adelante)
            val registrationUrl = "https://www.ejemplo.com/register"

            // Crear un Intent para abrir la página web en un navegador
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(registrationUrl))
            startActivity(intent)
        }

        // Botón para ir al inicio de sesión
        val goToLoginButton = findViewById<Button>(R.id.go_to_login_button)
        goToLoginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}