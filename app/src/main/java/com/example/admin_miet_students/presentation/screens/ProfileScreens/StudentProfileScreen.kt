package com.example.admin_miet_students.presentation.screens.ProfileScreens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.admin_miet_students.domain.models.StudentDataModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StudentProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = koinViewModel()
) {

    val state by viewModel.profileState.collectAsState()

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF00B4DB),
            Color(0xFF0083B0)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(20.dp)
    ) {
        when (state) {
            is ProfileScreenState.Loading -> LoadingUI()
            is ProfileScreenState.Error -> ErrorUI((state as ProfileScreenState.Error).message)
            is ProfileScreenState.Success -> {
                val student = (state as ProfileScreenState.Success).studentDataModel
                ProfileContent(student)
            }
        }
    }
}

@Composable
fun LoadingUI() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
fun ErrorUI(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(message, color = Color.Red, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ProfileContent(student: StudentDataModel) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(40.dp))

        // ⭐ CIRCLE PROFILE IMAGE WITH FIRST LETTER
        Box(
            modifier = Modifier
                .size(120.dp)
                .shadow(20.dp, CircleShape)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                student.name.firstOrNull()?.uppercase() ?: "?",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0083B0)
            )
        }

        Spacer(Modifier.height(20.dp))

        Text(
            student.name,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Text(
            student.email,
            fontSize = 16.sp,
            color = Color.White.copy(0.8f)
        )

        Spacer(Modifier.height(30.dp))

        // ⭐ STUDENT INFO CARD
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                ProfileInfoRow("Student Name", student.name)
                ProfileInfoRow("Email", student.email)
                ProfileInfoRow("Bus ID", student.busId)
                ProfileInfoRow("Course", student.course)
                ProfileInfoRow("Year", student.year)
            }
        }
    }
}

@Composable
fun ProfileInfoRow(title: String, value: String) {
    Column(
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        Text(title, fontWeight = FontWeight.Bold, color = Color(0xFF0083B0))
        Text(
            value,
            color = Color.DarkGray,
            fontSize = 15.sp,
            textAlign = TextAlign.Start
        )
    }
}
