package com.releaf.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.releaf.app.data.LanguagePreferences
import com.releaf.app.ui.theme.MyApplicationTheme
import com.releaf.app.ui.navigation.CalmNavigation
import com.releaf.app.ui.navigation.BottomNavigationBar
import com.releaf.app.ui.navigation.shouldShowBottomNav

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Apply language preference
        val languagePreferences = LanguagePreferences(this)
        languagePreferences.applyLanguage(this)
        
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                CalmApp(navController = navController)
            }
        }
    }
}

@Composable
fun CalmApp(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (shouldShowBottomNav(currentRoute)) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        CalmNavigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}