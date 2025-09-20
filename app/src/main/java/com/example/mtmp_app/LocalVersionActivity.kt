package com.example.mtmp_app

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun LocalVersionScreen() {
    Scaffold(
        containerColor = Color(0xFF060270),
        floatingActionButton = {
            val context = LocalContext.current
            FloatingActionButton(
                onClick = {
                    Toast.makeText(
                        context,
                        "Autor Juraj Brilla ZS 2025/26",
                        Toast.LENGTH_LONG
                    ).show()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var textNumber by remember { mutableStateOf("") }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier
                    .height(100.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    TextField(
                        value = textNumber,
                        onValueChange = { textNumber = it },
                        placeholder = { Text("Zadaj číslo") },
                        singleLine = true
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            var textLetter by remember { mutableStateOf("") }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier
                    .height(100.dp)

            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    TextField(
                        value = textLetter,
                        onValueChange = { textLetter = it },
                        placeholder = { Text("Zadaj písmeno") },
                        singleLine = true
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            var textSymbol by remember { mutableStateOf("") }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier
                    .height(100.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    TextField(
                        value = textSymbol,
                        onValueChange = { textSymbol = it },
                        placeholder = { Text("Zadaj znak") },
                        singleLine = true
                    )
                }
            }
        }
    }
}
