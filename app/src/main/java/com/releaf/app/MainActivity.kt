package com.releaf.app

import android.content.Context
import android.content.Intent
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

    companion object {
        private const val EXTRA_LANGUAGE_CHANGED = "language_changed"

        fun restart(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.putExtra(EXTRA_LANGUAGE_CHANGED, true)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                CalmApp(navController = navController)
            }
        }
    }

    /**
     * Applique la langue préférée au contexte de l'activité.
     * Cette méthode est appelée avant onCreate.
     */
    override fun attachBaseContext(newBase: Context?) {
        val wrappedContext = newBase?.let { LanguagePreferences.wrapContext(it) }
        super.attachBaseContext(wrappedContext ?: newBase)
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