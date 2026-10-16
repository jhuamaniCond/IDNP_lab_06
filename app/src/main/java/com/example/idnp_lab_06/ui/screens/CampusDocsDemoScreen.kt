package com.example.idnp_lab_06.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idnp_lab_06.ui.components.CustomCircularProgressBar
import com.example.idnp_lab_06.ui.components.CustomLoadCircleBar
import com.example.idnp_lab_06.ui.components.CustomProgressBar
import com.example.idnp_lab_06.ui.components.WaveComponent
import com.example.idnp_lab_06.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class StudentNote(
    val id: Int,
    val title: String,
    val author: String,
    val course: String,
    val fileSize: String,
    var progress: Float = 0f,
    var isDownloading: Boolean = false
)

@Composable
fun CampusDocsDemoScreen(
    onBackToComponents: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var isLoadingFeed by remember { mutableStateOf(false) }
    
    val notes = remember {
        mutableStateListOf(
            StudentNote(1, "Estructuras de Datos - Árboles AVL", "Juan Pérez", "Ingeniería de Software", "4.2 MB", 1f, false),
            StudentNote(2, "Cálculo III - Integrales Dobles y Triples", "María Gómez", "Matemática III", "6.8 MB", 0.45f, true),
            StudentNote(3, "Bases de Datos - Normalización y SQL", "Carlos Ruiz", "Base de Datos I", "3.1 MB", 0f, false),
            StudentNote(4, "Arquitectura de Software - Patrones MVVM", "Ana Torres", "IDNP", "5.5 MB", 1f, false)
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppNeutralBackground)
    ) {
        WaveComponent()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header / Top bar with back button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "CampusDocs",
                        color = AppNeutralSurface,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Apuntes compartidos por estudiantes",
                        color = AppNeutralSurface.copy(alpha = 0.8f),
                        fontSize = 13.sp
                    )
                }
                Button(
                    onClick = onBackToComponents,
                    colors = ButtonDefaults.buttonColors(containerColor = AppNeutralSurface)
                ) {
                    Text("Ver Laboratorio", color = AppPrimaryDefault)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main Content Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AppNeutralSurface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Apuntes Recientes de la UNSA",
                            color = AppNeutralTextMain,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (isLoadingFeed) {
                            CustomLoadCircleBar(modifier = Modifier.size(24.dp))
                        }
                    }

                    Text(
                        text = "Toca cualquier apunte para simular la descarga con barra de progreso Canvas.",
                        color = AppNeutralTextSecondary,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Notes list
                    notes.forEach { note ->
                        NoteItemView(
                            note = note,
                            onDownloadClick = {
                                if (!note.isDownloading && note.progress < 1f) {
                                    note.isDownloading = true
                                    coroutineScope.launch {
                                        while (note.progress < 1f) {
                                            delay(150)
                                            note.progress = (note.progress + 0.1f).coerceAtMost(1f)
                                        }
                                        note.isDownloading = false
                                    }
                                } else if (note.progress >= 1f) {
                                    // Reset to simulate re-download
                                    note.progress = 0f
                                    note.isDownloading = true
                                    coroutineScope.launch {
                                        while (note.progress < 1f) {
                                            delay(150)
                                            note.progress = (note.progress + 0.1f).coerceAtMost(1f)
                                        }
                                        note.isDownloading = false
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NoteItemView(
    note: StudentNote,
    onDownloadClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppNeutralBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = note.title,
                        color = AppNeutralTextMain,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Autor: ${note.author} • ${note.course} (${note.fileSize})",
                        color = AppNeutralTextSecondary,
                        fontSize = 12.sp
                    )
                }

                // Circular Progress Button / Indicator
                Box(
                    modifier = Modifier.size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (note.isDownloading) {
                        CustomCircularProgressBar(
                            progress = note.progress,
                            modifier = Modifier.size(36.dp)
                        )
                    } else {
                        IconButton(onClick = onDownloadClick) {
                            Text(
                                text = if (note.progress >= 1f) "✓" else "⬇",
                                color = AppPrimaryDefault,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            if (note.isDownloading || (note.progress > 0f && note.progress < 1f)) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (note.progress >= 1f) "Descargado" else "Descargando...",
                            color = AppPrimaryDefault,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "${(note.progress * 100).toInt()}%",
                            color = AppNeutralTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                    CustomProgressBar(
                        progress = note.progress,
                        height = 6.dp
                    )
                }
            }
        }
    }
}
