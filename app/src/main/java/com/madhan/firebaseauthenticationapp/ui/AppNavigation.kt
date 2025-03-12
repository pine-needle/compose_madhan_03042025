package com.madhan.firebaseauthenticationapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.madhan.firebaseauthenticationapp.ui.screens.About
import com.madhan.firebaseauthenticationapp.ui.screens.Contact
import com.madhan.firebaseauthenticationapp.ui.screens.Home
import com.madhan.firebaseauthenticationapp.ui.screens.Login
import com.madhan.firebaseauthenticationapp.ui.screens.SignUp
import com.madhan.firebaseauthenticationapp.utils.Routes

@Composable
fun AppNavigation(){
    val navController: NavHostController = rememberNavController()
    val isLoggedIn: Boolean = Firebase.auth.currentUser != null

    val firstPage = if(isLoggedIn) Routes.Home else Routes.Login

    NavHost(navController = navController, startDestination = firstPage) {
        composable(Routes.Home) { Home(navController) }
        composable(Routes.About) { About() }
        composable(Routes.Contact) { Contact() }
        composable(Routes.Login) { Login(navController) }
        composable(Routes.SignUp) { SignUp(navController) }
    }
}


