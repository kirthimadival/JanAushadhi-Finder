package com.example.finder.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finder.data.Medicine
import java.util.Locale

@Composable
fun MedicineSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: MedicineSearchViewModel
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val selectedMedicine by viewModel.selectedMedicine.collectAsState()
    val totalSavings by viewModel.totalSavings.collectAsState()

    Scaffold(
        topBar = { JanAushadhiHeader(totalSaved = totalSavings) },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            // Constant Search Bar
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search branded medicines (e.g. Crocin, Dolo)...", color = Color.LightGray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.LightGray) },
                shape = RoundedCornerShape(30.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00BCD4),
                    unfocusedBorderColor = Color(0xFFF1F3F4),
                    focusedContainerColor = Color(0xFFF8F9FA),
                    unfocusedContainerColor = Color(0xFFF8F9FA)
                )
            )

            if (selectedMedicine != null) {
                // Comparison Detail View (Matches Screenshot 19:27)
                ComparisonView(
                    medicine = selectedMedicine!!,
                    onAddResult = { viewModel.addSavings(selectedMedicine!!.savings) },
                    onReset = { viewModel.clearSelection() }
                )
            } else if (searchQuery.isEmpty()) {
                // Main Dashboard
                ExploreDashboard()
            } else {
                // Result List (Matches list style in screenshot 19:27)
                Spacer(modifier = Modifier.height(24.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(searchResults) { medicine ->
                        SearchResultItem(
                            medicine = medicine,
                            onClick = { viewModel.selectMedicine(medicine) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultItem(medicine: Medicine, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = Color.White
            ) {
                Icon(
                    Icons.Default.Medication,
                    contentDescription = null,
                    tint = Color(0xFF00BCD4),
                    modifier = Modifier.padding(10.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medicine.brandedName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                )
                Text(
                    text = medicine.genericName.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    letterSpacing = 0.5.sp
                )
            }
            Surface(
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "₹${medicine.genericPrice.toInt()}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}

@Composable
fun ComparisonView(medicine: Medicine, onAddResult: () -> Unit, onReset: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = medicine.brandedName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF2ECC71),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "STANDARD GENERIC AVAILABLE",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF2ECC71),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Surface(
                color = Color(0xFFF1F3F4),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "650MG", 
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Market Price Card (White Background, Gray text)
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(110.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("MARKET PRICE", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text(
                        text = "₹${medicine.brandedPrice.toInt()}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }

            // Aushadhi Price Card (Green Background, White text)
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(110.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2ECC71))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("AUSHADHI PRICE", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.8f))
                    Text(
                        text = "₹${medicine.genericPrice.toInt()}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Daily Savings Bar
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1C1E))
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF00BCD4)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.TrendingDown,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("DAILY SAVINGS", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.6f))
                    Text(
                        text = "₹${medicine.savings.toInt()} saved",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00BCD4)
                    )
                }
                Button(
                    onClick = onAddResult,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("ADD RESULT", color = Color(0xFF1A1C1E), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Info Sections
        InfoSection(title = "GENERIC CONTENT (SALT)", content = medicine.genericName.uppercase(), icon = Icons.Default.Info)
        Spacer(modifier = Modifier.height(24.dp))
        InfoSection(title = "INDICATIONS/USES", content = medicine.usage, icon = Icons.Default.Warning)

        Spacer(modifier = Modifier.weight(1f))
        
        TextButton(
            onClick = onReset,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "RESET COMPARISON",
                color = Color.LightGray,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun InfoSection(title: String, content: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier.size(24.dp),
            shape = CircleShape,
            color = Color(0xFFE0F7FA)
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF00BCD4), modifier = Modifier.padding(4.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(content, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF1A1C1E), fontWeight = FontWeight.Bold)
        }
    }
}
