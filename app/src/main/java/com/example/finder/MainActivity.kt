package com.example.finder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.finder.data.AppDatabase
import com.example.finder.data.MedicineRepository
import com.example.finder.ui.*
import com.example.finder.ui.theme.FinderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = AppDatabase.getDatabase(this)
        val repository = MedicineRepository(database.medicineDao())
        val viewModelFactory = MedicineViewModelFactory(application, repository)

        enableEdgeToEdge()
        setContent {
            FinderTheme {
                val navController = rememberNavController()
                val searchViewModel: MedicineSearchViewModel = viewModel(factory = viewModelFactory)
                val totalSavings by searchViewModel.totalSavings.collectAsState()
                
                val items = listOf(
                    Screen.Search,
                    Screen.Stores,
                    Screen.Refills,
                    Screen.Savings
                )
                
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            containerColor = Color.White,
                            tonalElevation = 0.dp
                        ) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            items.forEach { screen ->
                                val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                                NavigationBarItem(
                                    icon = { 
                                        Icon(
                                            imageVector = screen.icon, 
                                            contentDescription = null,
                                            tint = if (selected) Color(0xFF007991) else Color(0xFF49454F)
                                        ) 
                                    },
                                    label = { 
                                        Text(
                                            text = screen.label, 
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (selected) Color(0xFF007991) else Color(0xFF49454F),
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                                        ) 
                                    },
                                    selected = selected,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = Color(0xFFE0F7FA)
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Search.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Search.route) { 
                            MedicineSearchScreen(viewModel = searchViewModel) 
                        }
                        composable(Screen.Stores.route) { 
                            StoreLocatorScreen(totalSavings = totalSavings) 
                        }
                        composable(Screen.Refills.route) { 
                            RemindersScreen(
                                totalSavings = totalSavings,
                                viewModel = searchViewModel
                            )
                        }
                        composable(Screen.Savings.route) { 
                            SavingsScreen(totalSavings = totalSavings)
                        }
                    }
                }
            }
        }
    }
}
