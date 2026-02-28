package com.armagan.cinevo.data.repository

import com.armagan.cinevo.data.model.Review
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ReviewRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()
    private var lastDocument: DocumentSnapshot? = null
    suspend fun getReviewsByMovieId(
        movieId: String,
        page: Int,
        pageSize: Int
    ): List<Review> {
        return try {
            var query = db.collection("reviews")
                .whereEqualTo("movieId", movieId)
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(pageSize.toLong())

            if (page > 1 && lastDocument != null) {
                query = query.startAfter(lastDocument!!)
            }
            val snapshot = query.get().await()
            if (snapshot.documents.isNotEmpty()) {
                lastDocument = snapshot.documents.last()
            }
            snapshot.documents.map { doc ->
                Review(
                    id = doc.id,
                    createdAt = doc.getString("createdAt"),
                    userId = doc.getString("userId") ?: "",
                    movieId = doc.getString("movieId") ?: "",
                    review = doc.getString("review") ?: "",
                    rating = doc.getDouble("rating")?.toFloat() ?: 0f,
                    isSpoiler = doc.getBoolean("isSpoiler") ?: false,
                    username = doc.getString("username") ?: "-",
                    moviename = doc.getString("moviename")?: "-",
                    backdroppath = doc.getString("backdroppath")?:"-"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }



    suspend fun addReview(
        userId: String,
        movieId: String,
        review: String,
        rating: Float,
        isSpoiler: Boolean,
        username: String,
        moviename:String,
        backdroppath:String
    ): Boolean {
        return try {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val today = dateFormat.format(Date())
            val reviewData = hashMapOf(
                "createdAt" to today ,
                "userId" to userId,
                "movieId" to movieId,
                "review" to review,
                "rating" to rating,
                "isSpoiler" to isSpoiler,
                "username" to username,
                "moviename" to moviename,
                "backdroppath" to backdroppath
            )

            db.collection("reviews")
                .add(reviewData)
                .await()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    suspend fun getReviewsByUserId(userId: String): List<Review> {
        return try {
            val snapshot = db.collection("reviews")
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.map { doc ->
                Review(
                    id = doc.id,
                    createdAt = doc.getString("createdAt"),
                    userId = doc.getString("userId") ?: "",
                    movieId = doc.getString("movieId") ?: "",
                    review = doc.getString("review") ?: "",
                    rating = doc.getDouble("rating")?.toFloat() ?: 0f,
                    isSpoiler = doc.getBoolean("isSpoiler") ?: false,
                    username = doc.getString("username") ?: "-",
                    moviename = doc.getString("moviename")?: "-",
                    backdroppath = doc.getString("backdroppath")?:"-"

                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun deleteReviewById(id: String): Boolean {
        return try {
            db.collection("reviews")
                .document(id)
                .delete()
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}