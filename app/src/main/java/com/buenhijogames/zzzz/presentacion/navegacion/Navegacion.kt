package com.buenhijogames.zzzz.presentacion.navegacion

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.buenhijogames.zzzz.dominio.modelo.NivelDificultad
import com.buenhijogames.zzzz.presentacion.pantalla.PantallaJuego
import com.buenhijogames.zzzz.presentacion.pantalla.PantallaMenu
import com.buenhijogames.zzzz.presentacion.pantalla.PantallaPartidasGuardadas
import com.buenhijogames.zzzz.presentacion.viewmodel.JuegoViewModel

/**
 * Rutas de navegación de la aplicación.
 */
object Rutas {
    const val MENU = "menu"
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
        startDestination = Rutas.MENU
    ) {
        composable(Rutas.MENU) {
            PantallaMenu(
                onSeleccionarNivel = { nivel ->
                    juegoViewModel.iniciarNuevoJuego(nivel)
                    navController.navigate(Rutas.JUEGO) {
                        popUpTo(Rutas.MENU) { inclusive = false }
                    }
                },
                onIrAPartidasGuardadas = {
                    navController.navigate(Rutas.PARTIDAS_GUARDADAS)
                }
            )
        }

        composable(Rutas.JUEGO) {
            PantallaJuego(
                onIrAPartidasGuardadas = {
                    navController.navigate(Rutas.PARTIDAS_GUARDADAS)
                },
                onVolverAlMenu = {
                    navController.popBackStack(Rutas.MENU, inclusive = false)
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
                    navController.navigate(Rutas.JUEGO) {
                        popUpTo(Rutas.MENU) { inclusive = false }
                    }
                }
            )
        }
    }
}

