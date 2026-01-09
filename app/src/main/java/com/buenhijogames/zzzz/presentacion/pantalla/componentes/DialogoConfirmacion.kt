package com.buenhijogames.zzzz.presentacion.pantalla.componentes

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

/**
 * Diálogo de confirmación reutilizable.
 */
@Composable
fun DialogoConfirmacion(
    titulo: String,
    mensaje: String,
    textoConfirmar: String = "Sí",
    textoCancelar: String = "No",
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = {
            Text(
                text = titulo,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(text = mensaje)
        },
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text(
                    text = textoConfirmar,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text(
                    text = textoCancelar,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}
