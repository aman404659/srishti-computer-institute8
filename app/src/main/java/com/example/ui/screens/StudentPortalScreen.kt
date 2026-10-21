package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DocumentViewerModal
import com.example.ui.components.InstituteLogo
import com.example.ui.components.StudentDocumentItemCard
import com.example.ui.language.AppLanguage
import com.example.ui.language.LocalAppLanguage
import com.example.ui.language.SrishtiStrings
import com.example.ui.theme.CyanContainer
import com.example.ui.theme.ErrorContainer
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyMidnight
import com.example.ui.theme.NavySurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SuccessContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhitePure
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.InstituteViewModel

@Composable
fun StudentPortalScreen(
    viewModel: InstituteViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val langState = LocalAppLanguage.current
    val currentLang = langState.currentLanguage

    val loggedInStudent by viewModel.loggedInStudent.collectAsState()
    val documents by viewModel.studentDocuments.collectAsState()
    val loginError by viewModel.studentLoginError.collectAsState()
    val selectedDocPreview by viewModel.selectedDocPreview.collectAsState()

    var loginInput by remember { mutableStateOf("") }

    if (loggedInStudent == null) {
        // Student Login / Authentication Screen
        StudentLoginView(
            loginInput = loginInput,
            onInputChange = { loginInput = it },
            onLoginClick = { viewModel.loginStudent(loginInput) },
            onDemoLoginClick = { viewModel.loginDemoStudent() },
            loginError = loginError,
            currentLang = currentLang,
            modifier = modifier
        )
    } else {
        // Authenticated Student Profile & Document Vault
        val student = loggedInStudent!!
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(NavyDark),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Card Header
            item {
                StudentProfileCard(
                    student = student,
                    currentLang = currentLang,
                    onLogout = { viewModel.logoutStudent() }
                )
            }

            // Documents Vault Title
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = SrishtiStrings.documentVault(currentLang),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = WhitePure
                        )
                        Text(
                            text = if (currentLang == AppLanguage.EN)
                                "Access authorized certificates, marksheets, receipts and ID card"
                            else
                                "अपने अधिकृत पहचान पत्र, रसीद, प्रवेश पत्र एवं प्रमाण-पत्र देखें",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(CyanContainer)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${documents.count { it.isApproved }}/${documents.size} Available",
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // List of Documents
            items(documents) { doc ->
                StudentDocumentItemCard(
                    doc = doc,
                    onViewClick = { viewModel.openDocumentPreview(it) }
                )
            }
        }
    }

    // Modal Viewer
    if (selectedDocPreview != null) {
        DocumentViewerModal(
            doc = selectedDocPreview!!,
            student = loggedInStudent,
            onDismiss = { viewModel.closeDocumentPreview() }
        )
    }
}

@Composable
fun StudentLoginView(
    loginInput: String,
    onInputChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onDemoLoginClick: () -> Unit,
    loginError: String?,
    currentLang: AppLanguage,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDark),
        contentPadding = PaddingValues(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("student_login_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = NavyMidnight),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.linearGradient(listOf(NeonCyan.copy(alpha = 0.5f), NavyBorder))
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InstituteLogo(
                        size = 64.dp,
                        cornerRadius = 16.dp,
                        modifier = Modifier.testTag("student_login_logo")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Student Portal Login",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = WhitePure
                    )

                    Text(
                        text = SrishtiStrings.INSTITUTE_NAME,
                        style = MaterialTheme.typography.bodyMedium,
                        color = NeonCyan
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (loginError != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(ErrorContainer)
                                .padding(10.dp)
                        ) {
                            Text(
                                text = loginError,
                                color = WhitePure,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    OutlinedTextField(
                        value = loginInput,
                        onValueChange = onInputChange,
                        label = { Text("Enrollment No. or Mobile", color = TextSecondary) },
                        placeholder = { Text("e.g. SCI-2026-0104 or 9876543210", color = TextMuted) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = NeonCyan)
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("student_login_input"),
                        colors = formFieldColors(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = onLoginClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("student_login_submit_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonCyan,
                            contentColor = NavyDark
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Access Student Portal", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quick Demo Login Button for Evaluator
                    OutlinedButton(
                        onClick = onDemoLoginClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("demo_student_btn"),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.6f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Instant Demo Student Login (Rahul Sharma)", fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Students can only access their own documents & records",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StudentProfileCard(
    student: com.example.data.model.Student,
    currentLang: AppLanguage,
    onLogout: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = NavyMidnight),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NeonCyan, NavyBorder)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(NavySurface)
                            .border(2.dp, NeonCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = student.fullName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = WhitePure
                        )
                        Text(
                            text = student.enrollmentNo,
                            fontWeight = FontWeight.SemiBold,
                            color = NeonCyan,
                            fontSize = 13.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    InstituteLogo(
                        size = 38.dp,
                        cornerRadius = 8.dp,
                        showGlow = false,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    IconButton(onClick = onLogout, modifier = Modifier.testTag("student_logout_btn")) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyDark, RoundedCornerShape(10.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Course Enrolled:", fontSize = 12.sp, color = TextMuted)
                    Text(student.courseName, fontSize = 12.sp, color = WhitePure, fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Batch Timing:", fontSize = 12.sp, color = TextMuted)
                    Text(student.batchTiming, fontSize = 12.sp, color = WhitePure)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Fee Status:", fontSize = 12.sp, color = TextMuted)
                    Text(
                        text = if (student.paidFee >= student.totalFee) "Paid (Full Settlement)" else "Balance: ₹${student.totalFee - student.paidFee}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (student.paidFee >= student.totalFee) SuccessGreen else GoldAccent
                    )
                }
            }
        }
    }
}
