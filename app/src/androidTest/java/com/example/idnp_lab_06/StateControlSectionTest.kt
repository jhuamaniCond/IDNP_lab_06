package com.example.idnp_lab_06

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.example.idnp_lab_06.ui.screens.StateControlSection
import com.example.idnp_lab_06.ui.theme.IDNP_lab_06Theme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/** Pruebas de los controles reales con reloj manual, sin esperas de tiempo arbitrarias. */
class StateControlSectionTest {
    @get:Rule
    val rule = createComposeRule()

    private lateinit var restoration: StateRestorationTester

    @Before
    fun setUp() {
        rule.mainClock.autoAdvance = false
        restoration = StateRestorationTester(rule)
        restoration.setContent {
            IDNP_lab_06Theme {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    StateControlSection()
                }
            }
        }
        settle()
    }

    private fun settle() {
        // Incluye tween (600 ms) y la convergencia del spring del indicador.
        rule.mainClock.advanceTimeBy(3000)
        rule.waitForIdle()
    }

    private fun click(text: String) {
        rule.onNodeWithText(text).performScrollTo().performClick()
        rule.mainClock.advanceTimeByFrame()
    }

    private fun progress(tag: String = "linear_progress"): Float =
        rule.onNodeWithTag(tag).fetchSemanticsNode()
            .config[SemanticsProperties.ProgressBarRangeInfo].current

    private fun assertProgress(expected: Float) {
        assertEquals(expected, progress(), 0.0001f)
        assertEquals(expected, progress("circular_progress"), 0.0001f)
    }

    @Test
    fun initialStateExplainsBothCanvases() {
        assertProgress(0.30f)
        rule.onNodeWithTag("progress_values")
            .assertTextEquals("Avance visible: 30% · Objetivo: 30%")
        rule.onNodeWithTag("progress_status")
            .assertTextEquals("Avance parcial: la barra y el anillo muestran la misma proporción.")
    }

    @Test
    fun resetShowsIntermediateValueThenEmptyState() {
        click("Reiniciar")
        rule.mainClock.advanceTimeBy(200)
        val intermediate = progress()
        assertTrue("Debe existir un fotograma intermedio", intermediate > 0f && intermediate < 0.30f)
        assertEquals(intermediate, progress("circular_progress"), 0.0001f)
        rule.onNodeWithTag("progress_status")
            .assertTextEquals("En transición: la barra y el anillo se acercan al objetivo.")
        val values = rule.onNodeWithTag("progress_values").fetchSemanticsNode()
            .config[SemanticsProperties.Text].single().text
        assertTrue(values.endsWith("Objetivo: 0%"))
        settle()
        assertProgress(0f)
        rule.onNodeWithTag("progress_status")
            .assertTextEquals("Sin avance: la barra y el anillo están vacíos.")
    }

    @Test
    fun threeDecrementsReachExactZeroWithoutFloatResidue() {
        repeat(3) { click("-10%") }
        settle()
        assertProgress(0f)
        rule.onNodeWithText("-10%").assertIsNotEnabled()
        rule.onNodeWithText("+10%").assertIsEnabled()
    }

    @Test
    fun repeatedBoundaryClicksNeverLeaveZeroToOneRange() {
        repeat(20) { click("+10%") }
        settle()
        assertProgress(1f)
        rule.onNodeWithText("+10%").assertIsNotEnabled()
        rule.onNodeWithTag("progress_status")
            .assertTextEquals("Avance completado: la barra y el anillo llegaron al 100%.")
        repeat(20) { click("-10%") }
        settle()
        assertProgress(0f)
        rule.onNodeWithText("-10%").assertIsNotEnabled()
        repeat(5) { click("Reiniciar") }
        settle()
        assertProgress(0f)
    }

    @Test
    fun rapidChangesAndResetConvergeToLatestTarget() {
        // Redirige la animación antes de que termine, en 10 ciclos.
        repeat(10) {
            repeat(4) { click("+10%") }
            click("Reiniciar")
            click("+10%")
            rule.mainClock.advanceTimeBy(80)
            val value = progress()
            assertTrue(value in 0f..1f)
            assertEquals(value, progress("circular_progress"), 0.0001f)
            settle()
            assertProgress(0.1f)
        }
    }

    @Test
    fun circularTapRepeatsFourCompleteCycles() {
        click("Reiniciar")
        settle()
        repeat(4) {
            listOf(0.25f, 0.5f, 0.75f, 1f, 0f).forEach { expected ->
                rule.onNodeWithTag("circular_progress").performScrollTo().performClick()
                settle()
                assertProgress(expected)
            }
        }
    }

    @Test
    fun loaderRepeatedlyChangesAppearanceAndExplainsItsMeaning() {
        fun darkestPixel(): Float {
            val pixels = rule.onNodeWithTag("load_indicator")
                .performScrollTo().captureToImage().toPixelMap()
            return (0 until pixels.width).minOf { x ->
                (0 until pixels.height).minOf { y -> pixels[x, y].luminance() }
            }
        }
        val activeLuminance = darkestPixel()
        repeat(10) {
            click("Atenuar indicador")
            settle()
            rule.onNodeWithTag("loader_status").assertTextEquals(
                "Indicador atenuado: se reduce al 60% del tamaño y al 25% de opacidad; el giro continúa."
            )
            assertTrue("El indicador atenuado debe verse más claro", darkestPixel() > activeLuminance)
            click("Activar indicador")
            settle()
        }
        assertProgress(0.30f)
    }

    @Test
    fun disablingSmoothingAppliesTargetWithoutIntermediateProgress() {
        rule.onNodeWithTag("smooth_transition").performScrollTo().performClick()
        settle()
        click("+10%")
        // snap necesita componer el objetivo y aplicarlo en el siguiente fotograma.
        rule.mainClock.advanceTimeBy(64)
        assertProgress(0.4f)
        rule.onNodeWithTag("smooth_transition").performScrollTo().performClick()
        settle()
        click("+10%")
        rule.mainClock.advanceTimeBy(160)
        assertTrue(progress() > 0.4f && progress() < 0.5f)
        settle()
        assertProgress(0.5f)
    }

    @Test
    fun savedStateRestoresProgressLoaderAndSmoothing() {
        click("+10%")
        click("Atenuar indicador")
        rule.onNodeWithTag("smooth_transition").performScrollTo().performClick()
        settle()
        restoration.emulateSavedInstanceStateRestore()
        settle()
        assertProgress(0.4f)
        rule.onNodeWithText("Activar indicador").assertExists()
        rule.onNodeWithTag("smooth_transition").assertIsOff()
    }
}
