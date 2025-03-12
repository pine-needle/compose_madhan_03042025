package com.madhan.firebaseauthenticationapp.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.madhan.firebaseauthenticationapp.data.model.User

class FirebaseAuthViewModel: ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val firestore: FirebaseFirestore = Firebase.firestore

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    onResult(true, null)
                }else{
                    onResult(false, task.exception?.localizedMessage)
                }
            }
    }

    fun signUp(name: String, email: String, password: String, onResult: (Boolean, String?) -> Unit){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                 if (task.isSuccessful){
                     val userId: String? = task.result.user?.uid
                     val newUser = User(userId!!, name, email)

                     firestore.collection("user")
                         .document(userId)
                         .set(newUser)
                         .addOnCompleteListener { dbTask ->
                             if(dbTask.isSuccessful){
                                 onResult(true, null)
                             }else{
                                 onResult(false, "something went wrong")
                             }
                         }
                 }else{
                     onResult(false, task.exception?.localizedMessage)
                 }
                
            }
    }


    fun signOut(){
        Firebase.auth.signOut()
    }
}