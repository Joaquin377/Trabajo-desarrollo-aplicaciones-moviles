package com.example.trabajo_desarrollo_aplicaciones_moviles.datos

enum class Area(val label: String) {
    SUENO("Sueño"), ANIMO("Ánimo"), ESTRES("Estrés"), ENERGIA("Energía"), RELACIONES("Relaciones");

    companion object {
        fun desdeNombre(name: String): Area = entries.firstOrNull { it.name == name } ?: ANIMO
    }
}

data class OpcionAnimo(val value: Int, val label: String, val emoji: String)

val OPCIONES_ANIMO = listOf(
    OpcionAnimo(1, "Muy bajo", "😞"),
    OpcionAnimo(2, "Bajo", "😕"),
    OpcionAnimo(3, "Neutro", "😐"),
    OpcionAnimo(4, "Bien", "🙂"),
    OpcionAnimo(5, "Muy bien", "😄")
)

fun animoDe(value: Int): OpcionAnimo = OPCIONES_ANIMO.firstOrNull { it.value == value } ?: OPCIONES_ANIMO[2]

enum class EstadoCita(val label: String) {
    DISPONIBLE("Disponible"), SOLICITADA("Solicitada"), CONFIRMADA("Confirmada"), CANCELADA("Cancelada");

    companion object {
        fun desdeNombre(name: String): EstadoCita =
            entries.firstOrNull { it.name == name } ?: DISPONIBLE
    }
}

data class RegistroEmocional(
    val id: String,
    val marcaTiempo: Long,
    val animo: Int,
    val area: Area,
    val nota: String
)

data class Profesional(val id: String, val name: String, val especialidad: String)

data class Cita(
    val id: String,
    val profesionalId: String,
    val fechaHoraMillis: Long,
    val estado: EstadoCita
)

data class MensajeChat(
    val id: String,
    val delUsuario: Boolean,
    val text: String,
    val marcaTiempo: Long,
    val seguridad: Boolean = false,
    val rutaSugerida: String? = null,
    val etiquetaSugerida: String? = null
)

data class Recurso(
    val id: String,
    val title: String,
    val description: String,
    val content: String,
    val url: String
)

data class EstadoApp(
    val cargado: Boolean = false,
    val bienvenidaCompletada: Boolean = false,
    val recordatoriosActivos: Boolean = false,
    val registros: List<RegistroEmocional> = emptyList(),
    val citas: List<Cita> = emptyList(),
    val chat: List<MensajeChat> = emptyList(),
    val profesionales: List<Profesional> = emptyList(),
    val recursos: List<Recurso> = emptyList()
)
