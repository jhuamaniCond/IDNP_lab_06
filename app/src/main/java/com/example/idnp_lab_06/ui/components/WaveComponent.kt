package com.example.idnp_lab_06.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.idnp_lab_06.ui.theme.AppPrimaryDefault
import com.example.idnp_lab_06.ui.theme.AppPrimaryLight
import com.example.idnp_lab_06.ui.theme.AppPrimaryDark


@Composable
fun WaveComponent(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(220.dp),
    layerBack: Color = AppPrimaryLight,
    layerMiddle: Color = AppPrimaryDark,
    layerFront: Color = AppPrimaryDefault
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Capa 1 (Fondo - Claro)
        val pathBack = Path().apply {
            moveTo(0f, 0f)
            lineTo(0f, height * 0.75f)
            cubicTo( // Dibuja curvas
                width * 0.25f, height * 0.95f,
                width * 0.60f, height * 0.55f,
                width, height * 0.85f
            )
            lineTo(width, 0f)
            close()
        }
        drawPath(path = pathBack, color = layerBack)

        // Capa 2 (Intermedia - Oscuro)
        val pathMiddle = Path().apply {
            moveTo(0f, 0f)
            lineTo(0f, height * 0.55f)
            cubicTo(
                width * 0.35f, height * 0.82f,
                width * 0.70f, height * 0.38f,
                width, height * 0.65f
            )
            lineTo(width, 0f)
            close()
        }
        drawPath(path = pathMiddle, color = layerMiddle.copy(alpha = 0.85f))

        // Capa 3 (Frontal - Primario Default)
        val pathFront = Path().apply {
            moveTo(0f, 0f)
            lineTo(0f, height * 0.40f)
            cubicTo(
                width * 0.30f, height * 0.65f,
                width * 0.65f, height * 0.25f,
                width, height * 0.50f
            )
            lineTo(width, 0f)
            close()
        }
        drawPath(path = pathFront, color = layerFront)
    }
}

@Preview(showBackground = true, name = "Wave Component")
@Composable
fun WaveComponentPreview() {
    Surface(modifier = Modifier.padding(16.dp)) {
        WaveComponent()
    }
}