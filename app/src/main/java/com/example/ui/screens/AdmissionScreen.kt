package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.InitialCoursesData
import com.example.data.model.Course
import com.example.ui.components.InstituteLogo
import com.example.ui.language.AppLanguage
import com.example.ui.language.LocalAppLanguage
import com.example.ui.language.SrishtiStrings
import com.example.ui.theme.CyanContainer
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.ErrorContainer
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyMidnight
import com.example.ui.theme.NavySurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhitePure
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.InstituteViewModel

@Composable
fun AdmissionScreen(
    viewModel: InstituteViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val langState = LocalAppLanguage.current
    val currentLang = langState.currentLanguage

    val courses by viewModel.allCourses.collectAsState()
    val submissionResult by viewModel.formSubmissionResult.collectAsState()
    val errorMessage by viewModel.formErrorMessage.collectAsState()
    val isSubmittingAdmission by viewModel.isSubmittingAdmission.collectAsState()

    val formFullName by viewModel.formFullName.collectAsState()
    val formFatherName by viewModel.formFatherName.collectAsState()
    val formPhone by viewModel.formPhone.collectAsState()
    val formEmail by viewModel.formEmail.collectAsState()
    val formDob by viewModel.formDob.collectAsState()
    val formGender by viewModel.formGender.collectAsState()
    val formQualification by viewModel.formQualification.collectAsState()
    val formSelectedCourseName by viewModel.formSelectedCourseName.collectAsState()
    val formBatchTiming by viewModel.formBatchTiming.collectAsState()
    val formAddress by viewModel.formAddress.collectAsState()

    var showCoursePickerDialog by remember { mutableStateOf(false) }
    var timingDropdownExpanded by remember { mutableStateOf(false) }
    var qualificationDropdownExpanded by remember { mutableStateOf(false) }

    val batchTimings = listOf(
        "08:00 AM - 09:30 AM (Early Morning)",
        "10:00 AM - 11:30 AM (Morning)",
        "12:00 PM - 01:30 PM (Afternoon)",
        "02:00 PM - 03:30 PM (Afternoon)",
        "04:00 PM - 05:30 PM (Evening)",
        "06:00 PM - 07:30 PM (Late Evening)"
    )

    val qualifications = listOf(
        "10th Standard / Matriculation",
        "12th Standard (Arts / Science / Commerce)",
        "Undergraduate / Pursuing Graduation",
        "Graduate / Working Professional",
        "Other"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDark),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Page Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                InstituteLogo(
                    size = 52.dp,
                    cornerRadius = 14.dp,
                    modifier = Modifier.testTag("admission_form_logo")
                )
                Column {
                    Text(
                        text = if (currentLang == AppLanguage.EN) "Online Admission Form" else "ऑनलाइन प्रवेश आवेदन पत्र",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = WhitePure
                    )
                    Text(
                        text = if (currentLang == AppLanguage.EN)
                            "Srishti Computer Institute • Session 2026-2027"
                        else
                            "सृष्टि कंप्यूटर इंस्टीट्यूट • सत्र 2026-2027",
                        style = MaterialTheme.typography.bodySmall,
                        color = NeonCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Info Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CyanContainer)
                    .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (currentLang == AppLanguage.EN)
                            "Direct admission confirmed upon form verification. 1:1 dedicated computer lab allocation guaranteed."
                        else
                            "सत्यापन के उपरांत सीधा प्रवेश। अभ्यास हेतु व्यक्तिगत कंप्यूटर सिस्टम की गारंटी।",
                        fontSize = 12.sp,
                        color = WhitePure
                    )
                }
            }
        }

        // Error Banner if validation failed
        if (errorMessage != null) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(ErrorContainer)
                        .border(1.dp, ErrorRed, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = errorMessage!!,
                        color = WhitePure,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Admission Form Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = NavyMidnight),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(NavyBorder, NeonCyan.copy(alpha = 0.3f))))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // 1. Full Name
                    FormLabel(text = SrishtiStrings.fullName(currentLang) + " *")
                    OutlinedTextField(
                        value = formFullName,
                        onValueChange = { viewModel.formFullName.value = it },
                        placeholder = { Text("e.g. Rahul Sharma", color = TextMuted) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("form_full_name"),
                        colors = formFieldColors(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // 2. Father's Name
                    FormLabel(text = SrishtiStrings.fatherName(currentLang) + " *")
                    OutlinedTextField(
                        value = formFatherName,
                        onValueChange = { viewModel.formFatherName.value = it },
                        placeholder = { Text("e.g. Ramesh Sharma", color = TextMuted) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("form_father_name"),
                        colors = formFieldColors(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // 3. Mobile Number & Email in Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            FormLabel(text = "Mobile No. *")
                            OutlinedTextField(
                                value = formPhone,
                                onValueChange = { if (it.length <= 10) viewModel.formPhone.value = it },
                                placeholder = { Text("10 digits", color = TextMuted) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("form_phone"),
                                colors = formFieldColors(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            FormLabel(text = "Email Address")
                            OutlinedTextField(
                                value = formEmail,
                                onValueChange = { viewModel.formEmail.value = it },
                                placeholder = { Text("email@example.com", color = TextMuted) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("form_email"),
                                colors = formFieldColors(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }

                    // Date of Birth
                    FormLabel(text = "Date of Birth (DD/MM/YYYY) *")
                    OutlinedTextField(
                        value = formDob,
                        onValueChange = { viewModel.formDob.value = it },
                        placeholder = { Text("DD/MM/YYYY e.g. 15/07/2004", color = TextMuted) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("form_dob"),
                        colors = formFieldColors(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // 4. Course Selector Button
                    FormLabel(text = SrishtiStrings.selectCourse(currentLang) + " *")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(NavyDark)
                            .border(1.dp, if (formSelectedCourseName.isNotBlank()) NeonCyan else NavyBorder, RoundedCornerShape(10.dp))
                            .clickable { showCoursePickerDialog = true }
                            .padding(horizontal = 14.dp, vertical = 14.dp)
                            .testTag("form_course_selector")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (formSelectedCourseName.isNotBlank()) formSelectedCourseName else "Tap to Choose from 290+ Courses...",
                                    color = if (formSelectedCourseName.isNotBlank()) WhitePure else TextMuted,
                                    fontWeight = if (formSelectedCourseName.isNotBlank()) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp,
                                    maxLines = 1
                                )
                                if (formSelectedCourseName.isNotBlank()) {
                                    Text(
                                        text = "Tap to change course selection",
                                        color = NeonCyan,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search & Pick Course",
                                tint = NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // 5. Batch Timing Dropdown
                    FormLabel(text = SrishtiStrings.batchTiming(currentLang) + " *")
                    Box {
                        OutlinedTextField(
                            value = formBatchTiming,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = NeonCyan
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { timingDropdownExpanded = true }
                                .testTag("form_batch_timing"),
                            colors = formFieldColors(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        DropdownMenu(
                            expanded = timingDropdownExpanded,
                            onDismissRequest = { timingDropdownExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(NavyMidnight)
                        ) {
                            batchTimings.forEach { timing ->
                                DropdownMenuItem(
                                    text = { Text(timing, color = WhitePure, fontSize = 13.sp) },
                                    onClick = {
                                        viewModel.formBatchTiming.value = timing
                                        timingDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // 6. Highest Qualification Dropdown
                    FormLabel(text = SrishtiStrings.qualification(currentLang))
                    Box {
                        OutlinedTextField(
                            value = formQualification,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = NeonCyan
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { qualificationDropdownExpanded = true }
                                .testTag("form_qualification"),
                            colors = formFieldColors(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        DropdownMenu(
                            expanded = qualificationDropdownExpanded,
                            onDismissRequest = { qualificationDropdownExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(NavyMidnight)
                        ) {
                            qualifications.forEach { q ->
                                DropdownMenuItem(
                                    text = { Text(q, color = WhitePure, fontSize = 13.sp) },
                                    onClick = {
                                        viewModel.formQualification.value = q
                                        qualificationDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // 7. Residential Address
                    FormLabel(text = SrishtiStrings.residentialAddress(currentLang))
                    OutlinedTextField(
                        value = formAddress,
                        onValueChange = { viewModel.formAddress.value = it },
                        placeholder = { Text("Local area, ward, town / city", color = TextMuted) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("form_address"),
                        colors = formFieldColors(),
                        shape = RoundedCornerShape(10.dp),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Submit Button with Duplicate Prevention
                    Button(
                        onClick = { viewModel.submitAdmissionForm() },
                        enabled = !isSubmittingAdmission,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_admission_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonCyan,
                            contentColor = NavyDark,
                            disabledContainerColor = CyanContainer,
                            disabledContentColor = TextMuted
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isSubmittingAdmission) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = NeonCyan,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Submitting Application...",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        } else {
                            Text(
                                text = SrishtiStrings.submitApplication(currentLang),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }

    // Submission Success Dialog
    if (submissionResult != null) {
        val refNo = submissionResult!!
        Dialog(onDismissRequest = { viewModel.clearSubmissionResult() }) {
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
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(CyanContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Congratulations! Your admission form has been submitted successfully to Srishti Computer Institute.",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = WhitePure,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Save your unique Reference Number to track admission status with the administration:",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Reference Box with Copy button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(NavyMidnight)
                            .border(1.dp, NeonCyan, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("REFERENCE NUMBER", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                                Text(refNo, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = NeonCyan)
                            }
                            IconButton(onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Application Ref", refNo)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Copied Reference: $refNo", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = WhitePure)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            viewModel.clearSubmissionResult()
                            onNavigate(AppScreen.HOME)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonCyan,
                            contentColor = NavyDark
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Done & Return to Home", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (showCoursePickerDialog) {
        CoursePickerDialog(
            courses = courses,
            currentLang = currentLang,
            onDismiss = { showCoursePickerDialog = false },
            onSelectCourse = { course ->
                viewModel.selectCourseForAdmission(course)
                showCoursePickerDialog = false
            }
        )
    }
}

@Composable
private fun FormLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = WhitePure
    )
}

@Composable
fun formFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = NeonCyan,
    unfocusedBorderColor = NavyBorder,
    focusedTextColor = WhitePure,
    unfocusedTextColor = WhitePure,
    focusedContainerColor = NavyDark,
    unfocusedContainerColor = NavyDark
)

@Composable
fun CoursePickerDialog(
    courses: List<Course>,
    currentLang: AppLanguage,
    onDismiss: () -> Unit,
    onSelectCourse: (Course) -> Unit
) {
    var search by remember { mutableStateOf("") }
    var selectedSection by remember { mutableStateOf("All") }

    val sections = remember { InitialCoursesData.SECTIONS }

    val filtered = remember(courses, search, selectedSection) {
        courses.filter { c ->
            val matchSection = selectedSection == "All" || c.category.equals(selectedSection, ignoreCase = true)
            val matchSearch = search.isBlank() ||
                    c.nameEn.contains(search, ignoreCase = true) ||
                    c.nameHi.contains(search, ignoreCase = true) ||
                    c.code.contains(search, ignoreCase = true) ||
                    c.category.contains(search, ignoreCase = true)
            matchSection && matchSearch
        }
    }

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
                    .padding(18.dp)
            ) {
                // Dialog Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (currentLang == AppLanguage.EN) "Select Course" else "पाठ्यक्रम चुनें",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = WhitePure
                        )
                        Text(
                            text = "${filtered.size} courses available in directory",
                            fontSize = 11.sp,
                            color = NeonCyan
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = WhitePure)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Search field
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    placeholder = {
                        Text(
                            text = if (currentLang == AppLanguage.EN) "Search course name or code (e.g. AI252)..." else "कोर्स नाम या कोड खोजें...",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = NeonCyan)
                    },
                    trailingIcon = {
                        if (search.isNotEmpty()) {
                            IconButton(onClick = { search = "" }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = TextMuted)
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = formFieldColors(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Section Chips Row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(sections) { section ->
                        val isSel = selectedSection.equals(section, ignoreCase = true)
                        FilterChip(
                            selected = isSel,
                            onClick = { selectedSection = section },
                            label = {
                                Text(
                                    text = section,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
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
                                selected = isSel,
                                borderColor = if (isSel) NeonCyan else NavyBorder,
                                selectedBorderColor = NeonCyan,
                                borderWidth = 1.dp
                            ),
                            shape = RoundedCornerShape(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Courses List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filtered, key = { it.id }) { course ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectCourse(course) }
                                .testTag("picker_course_${course.code}"),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = NavyMidnight),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.linearGradient(listOf(NavyBorder, NavyBorder))
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(CyanContainer)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = course.code,
                                                color = NeonCyan,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp
                                            )
                                        }
                                        Text(
                                            text = course.category,
                                            color = TextMuted,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = course.nameEn,
                                        color = WhitePure,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        lineHeight = 17.sp
                                    )
                                    if (course.nameHi.isNotBlank() && course.nameHi != course.nameEn) {
                                        Text(
                                            text = course.nameHi,
                                            color = NeonCyan.copy(alpha = 0.85f),
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "₹${course.fee}",
                                        color = GoldAccent,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = course.durationEn,
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
