package com.example.idnp_lab_06

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AnimationScreenIntegrationTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private fun capture(name: String) {
        rule.waitForIdle()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val directory = File(context.getExternalFilesDir(null), "evidence").apply { mkdirs() }
        File(directory, "$name.png").outputStream().use { output ->
            rule.onRoot().captureToImage().asAndroidBitmap()
                .compress(Bitmap.CompressFormat.PNG, 100, output)
        }
    }

    @Test
    fun explanatoryIndicatorsWorkInsideTheRealScreen() {
        rule.mainClock.autoAdvance = false
        rule.onNodeWithText("Reiniciar").performScrollTo().performClick()
        rule.mainClock.advanceTimeBy(1000)
        rule.onNodeWithTag("progress_values")
            .assertTextEquals("Avance visible: 0% · Objetivo: 0%")
        rule.onNodeWithTag("progress_values").performScrollTo()
        capture("01-vacio")
        rule.onNodeWithText("+10%").performScrollTo().performClick()
        rule.mainClock.advanceTimeBy(1000)
        rule.onNodeWithTag("progress_values")
            .assertTextEquals("Avance visible: 10% · Objetivo: 10%")
        repeat(9) {
            rule.onNodeWithText("+10%").performScrollTo().performClick()
            rule.mainClock.advanceTimeByFrame()
        }
        rule.mainClock.advanceTimeBy(1000)
        rule.onNodeWithTag("progress_values")
            .assertTextEquals("Avance visible: 100% · Objetivo: 100%")
        rule.onNodeWithTag("progress_values").performScrollTo()
        capture("02-completo")

        rule.onNodeWithText("Reiniciar").performScrollTo().performClick()
        rule.mainClock.advanceTimeBy(200)
        val intermediate = rule.onNodeWithTag("linear_progress").fetchSemanticsNode()
            .config[SemanticsProperties.ProgressBarRangeInfo].current
        assertTrue(intermediate > 0f && intermediate < 1f)
        rule.onNodeWithTag("progress_values").performScrollTo()
        capture("03-transicion")

        rule.mainClock.advanceTimeBy(1000)
        rule.onNodeWithText("Atenuar indicador").performScrollTo().performClick()
        rule.mainClock.advanceTimeBy(3000)
        rule.onNodeWithTag("loader_status").performScrollTo()
        capture("04-atenuado")
    }
}
