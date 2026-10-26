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

