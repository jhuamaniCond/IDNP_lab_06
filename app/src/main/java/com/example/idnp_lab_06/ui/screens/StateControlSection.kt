package com.example.idnp_lab_06.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.progressSemantics
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idnp_lab_06.R
import kotlin.math.roundToInt
import com.example.idnp_lab_06.ui.components.CustomCircularProgressBar
import com.example.idnp_lab_06.ui.components.CustomLoadCircleBar
import com.example.idnp_lab_06.ui.components.CustomProgressBar
import com.example.idnp_lab_06.ui.theme.AppNeutralSurface
import com.example.idnp_lab_06.ui.theme.AppNeutralTextMain
import com.example.idnp_lab_06.ui.theme.AppNeutralTextSecondary
import com.example.idnp_lab_06.ui.theme.AppPrimaryDefault

private const val PROGRESS_STEP = 10
private const val TAP_STEP = 25

/**
 * Seccion que conecta los estados visuales de los componentes Canvas con eventos
 * de usuario (botones, toque y switch) y suaviza cada cambio con una animacion.
 */
@Composable
fun StateControlSection(modifier: Modifier = Modifier) {
    // 3. Estados que modifican los eventos (fuente de verdad)
    // Porcentajes enteros evitan residuos de Float al sumar y restar repetidamente.
    var targetPercent by rememberSaveable { mutableIntStateOf(30) }
    var isLoaderActive by rememberSaveable { mutableStateOf(true) }
    var smoothTransition by rememberSaveable { mutableStateOf(true) }

    // 4. Animaciones: interpolan el valor actual hacia el valor objetivo del estado
    val animatedProgress by animateFloatAsState(
        targetValue = targetPercent / 100f,
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
    val displayedPercent = (animatedProgress * 100).roundToInt()
    val progressLabel = stringResource(R.string.progress_description)
    val circularLabel = stringResource(R.string.circular_progress_description)
    val loaderLabel = stringResource(R.string.loader_description)
    val loaderStatus = stringResource(
        if (isLoaderActive) R.string.loader_active else R.string.loader_dimmed
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

        Text(
            text = stringResource(R.string.progress_values, displayedPercent, targetPercent),
            color = AppNeutralTextMain,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.testTag("progress_values")
        )
        Text(
            text = stringResource(
                when {
                    animatedProgress != targetPercent / 100f -> R.string.progress_changing
                    targetPercent == 0 -> R.string.progress_empty
                    targetPercent == 100 -> R.string.progress_complete
                    else -> R.string.progress_partial
                }
            ),
            color = AppNeutralTextSecondary,
            fontSize = 14.sp,
            modifier = Modifier.testTag("progress_status")
        )

        // Barra lineal: su ancho depende de animatedProgress
        CustomProgressBar(
            progress = animatedProgress,
            height = 12.dp,
            modifier = Modifier
                .testTag("linear_progress")
                .progressSemantics(animatedProgress)
                .semantics { contentDescription = progressLabel }
        )

        // Botones que modifican el estado targetPercent
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { targetPercent = (targetPercent - PROGRESS_STEP).coerceAtLeast(0) },
                enabled = targetPercent > 0,
                modifier = Modifier.weight(1f)
            ) { Text("-10%") }
            Button(
                onClick = { targetPercent = (targetPercent + PROGRESS_STEP).coerceAtMost(100) },
                enabled = targetPercent < 100,
                colors = ButtonDefaults.buttonColors(containerColor = AppPrimaryDefault),
                modifier = Modifier.weight(1f)
            ) { Text("+10%") }
            OutlinedButton(
                onClick = { targetPercent = 0 },
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
                    .testTag("circular_progress")
                    .progressSemantics(animatedProgress)
                    .semantics { contentDescription = circularLabel }
                    .clickable(onClickLabel = stringResource(R.string.circular_progress_action)) {
                        targetPercent = if (targetPercent >= 100) {
                            0
                        } else {
                            (targetPercent + TAP_STEP).coerceAtMost(100)
                        }
                    }
            )

            // Spinner: transparencia y escala dependen de isLoaderActive
            CustomLoadCircleBar(
                modifier = Modifier
                    .size(56.dp)
                    .testTag("load_indicator")
                    .semantics {
                        contentDescription = loaderLabel
                        stateDescription = loaderStatus
                    },
                alpha = loaderAlpha,
                scale = loaderScale
            )
        }

        Text(
            text = stringResource(R.string.circular_progress_hint),
            color = AppNeutralTextSecondary,
            fontSize = 14.sp
        )
        Text(
            text = loaderStatus,
            color = AppNeutralTextSecondary,
            fontSize = 14.sp,
            modifier = Modifier.testTag("loader_status")
        )

        Button(
            onClick = { isLoaderActive = !isLoaderActive },
            colors = ButtonDefaults.buttonColors(containerColor = AppPrimaryDefault),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(if (isLoaderActive) R.string.dim_loader else R.string.activate_loader))
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
                onCheckedChange = { smoothTransition = it },
                modifier = Modifier
                    .testTag("smooth_transition")
                    .semantics {
                        contentDescription = "Suavizar transición"
                    }
            )
        }
        Text(
            text = stringResource(
                if (smoothTransition) R.string.smooth_transition_on else R.string.smooth_transition_off
            ),
            color = AppNeutralTextSecondary,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
    }
}

@Preview(showBackground = true, name = "State Control Section", widthDp = 360)
@Composable
fun StateControlSectionPreview() {
    StateControlSection(modifier = Modifier.padding(16.dp))
}
