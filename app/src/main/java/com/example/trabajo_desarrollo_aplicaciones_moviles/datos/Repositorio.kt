package com.example.trabajo_desarrollo_aplicaciones_moviles.datos

import android.content.Context
import com.example.trabajo_desarrollo_aplicaciones_moviles.R
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar

/**
 * Punto de integración: hoy es local (datos sintéticos + SharedPreferences).
 * En una etapa posterior puede añadirse una implementación RemoteRepository (API REST)
 * para autenticación, historial, agenda y continuidad, sin cambiar la UI.
 */
interface RepositorioVitairos {
    fun cargar(): EstadoApp
    fun guardar(state: EstadoApp)
    fun restablecer(): EstadoApp
}

class RepositorioLocal(private val context: Context) : RepositorioVitairos {

    private val prefs = context.getSharedPreferences("vitairos_m_mvp", Context.MODE_PRIVATE)

    private fun dataset(): JSONObject =
        JSONObject(context.resources.openRawResource(R.raw.dataset_sintetico).bufferedReader().use { it.readText() })

    override fun cargar(): EstadoApp {
        val ds = dataset()
        if (!prefs.contains(K_ENTRIES)) sembrar(ds)
        return EstadoApp(
            cargado = true,
            bienvenidaCompletada = prefs.getBoolean(K_ONBOARDING, false),
            recordatoriosActivos = prefs.getBoolean(K_REMINDERS, false),
            registros = leerRegistros(prefs.getString(K_ENTRIES, "[]") ?: "[]"),
            citas = leerCitas(prefs.getString(K_APPTS, "[]") ?: "[]"),
            chat = leerChat(prefs.getString(K_CHAT, "[]") ?: "[]"),
            profesionales = leerProfesionales(ds),
            recursos = leerRecursos(ds)
        )
    }

    override fun guardar(state: EstadoApp) {
        prefs.edit()
            .putBoolean(K_ONBOARDING, state.bienvenidaCompletada)
            .putBoolean(K_REMINDERS, state.recordatoriosActivos)
            .putString(K_ENTRIES, registrosJson(state.registros))
            .putString(K_APPTS, citasJson(state.citas))
            .putString(K_CHAT, chatJson(state.chat))
            .apply()
    }

    override fun restablecer(): EstadoApp {
        val onboarding = prefs.getBoolean(K_ONBOARDING, false)
        prefs.edit().clear().putBoolean(K_ONBOARDING, onboarding).apply()
        return cargar()
    }

    // ---------- sembrar ----------
    private fun sembrar(ds: JSONObject) {
        val now = System.currentTimeMillis()
        val dayMs = 24L * 60 * 60 * 1000
        val midnight = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val registros = ds.getJSONArray("registros").let { arr ->
            (0 until arr.length()).map { arr.getJSONObject(it) }.map { o ->
                RegistroEmocional(
                    id = o.getString("id"),
                    marcaTiempo = now - o.getInt("diasAtras") * dayMs,
                    animo = o.getInt("animo"),
                    area = Area.desdeNombre(o.getString("dominio")),
                    nota = o.optString("nota", "")
                )
            }
        }
        val appts = ds.getJSONArray("citas").let { arr ->
            (0 until arr.length()).map { arr.getJSONObject(it) }.map { o ->
                Cita(
                    id = o.getString("id"),
                    profesionalId = o.getString("profesionalId"),
                    fechaHoraMillis = midnight + o.getInt("diasDesde") * dayMs + o.getInt("hora") * 3_600_000L,
                    estado = EstadoCita.desdeNombre(o.getString("estado"))
                )
            }
        }
        prefs.edit()
            .putString(K_ENTRIES, registrosJson(registros))
            .putString(K_APPTS, citasJson(appts))
            .putString(K_CHAT, "[]")
            .apply()
    }

    // ---------- JSON ----------
    private fun registrosJson(list: List<RegistroEmocional>) = JSONArray().also { a ->
        list.forEach {
            a.put(JSONObject().put("id", it.id).put("ts", it.marcaTiempo).put("animo", it.animo)
                .put("area", it.area.name).put("nota", it.nota))
        }
    }.toString()

    private fun leerRegistros(s: String): List<RegistroEmocional> = JSONArray(s).let { a ->
        (0 until a.length()).map { a.getJSONObject(it) }.map {
            RegistroEmocional(it.getString("id"), it.getLong("ts"), it.getInt("animo"),
                Area.desdeNombre(it.getString("area")), it.optString("nota", ""))
        }
    }

    private fun citasJson(list: List<Cita>) = JSONArray().also { a ->
        list.forEach {
            a.put(JSONObject().put("id", it.id).put("prof", it.profesionalId)
                .put("slot", it.fechaHoraMillis).put("estado", it.estado.name))
        }
    }.toString()

    private fun leerCitas(s: String): List<Cita> = JSONArray(s).let { a ->
        (0 until a.length()).map { a.getJSONObject(it) }.map {
            Cita(it.getString("id"), it.getString("prof"), it.getLong("slot"),
                EstadoCita.desdeNombre(it.getString("estado")))
        }
    }

    private fun chatJson(list: List<MensajeChat>) = JSONArray().also { a ->
        list.forEach {
            a.put(JSONObject().put("id", it.id).put("user", it.delUsuario).put("text", it.text)
                .put("ts", it.marcaTiempo).put("seguridad", it.seguridad)
                .put("route", it.rutaSugerida ?: "").put("label", it.etiquetaSugerida ?: ""))
        }
    }.toString()

    private fun leerChat(s: String): List<MensajeChat> = JSONArray(s).let { a ->
        (0 until a.length()).map { a.getJSONObject(it) }.map {
            MensajeChat(it.getString("id"), it.getBoolean("user"), it.getString("text"),
                it.getLong("ts"), it.optBoolean("seguridad", false),
                it.optString("route", "").ifEmpty { null }, it.optString("label", "").ifEmpty { null })
        }
    }

    private fun leerProfesionales(ds: JSONObject): List<Profesional> =
        ds.getJSONArray("profesionales").let { a ->
            (0 until a.length()).map { a.getJSONObject(it) }.map {
                Profesional(it.getString("id"), it.getString("nombre"), it.getString("especialidad"))
            }
        }

    private fun leerRecursos(ds: JSONObject): List<Recurso> =
        ds.getJSONArray("recursos").let { a ->
            (0 until a.length()).map { a.getJSONObject(it) }.map {
                Recurso(it.getString("id"), it.getString("titulo"), it.getString("descripcion"),
                    it.optString("contenido", ""), it.optString("url", ""))
            }
        }

    private companion object {
        const val K_ENTRIES = "registros"
        const val K_APPTS = "citas"
        const val K_CHAT = "chat"
        const val K_ONBOARDING = "onboarding"
        const val K_REMINDERS = "reminders"
    }
}
