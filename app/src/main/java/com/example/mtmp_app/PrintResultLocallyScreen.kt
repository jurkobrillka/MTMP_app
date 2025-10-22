package com.example.mtmp_app

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.math.*

@Composable
fun PrintResultLocallyScreen(navController: NavController) {
    val backStackEntry = remember { navController.previousBackStackEntry }

    val angleDegrees = backStackEntry?.savedStateHandle?.get<Float>("angle") ?: 0f
    val startSpeed = backStackEntry?.savedStateHandle?.get<Float>("startSpeed") ?: 0f

    var points by remember { mutableStateOf<List<ProjectilePoint>>(emptyList()) }
    var maxHeight by remember { mutableStateOf(0.0) }
    var range by remember { mutableStateOf(0.0) }
    var isCalculated by remember { mutableStateOf(false) }

    val g = 9.81f

    LaunchedEffect(Unit) {
        try {
            val angleRad = Math.toRadians(angleDegrees.toDouble())
            val v0x = startSpeed * cos(angleRad)
            val v0y = startSpeed * sin(angleRad)
            val totalTime = (2 * v0y / g)

            val localPoints = mutableListOf<ProjectilePoint>()
            var t = 0.0
            while (t <= totalTime) {
                var y = v0y * t - 0.5 * g * t * t
                if (y < 0) y = 0.0
                val x = v0x * t
                localPoints.add(ProjectilePoint(t.toFloat(), x.toFloat(), y.toFloat()))
                t += 0.1
            }

            val localMaxHeight = (startSpeed * startSpeed * sin(angleRad).pow(2)) / (2 * g)
            val localRange = (startSpeed * startSpeed * sin(2 * angleRad)) / g

            points = localPoints
            maxHeight = localMaxHeight
            range = localRange
            isCalculated = true
        } catch (e: Exception) {
            Log.e("LocalCalc", "Chyba pri výpočte: ${e.localizedMessage}")
        }
    }

    Scaffold(
        containerColor = Color(0xFF4A4AFF)
        //containerColor = Color.Cyan
    ) { innerPadding ->
        if (!isCalculated) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Info karta
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "points",
                                ArrayList(points)
                            )
                            navController.navigate("animation_screen")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Text(
                            text = "Animácia",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "points",
                                ArrayList(points)
                            )
                            navController.navigate("graph_screen")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Text(
                            text = "Graf",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.popBackStack() },
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
}


