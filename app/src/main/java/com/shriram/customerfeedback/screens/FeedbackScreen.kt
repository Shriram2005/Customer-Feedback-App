package com.shriram.customerfeedback.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.shriram.customerfeedback.data.AppViewModel
import com.shriram.customerfeedback.data.Feedback
import com.shriram.customerfeedback.navigation.Screen


@Composable
fun FeedbackScreen(
    navController: NavController,
    viewModel: AppViewModel,
    feedbackToEdit: Feedback? = null
) {
    val context = LocalContext.current
    var feedbackText by remember { mutableStateOf(feedbackToEdit?.text ?: "") }
    val isEditMode = feedbackToEdit != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = if (isEditMode) "Edit Feedback" else "Add Feedback",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // text input for feedback
        OutlinedTextField(
            value = feedbackText,
            onValueChange = { feedbackText = it },
            label = { Text("Enter your feedback") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
        )
        
        Spacer(Modifier.height(16.dp))
        
        // submit button
        Button(
            onClick = {
                if (feedbackText.isNotEmpty()) {
                    if (isEditMode) {
                        viewModel.updateFeedback(feedbackToEdit!!.id, feedbackText) {
                            Toast.makeText(context, "Feedback updated successfully!", Toast.LENGTH_SHORT).show()
                            navController.navigateUp()
                        }
                    } else {
                        viewModel.submitFeedback(feedbackText) {
                            Toast.makeText(context, "Feedback submitted successfully!", Toast.LENGTH_SHORT).show()
                            navController.navigateUp()
                        }
                    }
                } else {
                    Toast.makeText(context, "Please enter feedback text", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = !viewModel.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.height(24.dp))
            } else {
                Text(text = if (isEditMode) "Update" else "Submit")
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        Button(
            onClick = {
                navController.navigateUp()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Cancel")
        }
        
        // Show error message if any
        viewModel.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Snackbar {
                Text(text = error)
            }
        }
    }
}