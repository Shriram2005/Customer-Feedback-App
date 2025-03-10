package com.shriram.customerfeedback.screens

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun FeedbackScreen(navController: NavController) {

    var feedbackText by remember{ mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column {
            Text(
                text = "Feedback",
                modifier = Modifier.padding(bottom = 32.dp)
            )
            Row {
                // show 2 buttons to user for add feedback & edit feedback
                Button(
                    onClick = {

                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Add Feedback")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {

                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Edit Feedback")
                }
            }
            Column {
                Text(
                    text = "Your Feedback",
                    modifier = Modifier.padding(top = 16.dp)
                )
                // text input for feedback
                OutlinedTextField(
                    value = feedbackText,
                    onValueChange = { feedbackText =  it },
                    label = { Text("Enter your feedback") },
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                )
                Spacer(Modifier.height(16.dp))
                // submit button
                Button(
                    onClick = {},
                ) {
                    Text(text = "Submit")
                }

            }
        }
    }
}