package com.example.idnp_lab_06.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idnp_lab_06.ui.components.CustomCircularProgressBar
import com.example.idnp_lab_06.ui.components.CustomLoadCircleBar
import com.example.idnp_lab_06.ui.components.CustomProgressBar
import com.example.idnp_lab_06.ui.theme.AppNeutralSurface
import com.example.idnp_lab_06.ui.theme.AppNeutralTextMain
import com.example.idnp_lab_06.ui.theme.AppNeutralTextSecondary
import com.example.idnp_lab_06.ui.theme.AppPrimaryDefault

private const val PROGRESS_STEP = 0.10f
private const val TAP_STEP = 0.25f

/**
 * Seccion que conecta los estados visuales de los componentes Canvas con eventos
 * de usuario (botones, toque y switch) y suaviza cada cambio con una animacion.
 */
@Composable
fun StateControlSection(modifier: Modifier = Modifier) {
    // 3. Estados que modifican los eventos (fuente de verdad)
    var targetProgress by rememberSaveable { mutableFloatStateOf(0.30f) }
    var isLoaderActive by rememberSaveable { mutableStateOf(true) }
    var smoothTransition by rememberSaveable { mutableStateOf(true) }

    // 4. Animaciones: interpolan el valor actual hacia el valor objetivo del estado
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = if (smoothTransition) {
            tween(durationMillis = 600, easing = FastOutSlowInEasing)
        } else {
            snap()
        },
        label = "progress_anim"
    )
    val loaderAlpha by animateFloatAsState(
        targetValue = if (isLoaderActive) 1f else 0.25f,
        animationSpec = if (smoothTransition) tween(durationMillis = 400) else snap(),
        label = "loader_alpha_anim"
    )
    val loaderScale by animateFloatAsState(
        targetValue = if (isLoaderActive) 1f else 0.6f,
        animationSpec = if (smoothTransition) {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        } else {
            snap()
        },
        label = "loader_scale_anim"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppNeutralSurface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Control por eventos",
            color = AppNeutralTextMain,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        // Barra lineal: su ancho depende de animatedProgress
        CustomProgressBar(progress = animatedProgress, height = 12.dp)

        // Botones que modifican el estado targetProgress
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { targetProgress = (targetProgress - PROGRESS_STEP).coerceAtLeast(0f) },
                enabled = targetProgress > 0f,
                modifier = Modifier.weight(1f)
            ) { Text("-10%") }
            Button(
                onClick = { targetProgress = (targetProgress + PROGRESS_STEP).coerceAtMost(1f) },
                enabled = targetProgress < 1f,
                colors = ButtonDefaults.buttonColors(containerColor = AppPrimaryDefault),
                modifier = Modifier.weight(1f)
            ) { Text("+10%") }
            OutlinedButton(
                onClick = { targetProgress = 0f },
                modifier = Modifier.weight(1f)
            ) { Text("Reiniciar") }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Evento de toque directo sobre el Canvas circular: avanza 25% y vuelve a 0 al llenarse
            CustomCircularProgressBar(
                progress = animatedProgress,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .clickable {
                        targetProgress = if (targetProgress >= 1f) {
                            0f
                        } else {
                            (targetProgress + TAP_STEP).coerceAtMost(1f)
                        }
                    }
            )

            // Spinner: transparencia y escala dependen de isLoaderActive
            CustomLoadCircleBar(
                modifier = Modifier.size(56.dp),
                alpha = loaderAlpha,
                scale = loaderScale
            )
        }

        Button(
            onClick = { isLoaderActive = !isLoaderActive },
            colors = ButtonDefaults.buttonColors(containerColor = AppPrimaryDefault),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoaderActive) "Pausar carga" else "Reanudar carga")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Suavizar transición",
                color = AppNeutralTextSecondary,
                fontSize = 14.sp
            )
            Switch(
                checked = smoothTransition,
                onCheckedChange = { smoothTransition = it }
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
}

@Preview(showBackground = true, name = "State Control Section", widthDp = 360)
@Composable
fun StateControlSectionPreview() {
    StateControlSection(modifier = Modifier.padding(16.dp))
}
