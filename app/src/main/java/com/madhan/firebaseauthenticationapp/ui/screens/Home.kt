package com.madhan.firebaseauthenticationapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.madhan.firebaseauthenticationapp.utils.GoogleAuthClient
import com.madhan.firebaseauthenticationapp.utils.Routes
import com.madhan.firebaseauthenticationapp.viewmodel.FirebaseAuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Home(navController: NavHostController, authViewModel: FirebaseAuthViewModel  = viewModel()) {

    val applicationContext = LocalContext.current.applicationContext
    val googleAuthClient = GoogleAuthClient(applicationContext)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home Screen", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.size(32.dp))

        Button(
            onClick = {
                if(googleAuthClient.isSignedIn()){
                    CoroutineScope(Dispatchers.Main).launch {
                        googleAuthClient.signOut()
                    }
                }else{
                    authViewModel.signOut()
                }

                navController.navigate(Routes.Login) {
                    popUpTo(Routes.Home) { inclusive = true } // clear all backstack
                }
            },
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(text = "Logout")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val navController = rememberNavController()
    Home(navController)
}