package com.projects.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.example.projects.MainActivity
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InicioSesionUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testIniciarSesionFormulario() {
        composeTestRule.onNodeWithTag("botonScreenInicio").performClick()

        composeTestRule.onNodeWithTag("textFieldNormal")
            .performTextInput("paco")

        composeTestRule.onNodeWithTag("textFieldContrasenia")
            .performTextInput("12345")

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("botonIniciarSesion").performClick()

        composeTestRule.waitUntil {
            composeTestRule.activity.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
        }

        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule.onAllNodesWithTag("loadingIndicator").fetchSemanticsNodes().isEmpty()
        }

        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule.onAllNodesWithTag("libroListaScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("libroListaScreen").assertIsDisplayed()
    }
}
