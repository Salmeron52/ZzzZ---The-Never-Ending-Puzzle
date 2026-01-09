package com.buenhijogames.zzzz.presentacion.navegacion

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.buenhijogames.zzzz.presentacion.pantalla.PantallaJuego
import com.buenhijogames.zzzz.presentacion.pantalla.PantallaPartidasGuardadas
import com.buenhijogames.zzzz.presentacion.viewmodel.JuegoViewModel

/**
 * Rutas de navegación de la aplicación.
 */
object Rutas {
    const val JUEGO = "juego"
    const val PARTIDAS_GUARDADAS = "partidas_guardadas"
}

/**
 * Configuración de navegación de la aplicación.
 */
@Composable
fun NavegacionApp(
    navController: NavHostController = rememberNavController()
) {
    // Compartir el JuegoViewModel entre pantallas
    val juegoViewModel: JuegoViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Rutas.JUEGO
    ) {
        composable(Rutas.JUEGO) {
            PantallaJuego(
                onIrAPartidasGuardadas = {
                    navController.navigate(Rutas.PARTIDAS_GUARDADAS)
                },
                viewModel = juegoViewModel
            )
        }

        composable(Rutas.PARTIDAS_GUARDADAS) {
            PantallaPartidasGuardadas(
                onVolver = {
                    navController.popBackStack()
                },
                onContinuarPartida = { id ->
                    juegoViewModel.cargarPartidaGuardada(id)
                    navController.popBackStack()
                }
            )
        }
    }
}
