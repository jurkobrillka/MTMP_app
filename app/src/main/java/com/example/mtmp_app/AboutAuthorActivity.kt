package com.example.mtmp_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AboutAuthorActivity(navController: NavController) {
    Scaffold(
        containerColor = Color(0xFFEFEFEF)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4A90E2))
            ) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        text = "Android časť (cca 9 bodov)",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Pomocou Android Studio vytvorte mobilnú aplikáciu, ktorá bude realizovať šikmý vrh, " +
                                "t.j. zadávanie parametrov (počiatočná rýchlosť, uhol výstrelu), výpočet a vizualizáciu. " +
                                "Pod vizualizáciou sa rozumie numerický výpis – čas, pozícia x, pozícia y, ďalej animácia a " +
                                "vykreslenie grafu (závislosti výšky y od času – teda y(t) a nie y(x)).",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF50E3C2))
            ) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        text = "Klient-server časť (cca 6 bodov)",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Realizujte serverovú časť aplikácie šikmého vrhu, ktorej úlohou bude prijať vstupné údaje " +
                                "od klienta (počiatočná rýchlosť a uhol výstrelu) a generovať časové hodnoty trajektórie " +
                                "(t.j. vzdialenosť a výšku letu) a tieto hodnoty posielať späť klientovi. " +
                                "Klientská časť aplikácie umožní zadávať vstupné údaje, odosielať ich na server a prijímať " +
                                "údaje zo servera, ktoré bude vhodným spôsobom vizualizovať (numerický výpis prijatých hodnôt, " +
                                "animácia, graf). Klient má byť realizovaný ako mobilná aplikácia, pričom sa dá využiť " +
                                "aplikácia vytvorená v predošlej časti.",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
