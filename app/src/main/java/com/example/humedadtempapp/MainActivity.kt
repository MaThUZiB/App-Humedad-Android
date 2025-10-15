package com.example.humedadtempapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.database.FirebaseDatabase
import android.util.Log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Aquí podrías poner tu UI Compose
        }

        // 🔹 Obtener referencia de Firebase
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("mensaje") // Referencia "mensaje"

        // 🔹 Escribir un valor de prueba
        myRef.setValue("¡Hola Firebase desde Compose!")
            .addOnSuccessListener {
                Toast.makeText(this, "Datos enviados correctamente", Toast.LENGTH_SHORT).show()
                Log.d("FirebaseTest", "Datos enviados correctamente")
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al enviar datos", Toast.LENGTH_SHORT).show()
                Log.e("FirebaseTest", "Error al enviar datos", e)
            }

        // 🔹 Leer el valor de prueba
        myRef.get()
            .addOnSuccessListener { snapshot ->
                val valor = snapshot.getValue(String::class.java)
                Toast.makeText(this, "Valor leído: $valor", Toast.LENGTH_SHORT).show()
                Log.d("FirebaseTest", "Valor leído: $valor")
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al leer datos", Toast.LENGTH_SHORT).show()
                Log.e("FirebaseTest", "Error al leer datos", e)
            }
    }
}
