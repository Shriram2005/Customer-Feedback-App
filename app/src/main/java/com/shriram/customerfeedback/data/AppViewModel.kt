package com.shriram.customerfeedback.data

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class User(
    val uid: String = "",
    val email: String = "",
    val username: String = ""
)

data class Feedback(
    val id: String = "",
    val userId: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

class AppViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    
    var currentUser by mutableStateOf<FirebaseUser?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var feedbackList by mutableStateOf<List<Feedback>>(emptyList())
    
    init {
        currentUser = auth.currentUser
        if (currentUser != null) {
            fetchFeedbacks()
        }
    }
    
    fun registerUser(email: String, password: String, username: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                currentUser = result.user
                
                // Save user details to database
                currentUser?.let { user ->
                    val newUser = User(
                        uid = user.uid,
                        email = email,
                        username = username
                    )
                    database.child("users").child(user.uid).setValue(newUser).await()
                }
                
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("AppViewModel", "Registration error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
    
    fun loginUser(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                
                val result = auth.signInWithEmailAndPassword(email, password).await()
                currentUser = result.user
                fetchFeedbacks()
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("AppViewModel", "Login error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
    
    fun logout() {
        auth.signOut()
        currentUser = null
        feedbackList = emptyList()
    }
    
    fun submitFeedback(text: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                
                currentUser?.let { user ->
                    val feedbackRef = database.child("feedbacks").push()
                    val feedbackId = feedbackRef.key ?: return@let
                    
                    val feedback = Feedback(
                        id = feedbackId,
                        userId = user.uid,
                        text = text,
                        timestamp = System.currentTimeMillis()
                    )
                    
                    feedbackRef.setValue(feedback).await()
                    fetchFeedbacks()
                    onSuccess()
                }
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("AppViewModel", "Submit feedback error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
    
    fun updateFeedback(feedbackId: String, text: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                
                database.child("feedbacks").child(feedbackId)
                    .child("text").setValue(text).await()
                
                fetchFeedbacks()
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("AppViewModel", "Update feedback error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
    
    fun deleteFeedback(feedbackId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                
                database.child("feedbacks").child(feedbackId).removeValue().await()
                
                fetchFeedbacks()
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("AppViewModel", "Delete feedback error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
    
    private fun fetchFeedbacks() {
        currentUser?.let { user ->
            database.child("feedbacks")
                .orderByChild("userId")
                .equalTo(user.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val feedbacks = mutableListOf<Feedback>()
                        for (feedbackSnapshot in snapshot.children) {
                            val feedback = feedbackSnapshot.getValue(Feedback::class.java)
                            feedback?.let { feedbacks.add(it) }
                        }
                        feedbackList = feedbacks
                    }
                    
                    override fun onCancelled(error: DatabaseError) {
                        errorMessage = error.message
                        Log.e("AppViewModel", "Fetch feedbacks error: ${error.message}")
                    }
                })
        }
    }
}

