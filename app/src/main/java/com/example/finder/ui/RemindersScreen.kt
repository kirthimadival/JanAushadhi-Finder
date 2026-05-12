package com.example.finder.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finder.data.MedicineReminder

@Composable
fun RemindersScreen(
    modifier: Modifier = Modifier, 
    totalSavings: Double,
    viewModel: MedicineSearchViewModel
) {
    var showForm by remember { mutableStateOf(false) }
    var medicineName by remember { mutableStateOf("") }
    var refillDate by remember { mutableStateOf("") }
    
    val reminders by viewModel.reminders.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        JanAushadhiHeader(totalSaved = totalSavings)

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Monthly Refills",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black // True Black Heading
                    )
                    Text(
                        text = "Track and manage your medicine stock",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                
                SmallFloatingActionButton(
                    onClick = { showForm = !showForm },
                    containerColor = Color(0xFF00BCD4),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(if (showForm) Icons.Default.Close else Icons.Default.Add, contentDescription = "Toggle Form")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (showForm) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "MEDICINE NAME",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = medicineName,
                            onValueChange = { medicineName = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("e.g. Telmisartan 40mg", color = Color.LightGray) },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White, // Stays Pure White
                                focusedContainerColor = Color.White,   // Stays Pure White
                                unfocusedBorderColor = Color(0xFFF1F3F4),
                                focusedBorderColor = Color(0xFF00BCD4),
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "NEXT REFILL DATE",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = refillDate,
                            onValueChange = { refillDate = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("dd-mm-yyyy", color = Color.LightGray) },
                            trailingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color.Gray) },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White, // Stays Pure White
                                focusedContainerColor = Color.White,   // Stays Pure White
                                unfocusedBorderColor = Color(0xFFF1F3F4),
                                focusedBorderColor = Color(0xFF00BCD4),
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            )
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { 
                                if (medicineName.isNotBlank() && refillDate.isNotEmpty()) {
                                    viewModel.addReminder(medicineName, refillDate)
                                    medicineName = ""
                                    refillDate = ""
                                    showForm = false
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
                        ) {
                            Text("Set Reminder", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            } else if (reminders.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            modifier = Modifier.size(56.dp),
                            shape = CircleShape,
                            color = Color(0xFFF8F9FA)
                        ) {
                            Icon(
                                Icons.Default.NotificationsNone,
                                contentDescription = null,
                                tint = Color.LightGray,
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No active trackers",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black // True Black
                        )
                        Text(
                            text = "Add your monthly medications to\nnever miss a refill at Jan-Aushadhi\nstores.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth().weight(1f, fill = false)
                ) {
                    items(reminders) { reminder ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier.size(40.dp),
                                    shape = CircleShape,
                                    color = Color(0xFFE0F7FA)
                                ) {
                                    Icon(Icons.Default.Notifications, null, tint = Color(0xFF00BCD4), modifier = Modifier.padding(8.dp))
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(reminder.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.Black)
                                    Text("Next Refill: ${reminder.date}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                }
                                IconButton(onClick = { viewModel.deleteReminder(reminder) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.LightGray)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9).copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(32.dp),
                        shape = CircleShape,
                        color = Color.White
                    ) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = null,
                            tint = Color(0xFF2ECC71),
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Refill Savings Tip",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2ECC71)
                        )
                        Text(
                            text = "Getting your monthly refills from Jan-Aushadhi Kendras can save you up to ₹1,500 every month on average for chronic conditions.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF2ECC71).copy(alpha = 0.8f),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}
