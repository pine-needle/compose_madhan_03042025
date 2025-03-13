package com.madhan.firebaseauthenticationapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.madhan.firebaseauthenticationapp.ui.AppNavigation
import com.madhan.firebaseauthenticationapp.ui.theme.FirebaseAuthenticationAppTheme

class MainActivity : ComponentActivity() {
    private val TAG: String = "MainActivityLog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FirebaseAuthenticationAppTheme {
               AppNavigation()
            }
        }

        if(DataLeakCode.contextRef == null){
            DataLeakCode.contextRef = this
        } else {
            Toast.makeText(DataLeakCode.contextRef, "Already go it", Toast.LENGTH_SHORT).show()
        }

        // get the registration token
        getFirebaseToken()

        // get user permission for notification
        requestNotificationPermission()

    }

    private fun getFirebaseToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
            } else {
                val token = task.result
                Log.d(TAG, "Token Received: $token")
                Toast.makeText(baseContext, "Token 1 Received ", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+ requires explicit permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                Log.d(TAG, "Notification permission already granted")
            }
        }
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            Log.d(TAG, "Notification permission granted")
            Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Log.d(TAG, "Notification permission denied")
            Toast.makeText(this, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }


}

object DataLeakCode{
    var contextRef: Context? = null
}


