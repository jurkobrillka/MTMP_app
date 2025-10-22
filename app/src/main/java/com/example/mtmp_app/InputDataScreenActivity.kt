package com.example.mtmp_app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.widget.Toast
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun InputDataScreen(navController: NavController) {
    val context = LocalContext.current

    var startSpeed by remember { mutableStateOf(0f) }
    var throwAngle by remember { mutableStateOf(0f) }

    Scaffold(
        containerColor = Color(0xFF060270),
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
//                    Toast.makeText(context, "Autor Juraj Brilla ZS 2025/26", Toast.LENGTH_LONG).show()
//                }
//            ) {
//                Icon(imageVector = Icons.Default.Info, contentDescription = "Info")
//            }
//        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Počiatočná rýchlosť
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    TextField(
                        value = if (startSpeed == 0f) "" else startSpeed.toString(),
                        onValueChange = { input -> startSpeed = input.toFloatOrNull() ?: 0f },
                        placeholder = { Text("Zadaj počiatočnú rýchlosť (m/s)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Uhol hodu
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    TextField(
                        value = if (throwAngle == 0f) "" else throwAngle.toString(),
                        onValueChange = { input -> throwAngle = input.toFloatOrNull() ?: 0f },
                        placeholder = { Text("Zadaj uhol hodu (stupne)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("angle", throwAngle)
                        navController.currentBackStackEntry?.savedStateHandle?.set("startSpeed", startSpeed)
                        navController.navigate("print_result_server")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = "Hoď šíp na server",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("angle", throwAngle)
                        navController.currentBackStackEntry?.savedStateHandle?.set("startSpeed", startSpeed)
                        navController.navigate("print_result_locally")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = "Hoď šíp v mobile",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }
            }

        }
    }
}
