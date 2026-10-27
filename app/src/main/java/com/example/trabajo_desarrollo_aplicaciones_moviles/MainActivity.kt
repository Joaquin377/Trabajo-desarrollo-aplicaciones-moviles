package com.example.trabajo_desarrollo_aplicaciones_moviles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trabajo_desarrollo_aplicaciones_moviles.notificaciones.Recordatorios
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.ModeloVistaApp
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.AppVitairos
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.tema.TemaVitairos

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Recordatorios.ensureChannel(this)
        setContent {
            TemaVitairos {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val vm: ModeloVistaApp = viewModel()
                    AppVitairos(vm)
                }
            }
        }
    }
}
