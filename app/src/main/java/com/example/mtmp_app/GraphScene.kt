package com.example.mtmp_app

import android.graphics.Paint
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.math.roundToInt

@Composable
fun GraphScreen(navController: NavController) {

    val context = LocalContext.current
    val points = navController
        .previousBackStackEntry
        ?.savedStateHandle
        ?.get<ArrayList<ProjectilePoint>>("points")

    Scaffold(
        containerColor = Color(0xFF060270),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                Toast.makeText(context, "Autor: Juraj Brilla ZS 2025/26", Toast.LENGTH_LONG).show()
            }) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "Info")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Graf: Výška v závislosti od času",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (points.isNullOrEmpty()) {
                Text(
                    text = "Žiadne dáta na zobrazenie grafu.",
                    color = Color.White
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val padding = 60f
                        val width = size.width - padding * 2
                        val height = size.height - padding * 2

                        val maxY = (points.maxOf { it.y } * 1.1f)
                        val maxT = (points.maxOf { it.time } * 1.1f)

                        // Osi
                        drawLine(
                            color = Color.White,
                            start = Offset(padding, padding),
                            end = Offset(padding, height + padding),
                            strokeWidth = 4f
                        )
                        drawLine(
                            color = Color.White,
                            start = Offset(padding, height + padding),
                            end = Offset(width + padding, height + padding),
                            strokeWidth = 4f
                        )

                        // Popisky osí
                        val density = this@Canvas.drawContext.density
                        val textPadding = 20.dp.toPx() // 10 dp → pixely

                        drawContext.canvas.nativeCanvas.apply {
                            drawText(
                                "Výška [m]",
                                0f + textPadding, // pridáme padding zľava
                                padding + textPadding, // pridáme padding zhora
                                android.graphics.Paint().apply {
                                    color = android.graphics.Color.WHITE
                                    textSize = 36f
                                }
                            )
                            drawText(
                                "Čas [s]",
                                width / 2,
                                height + padding * 1.8f + textPadding, // pridáme padding zdola
                                android.graphics.Paint().apply {
                                    color = android.graphics.Color.WHITE
                                    textSize = 36f
                                }
                            )
                        }


                        // Číselné hodnoty (os Y)
                        val stepY = maxY / 5
                        for (i in 0..5) {
                            val yValue = stepY * i
                            val yPos = height + padding - (yValue / maxY) * height
                            drawContext.canvas.nativeCanvas.drawText(
                                yValue.roundToInt().toString(),
                                10f,
                                yPos,
                                Paint().apply {
                                    color = android.graphics.Color.WHITE
                                    textSize = 30f
                                }
                            )
                        }

                        // Číselné hodnoty (os X)
                        val stepT = maxT / 5
                        for (i in 0..5) {
                            val tValue = stepT * i
                            val xPos = padding + (tValue / maxT) * width
                            drawContext.canvas.nativeCanvas.drawText(
                                tValue.roundToInt().toString(),
                                xPos,
                                height + padding * 1.4f,
                                Paint().apply {
                                    color = android.graphics.Color.WHITE
                                    textSize = 30f
                                }
                            )
                        }

                        // Kreslenie grafu (čiary medzi bodmi)
                        val path = Path()
                        points.sortedBy { it.time }.forEachIndexed { index, p ->
                            val x = padding + (p.time / maxT) * width
                            val y = height + padding - (p.y / maxY) * height
                            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                        }

                        drawPath(
                            path = path,
                            color = Color.Cyan,
                            alpha = 1f, // plná viditeľnosť
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 6f), // čiarový štýl
                            colorFilter = null, // žiadny filter
                            blendMode = androidx.compose.ui.graphics.BlendMode.SrcOver // bežné prekreslenie
                        )
                    }
                }
            }
        }
    }
}
