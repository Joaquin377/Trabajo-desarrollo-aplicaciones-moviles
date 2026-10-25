package com.example.trabajo_desarrollo_aplicaciones_moviles.dominio


data class RespuestaBot(
    val text: String,
    val seguridad: Boolean = false,
    val rutaSugerida: String? = null,
    val etiquetaSugerida: String? = null
)

/**
 * Orientación conversacional SIMULADA basada en reglas (mock del servicio de IA).
 * En una etapa posterior se reemplazaría por una llamada API REST a VITAIROS-M.
 * No diagnostica ni reemplaza atención profesional.
 */
object MotorOrientacion {

    const val TEXTO_SEGURIDAD =
        "Gracias por contarlo. Lo que describes es importante y mereces apoyo ahora. " +
                "Esta app no puede darte atención de emergencia. Si estás en riesgo o piensas en hacerte daño, " +
                "contacta de inmediato a una persona de confianza o a servicios de ayuda (botón \"Ayuda inmediata\"). " +
                "Puedes llamar al 131 (emergencias de salud) o a la línea *4141 de prevención del suicidio."

    const val AVISO =
        "Orientación automatizada de prueba. No es un diagnóstico ni reemplaza a un profesional de salud."

    private fun has(t: String, vararg words: String) = words.any { t.contains(it) }

    fun responder(input: String): RespuestaBot {
        if (DetectorSeguridad.hayRiesgo(input)) return RespuestaBot(TEXTO_SEGURIDAD, seguridad = true)
        val t = DetectorSeguridad.normalizar(input)
        return when {
            has(t, "profesional", "psicolog", "agendar", "cita", "hora con") -> RespuestaBot(
                "Puedo ayudarte a revisar horarios disponibles con profesionales (datos de prueba). " +
                        "Recuerda que la orientación de esta app es complementaria a la atención profesional.",
                rutaSugerida = Rutas.AGENDA, etiquetaSugerida = "Ver agenda"
            )
            has(t, "dorm", "sueno", "insomnio", "desvel") -> RespuestaBot(
                "Dormir poco puede influir en cómo nos sentimos durante el día. Puedes registrar cómo " +
                        "estuvo tu sueño para tener un historial, y revisar ideas generales de hábitos de descanso. " +
                        "Si el problema persiste, conversarlo con un profesional puede ayudar.",
                rutaSugerida = Rutas.REGISTRO, etiquetaSugerida = "Hacer un registro"
            )
            has(t, "estres", "ansios", "nervios", "agobi", "preocup") -> RespuestaBot(
                "Parece un momento de mucha carga. Un ejercicio breve de respiración puede ayudar a pausar. " +
                        "También puedes dejar un registro para ver cómo evoluciona con los días.",
                rutaSugerida = Rutas.RECURSOS, etiquetaSugerida = "Ver recursos"
            )
            has(t, "triste", "animo", "solo", "sola", "desanim", "cansad") -> RespuestaBot(
                "Gracias por compartirlo. Registrar cómo te sientes ayuda a notar patrones en el tiempo. " +
                        "Si esta sensación se mantiene, considera hablar con alguien de confianza o un profesional.",
                rutaSugerida = Rutas.REGISTRO, etiquetaSugerida = "Hacer un registro"
            )
            has(t, "gracias", "ok", "listo") -> RespuestaBot(
                "¡Con gusto! Tu próximo paso puede ser dejar un registro de hoy o revisar tu historial."
            )
            else -> RespuestaBot(
                "Te escucho. Puedo orientarte sobre registrar tu estado de ánimo, revisar recursos o " +
                        "ver horarios con profesionales. ¿Qué te gustaría hacer ahora?",
                rutaSugerida = Rutas.REGISTRO, etiquetaSugerida = "Hacer un registro"
            )
        }
    }
}
