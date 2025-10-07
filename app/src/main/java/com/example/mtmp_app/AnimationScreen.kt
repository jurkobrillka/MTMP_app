package com.example.mtmp_app

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun AnimationScreen(navController: NavController) {

    val context = LocalContext.current
    val points = navController
        .previousBackStackEntry
        ?.savedStateHandle
        ?.get<ArrayList<ProjectilePoint>>("points")

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
        Column(modifier = Modifier.padding(innerPadding)) {
            ProjectileAnimation(points)
        }
    }
}

@Composable
fun ProjectileAnimation(points: ArrayList<ProjectilePoint>?) {
    if (points.isNullOrEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF060270)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Žiadne dáta na zobrazenie",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        return
    }

    val totalDuration = (points.size - 1) * 100L
    var playAnimation by remember { mutableStateOf(true) }
    var time by remember { mutableStateOf(0L) }

    LaunchedEffect(playAnimation) {
        if (playAnimation) {
            time = 0L
            val frameDelay = 16L
            while (time < totalDuration) {
                delay(frameDelay)
                time += frameDelay
            }
            playAnimation = false
        }
    }

    val progress = (time.toFloat() / totalDuration.toFloat()).coerceIn(0f, 1f)
    val currentIndex = ((points.size - 1) * progress).toInt()

    val maxT = points.maxOf { it.time }
    val maxY = points.maxOf { it.y }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF060270)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp)
        ) {
            val padding = 60f
            val width = size.width - padding * 2
            val height = size.height - padding * 2
            val scaleX = width / maxT
            val scaleY = height / maxY


            drawLine(
                color = Color.White,
                start = Offset(padding, padding),
                end = Offset(padding, height + padding),
                strokeWidth = 3f
            )
            drawLine(
                color = Color.White,
                start = Offset(padding, height + padding),
                end = Offset(width + padding, height + padding),
                strokeWidth = 3f
            )

            val density = this@Canvas.drawContext.density
            val textPadding = 20.dp.toPx()


            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "Čas [s]",
                    width / 2,
                    height + padding * 1.8f + textPadding,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 36f
                    }
                )
                drawText(
                    "Výška [m]",
                    0f + textPadding,
                    padding + textPadding,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 36f
                    }
                )
            }


            val stepY = maxY / 5
            for (i in 0..5) {
                val yValue = stepY * i
                val yPos = height + padding - (yValue / maxY) * height
                drawContext.canvas.nativeCanvas.drawText(
                    yValue.roundToInt().toString(),
                    10f,
                    yPos,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 30f
                    }
                )
            }

            val stepT = maxT / 5
            for (i in 0..5) {
                val tValue = stepT * i
                val xPos = padding + (tValue / maxT) * width
                drawContext.canvas.nativeCanvas.drawText(
                    tValue.roundToInt().toString(),
                    xPos,
                    height + padding * 1.4f,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 30f
                    }
                )
            }


            for (i in 1 until points.size) {
                val prev = points[i - 1]
                val next = points[i]
                drawLine(
                    color = Color.Cyan.copy(alpha = 0.3f),
                    start = Offset(padding + prev.time * scaleX, height + padding - prev.y * scaleY),
                    end = Offset(padding + next.time * scaleX, height + padding - next.y * scaleY),
                    strokeWidth = 2f
                )
            }


            for (i in 1..currentIndex) {
                if (i >= points.size) break
                val prev = points[i - 1]
                val next = points[i]
                val alpha = i.toFloat() / currentIndex.toFloat()
                drawLine(
                    color = Color.Cyan.copy(alpha = alpha),
                    start = Offset(padding + prev.time * scaleX, height + padding - prev.y * scaleY),
                    end = Offset(padding + next.time * scaleX, height + padding - next.y * scaleY),
                    strokeWidth = 3f
                )
            }


            val current = points.getOrNull(currentIndex)
            if (current != null) {
                drawCircle(
                    color = Color.Yellow,
                    radius = 10f,
                    center = Offset(padding + current.time * scaleX, height + padding - current.y * scaleY)
                )
            }
        }

        Text(
            text = "Čas: ${(time / 1000f).coerceAtMost(totalDuration / 1000f).toString().take(4)} s",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 24.dp)
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { playAnimation = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "Zopakovať animáciu",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

