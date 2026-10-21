package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ApplicationStatus
import com.example.data.model.Course
import com.example.data.model.Notice
import com.example.ui.language.AppLanguage
import com.example.ui.language.LocalAppLanguage
import com.example.ui.language.SrishtiStrings
import com.example.ui.components.InstituteLogo
import com.example.ui.theme.CyanContainer
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyMidnight
import com.example.ui.theme.NavySurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanDark
import com.example.ui.theme.SuccessContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WhitePure
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.InstituteViewModel

@Composable
fun HomeScreen(
    viewModel: InstituteViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val langState = LocalAppLanguage.current
    val currentLang = langState.currentLanguage

    val courses by viewModel.allCourses.collectAsState()
    val popularCourses = courses.filter { it.isPopular }
    val activeNotices by viewModel.activeNotices.collectAsState()
    val trackResult by viewModel.trackResult.collectAsState()
    val trackNotFound by viewModel.trackNotFound.collectAsState()

    var trackInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDark),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Hero Section
        item {
            HeroBanner(
                onApplyClick = { onNavigate(AppScreen.ADMISSION) },
                onExploreCourses = { onNavigate(AppScreen.COURSES) },
                onWhatsAppClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(SrishtiStrings.WHATSAPP_CHAT_URL)
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            )
        }

        // Live Admission / Notices ticker
        if (activeNotices.isNotEmpty()) {
            item {
                NoticeTickerSection(notices = activeNotices, currentLang = currentLang)
            }
        }

        // Stats Row
        item {
            KeyStatsSection()
        }

        // Quick Application Tracker Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyMidnight),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.linearGradient(listOf(NeonCyan, NavyBorder))
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CyanContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (currentLang == AppLanguage.EN) "Check Admission Status" else "प्रवेश स्थिति जांचें",
                                fontWeight = FontWeight.Bold,
                                color = WhitePure,
                                fontSize = 15.sp
                            )
                            Text(
                                text = if (currentLang == AppLanguage.EN) "Enter Application Ref No or Mobile Number" else "आवेदन संदर्भ संख्या या मोबाइल नंबर दर्ज करें",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = trackInput,
                            onValueChange = { trackInput = it },
                            placeholder = {
                                Text(
                                    text = "e.g. SCI-ADM-2026-0182 or Phone",
                                    color = TextMuted,
                                    fontSize = 13.sp
                                )
                            },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("track_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = NavyBorder,
                                focusedTextColor = WhitePure,
                                unfocusedTextColor = WhitePure,
                                focusedContainerColor = NavyDark,
                                unfocusedContainerColor = NavyDark
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Button(
                            onClick = { viewModel.trackApplication(trackInput) },
                            modifier = Modifier.testTag("track_search_btn"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonCyan,
                                contentColor = NavyDark
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Track", fontWeight = FontWeight.Bold)
                        }
                    }

                    // Track Result Banner
                    if (trackResult != null) {
                        val app = trackResult!!
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(NavySurface)
                                .border(1.dp, NeonCyan, RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = app.fullName,
                                        fontWeight = FontWeight.Bold,
                                        color = WhitePure,
                                        fontSize = 14.sp
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(
                                                when (app.status) {
                                                    ApplicationStatus.APPROVED -> SuccessContainer
                                                    ApplicationStatus.PENDING -> GoldContainer
                                                    ApplicationStatus.REJECTED -> Color(0xFF450A0A)
                                                }
                                            )
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = app.status.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = when (app.status) {
                                                ApplicationStatus.APPROVED -> SuccessGreen
                                                ApplicationStatus.PENDING -> GoldAccent
                                                ApplicationStatus.REJECTED -> Color(0xFFEF4444)
                                            }
                                        )
                                    }
                                }
                                Text(
                                    text = "Course: ${app.courseName}",
                                    fontSize = 12.sp,
                                    color = NeonCyan
                                )
                                Text(
                                    text = "Batch: ${app.batchTiming}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                                if (app.adminRemarks.isNotBlank()) {
                                    Text(
                                        text = "Remarks: ${app.adminRemarks}",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }
                            }
                        }
                    } else if (trackNotFound) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No application found for '$trackInput'. Please check the number.",
                            color = WarningAmber,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Popular Courses Section
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (currentLang == AppLanguage.EN) "Featured Certified Courses" else "प्रमुख प्रमाणित कोर्सेज",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = WhitePure
                        )
                        Text(
                            text = if (currentLang == AppLanguage.EN) "Industry-recognized job-oriented programs" else "रोजगार उन्मुख एवं व्यावसायिक प्रशिक्षण",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }

                    Text(
                        text = if (currentLang == AppLanguage.EN) "View All →" else "सभी देखें →",
                        color = NeonCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { onNavigate(AppScreen.COURSES) }
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(popularCourses) { course ->
                        FeaturedCourseCard(
                            course = course,
                            currentLang = currentLang,
                            onApply = { viewModel.selectCourseForAdmission(course) }
                        )
                    }
                }
            }
        }

        // Why Choose Srishti Section
        item {
            WhyChooseSection(currentLang = currentLang)
        }

        // Contact & Location Card
        item {
            ContactQuickCard(
                currentLang = currentLang,
                onWhatsAppClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(SrishtiStrings.WHATSAPP_CHAT_URL)
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                onCallClick = {
                    try {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse(SrishtiStrings.CALL_URI)
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            )
        }
    }
}

@Composable
fun HeroBanner(
    onApplyClick: () -> Unit,
    onExploreCourses: () -> Unit,
    onWhatsAppClick: () -> Unit
) {
    val langState = LocalAppLanguage.current
    val currentLang = langState.currentLanguage

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(NavyMidnight, NavyDark)
                )
            )
            .border(
                1.5.dp,
                Brush.horizontalGradient(listOf(NeonCyan, NavyBorder, NeonCyanDark)),
                RoundedCornerShape(20.dp)
            )
            .padding(20.dp)
    ) {
        Column {
            // Admission Open 2026-27 Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(CyanContainer)
                    .border(1.dp, NeonCyan, RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (currentLang == AppLanguage.EN) "ADMISSIONS OPEN 2026-2027" else "नवीन सत्र 2026-2027 प्रवेश प्रारंभ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                InstituteLogo(
                    size = 58.dp,
                    cornerRadius = 14.dp,
                    modifier = Modifier.testTag("hero_institute_logo")
                )
                Column {
                    Text(
                        text = SrishtiStrings.INSTITUTE_NAME,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.4.sp
                        ),
                        color = WhitePure
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = SrishtiStrings.heroTagline(currentLang),
                        style = MaterialTheme.typography.bodyMedium,
                        color = NeonCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = SrishtiStrings.heroDescription(currentLang),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // CTA Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onApplyClick,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("hero_apply_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonCyan,
                        contentColor = NavyDark
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = SrishtiStrings.applyNow(currentLang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                OutlinedButton(
                    onClick = onWhatsAppClick,
                    modifier = Modifier.testTag("hero_whatsapp_btn"),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SuccessGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "WhatsApp",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "WhatsApp", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun NoticeTickerSection(notices: List<Notice>, currentLang: AppLanguage) {
    val firstNotice = notices.firstOrNull() ?: return
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(NavySurface)
            .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(WarningAmber.copy(alpha = 0.2f))
                    .padding(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Campaign,
                    contentDescription = "Notice",
                    tint = WarningAmber,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (currentLang == AppLanguage.EN) firstNotice.titleEn else firstNotice.titleHi,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = WhitePure,
                    maxLines = 1
                )
                Text(
                    text = if (currentLang == AppLanguage.EN) firstNotice.descriptionEn else firstNotice.descriptionHi,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun KeyStatsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatItem(number = "500+", label = "Certified Students", icon = Icons.Default.School, modifier = Modifier.weight(1f))
        StatItem(number = "290+", label = "Certified Courses", icon = Icons.Default.Computer, modifier = Modifier.weight(1f))
        StatItem(number = "100%", label = "Lab Practical", icon = Icons.Default.CheckCircle, modifier = Modifier.weight(1f))
    }
}

@Composable
fun StatItem(
    number: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = NavyMidnight),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(NavyBorder, NeonCyan.copy(alpha = 0.3f))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = NeonCyan,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = number,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 17.sp,
                color = WhitePure
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = TextSecondary,
                lineHeight = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun FeaturedCourseCard(
    course: Course,
    currentLang: AppLanguage,
    onApply: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .testTag("featured_course_${course.code}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NavyMidnight),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NeonCyan.copy(alpha = 0.5f), NavyBorder)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(CyanContainer)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = course.code,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        fontSize = 12.sp
                    )
                }

                Text(
                    text = if (currentLang == AppLanguage.EN) course.durationEn else course.durationHi,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (currentLang == AppLanguage.EN) course.nameEn else course.nameHi,
                fontWeight = FontWeight.Bold,
                color = WhitePure,
                fontSize = 14.sp,
                maxLines = 2,
                minLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "₹${course.fee}",
                fontWeight = FontWeight.ExtraBold,
                color = NeonCyan,
                fontSize = 17.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onApply,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyanContainer,
                    contentColor = NeonCyan
                ),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.6f))
            ) {
                Text(
                    text = if (currentLang == AppLanguage.EN) "Apply Online" else "ऑनलाइन प्रवेश",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun WhyChooseSection(currentLang: AppLanguage) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NavyMidnight),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NavyBorder, NeonCyan.copy(alpha = 0.3f))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = if (currentLang == AppLanguage.EN) "Why Srishti Computer Institute?" else "सृष्टि कंप्यूटर क्यों चुनें?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = WhitePure
            )
            Spacer(modifier = Modifier.height(12.dp))

            FeatureItem(
                title = if (currentLang == AppLanguage.EN) "1:1 Dedicated PC Allocation" else "प्रत्येक छात्र हेतु व्यक्तिगत कंप्यूटर",
                desc = if (currentLang == AppLanguage.EN) "Full hands-on lab practice with individual high-speed desktop." else "प्रत्येक विद्यार्थी को अभ्यास के लिए समर्पित सिस्टम।"
            )
            FeatureItem(
                title = if (currentLang == AppLanguage.EN) "Industry Verified Syllabus" else "आधुनिक उद्योग-स्तरीय पाठ्यक्रम",
                desc = if (currentLang == AppLanguage.EN) "Tally Prime + GST, Web Technologies & Office Productivity." else "टैली प्राइम, जीएसटी, वेब डेवलपमेंट एवं ऑफिस टूल्स।"
            )
            FeatureItem(
                title = if (currentLang == AppLanguage.EN) "Recognized Student Document Vault" else "डिजिटल छात्र दस्तावेज एवं पोर्टल",
                desc = if (currentLang == AppLanguage.EN) "Download verified ID Cards, Admit Cards, Marksheets & Certificates." else "पहचान पत्र, प्रवेश पत्र, अंकतालिका एवं प्रमाण-पत्र सुरक्षित डाउनलोड।"
            )
        }
    }
}

@Composable
private fun FeatureItem(title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = NeonCyan,
            modifier = Modifier
                .size(18.dp)
                .padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, color = WhitePure, fontSize = 13.sp)
            Text(text = desc, color = TextSecondary, fontSize = 11.sp)
        }
    }
}

@Composable
fun ContactQuickCard(
    currentLang: AppLanguage,
    onWhatsAppClick: () -> Unit,
    onCallClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NavyBorder, NeonCyan.copy(alpha = 0.3f))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = if (currentLang == AppLanguage.EN) "Need Admission Guidance?" else "प्रवेश संबंधी मार्गदर्शन चाहिए?",
                fontWeight = FontWeight.Bold,
                color = WhitePure,
                fontSize = 15.sp
            )
            Text(
                text = if (currentLang == AppLanguage.EN) "Our counselors are available Monday to Saturday (9:00 AM - 6:00 PM)." else "सोमवार से शनिवार (प्रातः 9:00 से सायं 6:00 बजे) तक संपर्क करें।",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onWhatsAppClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SuccessGreen,
                        contentColor = WhitePure
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("WhatsApp", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Button(
                    onClick = onCallClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyanContainer,
                        contentColor = NeonCyan
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan)
                ) {
                    Text("Call Campus", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}
