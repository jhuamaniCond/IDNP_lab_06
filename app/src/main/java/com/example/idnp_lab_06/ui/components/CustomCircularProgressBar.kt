package com.example.idnp_lab_06.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.idnp_lab_06.ui.theme.AppPrimaryLight
import com.example.idnp_lab_06.ui.theme.AppPrimaryDefault

@Composable
fun CustomCircularProgressBar(
    progress: Float, // Valor entre 0.0f y 1.0f
    modifier: Modifier = Modifier.size(64.dp),
    trackColor: Color = AppPrimaryLight,
    progressColor: Color = AppPrimaryDefault,
    strokeWidth: Dp = 8.dp
) {
    val clampedProgress = progress.coerceIn(0f, 1f)

    Canvas(modifier = modifier) {
        val strokePx = strokeWidth.toPx()
        val arcSize = Size(size.width - strokePx, size.height - strokePx)
        val topLeft = Offset(strokePx / 2, strokePx / 2)

        // Circulo de Fondo
        drawArc(
            color = trackColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokePx)
        )

        // Arco de Progreso (Inicia a las 12 hrs: -90 grados)
        drawArc(
            color = progressColor,
            startAngle = -90f,
            sweepAngle = 360f * clampedProgress,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokePx, cap = StrokeCap.Round)
        )
    }
}


@Preview(showBackground = true, name = "Custom Circular Progress Bar")
@Composable
fun CustomCircularProgressBarPreview() {
    Surface(modifier = Modifier.padding(16.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "0%"
                )
                // Estado vacío (0%)
                CustomCircularProgressBar(progress = 0.0f)
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "50%"
                )
                // Estado a la mitad (50%)
                CustomCircularProgressBar(progress = 0.5f)
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "100%"
                )
                // Estado completo (100%)
                CustomCircularProgressBar(progress = 1.0f)
            }
        }
    }
}