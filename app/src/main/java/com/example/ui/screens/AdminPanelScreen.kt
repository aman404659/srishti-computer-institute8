package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import com.example.data.local.InitialCoursesData
import com.example.ui.components.InstituteLogo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AdmissionApplication
import com.example.data.model.ApplicationStatus
import com.example.data.model.Course
import com.example.data.model.DocumentType
import com.example.data.model.FeePayment
import com.example.data.model.Notice
import com.example.data.model.NoticeCategory
import com.example.data.model.Student
import com.example.data.model.StudentDocument
import com.example.ui.language.AppLanguage
import com.example.ui.language.LocalAppLanguage
import com.example.ui.language.SrishtiStrings
import com.example.ui.theme.CyanContainer
import com.example.ui.theme.ErrorContainer
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyMidnight
import com.example.ui.theme.NavySurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SuccessContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WhitePure
import com.example.ui.viewmodel.AdminTab
import com.example.ui.viewmodel.InstituteViewModel
import java.util.UUID

@Composable
fun AdminPanelScreen(
    viewModel: InstituteViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val langState = LocalAppLanguage.current
    val currentLang = langState.currentLanguage

    val isAuthenticated by viewModel.isAdminAuthenticated.collectAsState()
    val adminPinInput by viewModel.adminPinInput.collectAsState()
    val adminLoginError by viewModel.adminLoginError.collectAsState()
    val activeTab by viewModel.adminTab.collectAsState()

    val courses by viewModel.allCourses.collectAsState()
    val applications by viewModel.applications.collectAsState()
    val filteredApplications by viewModel.filteredApplications.collectAsState()
    val adminAppSearchQuery by viewModel.adminAppSearchQuery.collectAsState()
    val adminAppStatusFilter by viewModel.adminAppStatusFilter.collectAsState()
    val selectedAppForDetail by viewModel.selectedAppForDetail.collectAsState()
    val students by viewModel.allStudents.collectAsState()
    val payments by viewModel.allPayments.collectAsState()
    val documents by viewModel.allDocuments.collectAsState()
    val notices by viewModel.allNotices.collectAsState()

    var showAddCourseDialog by remember { mutableStateOf(false) }
    var editingCourse by remember { mutableStateOf<Course?>(null) }
    var showRecordPaymentDialog by remember { mutableStateOf(false) }
    var showAddNoticeDialog by remember { mutableStateOf(false) }
    var selectedStudentForPayment by remember { mutableStateOf<Student?>(null) }

    if (!isAuthenticated) {
        // Admin PIN Authentication Screen
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
                        .testTag("admin_login_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyMidnight),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(listOf(NeonCyan, NavyBorder))
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
                            modifier = Modifier.testTag("admin_login_logo")
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Administrator Console",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = WhitePure
                        )

                        Text(
                            text = SrishtiStrings.INSTITUTE_NAME,
                            style = MaterialTheme.typography.bodyMedium,
                            color = NeonCyan
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        if (adminLoginError != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ErrorContainer)
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = adminLoginError!!,
                                    color = WhitePure,
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        OutlinedTextField(
                            value = adminPinInput,
                            onValueChange = { viewModel.setAdminPin(it) },
                            label = { Text("Administrator Password", color = TextSecondary) },
                            placeholder = { Text("Enter authorized password", color = TextMuted) },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = NeonCyan)
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_pin_input"),
                            colors = formFieldColors(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = { viewModel.loginAdmin() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("admin_login_submit_btn"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonCyan,
                                contentColor = NavyDark
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Unlock Admin Dashboard", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick Test Fill Button for Initial Admin Password (258036)
                        OutlinedButton(
                            onClick = {
                                viewModel.setAdminPin("258036")
                                viewModel.loginAdmin()
                            },
                            modifier = Modifier.fillMaxWidth().testTag("autofill_admin_pass_btn"),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Auto-fill Initial Password (258036)", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    } else {
        // Authenticated Admin Dashboard
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(NavyDark),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Admin Top Header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyMidnight),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(NeonCyan, NavyBorder)))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            InstituteLogo(
                                size = 44.dp,
                                cornerRadius = 10.dp,
                                showGlow = false,
                                modifier = Modifier.testTag("admin_header_logo")
                            )
                            Column {
                                Text(
                                    text = "Admin Control Center",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = WhitePure
                                )
                                Text(
                                    text = SrishtiStrings.INSTITUTE_NAME,
                                    fontSize = 12.sp,
                                    color = NeonCyan
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = { viewModel.logoutAdmin() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("admin_logout_btn")
                        ) {
                            Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Logout", fontSize = 11.sp)
                        }
                    }
                }
            }

            // Metrics Summary Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricBox(
                        title = "Students",
                        value = "${students.size}",
                        color = NeonCyan,
                        modifier = Modifier.weight(1f)
                    )
                    MetricBox(
                        title = "Admissions",
                        value = "${applications.count { it.status == ApplicationStatus.PENDING }}",
                        color = WarningAmber,
                        modifier = Modifier.weight(1f)
                    )
                    val totalRev = payments.sumOf { it.amount }
                    MetricBox(
                        title = "Revenue",
                        value = "₹$totalRev",
                        color = SuccessGreen,
                        modifier = Modifier.weight(1.3f)
                    )
                    MetricBox(
                        title = "Courses",
                        value = "${courses.size}",
                        color = GoldAccent,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Tab Selector Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val tabs = AdminTab.values()
                    val pendingAppCount = applications.count { it.status == ApplicationStatus.PENDING }
                    items(tabs) { tab ->
                        val isSelected = activeTab == tab
                        val chipText = when (tab) {
                            AdminTab.APPLICATIONS -> if (pendingAppCount > 0) "Applications ($pendingAppCount New)" else "Applications"
                            else -> tab.name.capitalize()
                        }
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setAdminTab(tab) },
                            label = {
                                Text(
                                    text = chipText,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = NavyMidnight,
                                labelColor = TextSecondary,
                                selectedContainerColor = CyanContainer,
                                selectedLabelColor = NeonCyan
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) NeonCyan else NavyBorder,
                                selectedBorderColor = NeonCyan,
                                borderWidth = 1.dp
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.testTag("admin_tab_${tab.name}")
                        )
                    }
                }
            }

            // Render Content based on selected Admin Tab
            when (activeTab) {
                AdminTab.DASHBOARD -> {
                    item {
                        DashboardOverview(
                            applications = applications,
                            students = students,
                            payments = payments,
                            notices = notices,
                            onTabChange = { viewModel.setAdminTab(it) }
                        )
                    }
                }
                AdminTab.APPLICATIONS -> {
                    val pendingCount = applications.count { it.status == ApplicationStatus.PENDING }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Admission Applications",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = WhitePure
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    if (pendingCount > 0) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0xFF5C3D00))
                                                .border(1.dp, GoldAccent, RoundedCornerShape(8.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "$pendingCount NEW",
                                                color = GoldAccent,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "Showing ${filteredApplications.size} of ${applications.size} total submitted applications",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }

                            // Refresh button to retain and re-check all persistent applications
                            IconButton(
                                onClick = {
                                    Toast.makeText(context, "Dashboard refreshed: ${applications.size} applications loaded.", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.testTag("refresh_admissions_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh Applications",
                                    tint = NeonCyan
                                )
                            }
                        }
                    }

                    // Search input
                    item {
                        OutlinedTextField(
                            value = adminAppSearchQuery,
                            onValueChange = { viewModel.setAdminAppSearchQuery(it) },
                            placeholder = { Text("Search by student, ref no, phone, course...", color = TextMuted, fontSize = 12.sp) },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
                            },
                            trailingIcon = {
                                if (adminAppSearchQuery.isNotBlank()) {
                                    IconButton(onClick = { viewModel.setAdminAppSearchQuery("") }) {
                                        Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = TextMuted, modifier = Modifier.size(16.dp))
                                    }
                                }
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_app_search_input"),
                            colors = formFieldColors(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Status Filter Chips
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val filters = listOf(
                                "All" to applications.size,
                                "Pending" to applications.count { it.status == ApplicationStatus.PENDING },
                                "Approved" to applications.count { it.status == ApplicationStatus.APPROVED },
                                "Rejected" to applications.count { it.status == ApplicationStatus.REJECTED }
                            )
                            filters.forEach { (filterName, count) ->
                                val isSelected = adminAppStatusFilter.equals(filterName, ignoreCase = true)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.setAdminAppStatusFilter(filterName) },
                                    label = {
                                        Text("$filterName ($count)", fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        containerColor = NavyMidnight,
                                        labelColor = TextSecondary,
                                        selectedContainerColor = CyanContainer,
                                        selectedLabelColor = NeonCyan
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = isSelected,
                                        borderColor = if (isSelected) NeonCyan else NavyBorder,
                                        selectedBorderColor = NeonCyan,
                                        borderWidth = 1.dp
                                    ),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.testTag("admin_filter_$filterName")
                                )
                            }
                        }
                    }

                    if (filteredApplications.isEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = NavyMidnight),
                                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(NavyBorder, NeonCyan.copy(alpha = 0.2f))))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(36.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("No admission applications match filter", color = WhitePure, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("Try clearing the search query or selecting 'All' status.", color = TextSecondary, fontSize = 12.sp)
                                }
                            }
                        }
                    } else {
                        items(filteredApplications) { app ->
                            ApplicationAdminCard(
                                app = app,
                                onViewDetails = { viewModel.openApplicationDetail(app) },
                                onApprove = { viewModel.approveAdmission(app) },
                                onReject = { reason -> viewModel.rejectAdmission(app, reason) },
                                onUpdateStatus = { status, remarks ->
                                    viewModel.updateApplicationStatus(app, status, remarks)
                                }
                            )
                        }
                    }
                }
                AdminTab.STUDENTS -> {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Enrolled Students (${students.size})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = WhitePure
                            )

                            Button(
                                onClick = {
                                    val csvData = buildString {
                                        append("EnrollmentNo,Name,Phone,Course,PaidFee,TotalFee\n")
                                        students.forEach { s ->
                                            append("${s.enrollmentNo},\"${s.fullName}\",${s.phone},\"${s.courseName}\",${s.paidFee},${s.totalFee}\n")
                                        }
                                    }
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, csvData)
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Export Student Records"))
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = CyanContainer, contentColor = NeonCyan),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("export_students_btn")
                            ) {
                                Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Export Records", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    items(students) { student ->
                        StudentAdminCard(
                            student = student,
                            onRecordPaymentClick = {
                                selectedStudentForPayment = student
                                showRecordPaymentDialog = true
                            }
                        )
                    }
                }
                AdminTab.COURSES -> {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Manage Courses (${courses.size})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = WhitePure
                            )

                            Button(
                                onClick = { showAddCourseDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = NavyDark),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("add_course_btn")
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Course", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    items(courses) { course ->
                        CourseAdminCard(
                            course = course,
                            onEdit = { editingCourse = course },
                            onDelete = { viewModel.deleteCourse(course) }
                        )
                    }
                }
                AdminTab.PAYMENTS -> {
                    item {
                        Text(
                            text = "Payment Records & Fee Receipts (${payments.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = WhitePure
                        )
                    }
                    items(payments) { payment ->
                        PaymentAdminCard(payment = payment)
                    }
                }
                AdminTab.DOCUMENTS -> {
                    item {
                        Text(
                            text = "Manage Student Documents (${documents.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = WhitePure
                        )
                    }
                    items(documents) { doc ->
                        DocumentApprovalCard(
                            doc = doc,
                            onToggleApproval = { approved ->
                                viewModel.updateDocumentApproval(
                                    docId = doc.id,
                                    isApproved = approved,
                                    statusNotes = if (approved) "Approved & verified by Director" else "Pending verification",
                                    payload = doc.docPayload
                                )
                            }
                        )
                    }
                }
                AdminTab.NOTICES -> {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Notices & Announcements (${notices.size})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = WhitePure
                            )

                            Button(
                                onClick = { showAddNoticeDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = NavyDark),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("publish_notice_btn")
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Publish Notice", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    items(notices) { notice ->
                        NoticeAdminCard(
                            notice = notice,
                            onDelete = { viewModel.deleteNotice(notice) }
                        )
                    }
                }
            }
        }
    }

    // Application Details Modal
    if (selectedAppForDetail != null) {
        ApplicationDetailModal(
            app = selectedAppForDetail!!,
            onDismiss = { viewModel.closeApplicationDetail() },
            onUpdateStatus = { status, remarks ->
                viewModel.updateApplicationStatus(selectedAppForDetail!!, status, remarks)
                Toast.makeText(context, "Status updated to ${status.name}", Toast.LENGTH_SHORT).show()
            },
            onApprove = {
                viewModel.approveAdmission(selectedAppForDetail!!)
                Toast.makeText(context, "Student approved & enrolled successfully!", Toast.LENGTH_SHORT).show()
            },
            onReject = { reason ->
                viewModel.rejectAdmission(selectedAppForDetail!!, reason)
                Toast.makeText(context, "Application marked as Rejected", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Add Course Modal Dialog
    if (showAddCourseDialog) {
        AddCourseModal(
            onDismiss = { showAddCourseDialog = false },
            onSave = { course ->
                viewModel.addNewCourse(course)
                showAddCourseDialog = false
                Toast.makeText(context, "Course added successfully", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Edit Course Modal Dialog
    if (editingCourse != null) {
        EditCourseModal(
            course = editingCourse!!,
            onDismiss = { editingCourse = null },
            onSave = { updatedCourse ->
                viewModel.updateExistingCourse(updatedCourse)
                editingCourse = null
                Toast.makeText(context, "Course updated successfully", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Record Payment Modal Dialog
    if (showRecordPaymentDialog && selectedStudentForPayment != null) {
        RecordPaymentModal(
            student = selectedStudentForPayment!!,
            onDismiss = {
                showRecordPaymentDialog = false
                selectedStudentForPayment = null
            },
            onRecord = { amount, mode, ref, installment ->
                viewModel.recordStudentPayment(selectedStudentForPayment!!, amount, mode, ref, installment)
                showRecordPaymentDialog = false
                selectedStudentForPayment = null
                Toast.makeText(context, "Payment recorded & receipt issued successfully", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Add Notice Modal Dialog
    if (showAddNoticeDialog) {
        AddNoticeModal(
            onDismiss = { showAddNoticeDialog = false },
            onPublish = { titleEn, titleHi, descEn, descHi, category ->
                viewModel.publishNotice(titleEn, titleHi, descEn, descHi, category)
                showAddNoticeDialog = false
            }
        )
    }
}

@Composable
fun MetricBox(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = NavyMidnight),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(NavyBorder, color.copy(alpha = 0.4f))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = color)
            Text(text = title, fontSize = 10.sp, color = TextMuted)
        }
    }
}

@Composable
fun DashboardOverview(
    applications: List<AdmissionApplication>,
    students: List<Student>,
    payments: List<FeePayment>,
    notices: List<Notice>,
    onTabChange: (AdminTab) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = NavyMidnight),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NeonCyan, NavyBorder)))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Quick Action Shortcuts",
                    fontWeight = FontWeight.Bold,
                    color = WhitePure,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onTabChange(AdminTab.APPLICATIONS) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanContainer, contentColor = NeonCyan),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Review Apps", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { onTabChange(AdminTab.STUDENTS) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanContainer, contentColor = NeonCyan),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Students", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { onTabChange(AdminTab.DOCUMENTS) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanContainer, contentColor = NeonCyan),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Issue Docs", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ApplicationAdminCard(
    app: AdmissionApplication,
    onViewDetails: () -> Unit,
    onApprove: () -> Unit,
    onReject: (String) -> Unit,
    onUpdateStatus: (ApplicationStatus, String) -> Unit
) {
    val context = LocalContext.current
    val dateStr = remember(app.appliedAtMillis) {
        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(app.appliedAtMillis))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NavyMidnight),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                listOf(
                    when (app.status) {
                        ApplicationStatus.PENDING -> WarningAmber
                        ApplicationStatus.APPROVED -> SuccessGreen
                        ApplicationStatus.REJECTED -> ErrorRed
                    },
                    NavyBorder
                )
            )
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Name, Ref, Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = app.fullName, fontWeight = FontWeight.Bold, color = WhitePure, fontSize = 15.sp)
                        if (app.status == ApplicationStatus.PENDING) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF5C3D00))
                                    .border(1.dp, GoldAccent, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                            ) {
                                Text("NEW", color = GoldAccent, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                    Text(text = "Ref: ${app.referenceNo}", fontSize = 11.sp, color = NeonCyan, fontWeight = FontWeight.SemiBold)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            when (app.status) {
                                ApplicationStatus.APPROVED -> SuccessContainer
                                ApplicationStatus.PENDING -> Color(0xFF423405)
                                ApplicationStatus.REJECTED -> Color(0xFF450A0A)
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = app.status.name,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (app.status) {
                            ApplicationStatus.APPROVED -> SuccessGreen
                            ApplicationStatus.PENDING -> GoldAccent
                            ApplicationStatus.REJECTED -> ErrorRed
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Details
            Text(text = "Course: ${app.courseName}", fontSize = 12.sp, color = NeonCyan, fontWeight = FontWeight.SemiBold)
            Text(text = "Father: ${app.fatherName}", fontSize = 12.sp, color = TextSecondary)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Mobile: ${app.phone}", fontSize = 12.sp, color = TextSecondary)
                Spacer(modifier = Modifier.width(6.dp))
                IconButton(
                    onClick = {
                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${app.phone}"))
                        context.startActivity(dialIntent)
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = "Call Student", tint = NeonCyan, modifier = Modifier.size(15.dp))
                }
            }

            Text(text = "Applied: $dateStr", fontSize = 11.sp, color = TextMuted)
            Text(text = "Batch: ${app.batchTiming}", fontSize = 11.sp, color = TextMuted)
            if (app.adminRemarks.isNotBlank()) {
                Text(text = "Note: ${app.adminRemarks}", fontSize = 11.sp, color = TextMuted)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onViewDetails,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.8f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("View Full Details", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                if (app.status == ApplicationStatus.PENDING) {
                    Button(
                        onClick = onApprove,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen, contentColor = WhitePure),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Approve & Enroll", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { onReject("Seat unavailable / Requirements unmet") },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Reject", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun StudentAdminCard(
    student: Student,
    onRecordPaymentClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NavyMidnight),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NeonCyan, NavyBorder)))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = student.fullName, fontWeight = FontWeight.Bold, color = WhitePure, fontSize = 14.sp)
                    Text(text = "Enrollment: ${student.enrollmentNo}", fontSize = 11.sp, color = NeonCyan)
                }

                Button(
                    onClick = onRecordPaymentClick,
                    colors = ButtonDefaults.buttonColors(containerColor = CyanContainer, contentColor = NeonCyan),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("record_payment_${student.enrollmentNo}")
                ) {
                    Text("Record Payment", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Course: ${student.courseName}", fontSize = 12.sp, color = TextSecondary)
            Text(text = "Phone: ${student.phone} | Batch: ${student.batchTiming}", fontSize = 11.sp, color = TextMuted)

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total Fee: ₹${student.totalFee}",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Text(
                    text = "Paid: ₹${student.paidFee} (Balance: ₹${student.totalFee - student.paidFee})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (student.paidFee >= student.totalFee) SuccessGreen else GoldAccent
                )
            }
        }
    }
}

@Composable
fun CourseAdminCard(
    course: Course,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = NavyMidnight),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NavyBorder, NeonCyan.copy(alpha = 0.3f))))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = course.code, fontWeight = FontWeight.Bold, color = NeonCyan, fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Fee: ₹${course.fee}", fontWeight = FontWeight.Bold, color = GoldAccent, fontSize = 13.sp)
                    if (course.pdfCost > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "(PDF Cost: ₹${course.pdfCost})", fontSize = 11.sp, color = TextMuted)
                    }
                }
                Text(text = course.nameEn, fontWeight = FontWeight.SemiBold, color = WhitePure, fontSize = 13.sp)
                Text(text = "Duration: ${course.durationEn} | Category: ${course.category}", fontSize = 11.sp, color = TextSecondary)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onEdit, modifier = Modifier.testTag("edit_course_${course.code}")) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Course", tint = NeonCyan)
                }
                IconButton(onClick = onDelete, modifier = Modifier.testTag("delete_course_${course.code}")) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = ErrorRed)
                }
            }
        }
    }
}

@Composable
fun PaymentAdminCard(payment: FeePayment) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = NavyMidnight),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(SuccessGreen.copy(alpha = 0.5f), NavyBorder)))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = payment.receiptNo, fontWeight = FontWeight.Bold, color = NeonCyan, fontSize = 13.sp)
                Text(text = "₹${payment.amount}", fontWeight = FontWeight.ExtraBold, color = SuccessGreen, fontSize = 16.sp)
            }
            Text(text = "${payment.studentName} (${payment.studentEnrollmentNo})", color = WhitePure, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(text = "Type: ${payment.installmentType} | Mode: ${payment.paymentMode}", color = TextSecondary, fontSize = 11.sp)
        }
    }
}

@Composable
fun DocumentApprovalCard(
    doc: StudentDocument,
    onToggleApproval: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = NavyMidnight),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(if (doc.isApproved) SuccessGreen else WarningAmber, NavyBorder)))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "${doc.docType.name} - ${doc.title}", fontWeight = FontWeight.Bold, color = WhitePure, fontSize = 13.sp)
                Text(text = "Student: ${doc.studentName} (${doc.studentEnrollmentNo})", color = NeonCyan, fontSize = 12.sp)
                Text(text = "Status: ${doc.statusNotes}", color = TextMuted, fontSize = 11.sp)
            }

            Button(
                onClick = { onToggleApproval(!doc.isApproved) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (doc.isApproved) WarningAmber else SuccessGreen,
                    contentColor = NavyDark
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (doc.isApproved) "Revoke" else "Approve", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun NoticeAdminCard(notice: Notice, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = NavyMidnight),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NavyBorder, NeonCyan.copy(alpha = 0.3f))))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = notice.titleEn, fontWeight = FontWeight.Bold, color = WhitePure, fontSize = 13.sp)
                Text(text = notice.descriptionEn, color = TextSecondary, fontSize = 11.sp, maxLines = 2)
            }
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = ErrorRed)
            }
        }
    }
}

@Composable
fun AddCourseModal(
    onDismiss: () -> Unit,
    onSave: (Course) -> Unit
) {
    var code by remember { mutableStateOf("") }
    var nameEn by remember { mutableStateOf("") }
    var nameHi by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("6 Months") }
    var fee by remember { mutableStateOf("7500") }
    var category by remember { mutableStateOf("Diploma") }
    var syllabus by remember { mutableStateOf("Fundamentals, MS Office, Practical Lab") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .border(1.5.dp, NeonCyan, RoundedCornerShape(18.dp)),
            color = NavyDark
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Add New Course", fontWeight = FontWeight.Bold, color = WhitePure, fontSize = 16.sp)

                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Course Code (e.g. ADCA)", color = TextSecondary) },
                    singleLine = true,
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = nameEn,
                    onValueChange = { nameEn = it },
                    label = { Text("Course Name (English)", color = TextSecondary) },
                    singleLine = true,
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = fee,
                    onValueChange = { fee = it },
                    label = { Text("Course Fee (₹)", color = TextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = syllabus,
                    onValueChange = { syllabus = it },
                    label = { Text("Syllabus Summary", color = TextSecondary) },
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel", color = WhitePure)
                    }
                    Button(
                        onClick = {
                            val newCourse = Course(
                                id = "crs-${UUID.randomUUID()}",
                                code = code.ifBlank { "NEW-CRS" },
                                nameEn = nameEn.ifBlank { "New Computer Course" },
                                nameHi = nameHi.ifBlank { nameEn },
                                category = category,
                                durationEn = duration,
                                durationHi = duration,
                                fee = fee.toIntOrNull() ?: 5000,
                                eligibilityEn = "10th / 12th Pass",
                                eligibilityHi = "10वीं / 12वीं पास",
                                syllabusSummary = syllabus,
                                isPopular = false
                            )
                            onSave(newCourse)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = NavyDark),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save Course", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun RecordPaymentModal(
    student: Student,
    onDismiss: () -> Unit,
    onRecord: (amount: Int, mode: String, ref: String, installment: String) -> Unit
) {
    var amount by remember { mutableStateOf("${student.totalFee - student.paidFee}") }
    var mode by remember { mutableStateOf("Online UPI (QR / PhonePe)") }
    var ref by remember { mutableStateOf("UPI-${(10000000..99999999).random()}") }
    var installment by remember { mutableStateOf("Installment Payment") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .border(1.5.dp, SuccessGreen, RoundedCornerShape(18.dp)),
            color = NavyDark
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Record Student Payment", fontWeight = FontWeight.Bold, color = WhitePure, fontSize = 16.sp)
                Text("Student: ${student.fullName} (${student.enrollmentNo})", color = NeonCyan, fontSize = 12.sp)

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount Paid (₹)", color = TextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = mode,
                    onValueChange = { mode = it },
                    label = { Text("Payment Mode", color = TextSecondary) },
                    singleLine = true,
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = ref,
                    onValueChange = { ref = it },
                    label = { Text("Transaction / Receipt Ref", color = TextSecondary) },
                    singleLine = true,
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel", color = WhitePure)
                    }
                    Button(
                        onClick = {
                            val amt = amount.toIntOrNull() ?: 1000
                            onRecord(amt, mode, ref, installment)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen, contentColor = WhitePure),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Confirm & Issue", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AddNoticeModal(
    onDismiss: () -> Unit,
    onPublish: (titleEn: String, titleHi: String, descEn: String, descHi: String, category: NoticeCategory) -> Unit
) {
    var titleEn by remember { mutableStateOf("") }
    var titleHi by remember { mutableStateOf("") }
    var descEn by remember { mutableStateOf("") }
    var descHi by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(NoticeCategory.GENERAL) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .border(1.5.dp, NeonCyan, RoundedCornerShape(18.dp)),
            color = NavyDark
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Publish Announcement", fontWeight = FontWeight.Bold, color = WhitePure, fontSize = 16.sp)

                OutlinedTextField(
                    value = titleEn,
                    onValueChange = { titleEn = it },
                    label = { Text("Title (English)", color = TextSecondary) },
                    singleLine = true,
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = titleHi,
                    onValueChange = { titleHi = it },
                    label = { Text("Title (Hindi)", color = TextSecondary) },
                    singleLine = true,
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descEn,
                    onValueChange = { descEn = it },
                    label = { Text("Description", color = TextSecondary) },
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel", color = WhitePure)
                    }
                    Button(
                        onClick = {
                            onPublish(
                                titleEn.ifBlank { "Important Institute Announcement" },
                                titleHi.ifBlank { titleEn },
                                descEn.ifBlank { "Please check notice board for updates." },
                                descHi.ifBlank { descEn },
                                category
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = NavyDark),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Publish", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ApplicationDetailModal(
    app: AdmissionApplication,
    onDismiss: () -> Unit,
    onUpdateStatus: (ApplicationStatus, String) -> Unit,
    onApprove: () -> Unit,
    onReject: (String) -> Unit
) {
    val context = LocalContext.current
    val dateStr = remember(app.appliedAtMillis) {
        SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.getDefault()).format(Date(app.appliedAtMillis))
    }
    var selectedStatus by remember(app) { mutableStateOf(app.status) }
    var remarksInput by remember(app) { mutableStateOf(app.adminRemarks) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.5.dp, NeonCyan, RoundedCornerShape(20.dp)),
            color = NavyDark
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        InstituteLogo(size = 40.dp, cornerRadius = 8.dp, showGlow = false)
                        Column {
                            Text(
                                text = "Application Details",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = WhitePure
                            )
                            Text(
                                text = SrishtiStrings.INSTITUTE_NAME,
                                fontSize = 11.sp,
                                color = NeonCyan
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                HorizontalDivider(color = NavyBorder)

                // Reference Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(NavyMidnight)
                        .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("APPLICATION REFERENCE", fontSize = 9.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                            Text(app.referenceNo, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = NeonCyan)
                            Text("Submitted: $dateStr", fontSize = 10.sp, color = TextSecondary)
                        }
                        IconButton(onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Application Ref", app.referenceNo)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Copied Reference: ${app.referenceNo}", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = "Copy", tint = NeonCyan, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                // Student Personal Details
                Text("Applicant Information", fontWeight = FontWeight.Bold, color = WhitePure, fontSize = 13.sp)
                DetailRow("Student Full Name", app.fullName)
                DetailRow("Father / Guardian", app.fatherName)
                DetailRow("Date of Birth", app.dob)
                DetailRow("Gender", app.gender)
                DetailRow("Qualification", app.qualification)

                HorizontalDivider(color = NavyBorder.copy(alpha = 0.5f))

                // Course & Schedule Details
                Text("Course & Batch", fontWeight = FontWeight.Bold, color = WhitePure, fontSize = 13.sp)
                DetailRow("Selected Course", app.courseName)
                DetailRow("Course Code / ID", app.courseId)
                DetailRow("Preferred Batch", app.batchTiming)
                DetailRow("Residential Address", app.address)

                HorizontalDivider(color = NavyBorder.copy(alpha = 0.5f))

                // Contact Actions Row
                Text("Contact Student", fontWeight = FontWeight.Bold, color = WhitePure, fontSize = 13.sp)
                DetailRow("Mobile Phone", app.phone)
                if (app.email.isNotBlank()) {
                    DetailRow("Email", app.email)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${app.phone}"))
                            context.startActivity(dialIntent)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanContainer, contentColor = NeonCyan),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Call Student", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            val waIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/91${app.phone}?text=Hello%20${app.fullName}%2C%20regarding%20your%20admission%20at%20Srishti%20Computer%20Institute"))
                            context.startActivity(waIntent)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessContainer, contentColor = SuccessGreen),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("WhatsApp", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                HorizontalDivider(color = NavyBorder)

                // Status Management
                Text("Update Application Status", fontWeight = FontWeight.Bold, color = WhitePure, fontSize = 13.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val statuses = ApplicationStatus.values()
                    statuses.forEach { st ->
                        val isSel = selectedStatus == st
                        FilterChip(
                            selected = isSel,
                            onClick = { selectedStatus = st },
                            label = { Text(st.name, fontSize = 11.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = NavyMidnight,
                                labelColor = TextSecondary,
                                selectedContainerColor = when (st) {
                                    ApplicationStatus.APPROVED -> SuccessContainer
                                    ApplicationStatus.PENDING -> CyanContainer
                                    ApplicationStatus.REJECTED -> Color(0xFF450A0A)
                                },
                                selectedLabelColor = when (st) {
                                    ApplicationStatus.APPROVED -> SuccessGreen
                                    ApplicationStatus.PENDING -> NeonCyan
                                    ApplicationStatus.REJECTED -> ErrorRed
                                }
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSel,
                                borderColor = if (isSel) NeonCyan else NavyBorder,
                                selectedBorderColor = NeonCyan,
                                borderWidth = 1.dp
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                OutlinedTextField(
                    value = remarksInput,
                    onValueChange = { remarksInput = it },
                    label = { Text("Admin Remarks / Reason", color = TextSecondary) },
                    placeholder = { Text("e.g. Verified documents; batch assigned", color = TextMuted) },
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Dialog Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(0.8f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Close", color = WhitePure, fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            onUpdateStatus(selectedStatus, remarksInput)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1.2f),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = NavyDark),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Save Status", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(text = label, fontSize = 11.sp, color = TextSecondary, modifier = Modifier.weight(1f))
        Text(text = value, fontSize = 12.sp, color = WhitePure, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1.5f), textAlign = androidx.compose.ui.text.style.TextAlign.End)
    }
}

@Composable
fun EditCourseModal(
    course: Course,
    onDismiss: () -> Unit,
    onSave: (Course) -> Unit
) {
    var code by remember(course) { mutableStateOf(course.code) }
    var nameEn by remember(course) { mutableStateOf(course.nameEn) }
    var nameHi by remember(course) { mutableStateOf(course.nameHi) }
    var duration by remember(course) { mutableStateOf(course.durationEn) }
    var fee by remember(course) { mutableStateOf("${course.fee}") }
    var category by remember(course) { mutableStateOf(course.category) }
    var eligibility by remember(course) { mutableStateOf(course.eligibilityEn) }
    var syllabus by remember(course) { mutableStateOf(course.syllabusSummary) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .border(1.5.dp, NeonCyan, RoundedCornerShape(18.dp)),
            color = NavyDark
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Edit Course: ${course.code}", fontWeight = FontWeight.Bold, color = WhitePure, fontSize = 16.sp)

                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Course Code", color = TextSecondary) },
                    singleLine = true,
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = nameEn,
                    onValueChange = { nameEn = it },
                    label = { Text("Course Name (English)", color = TextSecondary) },
                    singleLine = true,
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = fee,
                    onValueChange = { fee = it },
                    label = { Text("Course Fee (₹)", color = TextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Duration (e.g. 6 Months)", color = TextSecondary) },
                    singleLine = true,
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = eligibility,
                    onValueChange = { eligibility = it },
                    label = { Text("Eligibility Criteria", color = TextSecondary) },
                    singleLine = true,
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = syllabus,
                    onValueChange = { syllabus = it },
                    label = { Text("Syllabus Summary", color = TextSecondary) },
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel", color = WhitePure)
                    }
                    Button(
                        onClick = {
                            val updated = course.copy(
                                code = code.ifBlank { course.code },
                                nameEn = nameEn.ifBlank { course.nameEn },
                                nameHi = nameHi.ifBlank { nameEn },
                                fee = fee.toIntOrNull() ?: course.fee,
                                durationEn = duration.ifBlank { course.durationEn },
                                durationHi = duration.ifBlank { course.durationHi },
                                eligibilityEn = eligibility.ifBlank { course.eligibilityEn },
                                syllabusSummary = syllabus.ifBlank { course.syllabusSummary }
                            )
                            onSave(updated)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = NavyDark),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save Changes", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
