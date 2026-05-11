package com.example.finder.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Search : Screen("search", "SEARCH", Icons.Default.Search)
    object Stores : Screen("stores", "STORES", Icons.Default.Home)
    object Refills : Screen("refills", "REFILLS", Icons.Default.Notifications)
    object Savings : Screen("savings", "SAVINGS", Icons.Default.AccountBalanceWallet)
}
