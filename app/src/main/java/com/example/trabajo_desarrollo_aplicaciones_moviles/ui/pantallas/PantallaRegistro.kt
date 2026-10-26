@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.trabajo_desarrollo_aplicaciones_moviles.ui.pantallas

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.BotonPrincipal
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.Area
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.OPCIONES_ANIMO
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.DialogoAyuda

@Composable
fun PantallaRegistro(alGuardar: (Int, Area, String) -> Boolean, alGuardado: () -> Unit) {
    val context = LocalContext.current
    var animo by rememberSaveable { mutableStateOf(0) }          // 0 = sin seleccionar
    var nombreArea by rememberSaveable { mutableStateOf("") }
    var nota by rememberSaveable { mutableStateOf("") }
    var showHelp by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(12.dp)

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("1. ¿Cómo te sientes ahora?", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            OPCIONES_ANIMO.forEach { opt ->
                val sel = animo == opt.value
                Column(
                    Modifier.weight(1f).heightIn(min = 72.dp).clip(shape)
                        .background(if (sel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                        .border(
                            BorderStroke(if (sel) 2.dp else 1.dp,
                                if (sel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant),
                            shape
                        )
                        .selectable(selected = sel, role = Role.RadioButton, onClick = { animo = opt.value })
                        .padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(opt.emoji, style = MaterialTheme.typography.headlineSmall)
                    Text(opt.label, style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Text("2. ¿Sobre qué área?", style = MaterialTheme.typography.titleMedium)
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Area.entries.forEach { d ->
                FilterChip(
                    selected = nombreArea == d.name,
                    onClick = { nombreArea = d.name },
                    label = { Text(d.label) }
                )
            }
        }

        Text("3. Nota (opcional)", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = nota,
            onValueChange = { if (it.length <= 200) nota = it },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            supportingText = { Text("${nota.length}/200 · No escribas datos personales reales (datos de prueba)") }
        )

        BotonPrincipal(
            onClick = {
                val risk = alGuardar(animo, Area.desdeNombre(nombreArea), nota)
                Toast.makeText(context, "Registro guardado", Toast.LENGTH_SHORT).show()
                animo = 0; nombreArea = ""; nota = ""
                if (risk) showHelp = true else alGuardado()
            },
            enabled = animo != 0 && nombreArea.isNotEmpty(),
            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
        ) { Text("Guardar registro") }
    }

    if (showHelp) {
        DialogoAyuda(
            alCerrar = { showHelp = false; alGuardado() },
            message = "Gracias por registrarlo. Si te sientes en riesgo o muy mal, busca apoyo ahora: " +
                    "habla con alguien de confianza o contacta estos servicios. Esta app no es atención de emergencia."
        )
    }
}

