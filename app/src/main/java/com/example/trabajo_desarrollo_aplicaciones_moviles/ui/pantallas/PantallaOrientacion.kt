package com.example.trabajo_desarrollo_aplicaciones_moviles.ui.pantallas

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.MensajeChat
import com.example.trabajo_desarrollo_aplicaciones_moviles.dominio.MotorOrientacion
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.DialogoAyuda
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.formatearHora

@Composable
fun PantallaOrientacion(messages: List<MensajeChat>, alEnviar: (String) -> Unit, alNavegar: (String) -> Unit) {
    var input by remember { mutableStateOf("") }
    var showHelp by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.lastIndex)
    }

    Column(Modifier.fillMaxSize()) {
        Text(
            MotorOrientacion.AVISO,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    Text(
                        "Hola, puedo orientarte con registros, recursos y horarios de profesionales (datos de prueba). ¿Cómo estás hoy?",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            items(messages, key = { it.id }) { m ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = if (m.delUsuario) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        modifier = Modifier.widthIn(max = 300.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                m.seguridad -> MaterialTheme.colorScheme.errorContainer
                                m.delUsuario -> MaterialTheme.colorScheme.primaryContainer
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
                    ) {
                        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(m.text, style = MaterialTheme.typography.bodyMedium)
                            Text(formatearHora(m.marcaTiempo), style = MaterialTheme.typography.labelSmall)
                            if (m.seguridad) {
                                OutlinedButton(onClick = { showHelp = true }) { Text("Ayuda inmediata") }
                            } else if (m.rutaSugerida != null && m.etiquetaSugerida != null) {
                                OutlinedButton(onClick = { alNavegar(m.rutaSugerida) }) { Text(m.etiquetaSugerida) }
                            }
                        }
                    }
                }
            }
        }

        Row(Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Dormí mal", "Estoy estresado/a", "Quiero hablar con un profesional").forEach { s ->
                AssistChip(onClick = { alEnviar(s) }, label = { Text(s) })
            }
        }

        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Escribe aquí (datos de prueba)") },
                maxLines = 4
            )
            IconButton(
                onClick = { alEnviar(input); input = "" },
                enabled = input.isNotBlank()
            ) { Icon(Icons.Outlined.Send, contentDescription = "Enviar mensaje") }
        }
    }

    if (showHelp) DialogoAyuda(alCerrar = { showHelp = false })
}
