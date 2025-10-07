package com.example.mtmp_app

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

@Composable
fun GraphScreen(navController: NavController){

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
            text = "CAUKO GRAF x = ${points?.get(2)?.x}, y = ${points?.get(2)?.y}, t = ${points?.get(2)?.time}",
            modifier = Modifier.padding(innerPadding),
            color = Color.White
        )

    }

}