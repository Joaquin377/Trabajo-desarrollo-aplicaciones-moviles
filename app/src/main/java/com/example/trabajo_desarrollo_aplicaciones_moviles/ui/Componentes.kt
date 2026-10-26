package com.example.trabajo_desarrollo_aplicaciones_moviles.uiimport android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.graphics.graphicsLayer
import com.example.trabajo_desarrollo_aplicaciones_moviles.R
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.tema.ColoresVitairos
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// ---------- Formato ----------
private val localeCl = Locale("es", "CL")
fun formatearFechaHora(millis: Long): String = SimpleDateFormat("EEE d MMM, HH:mm", localeCl).format(Date(millis))
fun formatearDia(millis: Long): String = SimpleDateFormat("EEEE d 'de' MMMM", localeCl).format(Date(millis))
fun formatearHora(millis: Long): String = SimpleDateFormat("HH:mm", localeCl).format(Date(millis))

fun mismoDia(a: Long, b: Long): Boolean {
    val ca = Calendar.getInstance().apply { timeInMillis = a }
    val cb = Calendar.getInstance().apply { timeInMillis = b }
    return ca.get(Calendar.YEAR) == cb.get(Calendar.YEAR) &&
            ca.get(Calendar.DAY_OF_YEAR) == cb.get(Calendar.DAY_OF_YEAR)
}

fun hayConexion(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val caps = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
    return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

// ---------- Acciones externas ----------
fun llamar(context: Context, number: String) {
    runCatching {
        context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(number))))
    }
}

fun abrirUrl(context: Context, url: String) {
    runCatching { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
}

// ---------- Seguridad ----------
@Composable
fun DialogoAyuda(alCerrar: () -> Unit, message: String? = null) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = alCerrar,
        icon = { Icon(Icons.Outlined.Warning, contentDescription = null) },
        title = { Text("Ayuda inmediata") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    message ?: "Si estás en riesgo o necesitas apoyo ahora, no esperes a la app: " +
                    "contacta a una persona de confianza o a estos servicios."
                )
                BotonAyuda("Emergencias de salud (131)") { llamar(context, "131") }
                BotonAyuda("Prevención del suicidio (*4141)") { llamar(context, "*4141") }
                BotonAyuda("Salud Responde (600 360 7777)") { llamar(context, "6003607777") }
                Text(
                    "Verifica los números vigentes en el sitio del Ministerio de Salud.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = { TextButton(onClick = alCerrar) { Text("Cerrar") } }
    )
}

@Composable
private fun BotonAyuda(label: String, onClick: () -> Unit) {
    BotonPrincipal(onClick = onClick, modifier = Modifier.fillMaxWidth().height(48.dp)) {
        Icon(Icons.Outlined.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.size(8.dp))
        Text(label)
    }
}

@Composable
fun AvisoSeguridad(modifier: Modifier = Modifier) {
    var showHelp by remember { mutableStateOf(false) }
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Info, contentDescription = null,
                    tint = ColoresVitairos.CianTexto)
                Spacer(Modifier.size(8.dp))
                Text(
                    "VITAIROS-M ofrece orientación y acompañamiento. No es un diagnóstico " +
                            "ni reemplaza la atención profesional.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
            OutlinedButton(onClick = { showHelp = true }, modifier = Modifier.heightIn(min = 48.dp)) { Text("Ayuda inmediata") }
        }
    }
    if (showHelp) DialogoAyuda(alCerrar = { showHelp = false })
}


// ---------- Marca (Manual de identidad visual v3.0) ----------

/**
 * Logo oficial completo como imagen/arte final (manual §2): sin reescritura tipográfica,
 * sin recolorear, sin efectos y conservando siempre la relación de aspecto original (602:184).
 * Ancho mínimo digital recomendado: 220 dp. El logo está pensado para fondo blanco o muy claro,
 * por eso se apoya en una superficie blanca (manual §3, "Fondos").
 */
@Composable
fun LogoVitairos(modifier: Modifier = Modifier, ancho: androidx.compose.ui.unit.Dp = 260.dp) {
    Box(modifier = modifier.background(Color.White).padding(horizontal = 12.dp, vertical = 8.dp)) { // área de resguardo
        Image(
            painter = painterResource(R.drawable.logo_vitairos_m),
            contentDescription = "VITAIROS-M, salud mental digital",
            contentScale = ContentScale.Fit,
            modifier = Modifier.widthIn(min = 220.dp).width(maxOf(ancho, 220.dp)).aspectRatio(602f / 184f)
        )
    }
}

/** Isotipo completo (manual §3): avatar, navegación y piezas donde el nombre ya está identificado. Mínimo 32 dp. */
@Composable
fun IsotipoVitairos(modifier: Modifier = Modifier, tamano: androidx.compose.ui.unit.Dp = 36.dp) {
    Image(
        painter = painterResource(R.drawable.isotipo_vitairos_m),
        contentDescription = "VITAIROS-M",
        contentScale = ContentScale.Fit,
        modifier = modifier.size(maxOf(tamano, 32.dp))
    )
}

/**
 * Botón de acción principal: gradiente verde → cian → azul, forma pill y texto azul noche
 * para mantener contraste (manual §4 y §8). El gradiente se reserva para CTA de tamaño acotado.
 */
@Composable
fun BotonPrincipal(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val forma = RoundedCornerShape(50)
    val fondo = if (enabled) ColoresVitairos.GradientePrincipal
    else SolidColor(MaterialTheme.colorScheme.outlineVariant)
    val colorTexto = if (enabled) ColoresVitairos.Navy else MaterialTheme.colorScheme.onSurfaceVariant
    Box(
        modifier = modifier
            .heightIn(min = 48.dp)
            .clip(forma)
            .background(fondo)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides colorTexto) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}
