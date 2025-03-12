package com.madhan.firebaseauthenticationapp.utils

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class GoogleAuthClient(
    private val context: Context
) {

    private val TAG: String = "GoogleAuthClient: "
    // to get credentials
    private val credentialManager = CredentialManager.create(context)

    // sign in with credentials
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun isSignedIn(): Boolean{
        if(firebaseAuth.currentUser != null){
            println(TAG + "Already SignedIn")
            return true
        }

        return false
    }

    suspend fun signIn(): Boolean{

        if(isSignedIn()){
            return true
        }

        try {
            val result = buildCredentialRequest()
            return handleSignIn(result)

        } catch (e: Exception){
            if(e is CancellationException) throw e
            println(TAG + "signIn error: ${e.message}")
            return false
        }
    }

    private suspend fun handleSignIn(result: GetCredentialResponse): Boolean{
        val credential = result.credential

        if(credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){

            try {
                val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                println(TAG + "Name: ${tokenCredential.displayName}")

                val authCredential = GoogleAuthProvider.getCredential(tokenCredential.idToken, null)

                val authResult = firebaseAuth.signInWithCredential(authCredential).await()

                return authResult.user != null

            } catch (e: GoogleIdTokenParsingException){
                println(TAG + "GoogleIdTokenParsingException: ${e.message}")
                return false
            }
        }else{
            println(TAG + "credential is not GoogleIdTokenCredential")
            return false
        }
    }

    private suspend fun buildCredentialRequest(): GetCredentialResponse{
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("115553707440-dd53o6tj4l8hkqtqckg2ejvtadkrp2i9.apps.googleusercontent.com")
                    .setAutoSelectEnabled(false)
                    .build()
            ).build()

        return credentialManager.getCredential(request = request, context = context)
    }

    suspend fun signOut(){
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
        firebaseAuth.signOut()
    }

}