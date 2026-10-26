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

