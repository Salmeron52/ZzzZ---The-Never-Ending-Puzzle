package com.buenhijogames.zzzz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.buenhijogames.zzzz.presentacion.navegacion.NavegacionApp
import com.buenhijogames.zzzz.ui.theme.ZzzzTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity principal del juego ZzzZ.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZzzzTheme {
                NavegacionApp()
            }
        }
    }
}

