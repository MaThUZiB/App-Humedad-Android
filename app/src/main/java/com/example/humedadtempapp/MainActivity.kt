package com.example.humedadtempapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SensorApp()
        }
    }
}

@Composable
fun SensorApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("inicio") { SensorScreen() }
            composable("info") { InfoScreen() }
            composable("config") { ConfigScreen() }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavItem("inicio", Icons.Default.Home, "Inicio"),
        NavItem("info", Icons.Default.Info, "Info"),
        NavItem("config", Icons.Default.Settings, "Config")
    )

    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

data class NavItem(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val label: String)


// ---------- Pantalla de sensores ----------
@Composable
fun SensorScreen() {
    val database = FirebaseDatabase.getInstance().reference.child("Sensor")

    var temperatura by remember { mutableStateOf<String?>(null) }
    var humedad by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                temperatura = snapshot.child("temperatura").getValue(Double::class.java)?.toString()
                humedad = snapshot.child("humedad").getValue(Double::class.java)?.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                temperatura = "Error"
                humedad = "Error"
            }
        })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Lectura de Sensores", fontSize = 22.sp)
            Spacer(Modifier.height(20.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🌡 Temperatura", fontSize = 18.sp)
                    Text("${temperatura ?: "Cargando..."} °C", fontSize = 24.sp)
                    Spacer(Modifier.height(10.dp))
                    Text("💧 Humedad", fontSize = 18.sp)
                    Text("${humedad ?: "Cargando..."} %", fontSize = 24.sp)
                }
            }
        }
    }
}


// ---------- Pantalla Info ----------
@Composable
fun InfoScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = """
                Aplicación de monitoreo ambiental.
                Desarrollada con Firebase y Compose.
            """.trimIndent(),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}


// ---------- Pantalla Config ----------
@Composable
fun ConfigScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Configuraciones futuras aquí.", fontSize = 18.sp)
    }
}
