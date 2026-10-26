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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.Recurso
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.AvisoSeguridad
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.abrirUrl

@Composable
fun PantallaRecursos(recursos: List<Recurso>) {
    val context = LocalContext.current
    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item { AvisoSeguridad() }
        items(recursos, key = { it.id }) { r ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(r.title, style = MaterialTheme.typography.titleSmall)
                    Text(r.description, style = MaterialTheme.typography.bodySmall)
                    if (r.content.isNotBlank()) Text(r.content)
                    if (r.url.isNotBlank()) {
                        OutlinedButton(onClick = { abrirUrl(context, r.url) }) { Text("Abrir sitio público") }
                    }
                }
            }
        }
    }
}