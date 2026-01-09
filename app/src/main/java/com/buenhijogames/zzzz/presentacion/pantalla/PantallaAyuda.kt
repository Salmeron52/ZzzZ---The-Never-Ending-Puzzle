package com.buenhijogames.zzzz.presentacion.pantalla

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buenhijogames.zzzz.BuildConfig
import com.buenhijogames.zzzz.R

/**
 * Pantalla de información y ayuda con diseño premium.
 * Muestra instrucciones, objetivo y créditos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAyuda(
    onVolver: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.info_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_desc)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Info del Desarrollador (Moved to top)
                InfoFooter(
                    onOpenUrl = { url -> uriHandler.openUri(url) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Sección Cómo Jugar
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.how_to_play_title),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.how_to_play_content),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Sección Objetivo
                Text(
                    text = stringResource(R.string.goal_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = stringResource(R.string.goal_desc),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Ficha Gigante ZzzZ
                Box(
                    modifier = Modifier
                        .size(140.dp) // Ficha grande
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primary), // Color llamativo
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ZzzZ",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))
                
                // Footer moved to top
            }
        }
    }
}

@Composable
private fun InfoFooter(
    onOpenUrl: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Desarrollado por
        Text(
            text = "${stringResource(R.string.developed_by)} buenhijoGames", // Nombre hardcodeado intencionalmente
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Link Desarrollador
        Text(
            text = stringResource(R.string.developer_site),
            style = MaterialTheme.typography.labelLarge.copy(
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                onOpenUrl("https://play.google.com/store/apps/dev?id=5600754157337799725&hl=es_419")
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Versión automática
        Text(
            text = "${stringResource(R.string.version)} ${BuildConfig.VERSION_NAME}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
    }
}
