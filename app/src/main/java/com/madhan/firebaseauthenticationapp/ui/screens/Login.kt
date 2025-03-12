package com.madhan.firebaseauthenticationapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.madhan.firebaseauthenticationapp.R
import com.madhan.firebaseauthenticationapp.utils.GoogleAuthClient
import com.madhan.firebaseauthenticationapp.utils.Routes
import com.madhan.firebaseauthenticationapp.viewmodel.FirebaseAuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    navController: NavHostController,
    authViewModel: FirebaseAuthViewModel = viewModel()
) {

    var email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current
    val applicationContext = context.applicationContext
    val googleAuthClient = GoogleAuthClient(applicationContext)

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign in to your Account",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.size(32.dp))

            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
                )

            Spacer(modifier = Modifier.size(12.dp))

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                modifier = Modifier,
                label = { Text(text = "Email address") },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                ),
                maxLines = 1
            )

            Spacer(modifier = Modifier.size(12.dp))

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                modifier = Modifier,
                label = { Text(text = "Password") },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                ),
                maxLines = 1,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.size(12.dp))

            Button(
                onClick = {
                    // validate details and navigate to home page
                    authViewModel.login(email.value, password.value){ success, errorMessage ->
                        if (success) {
                            Toast.makeText(context, "Account Login Successful", Toast.LENGTH_LONG).show()
                            navController.navigate(Routes.Home) {
                                popUpTo(Routes.Login) { inclusive = true } // clear all backstack
                            }

                        } else {
                            Toast.makeText(context, errorMessage ?: "something went wrong", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                modifier = Modifier.width(280.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                enabled = (email.value.length >=13 && password.value.length >= 6)
            ) {
                Text(
                    text = "Login"
                )
            }

            Spacer(modifier = Modifier.size(16.dp))
            Text(text = "--- or ---")
            Spacer(modifier = Modifier.size(16.dp))

            OutlinedButton(
                onClick = {
                    // navigate to home screen
                    CoroutineScope(Dispatchers.Main).launch {

                        if (!googleAuthClient.isSignedIn()) {
                            val isSignedIn = googleAuthClient.signIn() // Store result
                            if (isSignedIn) { // Only navigate if sign-in succeeds
                                navController.navigate(Routes.Home) {
                                    popUpTo(Routes.Login) { inclusive = true } // clear all backstack
                                }
                            } else {
                                Toast.makeText(context, "Google Sign-in Failed", Toast.LENGTH_LONG).show()
                            }
                        }

                    }
                },
                modifier = Modifier.width(280.dp),
                shape = RectangleShape,
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.googel_logo),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                    )
                    Spacer(modifier = Modifier.size(5.dp))
                    Text(
                        text = "Continue with Google"
                    )
                }
            }

            Spacer(modifier = Modifier.size(24.dp))

            Row {
                Text(
                    text = "Don't have an account?"
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "Click here to create",
                    color = Color.Blue,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.SignUp)
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview(){
    val navController = rememberNavController()
    Login(navController)
}