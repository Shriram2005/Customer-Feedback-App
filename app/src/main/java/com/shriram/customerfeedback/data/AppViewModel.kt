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
    var allFeedbacksList by mutableStateOf<List<FeedbackWithUser>>(emptyList())
    var isAdmin by mutableStateOf(false)
    
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
                
                // Check if admin login
                if (email == "admin" && password == "admin") {
                    isAdmin = true
                    fetchAllFeedbacks()
                    onSuccess()
                    return@launch
                }
                
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
        allFeedbacksList = emptyList()
        isAdmin = false
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
                
                // If admin is updating, refresh all feedbacks
                if (isAdmin) {
                    fetchAllFeedbacks()
                } else {
                    fetchFeedbacks()
                }
                
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
                
                // If admin is deleting, refresh all feedbacks
                if (isAdmin) {
                    fetchAllFeedbacks()
                } else {
                    fetchFeedbacks()
                }
                
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
    
    data class FeedbackWithUser(
        val id: String = "",
        val userId: String = "",
        val username: String = "",
        val text: String = "",
        val timestamp: Long = System.currentTimeMillis()
    )
    
    private fun fetchAllFeedbacks() {
        isLoading = true
        database.child("feedbacks")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val feedbacksWithUsers = mutableListOf<FeedbackWithUser>()
                    
                    // First, get all feedbacks
                    val feedbacks = mutableListOf<Pair<String, Feedback>>()
                    for (feedbackSnapshot in snapshot.children) {
                        val feedback = feedbackSnapshot.getValue(Feedback::class.java)
                        feedback?.let { 
                            feedbacks.add(Pair(feedbackSnapshot.key ?: "", it))
                        }
                    }
                    
                    // If no feedbacks, set empty list and return
                    if (feedbacks.isEmpty()) {
                        allFeedbacksList = emptyList()
                        isLoading = false
                        return
                    }
                    
                    // Get all users to match with feedbacks
                    database.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(usersSnapshot: DataSnapshot) {
                            val userMap = mutableMapOf<String, String>()
                            
                            // Create map of userId to username
                            for (userSnapshot in usersSnapshot.children) {
                                val user = userSnapshot.getValue(User::class.java)
                                user?.let {
                                    userMap[it.uid] = it.username
                                }
                            }
                            
                            // Create FeedbackWithUser objects
                            for ((id, feedback) in feedbacks) {
                                val username = userMap[feedback.userId] ?: "Unknown User"
                                feedbacksWithUsers.add(
                                    FeedbackWithUser(
                                        id = id,
                                        userId = feedback.userId,
                                        username = username,
                                        text = feedback.text,
                                        timestamp = feedback.timestamp
                                    )
                                )
                            }
                            
                            allFeedbacksList = feedbacksWithUsers
                            isLoading = false
                        }
                        
                        override fun onCancelled(error: DatabaseError) {
                            errorMessage = error.message
                            Log.e("AppViewModel", "Fetch users error: ${error.message}")
                            isLoading = false
                        }
                    })
                }
                
                override fun onCancelled(error: DatabaseError) {
                    errorMessage = error.message
                    Log.e("AppViewModel", "Fetch all feedbacks error: ${error.message}")
                    isLoading = false
                }
            })
    }
}

