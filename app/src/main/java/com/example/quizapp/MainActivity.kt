package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.quizapp.ui.theme.QuizAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                QuizApp()
            }
        }
    }
}

@Composable
fun QuizApp() {
    val questions = listOf(
        "How many elements are in the periodic table?" to "118",
        "Which planet in the Milky Way is the hottest?" to "Venus",
        "Who discovered that the Earth revolves around the sun?" to "Nicolaus Copernicus",
        "Which planet has the most moons?" to "Saturn",
        "Which planet is closest to the sun?" to "Mercury",
        "Where is the strongest human muscle located?" to "Jaw",
        "Which is the only body part that is fully grown from birth?" to "Eyes",
        "How many bones do we have in an ear?" to "3",
        "What scientific theory proposed that Earth revolves around the sun?" to "Heliocentrism",
        "What is the process by which plants convert sunlight to energy?" to "Photosynthesis",
        "What is the scientific theory that explains the origin of the universe?" to "The Big Bang Theory",
        "What is the name of the world's first artificial satellite?" to "Sputnik 1",
        "What is the chemical element with the symbol Fe?" to "Iron",
        "What is the smallest unit of matter?" to "Atom",
        "What is the outermost layer of the Earth’s atmosphere called?" to "Exosphere",
        "What is the process by which a liquid changes into a gas?" to "Evaporation"
    )

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var userAnswer by remember { mutableStateOf("") }
    var isQuizComplete by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isQuizComplete) {
                    Text(text = "Quiz Complete!")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        // Restart quiz
                        currentQuestionIndex = 0
                        userAnswer = ""
                        isQuizComplete = false
                    }) {
                        Text("Restart Quiz")
                    }
                } else {
                    val (question, answer) = questions[currentQuestionIndex]

                    // Questions
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = question,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Answer
                    TextField(
                        value = userAnswer,
                        onValueChange = { userAnswer = it },
                        label = { Text("Enter your answer") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Submit Button
                    ElevatedButton(
                        onClick = {
                            if (userAnswer.equals(answer, ignoreCase = true)) {
                                if (currentQuestionIndex < questions.size - 1) {
                                    currentQuestionIndex++
                                    userAnswer = ""
                                    snackbarMessage = "Correct!"
                                } else {
                                    isQuizComplete = true
                                    snackbarMessage = "Quiz Completed!"
                                }
                            } else {
                                snackbarMessage = "Incorrect. Try again!"
                            }
                            showSnackbar = true
                            scope.launch {
                                snackbarHostState.showSnackbar(snackbarMessage)
                            }
                        }
                    ) {
                        Text("Submit Answer")
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun FlashcardQuizAppPreview() {
    QuizApp()
}