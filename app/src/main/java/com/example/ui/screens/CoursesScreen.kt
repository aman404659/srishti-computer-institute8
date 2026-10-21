package com.example.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsScore
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.InitialCoursesData
import com.example.data.model.Course
import com.example.ui.language.AppLanguage
import com.example.ui.language.LocalAppLanguage
import com.example.ui.language.SrishtiStrings
import com.example.ui.theme.CyanContainer
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyMidnight
import com.example.ui.theme.NavySurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhitePure
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.InstituteViewModel

@Composable
fun CoursesScreen(
    viewModel: InstituteViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val langState = LocalAppLanguage.current
    val currentLang = langState.currentLanguage

    val searchQuery by viewModel.courseSearchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategoryFilter.collectAsState()
    val selectedCredential by viewModel.selectedCredentialFilter.collectAsState()
    val filteredCourses by viewModel.filteredCourses.collectAsState()
    val allCourses by viewModel.allCourses.collectAsState()

    var selectedCourseForDetail by remember { mutableStateOf<Course?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDark),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Page Title Header
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (currentLang == AppLanguage.EN) "Course Directory" else "पाठ्यक्रम निर्देशिका",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = WhitePure
                        )
                        Text(
                            text = if (currentLang == AppLanguage.EN)
                                "290+ certified courses organized section-wise with online admission"
                            else
                                "290+ प्रमाणित पाठ्यक्रम अनुभाग-वार (सेक्शन-वाइज) उपलब्ध",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }

                    // Total Courses Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(CyanContainer)
                            .border(1.dp, NeonCyan, RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${allCourses.size} Courses",
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setCourseSearchQuery(it) },
                placeholder = {
                    Text(
                        text = if (currentLang == AppLanguage.EN)
                            "Search by course name, code (e.g. AI252) or section..."
                        else
                            "कोर्स का नाम, कोड (उदा. AI252) या सेक्शन खोजें...",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = NeonCyan
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setCourseSearchQuery("") }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = TextMuted)
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("course_search_bar"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = NavyBorder,
                    focusedTextColor = WhitePure,
                    unfocusedTextColor = WhitePure,
                    focusedContainerColor = NavyMidnight,
                    unfocusedContainerColor = NavyMidnight
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Section Category Filter Header & Chips
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (currentLang == AppLanguage.EN) "SECTIONS / DOMAINS" else "कोर्स अनुभाग (सेक्शन)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        letterSpacing = 1.sp
                    )

                    if (selectedCategory != "All" || selectedCredential != "All Credentials" || searchQuery.isNotEmpty()) {
                        Text(
                            text = if (currentLang == AppLanguage.EN) "Reset Filters" else "फ़िल्टर रीसेट",
                            fontSize = 11.sp,
                            color = GoldAccent,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clickable {
                                    viewModel.setCategoryFilter("All")
                                    viewModel.setCredentialFilter("All Credentials")
                                    viewModel.setCourseSearchQuery("")
                                }
                                .padding(4.dp)
                        )
                    }
                }

                // Section Chips Row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(InitialCoursesData.SECTIONS) { section ->
                        val isSelected = selectedCategory.equals(section, ignoreCase = true)
                        val icon = getSectionIcon(section)
                        val count = if (section == "All") allCourses.size else allCourses.count { it.category.equals(section, ignoreCase = true) }

                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setCategoryFilter(section) },
                            leadingIcon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (isSelected) NeonCyan else TextSecondary
                                )
                            },
                            label = {
                                Text(
                                    text = if (section == "All") "All Sections ($count)" else "$section ($count)",
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
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
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.testTag("filter_section_${section.replace(" ", "_")}")
                        )
                    }
                }
            }
        }

        // Credential Type Filter Row (Certificate, Diploma, Advance Diploma, Master)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = if (currentLang == AppLanguage.EN) "CREDENTIAL LEVEL" else "प्रमाणपत्र / डिप्लोमा प्रकार",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(InitialCoursesData.CREDENTIAL_TYPES) { cred ->
                        val isSelected = selectedCredential.equals(cred, ignoreCase = true)
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setCredentialFilter(cred) },
                            label = {
                                Text(
                                    text = cred,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = NavyMidnight,
                                labelColor = TextSecondary,
                                selectedContainerColor = GoldAccent.copy(alpha = 0.2f),
                                selectedLabelColor = GoldAccent
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) GoldAccent else NavyBorder,
                                selectedBorderColor = GoldAccent,
                                borderWidth = 1.dp
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.testTag("filter_cred_${cred.replace(" ", "_")}")
                        )
                    }
                }
            }
        }

        // Results Summary Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildString {
                        append("${filteredCourses.size} ")
                        if (currentLang == AppLanguage.EN) {
                            append("Courses Found")
                            if (selectedCategory != "All") append(" in $selectedCategory")
                        } else {
                            append("पाठ्यक्रम उपलब्ध")
                            if (selectedCategory != "All") append(" ($selectedCategory)")
                        }
                    },
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )

                if (selectedCategory != "All") {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NavySurface)
                            .border(1.dp, NavyBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = selectedCategory,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // List of Course Cards
        if (filteredCourses.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyMidnight),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(listOf(NavyBorder, NavyBorder))
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = if (currentLang == AppLanguage.EN) "No courses match your criteria" else "कोई पाठ्यक्रम नहीं मिला",
                            fontWeight = FontWeight.Bold,
                            color = WhitePure,
                            fontSize = 15.sp
                        )
                        Text(
                            text = if (currentLang == AppLanguage.EN)
                                "Try searching by a different course keyword, code, or reset the filters."
                            else
                                "कृपया दूसरा कीवर्ड खोजें या फ़िल्टर रीसेट करें।",
                            color = TextMuted,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Button(
                            onClick = {
                                viewModel.setCategoryFilter("All")
                                viewModel.setCredentialFilter("All Credentials")
                                viewModel.setCourseSearchQuery("")
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonCyan,
                                contentColor = NavyDark
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Reset All Filters", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        } else {
            items(filteredCourses, key = { it.id }) { course ->
                CourseDetailCard(
                    course = course,
                    currentLang = currentLang,
                    onApplyClick = { viewModel.selectCourseForAdmission(course) },
                    onViewSyllabusClick = { selectedCourseForDetail = course }
                )
            }
        }
    }

    // Full Syllabus Modal
    if (selectedCourseForDetail != null) {
        val course = selectedCourseForDetail!!
        CourseSyllabusModal(
            course = course,
            currentLang = currentLang,
            onDismiss = { selectedCourseForDetail = null },
            onApply = {
                selectedCourseForDetail = null
                viewModel.selectCourseForAdmission(course)
            }
        )
    }
}

private fun getSectionIcon(section: String): ImageVector {
    return when (section) {
        "Medical & Healthcare" -> Icons.Default.MedicalServices
        "Computer & IT" -> Icons.Default.Computer
        "Finance & Accounting" -> Icons.Default.LocalAtm
        "Technical & Engineering" -> Icons.Default.Engineering
        "Media & Animation" -> Icons.Default.Movie
        "Beauty & Fashion" -> Icons.Default.Face
        "Hospitality & Tourism" -> Icons.Default.Hotel
        "Teaching & Sports" -> Icons.Default.SportsScore
        "Yoga & Astrology" -> Icons.Default.SelfImprovement
        "Vocational & Safety" -> Icons.Default.Shield
        "Agriculture & Farming" -> Icons.Default.Agriculture
        else -> Icons.Default.School
    }
}

private fun getCredentialBadge(nameEn: String): String {
    return when {
        nameEn.startsWith("Post Graduate", ignoreCase = true) ||
        nameEn.startsWith("Master Diploma", ignoreCase = true) ||
        nameEn.startsWith("Post Diploma", ignoreCase = true) ||
        nameEn.startsWith("Higher Diploma", ignoreCase = true) -> "Master / PG Diploma"
        nameEn.startsWith("Advance Diploma", ignoreCase = true) ||
        nameEn.startsWith("Advanced Diploma", ignoreCase = true) ||
        nameEn.startsWith("Advance Certificate", ignoreCase = true) -> "Advance Diploma"
        nameEn.startsWith("Diploma", ignoreCase = true) -> "Diploma"
        nameEn.startsWith("Certificate", ignoreCase = true) ||
        nameEn.startsWith("Certification", ignoreCase = true) ||
        nameEn.startsWith("Certified", ignoreCase = true) -> "Certificate"
        else -> "Certificate Course"
    }
}

@Composable
fun CourseDetailCard(
    course: Course,
    currentLang: AppLanguage,
    onApplyClick: () -> Unit,
    onViewSyllabusClick: () -> Unit
) {
    val credentialType = remember(course.nameEn) { getCredentialBadge(course.nameEn) }
    val sectionIcon = remember(course.category) { getSectionIcon(course.category) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("course_card_${course.code}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NavyMidnight),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                listOf(NeonCyan.copy(alpha = if (course.isPopular) 0.6f else 0.3f), NavyBorder)
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top badges row: Code + Section + Credential
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Course Code badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(CyanContainer)
                            .border(1.dp, NeonCyan, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = course.code,
                            fontWeight = FontWeight.ExtraBold,
                            color = NeonCyan,
                            fontSize = 12.sp
                        )
                    }

                    // Section badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NavySurface)
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = sectionIcon,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = course.category,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Credential Badge / Popular
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (credentialType.contains("Diploma"))
                                    GoldAccent.copy(alpha = 0.15f)
                                else
                                    NeonCyan.copy(alpha = 0.15f)
                            )
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = credentialType,
                            color = if (credentialType.contains("Diploma")) GoldAccent else NeonCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (course.isPopular) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(GoldAccent.copy(alpha = 0.2f))
                                .border(1.dp, GoldAccent, RoundedCornerShape(10.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "★",
                                color = GoldAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Course Title (English)
            Text(
                text = course.nameEn,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = WhitePure,
                lineHeight = 20.sp
            )

            // Course Title (Hindi translation)
            if (course.nameHi.isNotBlank() && course.nameHi != course.nameEn) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = course.nameHi,
                    style = MaterialTheme.typography.bodySmall,
                    color = NeonCyan.copy(alpha = 0.85f),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Duration & Fee Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (currentLang == AppLanguage.EN) course.durationEn else course.durationHi,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CurrencyRupee,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "₹${course.fee}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = GoldAccent
                    )
                    if (course.pdfCost > 0) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "(Reg/Cert: ₹${course.pdfCost})",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Eligibility
            Text(
                text = "Eligibility: " + (if (currentLang == AppLanguage.EN) course.eligibilityEn else course.eligibilityHi),
                fontSize = 11.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onViewSyllabusClick,
                    modifier = Modifier.weight(1f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = WhitePure),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "View Details", fontSize = 12.sp)
                }

                Button(
                    onClick = onApplyClick,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("apply_course_${course.code}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonCyan,
                        contentColor = NavyDark
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = if (currentLang == AppLanguage.EN) "Apply Online" else "प्रवेश लें",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CourseSyllabusModal(
    course: Course,
    currentLang: AppLanguage,
    onDismiss: () -> Unit,
    onApply: () -> Unit
) {
    val topics = course.syllabusSummary.split(",").map { it.trim() }
    val credentialType = remember(course.nameEn) { getCredentialBadge(course.nameEn) }

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
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(CyanContainer)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = course.code,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonCyan,
                                    fontSize = 12.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(GoldAccent.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = credentialType,
                                    color = GoldAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = course.nameEn,
                            fontWeight = FontWeight.Bold,
                            color = WhitePure,
                            fontSize = 16.sp
                        )

                        if (course.nameHi.isNotBlank() && course.nameHi != course.nameEn) {
                            Text(
                                text = course.nameHi,
                                color = NeonCyan,
                                fontSize = 13.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = WhitePure)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Section Tag & Details
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(NavyMidnight)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("SECTION", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                        Text(course.category, fontSize = 12.sp, color = WhitePure, fontWeight = FontWeight.SemiBold)
                    }
                    Column {
                        Text("DURATION", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                        Text(course.durationEn, fontSize = 12.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("TOTAL FEE", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                        Text("₹${course.fee}", fontSize = 12.sp, color = GoldAccent, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Eligibility
                Text(
                    text = "Eligibility: " + (if (currentLang == AppLanguage.EN) course.eligibilityEn else course.eligibilityHi),
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (currentLang == AppLanguage.EN) "Syllabus & Core Modules" else "पाठ्यक्रम एवं मुख्य विषय",
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    topics.forEach { topic ->
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = topic,
                                color = WhitePure,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onApply,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonCyan,
                        contentColor = NavyDark
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (currentLang == AppLanguage.EN) "Enroll in this Course" else "इस कोर्स में प्रवेश लें",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
