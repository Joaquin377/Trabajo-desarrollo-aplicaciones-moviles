# VITAIROS-M · 

Aplicación Android de orientación y bienestar emocional desarrollada para la asignatura **DSY1105 · Desarrollo de Aplicaciones Móviles**. Permite registrar el estado de ánimo, recibir orientación inicial, organizar citas con profesionales y consultar recursos de apoyo.

> **Alcance.** Prototipo académico con **datos 100 % ficticios**. No diagnostica ni reemplaza atención profesional. Los números de ayuda y los enlaces deben verificarse antes de cualquier uso fuera del contexto académico.

## Funcionalidades

| Pantalla | Qué hace |
|---|---|
| Bienvenida | Aviso inicial y aceptación de condiciones. |
| Inicio | Próximo paso, acciones frecuentes y último registro. |
| Registro emocional | Registro del estado de ánimo en 3 pasos. |
| Orientación | Chat guiado por reglas (simulación de IA) con atajos. |
| Agenda | Solicitar, confirmar (demo) y cancelar citas simuladas. |
| Historial | Resumen de 7 días y lista de registros. |
| Recursos | Contenido local y enlaces públicos. |
| Ajustes | Recordatorios y restablecimiento de datos de prueba. |

## Requisitos técnicos cubiertos

- **Almacenamiento local:** registros, citas y conversación se guardan con `SharedPreferences` + JSON y se conservan al cerrar y reabrir la app.
- **Recursos nativos del dispositivo:** notificaciones locales (recordatorios), estado de la red, marcador telefónico (línea de ayuda) y navegador (enlaces). No usa cámara ni GPS.
- **Animaciones:** transición animada entre pantallas (fundido con desplazamiento suave) en `Navegacion.kt`.
- **Seguridad:** detector de frases de riesgo que muestra ayuda inmediata (131, *4141), aviso permanente de que no es un diagnóstico, y respaldo en la nube desactivado.
- **Funciona sin conexión.**

## Tecnologías

Kotlin · Jetpack Compose · Material 3 · Navigation Compose · ViewModel + StateFlow · Gradle Kotlin DSL (catálogo de versiones). `minSdk 26`.

## Arquitectura (MVVM en 4 capas)

- **Interfaz:** Compose + Navigation, 8 pantallas y tema propio.
- **Estado:** `ModeloVistaApp` (ViewModel) expone un `StateFlow` y guarda cada cambio.
- **Dominio:** Kotlin puro (`DetectorSeguridad`, `MotorOrientacion`, `Rutas`), fácil de probar.
- **Datos:** `RepositorioVitairos` (interfaz) con implementación local; permite cambiar a una API REST sin tocar la interfaz.

## Estructura del proyecto

```
app/src/main/
├─ AndroidManifest.xml
├─ java/<paquete>/
│  ├─ MainActivity.kt
│  ├─ datos/           Modelos.kt · Repositorio.kt
│  ├─ dominio/         Rutas.kt · DetectorSeguridad.kt · MotorOrientacion.kt
│  ├─ notificaciones/  Recordatorios.kt
│  └─ ui/              Navegacion.kt · ModeloVistaApp.kt · Componentes.kt
│     ├─ pantallas/    Bienvenida · Inicio · Registro · Orientacion · Agenda · Historial · Recursos · Ajustes
│     └─ tema/         Tema.kt
├─ res/
│  ├─ raw/             dataset_sintetico.json (datos ficticios)
│  ├─ drawable-nodpi/  logo, isotipo e ícono
│  ├─ mipmap-anydpi*/  ícono adaptable
│  └─ values/          colors.xml · strings.xml · themes.xml
└─ keepRules/
app/src/test/java/<paquete>/PruebasDominio.kt
```

`<paquete>` es el `namespace` de `app/build.gradle.kts` (por ejemplo `com.example.trabajo_desarrollo_aplicaciones_moviles`). Todos los archivos `.kt` deben usar ese mismo paquete.

## Cómo ejecutarlo

1. Abre la carpeta del proyecto (la que contiene `settings.gradle.kts` y `gradlew`) con **Android Studio** compatible con AGP 9.x.
2. Espera el **Gradle Sync** (requiere internet la primera vez).
3. En **Device Manager** crea un emulador con **API 26 o superior** (por ejemplo, Pixel con API 34), o conecta un teléfono con depuración USB.
4. Selecciona el módulo `app` y pulsa **Run ▶**.

Por terminal: `./gradlew assembleDebug` y `./gradlew test`.

## Pruebas manuales sugeridas

1. **Primer uso:** aceptar el aviso, guardar un registro y verlo en Historial.
2. **Orientación:** tocar «Dormí mal» y usar el atajo «Hacer un registro».
3. **Seguridad:** escribir una frase de riesgo de prueba, o registrar ánimo «Muy bajo»; debe aparecer el mensaje de seguridad y el botón de ayuda inmediata.
4. **Agenda:** solicitar horario, «Demo: confirmar» y cancelar.
5. **Persistencia:** cerrar y reabrir la app; los datos se mantienen.
6. **Sin conexión:** activar modo avión; la app sigue funcionando.
7. **Reinicio:** Ajustes > Restablecer datos de prueba.

## Solución de problemas

| Síntoma | Solución |
|---|---|
| No aparece la vista **Android** en el panel de proyecto | Se abrió la carpeta equivocada o falta el Sync: usa `File > Sync Project with Gradle Files`. |
| `ClassNotFoundException: ...MainActivity` al abrir | El `package` de `MainActivity.kt` no coincide con el `namespace` de `app/build.gradle.kts`. |
| `Unresolved reference: R` | El `import ...R` debe usar el mismo paquete que el `namespace`. Haz `Build > Clean Project` y `Rebuild`. |
| `resource drawable/ic_launcher_background not found` | `ic_launcher.xml` e `ic_launcher_round.xml` deben usar `@color/icono_fondo`. |
| La fuente no cambia | Opcional: agrega `outfit_regular`, `outfit_medium`, `outfit_semibold` y `outfit_bold` (`.ttf`) en `res/font/`. |
