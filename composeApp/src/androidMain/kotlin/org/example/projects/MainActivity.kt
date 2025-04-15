package org.example.projects

import AndroidNavigator
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.projects.Screens.LibrosScreen
import androidx.compose.material.MaterialTheme
import org.example.projects.Screens.Login
import org.example.projects.ViewModel.SharedViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme { // Reemplaza MyAppTheme con MaterialTheme
                val navController = rememberNavController()
                val navigator = remember { AndroidNavigator(navController) }
                val viewModel = remember { SharedViewModel() }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    content = { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "login",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("detail") { LibrosScreen(navigator,viewModel) }
                            composable("login") { Login(Modifier,navigator,viewModel) }
                        }
                    }
                )
            }
        }
    }
}