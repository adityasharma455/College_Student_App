package com.example.admin_miet_students.presentation.screens.AuthScreens.LogInScreens


import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.admin_miet_students.presentation.navigation.Routes
import com.example.admin_miet_students.presentation.screens.AuthScreens.AuthScreenState
import org.koin.compose.viewmodel.koinViewModel
import kotlin.text.isNotBlank
import kotlin.text.lowercase
import kotlin.text.replace
import kotlin.text.toRegex
import kotlin.text.trim

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentLogInScreen(
    viewModel: LogInViewModel = koinViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val state by viewModel.logInState.collectAsStateWithLifecycle()

    var email by rememberSaveable  { mutableStateOf("") }
    var password by rememberSaveable  { mutableStateOf("") }
    val isFormValid = email.isNotBlank() && password.isNotBlank()

    // Handle state changes (Success, Error, etc.)
    LaunchedEffect(state) {
        when (state) {
            is AuthScreenState.LogInSuccess -> {
                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                // navigate to main screen if needed
            }
            is AuthScreenState.Error -> {
                Toast.makeText(context, (state as AuthScreenState.Error).message, Toast.LENGTH_LONG).show()
            }
            else -> Unit
        }
    }

    // Gradient background 🌈
    val gradientColors = listOf(
        Color(0xFF1565C0),
        Color(0xFF42A5F5),
        Color(0xFF80D8FF)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(gradientColors))
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraLarge),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // 🚌 App Title
                Text(
                    text = "Student Login",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    ),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Welcome back MIETian! Please log in to continue.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ✉️ Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    shape = MaterialTheme.shapes.large,
                )



                // 🔒 Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = MaterialTheme.shapes.large
                )

                // 🔘 Login Button
                Button(
                    onClick = {
                        if (isFormValid) {
                            val cleanEmail = email.replace("\\s".toRegex(), "").lowercase()
                            val cleanPassword = password.trim()
                            Log.d("LoginDebug", "Email: '$cleanEmail', Password: '$cleanPassword'")
                            viewModel.logInStudent(cleanEmail, cleanPassword)
                        } else {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = isFormValid && state !is AuthScreenState.Loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    AnimatedVisibility(
                        visible = state is AuthScreenState.Loading,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    AnimatedVisibility(
                        visible = state !is AuthScreenState.Loading,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text("Log In", style = MaterialTheme.typography.labelLarge)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 🧭 Navigate to Sign Up
                TextButton(
                   onClick ={ navController.navigate(Routes.StudentSignUpScreenRoutes)}
                ) {
                    Text(
                        text = "Don't have an account? Sign Up",
                        color = Color(0xFF1565C0),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
