package com.example.trabajo_desarrollo_aplicaciones_moviles.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.BotonPrincipal
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.EstadoApp
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.EstadoCita
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.animoDe
import com.example.trabajo_desarrollo_aplicaciones_moviles.dominio.Rutas
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.AvisoSeguridad
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.LogoVitairos
import androidx.compose.ui.Alignment
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.formatearFechaHora
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.hayConexion
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.mismoDia

@Composable
fun PantallaInicio(state: EstadoApp, alNavegar: (String) -> Unit) {
    val context = LocalContext.current
    val online = remember { hayConexion(context) }
    val now = System.currentTimeMillis()
    val todayDone = state.registros.any { mismoDia(it.marcaTiempo, now) }
    val upcoming = state.citas
        .filter { (it.estado == EstadoCita.CONFIRMADA || it.estado == EstadoCita.SOLICITADA) && it.fechaHoraMillis > now }
        .minByOrNull { it.fechaHoraMillis }
    val profName = upcoming?.let { a -> state.profesionales.firstOrNull { it.id == a.profesionalId }?.name }
    val last = state.registros.maxByOrNull { it.marcaTiempo }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        LogoVitairos(Modifier.align(Alignment.CenterHorizontally), ancho = 240.dp)
        AvisoSeguridad()

        // Próximo paso (siempre visible y único)
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Tu próximo paso", style = MaterialTheme.typography.labelLarge)
                when {
                    !todayDone -> {
                        Text("Registra cómo te sientes hoy (toma menos de un minuto).",
                            style = MaterialTheme.typography.titleMedium)
                        BotonPrincipal(onClick = { alNavegar(Rutas.REGISTRO) },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text("Hacer registro") }
                    }
                    upcoming != null -> {
                        Text("Cita ${upcoming.estado.label.lowercase()}: ${formatearFechaHora(upcoming.fechaHoraMillis)} con ${profName ?: "profesional"} (simulada).",
                            style = MaterialTheme.typography.titleMedium)
                        BotonPrincipal(onClick = { alNavegar(Rutas.AGENDA) },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text("Ver agenda") }
                    }
                    else -> {
                        Text("Ya registraste hoy. Puedes conversar o revisar recursos.",
                            style = MaterialTheme.typography.titleMedium)
                        BotonPrincipal(onClick = { alNavegar(Rutas.ORIENTACION) },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text("Ir a orientación") }
                    }
                }
            }
        }

        // Acciones frecuentes: máximo 1 toque desde Inicio
        Text("Acciones frecuentes", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { alNavegar(Rutas.REGISTRO) }, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text("Registrar") }
            OutlinedButton(onClick = { alNavegar(Rutas.ORIENTACION) }, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text("Conversar") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { alNavegar(Rutas.AGENDA) }, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text("Agendar") }
            OutlinedButton(onClick = { alNavegar(Rutas.RECURSOS) }, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) { Text("Recursos") }
        }

        // Continuidad entre sesiones
        if (last != null) {
            Card {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Último registro", style = MaterialTheme.typography.labelLarge)
                    val m = animoDe(last.animo)
                    Text("${m.emoji} ${m.label} · ${last.area.label}", style = MaterialTheme.typography.titleMedium)
                    Text(formatearFechaHora(last.marcaTiempo), style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        AssistChip(
            onClick = {},
            label = { Text(if (online) "Con conexión · MVP usa datos locales de prueba" else "Sin conexión · funciona con datos locales") }
        )
    }
}


