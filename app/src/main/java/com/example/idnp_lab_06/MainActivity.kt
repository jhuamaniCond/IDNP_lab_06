package com.example.idnp_lab_06

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.idnp_lab_06.ui.screens.CampusDocsDemoScreen
import com.example.idnp_lab_06.ui.screens.ComponentsDesignPreview
import com.example.idnp_lab_06.ui.theme.IDNP_lab_06Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IDNP_lab_06Theme {
                var currentScreen by remember { mutableStateOf("components") }
                Scaffold { innerPadding ->
                    if (currentScreen == "components") {
                        ComponentsDesignPreview(
                            modifier = Modifier.padding(innerPadding),
                            onNavigateToCampusDocs = { currentScreen = "campusdocs" }
                        )
                    } else {
                        CampusDocsDemoScreen(
                            onBackToComponents = { currentScreen = "components" },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
