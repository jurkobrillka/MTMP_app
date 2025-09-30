package com.example.mtmp_app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.widget.Toast
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

data class ProjectilePoint(val time: Float, val x: Float, val y: Float)

@Composable
fun PrintResultScreen(navController: NavController) {
    val context = LocalContext.current

    val backStackEntry = remember { navController.previousBackStackEntry }
    val angleDegrees = backStackEntry?.savedStateHandle?.get<Float>("angle") ?: 0f
    val startSpeed = backStackEntry?.savedStateHandle?.get<Float>("startSpeed") ?: 0f


    // vzorce pre v0x a v0y ktore sa menit nebudu, bude sa menit len cas
    // source: https://cs.wikipedia.org/wiki/Vrh_%C5%A1ikm%C3%BD
    val g = 9.81f
    val angleRad = angleDegrees * (PI.toFloat() / 180f)
    val v0x = startSpeed * cos(angleRad)
    val v0y = startSpeed * sin(angleRad)

    // vzorce pre vypocet max Vysky a dlzku vrhu
    // source: https://cs.wikipedia.org/wiki/Vrh_%C5%A1ikm%C3%BD
    val maxHeight = (startSpeed * startSpeed * sin(angleRad) * sin(angleRad)) / (2 * g)
    val range = (startSpeed * startSpeed * sin(2 * angleRad)) / g


    // vzorec pre cas letu
    // source: https://www.dopocitej.cz/vrh_sikmy.html
    val totalTime = (2 * v0y) / g

    // Tu sa meni cas, generovanie bodov po 0.1 s
    val points = remember {
        mutableStateListOf<ProjectilePoint>().apply {
            var t = 0f
            while (t <= totalTime) {
                //vzorce pre vypocitanie suradnic x,y pre dany cas t ktory kazdou iteraciou zvacsujeme o 0.1sek
                //source: https://cs.wikipedia.org/wiki/Vrh_%C5%A1ikm%C3%BD
                val x = v0x * t
                val y = v0y * t - 0.5f * g * t * t
                add(ProjectilePoint(t, x, y))
                t += 0.1f
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFF060270),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                Toast.makeText(context, "Autor Juraj Brilla ZS 2025/26", Toast.LENGTH_LONG).show()
            }) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "Info")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Uhol hodu: $angleDegrees°",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Počiatočná rýchlosť: $startSpeed m/s",
                            fontSize = 22.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
            ) {
                items(points) { point ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E8F))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Čas: %.1fs".format(point.time),
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Výška: %.2f m".format(point.y),
                                color = Color.White
                            )
                            Text(
                                text = "Dĺžka: %.2f m".format(point.x),
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E8F))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {

                    Text(
                        text = "Max. výška: %.2f m".format(maxHeight),
                        color = Color.White
                    )
                    Text(
                        text = "Dĺžka (rozmedzie hodu): %.2f m".format(range),
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "Hoď ešte raz",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    }
}
