package com.example.trabajo_desarrollo_aplicaciones_moviles.ui.tema


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Tokens de identidad visual VITAIROS-M (Manual de identidad visual v3.0, sección 4).
 * Los valores provienen del CSS vigente del sitio.
 */
object ColoresVitairos {
    val Navy = Color(0xFF000E2E)          // Titulares, texto fuerte, identidad
    val GrisAzulado = Color(0xFF536477)   // Texto secundario y metadata
    val Verde = Color(0xFF00C98E)         // Inicio del gradiente, señal positiva
    val Cian = Color(0xFF00B9D3)          // Puente visual, enlaces y acentos
    val Azul = Color(0xFF00A5F4)          // Tecnología, acción y foco
    val CianTexto = Color(0xFF007F9E)     // Kickers y texto de acento
    val BlancoBase = Color(0xFFFEFEFE)    // Fondo principal
    val FondoSecundario = Color(0xFFF2F8FB) // Secciones suaves
    val MintSuave = Color(0xFFDDF6F0)     // Fondos y estados suaves
    val AzulSuave = Color(0xFFE1F3FE)     // Fondos tecnológicos suaves

    // Gradiente de texto (titulares / palabras clave sobre fondos claros)
    val TextoGradInicio = Color(0xFF008E70)
    val TextoGradFin = Color(0xFF006FB8)

    /** Gradiente principal: #00C98E 0% → #00B9D3 54% → #00A5F4 100%. Solo acentos, progreso y CTA. */
    val GradientePrincipal: Brush = Brush.horizontalGradient(
        0f to Verde, 0.54f to Cian, 1f to Azul
    )

    /** Gradiente de texto: #008E70 → #007F9E → #006FB8. */
    val GradienteTexto: Brush = Brush.horizontalGradient(
        0f to TextoGradInicio, 0.54f to CianTexto, 1f to TextoGradFin
    )
}

private val LightScheme = lightColorScheme(
    primary = ColoresVitairos.CianTexto,
    onPrimary = Color.White,
    primaryContainer = ColoresVitairos.MintSuave,
    onPrimaryContainer = ColoresVitairos.Navy,
    secondary = ColoresVitairos.GrisAzulado,
    onSecondary = Color.White,
    secondaryContainer = ColoresVitairos.AzulSuave,
    onSecondaryContainer = ColoresVitairos.Navy,
    tertiary = ColoresVitairos.Azul,
    background = ColoresVitairos.BlancoBase,
    onBackground = ColoresVitairos.Navy,
    surface = ColoresVitairos.BlancoBase,
    onSurface = ColoresVitairos.Navy,
    surfaceVariant = ColoresVitairos.FondoSecundario,
    onSurfaceVariant = ColoresVitairos.GrisAzulado,
    surfaceContainer = ColoresVitairos.FondoSecundario,
    surfaceContainerLow = ColoresVitairos.FondoSecundario,
    surfaceContainerHighest = ColoresVitairos.FondoSecundario,
    outline = Color(0xFFB7C9D6),
    outlineVariant = Color(0xFFDCE8EF),
    // Seguridad: se mantiene diferenciable, pero sin rojo alarmista (manual §7).
    errorContainer = ColoresVitairos.AzulSuave,
    onErrorContainer = ColoresVitairos.Navy
)

private val DarkScheme = darkColorScheme(
    primary = ColoresVitairos.Cian,
    onPrimary = ColoresVitairos.Navy,
    primaryContainer = Color(0xFF0A3A48),
    onPrimaryContainer = ColoresVitairos.MintSuave,
    secondary = Color(0xFFA9BACB),
    onSecondary = ColoresVitairos.Navy,
    secondaryContainer = Color(0xFF12304A),
    onSecondaryContainer = ColoresVitairos.AzulSuave,
    tertiary = ColoresVitairos.Azul,
    background = ColoresVitairos.Navy,
    onBackground = ColoresVitairos.FondoSecundario,
    surface = ColoresVitairos.Navy,
    onSurface = ColoresVitairos.FondoSecundario,
    surfaceVariant = Color(0xFF0B2342),
    onSurfaceVariant = Color(0xFFA9BACB),
    surfaceContainer = Color(0xFF0B2342),
    surfaceContainerLow = Color(0xFF0B2342),
    surfaceContainerHighest = Color(0xFF12304A),
    outline = Color(0xFF3B5169),
    outlineVariant = Color(0xFF1E3550),
    errorContainer = Color(0xFF12304A),
    onErrorContainer = ColoresVitairos.AzulSuave
)

/** Radios amplios, coherentes con los botones tipo pill de la web (manual §8). */
private val FormasVitairos = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

/**
 * Outfit es la tipografía de interfaz (manual §5). No se reconstruye el wordmark con ella.
 * Si agregas Outfit-Regular/Medium/SemiBold/Bold.ttf en res/font (nombres en minúscula:
 * outfit_regular.ttf, outfit_medium.ttf, outfit_semibold.ttf, outfit_bold.ttf) se usa
 * automáticamente; si no existen, se usa la fuente sans del sistema (sin errores).
 */
@Composable
private fun rememberFamiliaOutfit(): FontFamily {
    val context = LocalContext.current
    return remember(context) {
        fun f(nombre: String, peso: FontWeight): Font? {
            val id = context.resources.getIdentifier(nombre, "font", context.packageName)
            return if (id != 0) Font(id, peso) else null
        }
        val fuentes = listOfNotNull(
            f("outfit_regular", FontWeight.Normal),
            f("outfit_medium", FontWeight.Medium),
            f("outfit_semibold", FontWeight.SemiBold),
            f("outfit_bold", FontWeight.Bold)
        )
        if (fuentes.isEmpty()) FontFamily.SansSerif else FontFamily(fuentes)
    }
}

private fun tipografia(familia: FontFamily): Typography {
    val base = Typography()
    fun TextStyle.o(peso: FontWeight? = null) =
        copy(fontFamily = familia, fontWeight = peso ?: fontWeight)
    return Typography(
        displayLarge = base.displayLarge.o(FontWeight.Bold),
        displayMedium = base.displayMedium.o(FontWeight.Bold),
        displaySmall = base.displaySmall.o(FontWeight.Bold),
        // Titulares: Bold 700, alto contraste
        headlineLarge = base.headlineLarge.o(FontWeight.Bold),
        headlineMedium = base.headlineMedium.o(FontWeight.Bold),
        headlineSmall = base.headlineSmall.o(FontWeight.Bold),
        // Subtítulos: Semibold 600
        titleLarge = base.titleLarge.o(FontWeight.SemiBold),
        titleMedium = base.titleMedium.o(FontWeight.SemiBold),
        titleSmall = base.titleSmall.o(FontWeight.SemiBold),
        // Lectura: Regular 400
        bodyLarge = base.bodyLarge.o(FontWeight.Normal),
        bodyMedium = base.bodyMedium.o(FontWeight.Normal),
        bodySmall = base.bodySmall.o(FontWeight.Normal),
        // Etiquetas: 700, mayúsculas espaciadas se aplican solo donde corresponde (kickers)
        labelLarge = base.labelLarge.o(FontWeight.SemiBold),
        labelMedium = base.labelMedium.o(FontWeight.SemiBold),
        labelSmall = base.labelSmall.o(FontWeight.SemiBold)
    )
}

@Composable
fun TemaVitairos(content: @Composable () -> Unit) {
    val familia = rememberFamiliaOutfit()
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkScheme else LightScheme,
        typography = remember(familia) { tipografia(familia) },
        shapes = FormasVitairos,
        content = content
    )
}