package com.example.trabajo_desarrollo_aplicaciones_moviles.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.BotonPrincipal
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.EstadoApp
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.Cita
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.EstadoCita
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.formatearFechaHora

@Composable
fun PantallaAgenda(
    state: EstadoApp,
    alSolicitar: (Cita) -> Unit,
    alConfirmarDemo: (Cita) -> Unit,
    alCancelar: (Cita) -> Unit
) {
    var pending by remember { mutableStateOf<Cita?>(null) }
    fun profName(a: Cita) = state.profesionales.firstOrNull { it.id == a.profesionalId }?.name ?: "Profesional"
    fun profSpec(a: Cita) = state.profesionales.firstOrNull { it.id == a.profesionalId }?.especialidad ?: ""

    val mine = state.citas.filter { it.estado != EstadoCita.DISPONIBLE }.sortedBy { it.fechaHoraMillis }
    val free = state.citas.filter { it.estado == EstadoCita.DISPONIBLE }.sortedBy { it.fechaHoraMillis }

    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Agenda simulada con perfiles ficticios. La atención profesional real no se gestiona en este MVP.",
                style = MaterialTheme.typography.bodySmall)
        }
        item { Text("Mis citas", style = MaterialTheme.typography.titleMedium) }
        if (mine.isEmpty()) item { Text("Aún no tienes citas solicitadas.") }
        items(mine, key = { "m" + it.id }) { a ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(profName(a), style = MaterialTheme.typography.titleSmall)
                    Text(formatearFechaHora(a.fechaHoraMillis))
                    Text("Estado: ${a.estado.label}", style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (a.estado == EstadoCita.SOLICITADA) {
                            OutlinedButton(onClick = { alConfirmarDemo(a) }) { Text("Demo: confirmar") }
                        }
                        if (a.estado == EstadoCita.SOLICITADA || a.estado == EstadoCita.CONFIRMADA) {
                            TextButton(onClick = { alCancelar(a) }) { Text("Cancelar") }
                        }
                    }
                }
            }
        }
        item { Text("Horarios disponibles", style = MaterialTheme.typography.titleMedium) }
        if (free.isEmpty()) item { Text("No hay horarios disponibles. Puedes restablecer los datos de prueba en Ajustes.") }
        items(free, key = { "f" + it.id }) { a ->
            Card(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(Modifier.weight(1f)) {
                        Text(profName(a), style = MaterialTheme.typography.titleSmall)
                        Text(profSpec(a), style = MaterialTheme.typography.bodySmall)
                        Text(formatearFechaHora(a.fechaHoraMillis))
                    }
                    BotonPrincipal(onClick = { pending = a }, modifier = Modifier.heightIn(min = 48.dp)) { Text("Solicitar") }
                }
            }
        }
    }

    pending?.let { a ->
        AlertDialog(
            onDismissRequest = { pending = null },
            title = { Text("Solicitar cita") },
            text = { Text("${profName(a)}\n${formatearFechaHora(a.fechaHoraMillis)}\n\nLa solicitud queda registrada en tu historial de prueba.") },
            confirmButton = { TextButton(onClick = { alSolicitar(a); pending = null }) { Text("Confirmar solicitud") } },
            dismissButton = { TextButton(onClick = { pending = null }) { Text("Volver") } }
        )
    }
}