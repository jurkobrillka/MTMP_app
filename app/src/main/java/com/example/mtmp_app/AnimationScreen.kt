package com.example.mtmp_app

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

//@Serializable
//data class ProjectilePoint(val time: Float, val x: Float, val y: Float)
@Composable
fun AnimationScreen(navController: NavController){

    val context = LocalContext.current
    val backStackEntry = remember { navController.previousBackStackEntry }
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
        Text(
            text = "x = ${points?.get(2)?.x}, y = ${points?.get(2)?.y}, t = ${points?.get(2)?.time}",
            modifier = Modifier.padding(innerPadding),
            color = Color.White
        )
        ProjectileAnimation(points)
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

    val totalDuration = (points.size - 1) * 100L // 100 ms na bod
    var playAnimation by remember { mutableStateOf(true) }

    // Aktuálny čas animácie (v ms)
    var time by remember { mutableStateOf(0L) }

    // Spusti alebo resetuj animáciu
    LaunchedEffect(playAnimation) {
        if (playAnimation) {
            time = 0L
            val frameDelay = 16L // ~60 FPS
            while (time < totalDuration) {
                delay(frameDelay)
                time += frameDelay
            }
            playAnimation = false
        }
    }

    val progress = (time.toFloat() / totalDuration.toFloat()).coerceIn(0f, 1f)
    val currentIndex = ((points.size - 1) * progress).toInt()

    val maxX = points.maxOf { it.x }
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
            val scaleX = size.width / maxX
            val scaleY = size.height / maxY

            // --- OSY ---
            drawLine(
                color = Color.Gray,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = 2f
            )
            drawLine(
                color = Color.Gray,
                start = Offset(0f, 0f),
                end = Offset(0f, size.height),
                strokeWidth = 2f
            )

            // Značky na osi X
            val stepX = maxX / 10
            for (i in 0..10) {
                val x = i * stepX * scaleX
                drawLine(
                    color = Color.Gray,
                    start = Offset(x, size.height),
                    end = Offset(x, size.height - 10),
                    strokeWidth = 1f
                )
            }

            // --- TRAJEKTÓRIA ---
            for (i in 1 until points.size) {
                val prev = points[i - 1]
                val next = points[i]
                drawLine(
                    color = Color.Cyan.copy(alpha = 0.3f),
                    start = Offset(prev.x * scaleX, size.height - prev.y * scaleY),
                    end = Offset(next.x * scaleX, size.height - next.y * scaleY),
                    strokeWidth = 2f
                )
            }

            // --- TRAIL EFEKT (stopa za guľkou) ---
            for (i in 1..currentIndex) {
                if (i >= points.size) break
                val prev = points[i - 1]
                val next = points[i]
                val alpha = i.toFloat() / currentIndex.toFloat()
                drawLine(
                    color = Color.Cyan.copy(alpha = alpha),
                    start = Offset(prev.x * scaleX, size.height - prev.y * scaleY),
                    end = Offset(next.x * scaleX, size.height - next.y * scaleY),
                    strokeWidth = 3f
                )
            }

            // --- PROJEKTIL ---
            val current = points.getOrNull(currentIndex)
            if (current != null) {
                drawCircle(
                    color = Color.Yellow,
                    radius = 10f,
                    center = Offset(current.x * scaleX, size.height - current.y * scaleY)
                )
            }
        }

        // --- OVLÁDACIE TLAČIDLÁ ---
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


