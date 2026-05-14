package dev.andre.kvadraos_apps___services

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import dev.andre.kvadraos_apps___services.presentation.ContactsScreen
import dev.andre.kvadraos_apps___services.ui.theme.KvadraOS_Apps___ServicesTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KvadraOS_Apps___ServicesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ContactsScreen()
                }
            }
        }
    }
}