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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
//import androidx.compose.runtime.setValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.madhan.firebaseauthenticationapp.R
import com.madhan.firebaseauthenticationapp.utils.Routes
import com.madhan.firebaseauthenticationapp.viewmodel.FirebaseAuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(navController: NavHostController, authViewModel: FirebaseAuthViewModel = viewModel()) {

    var username = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }

    var context = LocalContext.current

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create an Account",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.size(32.dp))

            Image(
                painter = painterResource(id = R.drawable.signup),
                contentDescription = null,
                modifier = Modifier.size(200.dp),

                )

            Spacer(modifier = Modifier.size(12.dp))

            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                modifier = Modifier,
                label = { Text(text = "User name") },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                ),
                maxLines = 1,
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
                maxLines = 1,
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
                    // validate details and navigate to login page
                    authViewModel.signUp(
                        username.value,
                        email.value,
                        password.value
                    ) { success, errorMessage ->
                        if (success) {
                            Toast.makeText(context, "Account Created", Toast.LENGTH_LONG).show()
                            navController.navigate(Routes.Login) {
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
                enabled = (username.value.length >= 6 && email.value.length >= 13 && password.value.length >= 6 )
            ) {
                Text(
                    text = "Sign Up"
                )
            }

            Spacer(modifier = Modifier.size(32.dp))

            Row {
                Text(
                    text = "Already have an account?"
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "Click here to login",
                    color = Color.Blue,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.Login)
                    }
                )
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun SignUpPreview() {
    val navController = rememberNavController()
    SignUp(navController)
}