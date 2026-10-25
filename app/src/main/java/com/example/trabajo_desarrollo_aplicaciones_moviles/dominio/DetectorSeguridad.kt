package com.example.trabajo_desarrollo_aplicaciones_moviles.dominio


import java.text.Normalizer

/**
 * Detector simple por reglas de expresiones que sugieren riesgo.
 * NO es un diagnóstico ni un sistema clínico: solo activa el mensaje de seguridad
 * y muestra alternativas de ayuda inmediata.
 */
object DetectorSeguridad {
    private val riskPhrases = listOf(
        "suicid", "quitarme la vida", "quitarse la vida", "matarme", "no quiero vivir",
        "ya no quiero estar aqui", "hacerme dano", "autolesion", "cortarme",
        "acabar con todo", "terminar con mi vida", "quiero morir", "morirme"
    )

    fun normalizar(text: String): String =
        Normalizer.normalize(text.lowercase(), Normalizer.Form.NFD)
            .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")

    fun hayRiesgo(text: String): Boolean {
        val t = normalizar(text)
        return riskPhrases.any { t.contains(it) }
    }
}
