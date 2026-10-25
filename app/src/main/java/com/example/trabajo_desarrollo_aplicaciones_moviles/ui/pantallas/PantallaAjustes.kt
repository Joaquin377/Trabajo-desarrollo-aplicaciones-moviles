package com.example.trabajo_desarrollo_aplicaciones_moviles.ui.pantallas

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.EstadoApp
import com.example.trabajo_desarrollo_aplicaciones_moviles.notificaciones.Recordatorios

@Composable
fun PantallaAjustes(state: EstadoApp, alCambiarRecordatorios: (Boolean) -> Unit, alRestablecer: () -> Unit) {
    val context = LocalContext.current
    var confirmReset by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        alCambiarRecordatorios(granted)
        if (!granted) Toast.makeText(context, "Sin permiso, no se mostrarán recordatorios", Toast.LENGTH_SHORT).show()
    }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Recordatorios simulados", style = MaterialTheme.typography.titleMedium)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Activar recordatorios", Modifier.weight(1f))
            Switch(
                checked = state.recordatoriosActivos,
                onCheckedChange = { on ->
                    if (on && Build.VERSION.SDK_INT >= 33 && !Recordatorios.hasPermission(context)) {
                        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else alCambiarRecordatorios(on)
                }
            )
        }
        OutlinedButton(
            enabled = state.recordatoriosActivos,
            onClick = {
                if (!Recordatorios.showTest(context))
                    Toast.makeText(context, "Falta permiso de notificaciones", Toast.LENGTH_SHORT).show()
            }
        ) { Text("Probar recordatorio ahora") }
        Text("El texto de la notificación es neutro para proteger tu privacidad en la pantalla de bloqueo.",
            style = MaterialTheme.typography.bodySmall)

        Text("Datos de prueba", style = MaterialTheme.typography.titleMedium)
        Text("Todos los datos son sintéticos y viven solo en este teléfono. Puedes restablecerlos al dataset original.")
        OutlinedButton(onClick = { confirmReset = true }) { Text("Restablecer datos de prueba") }

        Text("Acerca de", style = MaterialTheme.typography.titleMedium)
        Text("VITAIROS-M · MVP académico DSY1105 · v1.0-mvp\n" +
                "Sin cámara, sin ubicación, sin credenciales productivas. " +
                "No es un producto clínico ni reemplaza la atención profesional.",
            style = MaterialTheme.typography.bodySmall)
    }

    if (confirmReset) {
        AlertDialog(
            onDismissRequest = { confirmReset = false },
            title = { Text("Restablecer datos") },
            text = { Text("Se borrarán tus registros, citas y conversación de prueba y se cargará el dataset original.") },
            confirmButton = {
                TextButton(onClick = { alRestablecer(); confirmReset = false }) { Text("Restablecer") }
            },
            dismissButton = { TextButton(onClick = { confirmReset = false }) { Text("Cancelar") } }
        )
    }
}



FELIPE ANDRES VARGAS IBARRA
15:27

este segundo era PantallaAjustes

FELIPE ANDRES VARGAS IBARRA
15:28

PantallaBienvenida



package com.example.trabajo_desarrollo_aplicaciones_moviles.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.BotonPrincipal
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.AvisoSeguridad
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.LogoVitairos
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign

@Composable
fun PantallaBienvenida(alAceptar: () -> Unit) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LogoVitairos(Modifier.align(Alignment.CenterHorizontally), ancho = 280.dp)
        Text(
            "La vida no ocurre solo en la sesión.",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text("Bienvenido/a a VITAIROS-M", style = MaterialTheme.typography.titleLarge)
        Text(
            "Prototipo académico (MVP) de orientación y acompañamiento en salud mental digital.",
            style = MaterialTheme.typography.bodyLarge
        )
        AvisoSeguridad()
        Text("Antes de comenzar, ten presente:", style = MaterialTheme.typography.titleMedium)
        Text("• La app NO diagnostica ni reemplaza la atención de un profesional.")
        Text("• Todos los perfiles, registros y citas son FICTICIOS (datos sintéticos). No ingreses datos reales.")
        Text("• La información se guarda solo en este teléfono, para fines de prueba.")
        Text("• Si estás en riesgo, usa el botón \"Ayuda inmediata\" o llama al 131.")
        BotonPrincipal(onClick = alAceptar, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) {
            Text("Entiendo y quiero continuar")
        }
    }
}

FELIPE ANDRES VARGAS IBARRA
15:28

4 PantallaHistorial





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

FELIPE ANDRES VARGAS IBARRA
15:29

5 PantallaInicio



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

FELIPE ANDRES VARGAS IBARRA
15:30

6 PantallaOrientacion

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

FELIPE ANDRES VARGAS IBARRA
15:30

7 PantallaRecursos





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


