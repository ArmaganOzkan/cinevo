package com.armagan.cinevo.data.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import javax.inject.Inject

class MovieFavRepository @Inject constructor(){

    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val db = Firebase.firestore

    fun addFavorite(MovieId:String){

        val data =mapOf("movieId" to MovieId)

        db.collection("users")
            .document(userId!!)
            .collection("favorites")
            .document(MovieId.toString())
            .set(data)

    }

    fun isFavorite(movieId: String, onResult: (Boolean) -> Unit) {
        db.collection("users")
            .document(userId!!)
            .collection("favorites")
            .document(movieId)
            .get()
            .addOnSuccessListener { document ->
                onResult(document.exists())
            }
    }
    fun removeFavorite(movieId: String) {
        db.collection("users")
            .document(userId!!)
            .collection("favorites")
            .document(movieId)
            .delete()
    }

    fun getFavoriteMovieIds(onResult: (List<String>) -> Unit) {
        db.collection("users")
            .document(userId!!)
            .collection("favorites")
            .get()
            .addOnSuccessListener { result ->
                val ids = result.documents.mapNotNull { it.id }
                onResult(ids)
            }
    }


}