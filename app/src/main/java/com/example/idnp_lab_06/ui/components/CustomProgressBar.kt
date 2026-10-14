package com.example.idnp_lab_06.ui.components

import android.view.Surface
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.idnp_lab_06.ui.theme.AppPrimaryDark
import com.example.idnp_lab_06.ui.theme.AppPrimaryLight
import com.example.idnp_lab_06.ui.theme.AppPrimaryDefault

@Composable
fun CustomProgressBar(
    progress: Float, // Valor entre 0.0f y 1.0f
    alpha: Float = 1f,           // <--- Nueva propiedad visual vinculable
    scale: Float = 1f,           // <--- Nueva propiedad visual vinculable
    modifier: Modifier = Modifier,
    trackColor: Color = AppPrimaryLight,
    progressColor: Color = AppPrimaryDefault,
    stripeColor: Color = AppPrimaryDark,
    height: Dp = 10.dp
) {
    val clampedProgress = progress.coerceIn(0f, 1f)

    // 1. Creamos una animación infinita para el desplazamiento de las franjas
    val infiniteTransition = rememberInfiniteTransition(label = "StripeTransition")
    val stripeOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 60f, // Define el ancho del ciclo del patrón antes de repetirse
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "StripeOffset"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            // Vinculación de Escala y Transparencia en la capa gráfica del sistema
            .graphicsLayer {
                this.alpha = alpha
                this.scaleX = scale
                this.scaleY = scale
            }
    ) {
        val cornerRadius = size.height / 2

        // Fondo del Track
        drawRoundRect(
            color = trackColor,
            size = size,
            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
        )

        // Indicador de Progreso
        val progressWidth = size.width * clampedProgress
        if (progressWidth > 0f) {

            // 2. Creamos un gradiente lineal repetitivo con franjas diagonales intercaladas
            val stripeBrush = Brush.linearGradient(
                0.0f to progressColor,
                0.4f to progressColor,
                0.4f to stripeColor,
                0.5f to stripeColor,
                0.9f to stripeColor,
                0.9f to progressColor,
                1.0f to progressColor,
                // El Offset controla el ángulo diagonal (x e y) y sumamos el stripeOffset para animarlo
                start = Offset(stripeOffset, stripeOffset),
                end = Offset(stripeOffset + 40f, stripeOffset + 40f),
                tileMode = TileMode.Repeated // <--- Clave: Hace que el patrón se repita infinitamente
            )

            // 3. Dibujamos el progreso usando el Brush animado en lugar de un único color
            drawRoundRect(
                brush = stripeBrush,
                size = Size(width = progressWidth, height = size.height),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )
        }
    }
}

@Preview(showBackground = true, name = "Custom Progress Bar")
@Composable
fun CustomProgressBarPreview() {
    Surface(modifier = Modifier.padding(16.dp)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "0%"
            )
            // Estado vacío (0%)
            CustomProgressBar(progress = 0.0f)
            Text(
                text = "50%"
            )
            // Estado a la mitad (50%)
            CustomProgressBar(progress = 0.5f)
            Text(
                text = "100%"
            )
            // Estado completo (100%)
            CustomProgressBar(progress = 1.0f)
        }
    }
}
