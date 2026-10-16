package com.example.idnp_lab_06.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idnp_lab_06.ui.theme.AppNeutralBackground
import com.example.idnp_lab_06.ui.theme.AppNeutralSurface
import com.example.idnp_lab_06.ui.theme.AppNeutralTextMain
import com.example.idnp_lab_06.ui.theme.AppPrimaryDefault
import com.example.idnp_lab_06.ui.components.WaveComponent
import com.example.idnp_lab_06.ui.components.CustomProgressBar
import com.example.idnp_lab_06.ui.components.CustomLoadCircleBar
import com.example.idnp_lab_06.ui.components.CustomCircularProgressBar

//@Preview(showBackground = true, widthDp = 360, heightDp = 740)
@Composable
fun ComponentsDesignPreview(
    modifier: Modifier,
    onNavigateToCampusDocs: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppNeutralBackground)
    ) {
        // Fondo Ondulado (WaveComponent)
        WaveComponent()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Diseño de Componentes",
                    color = AppNeutralSurface,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = onNavigateToCampusDocs,
                    colors = ButtonDefaults.buttonColors(containerColor = AppNeutralSurface)
                ) {
                    Text("CampusDocs 🚀", color = AppPrimaryDefault, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            // 0. Componentes controlados por eventos y animados
            StateControlSection()

            // 1. ProgressBar
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("CustomProgressBar (65%)", color = AppNeutralTextMain, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                CustomProgressBar(progress = 0.65f)
            }

            // 2. CircularProgressBar
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("CustomCircularProgressBar (80%)", color = AppNeutralTextMain, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(12.dp))
                CustomCircularProgressBar(progress = 0.80f)
            }

            // 3. LoadCircleBar
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("CustomLoadCircleBar (Indeterminado)", color = AppNeutralTextMain, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(12.dp))
                CustomLoadCircleBar()
            }
        }
    }
}