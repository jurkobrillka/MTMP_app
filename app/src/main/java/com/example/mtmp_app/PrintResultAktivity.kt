package com.example.mtmp_app

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Composable
fun PrintResultScreen(navController: NavController) {
    val context = LocalContext.current
    val backStackEntry = remember { navController.previousBackStackEntry }

    val angleDegrees = backStackEntry?.savedStateHandle?.get<Float>("angle") ?: 0f
    val startSpeed = backStackEntry?.savedStateHandle?.get<Float>("startSpeed") ?: 0f

    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var points by remember { mutableStateOf<List<ProjectilePoint>>(emptyList()) }
    var maxHeight by remember { mutableStateOf(0f) }
    var range by remember { mutableStateOf(0f) }

    val retrofit = Retrofit.Builder()
        // v cmd zadajte prikaz ipconfig, vyhladajte adresu IPV4
        .baseUrl("http://192.168.0.103:8080/") //tu naimportujte adresu IPV4 vasho PC
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api = retrofit.create(CalculationApi::class.java)

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = CalculationRequest(startSpeed, angleDegrees)

                val trajectoryResponse = api.calculateTrajectory(request)
                val additionalInfoResponse = api.calculateAdditionalInfo(request)

                if (trajectoryResponse.isSuccessful && trajectoryResponse.body() != null &&
                    additionalInfoResponse.isSuccessful && additionalInfoResponse.body() != null
                ) {
                    points = trajectoryResponse.body()!!
                    maxHeight = additionalInfoResponse.body()!!.maxHeight
                    range = additionalInfoResponse.body()!!.range
                    isLoading = false
                } else {
                    error = true
                    isLoading = false
                    errorMessage = "Chyba: Trajectory response: ${trajectoryResponse.code()} ${trajectoryResponse.message()}, " +
                            "Additional info response: ${additionalInfoResponse.code()} ${additionalInfoResponse.message()}"
                    Log.e("PrintResultScreen", errorMessage)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                error = true
                isLoading = false
                errorMessage = "Výnimka pri volaní servera: ${e.localizedMessage}"
                Log.e("PrintResultScreen", errorMessage)
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFF060270),
//        floatingActionButton = {
//            FloatingActionButton(onClick = {
//                Toast.makeText(context, "Autor Juraj Brilla ZS 2025/26", Toast.LENGTH_LONG).show()
//            }) {
//                Icon(imageVector = Icons.Default.Info, contentDescription = "Info")
//            }
//        }
    ) { innerPadding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = Color.Red,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Nepodarilo sa kontaktovať server",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            else -> {
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
}
