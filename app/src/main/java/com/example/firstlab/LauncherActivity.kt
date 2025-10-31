package com.example.firstlab

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class LauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LauncherScreen { nombreUsuario ->
                // Cuando se presione "Jugar", abrimos MainActivity y enviamos el nombre
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("nombre_usuario", nombreUsuario)
                startActivity(intent)
            }
        }
    }

}
@Composable
fun LauncherScreen(onPlayClicked: (String) -> Unit){
    var nombre by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1️⃣ Cabecera
        Text(
            text = "Mi Aplicación",
            fontSize = 32.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 2️⃣ Campo de texto
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre de usuario") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 3️⃣ Botón "Jugar"
        Button(onClick = { onPlayClicked(nombre) }) {
            Text("Jugar")
        }
    }
}