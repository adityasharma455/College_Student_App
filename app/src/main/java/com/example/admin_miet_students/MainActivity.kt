package com.example.admin_miet_students

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
import com.example.admin_miet_students.presentation.navigation.AppNavigation
import com.example.admin_miet_students.ui.theme.Admin_Miet_StudentsTheme
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val firebaseAuth : FirebaseAuth by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        org.osmdroid.config.Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences("osmdroid", MODE_PRIVATE)
        )
        enableEdgeToEdge()
        setContent {
            Admin_Miet_StudentsTheme {
                AppNavigation(firebaseAuth)

            }
            }
        }
    }

