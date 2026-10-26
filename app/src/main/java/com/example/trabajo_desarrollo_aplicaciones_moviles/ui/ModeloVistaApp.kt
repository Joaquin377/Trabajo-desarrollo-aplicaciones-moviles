package com.example.trabajo_desarrollo_aplicaciones_moviles.ui


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.EstadoApp
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.Cita
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.EstadoCita
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.MensajeChat
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.Area
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.RegistroEmocional
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.RepositorioLocal
import com.example.trabajo_desarrollo_aplicaciones_moviles.datos.RepositorioVitairos
import com.example.trabajo_desarrollo_aplicaciones_moviles.dominio.MotorOrientacion
import com.example.trabajo_desarrollo_aplicaciones_moviles.dominio.DetectorSeguridad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class ModeloVistaApp(app: Application) : AndroidViewModel(app) {

    private val repo: RepositorioVitairos = RepositorioLocal(app)
    private val _state = MutableStateFlow(EstadoApp())
    val state: StateFlow<EstadoApp> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val cargado = withContext(Dispatchers.IO) { repo.cargar() }
            _state.value = cargado
        }
    }

    private fun modificar(block: (EstadoApp) -> EstadoApp) {
        _state.update(block)
        repo.guardar(_state.value)
    }

    fun completarBienvenida() = modificar { it.copy(bienvenidaCompletada = true) }

    fun activarRecordatorios(on: Boolean) = modificar { it.copy(recordatoriosActivos = on) }

    /** Guarda un registro emocional. Devuelve true si se debe mostrar el mensaje de seguridad. */
    fun agregarRegistro(animo: Int, area: Area, nota: String): Boolean {
        val entry = RegistroEmocional(
            id = "r" + UUID.randomUUID().toString().take(6),
            marcaTiempo = System.currentTimeMillis(),
            animo = animo, area = area, nota = nota.trim()
        )
        modificar { it.copy(registros = it.registros + entry) }
        return animo <= 1 || DetectorSeguridad.hayRiesgo(nota)
    }

    fun enviarMensaje(text: String) {
        val clean = text.trim()
        if (clean.isEmpty()) return
        val now = System.currentTimeMillis()
        val respuesta = MotorOrientacion.responder(clean)
        val userMsg = MensajeChat("m" + UUID.randomUUID().toString().take(6), true, clean, now)
        val botMsg = MensajeChat(
            "m" + UUID.randomUUID().toString().take(6), false, respuesta.text, now + 1,
            respuesta.seguridad, respuesta.rutaSugerida, respuesta.etiquetaSugerida
        )
        modificar { it.copy(chat = it.chat + userMsg + botMsg) }
    }

    private fun cambiarEstado(id: String, from: EstadoCita, to: EstadoCita) = modificar { s ->
        s.copy(citas = s.citas.map {
            if (it.id == id && it.estado == from) it.copy(estado = to) else it
        })
    }

    fun solicitarCita(a: Cita) = cambiarEstado(a.id, EstadoCita.DISPONIBLE, EstadoCita.SOLICITADA)

    /** Solo para la demo: simula la respuesta del profesional. */
    fun simularConfirmacion(a: Cita) = cambiarEstado(a.id, EstadoCita.SOLICITADA, EstadoCita.CONFIRMADA)

    fun cancelarCita(a: Cita) = modificar { s ->
        s.copy(citas = s.citas.map {
            if (it.id == a.id && (it.estado == EstadoCita.SOLICITADA || it.estado == EstadoCita.CONFIRMADA))
                it.copy(estado = EstadoCita.CANCELADA) else it
        })
    }

    fun restablecerDatosPrueba() {
        _state.value = repo.restablecer()
    }
}
