@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.trabajo_desarrollo_aplicaciones_moviles.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.unit.dp
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.tema.ColoresVitairos
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.trabajo_desarrollo_aplicaciones_moviles.dominio.Rutas
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.pantallas.PantallaAgenda
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.pantallas.PantallaOrientacion
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.pantallas.PantallaHistorial
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.pantallas.PantallaInicio
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.pantallas.PantallaRegistro
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.pantallas.PantallaBienvenida
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.pantallas.PantallaRecursos
import com.example.trabajo_desarrollo_aplicaciones_moviles.ui.pantallas.PantallaAjustes

private data class ItemNav(val route: String, val label: String, val icon: ImageVector)

private val itemsNav = listOf(
    ItemNav(Rutas.INICIO, "Inicio", Icons.Outlined.Home),
    ItemNav(Rutas.REGISTRO, "Registro", Icons.Outlined.Create),
    ItemNav(Rutas.ORIENTACION, "Orientación", Icons.Outlined.Face),
    ItemNav(Rutas.AGENDA, "Agenda", Icons.Outlined.DateRange),
    ItemNav(Rutas.HISTORIAL, "Historial", Icons.Outlined.List)
)

private fun tituloPara(route: String?): String = when (route) {
    Rutas.RECURSOS -> "Recursos de ayuda"
    Rutas.AJUSTES -> "Ajustes"
    else -> itemsNav.firstOrNull { it.route == route }?.label ?: ""
}

fun NavHostController.irA(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun AppVitairos(vm: ModeloVistaApp) {
    val state by vm.state.collectAsStateWithLifecycle()
    when {
        !state.cargado -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            IsotipoVitairos(tamano = 72.dp)
        }
        !state.bienvenidaCompletada -> PantallaBienvenida(alAceptar = vm::completarBienvenida)
        else -> {
            val nav = rememberNavController()
            val entry by nav.currentBackStackEntryAsState()
            val route = entry?.destination?.route
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            titleContentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        navigationIcon = { IsotipoVitairos(Modifier.padding(start = 12.dp), 36.dp) },
                        title = { Text(tituloPara(route)) },
                        actions = {
                            IconButton(onClick = { nav.irA(Rutas.RECURSOS) }) {
                                Icon(Icons.Outlined.Info, contentDescription = "Recursos de ayuda")
                            }
                            IconButton(onClick = { nav.irA(Rutas.AJUSTES) }) {
                                Icon(Icons.Outlined.Settings, contentDescription = "Ajustes")
                            }
                        }
                    )
                },
                bottomBar = {
                    NavigationBar(containerColor = MaterialTheme.colorScheme.background) {
                        itemsNav.forEach { item ->
                            NavigationBarItem(
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = ColoresVitairos.Navy,
                                    selectedTextColor = MaterialTheme.colorScheme.onBackground,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                selected = route == item.route,
                                onClick = { nav.irA(item.route) },
                                icon = { Icon(item.icon, contentDescription = null) },
                                label = { Text(item.label) }
                            )
                        }
                    }
                }
            ) { padding ->
                NavHost(nav, startDestination = Rutas.INICIO, modifier = Modifier.padding(padding)) {
                    composable(Rutas.INICIO) { PantallaInicio(state, alNavegar = nav::irA) }
                    composable(Rutas.REGISTRO) {
                        PantallaRegistro(
                            alGuardar = vm::agregarRegistro,
                            alGuardado = { nav.irA(Rutas.HISTORIAL) }
                        )
                    }
                    composable(Rutas.ORIENTACION) {
                        PantallaOrientacion(state.chat, alEnviar = vm::enviarMensaje, alNavegar = nav::irA)
                    }
                    composable(Rutas.AGENDA) {
                        PantallaAgenda(
                            state,
                            alSolicitar = vm::solicitarCita,
                            alConfirmarDemo = vm::simularConfirmacion,
                            alCancelar = vm::cancelarCita
                        )
                    }
                    composable(Rutas.HISTORIAL) { PantallaHistorial(state) }
                    composable(Rutas.RECURSOS) { PantallaRecursos(state.recursos) }
                    composable(Rutas.AJUSTES) {
                        PantallaAjustes(
                            state,
                            alCambiarRecordatorios = vm::activarRecordatorios,
                            alRestablecer = vm::restablecerDatosPrueba
                        )
                    }
                }
            }
        }
    }
}
