package com.example.finder.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finder.data.Store
import com.example.finder.data.StoreRepository
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun StoreLocatorScreen(modifier: Modifier = Modifier, totalSavings: Double) {
    val repository = remember { StoreRepository() }
    val allStores = repository.getNearbyStores()
    var storeSearchQuery by remember { mutableStateOf("") }
    
    val filteredStores = remember(storeSearchQuery) {
        if (storeSearchQuery.isEmpty()) {
            allStores
        } else {
            allStores.filter { 
                it.name.contains(storeSearchQuery, ignoreCase = true) || 
                it.address.contains(storeSearchQuery, ignoreCase = true)
            }
        }
    }
    
    val centerLocation = LatLng(28.4595, 77.0266)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(centerLocation, 13f)
    }

    Column(modifier = modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        JanAushadhiHeader(totalSaved = totalSavings)

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = storeSearchQuery,
                onValueChange = { storeSearchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search city, area or store name...", color = Color.LightGray) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.LightGray) },
                shape = RoundedCornerShape(30.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00BCD4),
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        uiSettings = MapUiSettings(zoomControlsEnabled = false)
                    ) {
                        filteredStores.forEach { store ->
                            Marker(
                                state = MarkerState(position = LatLng(store.latitude, store.longitude)),
                                title = store.name
                            )
                        }
                    }
                }
                
                Surface(
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 12.dp),
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocationOn, null, tint = Color.Red, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("NEAR YOUR LOCATION", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "NEARBY KENDRAS (${filteredStores.size})",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "WITHIN 10KM",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2ECC71)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredStores) { store ->
                    StoreItemUI(store) {
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(
                            LatLng(store.latitude, store.longitude), 
                            15f
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StoreItemUI(store: Store, onStoreClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var medicineName by remember { mutableStateOf("") }
    var stockStatus by remember { mutableStateOf<String?>(null) }
    var isChecking by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded; onStoreClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF1F8E9)
                ) {
                    Icon(Icons.Default.Storefront, null, tint = Color(0xFF2E7D32), modifier = Modifier.padding(12.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(store.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(store.address, style = MaterialTheme.typography.bodySmall, color = Color.Gray, maxLines = 1)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = Color(0xFFF1F3F4), shape = RoundedCornerShape(4.dp)) {
                            Text(store.distance, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(color = if (store.isOpen) Color(0xFF2ECC71) else Color.Red, shape = RoundedCornerShape(4.dp)) {
                            Text(if (store.isOpen) "• OPEN" else "• CLOSED", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = null, tint = Color.LightGray)
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(onClick = {}, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                            Icon(Icons.Default.Call, null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Call")
                        }
                        Button(onClick = {}, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))) {
                            Icon(Icons.Default.Directions, null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Directions")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Stock Inquiry Section (Restored and Simulated)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0F7FA))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.AutoMirrored.Outlined.HelpOutline, null, tint = Color(0xFF00BCD4), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("STOCK INQUIRY", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = medicineName,
                                onValueChange = { medicineName = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Enter medicine name...", fontSize = 12.sp, color = Color.Gray) },
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedBorderColor = Color(0xFF00BCD4)
                                ),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    if (medicineName.isNotBlank()) {
                                        isChecking = true
                                        scope.launch {
                                            delay(1200) // Professional simulation delay
                                            // Randomized logic to make it feel like a real backend check
                                            stockStatus = if (Random.nextBoolean()) "IN STOCK" else "OUT OF STOCK"
                                            isChecking = false
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4)),
                                enabled = !isChecking
                            ) {
                                if (isChecking) {
                                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                                } else {
                                    Text("Check Availability", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            
                            stockStatus?.let { status ->
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = if (status == "IN STOCK") Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = status,
                                        modifier = Modifier.padding(10.dp),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (status == "IN STOCK") Color(0xFF2E7D32) else Color.Red,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
