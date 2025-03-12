package com.madhan.firebaseauthenticationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.madhan.firebaseauthenticationapp.ui.AppNavigation
import com.madhan.firebaseauthenticationapp.ui.theme.FirebaseAuthenticationAppTheme
import com.madhan.firebaseauthenticationapp.utils.GoogleAuthClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FirebaseAuthenticationAppTheme {
               AppNavigation()
            }
        }
    }
}
