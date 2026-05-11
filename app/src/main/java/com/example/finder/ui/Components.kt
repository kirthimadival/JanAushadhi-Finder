package com.example.finder.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.util.Locale

@Composable
fun JanAushadhiHeader(totalSaved: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(42.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF00BCD4)
        ) {
            Icon(
                imageVector = Icons.Default.MedicalServices,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(10.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Jan-Aushadhi",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1A1C1E),
                fontSize = 20.sp
            )
            Text(
                text = "HEALTHCARE SAVINGS",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF00BCD4),
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        if (totalSaved > 0) {
            Surface(
                color = Color(0xFFE0F7FA),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(6.dp).background(Color(0xFF2ECC71), CircleShape))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = String.format(Locale.getDefault(), "₹%.0f Saved", totalSaved),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = { /* Info */ }, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Info, contentDescription = null, tint = Color.LightGray)
        }
    }
}

data class InfoModalData(
    val title: String,
    val icon: ImageVector,
    val iconColor: Color,
    val items: List<Pair<String, String>>
)

@Composable
fun ExploreDashboard() {
    var selectedInfo by remember { mutableStateOf<InfoModalData?>(null) }

    Column(modifier = Modifier.padding(top = 24.dp)) {
        Text(
            text = "IMPACT & LITERACY",
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                DashboardItem(
                    title = "Medicine Quality",
                    subtitle = "Learn how generics are tested and certified.",
                    icon = Icons.Default.Security,
                    iconBg = Color(0xFFF3E5F5),
                    iconColor = Color(0xFF9C27B0),
                    onClick = {
                        selectedInfo = InfoModalData(
                            title = "Certified Quality",
                            icon = Icons.Default.Security,
                            iconColor = Color(0xFF9C27B0),
                            items = listOf(
                                "WHO-GMP Verified" to "Manufactured in world-class facilities meeting global safety protocols.",
                                "Triple NABL Testing" to "Every batch is independently tested at certified government labs.",
                                "Therapeutic Parity" to "Bio-equivalence studies ensure it works exactly like the expensive brand."
                            )
                        )
                    }
                )
                DashboardItem(
                    title = "Affordability Goal",
                    subtitle = "PMBJP's vision for subsidized medicine.",
                    icon = Icons.Default.AdsClick,
                    iconBg = Color(0xFFFFF3E0),
                    iconColor = Color(0xFFFB8C00),
                    onClick = {
                        selectedInfo = InfoModalData(
                            title = "The PMBJP Goal",
                            icon = Icons.Default.AdsClick,
                            iconColor = Color(0xFFFB8C00),
                            items = listOf(
                                "Health for All" to "Providing quality healthcare at prices affordable to every Indian citizen.",
                                "Subsidized Price" to "Reducing the out-of-pocket expenditure on medicines by up to 90%.",
                                "Vast Network" to "Over 10,000+ Kendras ensuring last-mile delivery of essential drugs."
                            )
                        )
                    }
                )
                DashboardItem(
                    title = "Savings Simulator",
                    subtitle = "Calculate savings for family prescriptions.",
                    icon = Icons.Default.Calculate,
                    iconBg = Color(0xFFE1F5FE),
                    iconColor = Color(0xFF03A9F4),
                    onClick = {
                        selectedInfo = InfoModalData(
                            title = "Saving Potential",
                            icon = Icons.Default.Calculate,
                            iconColor = Color(0xFF03A9F4),
                            items = listOf(
                                "Family Savings" to "A typical chronic patient can save ₹2,000 to ₹5,000 every month.",
                                "Same Formula" to "Pay for the medicine, not the brand's marketing and celebrity ads.",
                                "Smart Tracking" to "Use this app to log your daily savings and see your yearly impact."
                            )
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Milestone Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1C1E))
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "CURRENT MILESTONE",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Health Literacy Champion",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { 0.75f },
                        color = Color(0xFF00BCD4),
                        trackColor = Color.White.copy(alpha = 0.1f),
                        strokeWidth = 4.dp,
                        modifier = Modifier.size(56.dp)
                    )
                    Text(
                        text = "75%",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    if (selectedInfo != null) {
        Dialog(onDismissRequest = { selectedInfo = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = selectedInfo!!.iconColor.copy(alpha = 0.1f)
                    ) {
                        Icon(
                            imageVector = selectedInfo!!.icon,
                            contentDescription = null,
                            tint = selectedInfo!!.iconColor,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = selectedInfo!!.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    selectedInfo!!.items.forEach { (heading, desc) ->
                        Row(modifier = Modifier.padding(bottom = 20.dp)) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .size(6.dp)
                                    .background(Color(0xFF2ECC71), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = heading,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A1C1E)
                                )
                                Text(
                                    text = desc,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                    
                    Button(
                        onClick = { selectedInfo = null },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1C1E)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("CLOSE HEADER", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardItem(
    title: String, 
    subtitle: String, 
    icon: ImageVector, 
    iconBg: Color, 
    iconColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(12.dp),
            color = iconBg
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.padding(12.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.LightGray
        )
    }
}
