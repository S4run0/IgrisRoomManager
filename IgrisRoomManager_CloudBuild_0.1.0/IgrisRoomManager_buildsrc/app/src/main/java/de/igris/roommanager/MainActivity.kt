package de.igris.roommanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF101319)
private val Panel = Color(0xFF1A2029)
private val Accent = Color(0xFF8D7CFF)
private val Muted = Color(0xFF9AA4B2)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { IgrisApp() }
    }
}

@Composable
fun IgrisApp() {
    var page by remember { mutableStateOf("Home") }
    var mode by remember { mutableStateOf("2D") }
    var roomName by remember { mutableStateOf("Wohnzimmer") }
    var width by remember { mutableStateOf("5.0") }
    var length by remember { mutableStateOf("4.0") }
    var furniture by remember { mutableStateOf(listOf("Sofa", "Couchtisch")) }

    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Bg, surface = Panel, primary = Accent,
            onBackground = Color.White, onSurface = Color.White
        )
    ) {
        Scaffold(
            containerColor = Bg,
            bottomBar = {
                NavigationBar(containerColor = Panel, contentColor = Color.White) {
                    listOf("Home", "Planer", "Möbel", "Budget").forEach { item ->
                        NavigationBarItem(
                            selected = page == item,
                            onClick = { page = item },
                            icon = {
                                Icon(
                                    when (item) {
                                        "Home" -> Icons.Default.Home
                                        "Planer" -> Icons.Default.GridView
                                        "Möbel" -> Icons.Default.Weekend
                                        else -> Icons.Default.Calculate
                                    }, contentDescription = item
                                )
                            },
                            label = { Text(item) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Accent,
                                selectedTextColor = Color.White,
                                indicatorColor = Accent.copy(alpha = .18f),
                                unselectedIconColor = Muted,
                                unselectedTextColor = Muted
                            )
                        )
                    }
                }
            }
        ) { padding ->
            when (page) {
                "Home" -> HomeScreen(Modifier.padding(padding), onOpen = { page = it })
                "Planer" -> PlannerScreen(
                    Modifier.padding(padding), mode, { mode = it },
                    roomName, { roomName = it }, width, { width = it },
                    length, { length = it }, furniture, { furniture = it }
                )
                "Möbel" -> FurnitureScreen(Modifier.padding(padding), furniture) { furniture = furniture + it }
                else -> BudgetScreen(Modifier.padding(padding), furniture.size)
            }
        }
    }
}

@Composable
fun ScreenTitle(title: String, subtitle: String? = null) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Color.White)
        if (subtitle != null) Text(subtitle, color = Muted, fontSize = 13.sp)
    }
}

@Composable
fun HomeScreen(modifier: Modifier, onOpen: (String) -> Unit) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("IGRIS", color = Accent, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 4.sp)
            ScreenTitle("Room Manager", "Deine Wohnung. Dein Raum. Deine Regeln.")
        }
        item {
            Surface(shape = RoundedCornerShape(22.dp), color = Panel, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(Icons.Default.Home, null, tint = Accent, modifier = Modifier.size(32.dp))
                    Text("Dein Zuhause beginnt hier", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Text("Plane Räume, probiere Möbel aus und behalte dein Budget im Blick.", color = Muted)
                    Button(onClick = { onOpen("Planer") }) { Text("Planung starten") }
                }
            }
        }
        item { Text("DEINE WERKZEUGE", color = Muted, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                HomeTile("Sandbox", "2D & 3D", Icons.Default.GridView, Modifier.weight(1f)) { onOpen("Planer") }
                HomeTile("Möbelkatalog", "Eigene Möbel", Icons.Default.Weekend, Modifier.weight(1f)) { onOpen("Möbel") }
            }
        }
        item {
            HomeTile("Budgetplaner", "Kostenübersicht", Icons.Default.Calculate, Modifier.fillMaxWidth()) { onOpen("Budget") }
        }
        item { Text("Version 0.1.0 · Entwicklungsprototyp", color = Muted, fontSize = 11.sp) }
    }
}

@Composable
fun HomeTile(title: String, desc: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Surface(onClick = onClick, color = Panel, shape = RoundedCornerShape(18.dp), modifier = modifier) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, null, tint = Accent)
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(desc, color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
fun PlannerScreen(
    modifier: Modifier, mode: String, setMode: (String) -> Unit,
    roomName: String, setRoomName: (String) -> Unit,
    width: String, setWidth: (String) -> Unit,
    length: String, setLength: (String) -> Unit,
    furniture: List<String>, setFurniture: (List<String>) -> Unit
) {
    var sofaX by remember { mutableFloatStateOf(0.36f) }
    var sofaY by remember { mutableFloatStateOf(0.42f) }
    LazyColumn(modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { ScreenTitle("Raum-Sandbox", "Grundriss planen und Einrichtung testen") }
        item {
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                listOf("2D", "3D").forEachIndexed { i, item ->
                    SegmentedButton(
                        selected = mode == item,
                        onClick = { setMode(item) },
                        shape = SegmentedButtonDefaults.itemShape(i, 2)
                    ) { Text(item) }
                }
            }
        }
        item {
            if (mode == "2D") {
                Surface(color = Panel, shape = RoundedCornerShape(18.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text("Grundriss · von oben", fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(8.dp))
                        Box(Modifier.fillMaxWidth().height(270.dp)) {
                            Canvas(Modifier.fillMaxSize().background(Color(0xFF131922))) {
                                val grid = 24f
                                for (x in 0..(size.width / grid).toInt()) {
                                    drawLine(Color(0xFF26303D), Offset(x * grid, 0f), Offset(x * grid, size.height), 1f)
                                }
                                for (y in 0..(size.height / grid).toInt()) {
                                    drawLine(Color(0xFF26303D), Offset(0f, y * grid), Offset(size.width, y * grid), 1f)
                                }
                                val left = size.width * .08f
                                val top = size.height * .08f
                                val rw = size.width * .84f
                                val rh = size.height * .84f
                                drawRect(Color(0xFFB8C2D1), Offset(left, top), Size(rw, rh), style = androidx.compose.ui.graphics.drawscope.Stroke(5f))
                                drawRect(Accent.copy(alpha = .8f), Offset(left + sofaX * rw, top + sofaY * rh), Size(rw * .30f, rh * .17f))
                                drawRect(Color(0xFF526174), Offset(left + rw * .48f, top + rh * .53f), Size(rw * .18f, rh * .14f))
                            }
                            Box(
                                Modifier.fillMaxSize().pointerInput(Unit) {
                                    detectDragGestures { change, drag ->
                                        change.consume()
                                        sofaX = (sofaX + drag.x / size.width).coerceIn(0f, .68f)
                                        sofaY = (sofaY + drag.y / size.height).coerceIn(0f, .78f)
                                    }
                                }
                            )
                            Text("Sofa verschieben: im Plan ziehen", color = Color.White, fontSize = 10.sp,
                                modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp))
                        }
                        Text("Prototyp: Sofa-Position lässt sich bereits verschieben.", color = Muted, fontSize = 11.sp)
                    }
                }
            } else {
                Surface(color = Panel, shape = RoundedCornerShape(18.dp)) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(Icons.Default.ViewInAr, null, tint = Accent, modifier = Modifier.size(36.dp))
                        Text("3D-Ansicht ist vorgemerkt", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                        Text("Der nächste Entwicklungsschritt ergänzt eine echte 3D-Raumansicht mit Wänden, Türen, Fenstern und maßstabsgetreuen Möbeln.", color = Muted)
                    }
                }
            }
        }
        item {
            Surface(color = Panel, shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Raumdaten", fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(value = roomName, onValueChange = setRoomName, label = { Text("Raumname") }, modifier = Modifier.fillMaxWidth())
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = width, onValueChange = setWidth, label = { Text("Breite (m)") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = length, onValueChange = setLength, label = { Text("Länge (m)") }, modifier = Modifier.weight(1f))
                    }
                    val area = (width.toDoubleOrNull() ?: 0.0) * (length.toDoubleOrNull() ?: 0.0)
                    Text("Fläche: %.2f m²".format(area), color = Accent, fontWeight = FontWeight.Bold)
                    Text("Möbel im Raum: ${furniture.joinToString()}", color = Muted, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun FurnitureScreen(modifier: Modifier, furniture: List<String>, add: (String) -> Unit) {
    val items = listOf(
        "Sofa" to "Demo · frei platzierbar",
        "Bett" to "Demo · frei platzierbar",
        "Kleiderschrank" to "Demo · Maße später ergänzen",
        "Esstisch" to "Demo · frei platzierbar",
        "Regal" to "Demo · frei platzierbar",
        "Eigene Möbel" to "Manuellen Platzhalter hinzufügen"
    )
    LazyColumn(modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenTitle("Möbelkatalog", "IKEA · OTTO · eigene Möbel") }
        item {
            Surface(color = Panel, shape = RoundedCornerShape(16.dp)) {
                Text("Händlerprodukte sind in diesem Prototyp noch nicht live angebunden. Später übernehmen wir verifizierte Produktdaten, Maße, Preise und Links.", color = Muted, modifier = Modifier.padding(14.dp))
            }
        }
        items(items.size) { index ->
            val item = items[index]
            Surface(color = Panel, shape = RoundedCornerShape(14.dp)) {
                Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Weekend, null, tint = Accent)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(item.first, fontWeight = FontWeight.SemiBold)
                        Text(item.second, color = Muted, fontSize = 11.sp)
                    }
                    TextButton(onClick = { add(item.first) }) { Text("Hinzufügen") }
                }
            }
        }
        item { Text("Aktuell im Projekt: ${furniture.joinToString()}", color = Muted, fontSize = 12.sp) }
    }
}

@Composable
fun BudgetScreen(modifier: Modifier, count: Int) {
    Column(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        ScreenTitle("Budgetplaner", "Einrichtungskosten im Blick behalten")
        Surface(color = Panel, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Deine Planung", color = Muted)
                Text("$count Möbel-Einträge", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Preisfelder, Summen und Budgetgrenzen kommen im nächsten Ausbauschritt.", color = Muted)
            }
        }
    }
}
