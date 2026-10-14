package com.example.idnp_lab_06.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.idnp_lab_06.ui.theme.AppPrimaryDefault
import com.example.idnp_lab_06.ui.theme.AppPrimaryLight
@Composable
fun CustomLoadCircleBar(
    modifier: Modifier = Modifier.size(48.dp),
    alpha: Float = 1f,           // <--- Nueva propiedad visual vinculable
    scale: Float = 1f,           // <--- Nueva propiedad visual vinculable
    color: Color = AppPrimaryDefault,
    trackColor: Color = AppPrimaryLight,
    strokeWidth: Dp = 5.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "load_circle_transition")

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation_anim"
    )

    Canvas(
        modifier = modifier
            .rotate(rotationAngle)
            .graphicsLayer {
                this.alpha = alpha
                this.scaleX = scale
                this.scaleY = scale
            }
    ) {
        val strokePx = strokeWidth.toPx()
        val arcSize = Size(size.width - strokePx, size.height - strokePx)
        val topLeft = Offset(strokePx / 2, strokePx / 2)

        // Anillo de Fondo (Track)
        drawArc(
            color = trackColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokePx)
        )

        // 1. Creamos el gradiente de barrido para el efecto cometa
        // El arco mide 270 grados, lo que equivale al 75% de un círculo (270 / 360 = 0.75)
        val cometBrush = Brush.sweepGradient(
            // La cola empieza transparente (0.0) y se intensifica hasta la cabeza (0.75)
            0.0f to Color.Transparent,
            0.75f to color,
            // El 25% restante del círculo debe ser transparente para que no se encime el inicio con el fin
            0.751f to Color.Transparent,
            1.0f to Color.Transparent,
            center = center
        )

        // 2. Dibujamos el arco utilizando el Brush en lugar de un color estático
        drawArc(
            brush = cometBrush,
            startAngle = 0f,
            sweepAngle = 270f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            // Cambiamos a StrokeCap.Square o lo removemos si queremos la punta recta,
            // ya que el difuminado limpia la cola de forma nativa.
            style = Stroke(width = strokePx, cap = StrokeCap.Round)
        )
    }
}


@Preview(showBackground = true, name = "Custom Load Circle Bar")
@Composable
fun CustomLoadCircleBarPreview() {
    Surface(modifier = Modifier.padding(16.dp)) {
        CustomLoadCircleBar()
    }
}