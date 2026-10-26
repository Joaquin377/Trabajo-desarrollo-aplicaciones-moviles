package com.example.trabajo_desarrollo_aplicaciones_moviles.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.EstadoApp
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.animoDe
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.formatearFechaHora

@Composable
fun PantallaHistorial(state: EstadoApp) {
    val sorted = state.registros.sortedByDescending { it.marcaTiempo }
    val weekAgo = System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000
    val recent = state.registros.filter { it.marcaTiempo >= weekAgo }

    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Últimos 7 días", style = MaterialTheme.typography.titleMedium)
                    if (recent.isEmpty()) {
                        Text("Sin registros recientes.")
                    } else {
                        val avg = recent.map { it.animo }.average()
                        Text("${recent.size} registros · promedio de ánimo %.1f / 5".format(avg))
                    }
                    Text("Resumen informativo: no es un diagnóstico.", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        if (sorted.isEmpty()) item { Text("Aún no hay registros. Haz tu primer registro en la pestaña Registro.") }
        items(sorted, key = { it.id }) { e ->
            val m = animoDe(e.animo)
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("${m.emoji} ${m.label} · ${e.area.label}", style = MaterialTheme.typography.titleSmall)
                    Text(formatearFechaHora(e.marcaTiempo), style = MaterialTheme.typography.bodySmall)
                    if (e.nota.isNotBlank()) Text(e.nota)
                    Text("Ref. #${e.id}", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

