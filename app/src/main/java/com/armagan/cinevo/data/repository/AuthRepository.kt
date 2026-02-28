package com.armagan.cinevo.data.repository

import com.armagan.cinevo.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()


    suspend fun getUsernameByUserId(userId: String): String? {
        return try {
            val docSnapshot = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            if (docSnapshot.exists()) {
                docSnapshot.getString("username")
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    suspend fun registerUser(email: String, password: String, username: String): Result<FirebaseUser?> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("UID null döndü")

            val userData = mapOf("username" to username)
            firestore.collection("users").document(uid).set(userData).await()

            Result.success(result.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
     fun loginUser(email:String,password: String,onResult: (Boolean, String?) -> Unit){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                task ->
                if (task.isSuccessful){
                    onResult(true,null)
                }
                else{
                    onResult(false,task.exception?.message)
                }
            }
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    suspend fun getUserData(): Result<User?> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("Kullanıcı oturumda değil.")
            val snapshot = firestore.collection("users").document(uid).get().await()


            if (snapshot.exists()) {
                Result.success(snapshot.toObject(User::class.java))
            } else {
                Result.failure(Exception("Kullanıcı verisi bulunamadı."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logoutUser() {
        auth.signOut()
    }



}



