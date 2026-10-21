package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.DocumentType
import com.example.data.model.Student
import com.example.data.model.StudentDocument
import com.example.ui.language.AppLanguage
import com.example.ui.language.LocalAppLanguage
import com.example.ui.language.SrishtiStrings
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
import com.example.ui.theme.WarningContainer
import com.example.ui.theme.WhitePure
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StudentDocumentItemCard(
    doc: StudentDocument,
    onViewClick: (StudentDocument) -> Unit,
    modifier: Modifier = Modifier
) {
    val langState = LocalAppLanguage.current
    val currentLang = langState.currentLanguage

    val icon = when (doc.docType) {
        DocumentType.ID_CARD -> Icons.Default.Badge
        DocumentType.FEE_RECEIPT -> Icons.Default.Payment
        DocumentType.ENROLLMENT_SLIP -> Icons.Default.Assignment
        DocumentType.ADMIT_CARD -> Icons.Default.ConfirmationNumber
        DocumentType.MARKSHEET -> Icons.Default.Description
        DocumentType.CERTIFICATE -> Icons.Default.School
    }

    val typeLabel = when (doc.docType) {
        DocumentType.ID_CARD -> SrishtiStrings.idCard(currentLang)
        DocumentType.FEE_RECEIPT -> SrishtiStrings.feeReceipt(currentLang)
        DocumentType.ENROLLMENT_SLIP -> SrishtiStrings.enrollmentSlip(currentLang)
        DocumentType.ADMIT_CARD -> SrishtiStrings.admitCard(currentLang)
        DocumentType.MARKSHEET -> SrishtiStrings.marksheet(currentLang)
        DocumentType.CERTIFICATE -> SrishtiStrings.certificate(currentLang)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("doc_card_${doc.docType.name}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (doc.isApproved) NavySurface else NavyMidnight
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                colors = if (doc.isApproved) listOf(NeonCyan.copy(alpha = 0.6f), NavyBorder)
                else listOf(WarningAmber.copy(alpha = 0.4f), NavyBorder)
            ),
            width = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (doc.isApproved) CyanContainer else WarningContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = typeLabel,
                            tint = if (doc.isApproved) NeonCyan else WarningAmber,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = typeLabel,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = WhitePure
                        )
                        Text(
                            text = doc.title,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                // Approval Status Chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (doc.isApproved) SuccessContainer else WarningContainer)
                        .border(
                            1.dp,
                            if (doc.isApproved) SuccessGreen else WarningAmber,
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (doc.isApproved) Icons.Default.CheckCircle else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (doc.isApproved) SuccessGreen else WarningAmber,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (doc.isApproved) "VERIFIED" else "PENDING",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (doc.isApproved) SuccessGreen else WarningAmber
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Notice text or approval state
            if (doc.isApproved) {
                Text(
                    text = doc.statusNotes.ifBlank { "Official document verified by administrator." },
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { onViewClick(doc) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("view_doc_btn_${doc.docType.name}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonCyan,
                        contentColor = NavyDark
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (currentLang == AppLanguage.EN) "View & Download Document" else "दस्तावेज देखें एवं डाउनलोड करें",
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(NavyDark.copy(alpha = 0.6f))
                        .border(1.dp, NavyBorder, RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = WarningAmber,
                            modifier = Modifier
                                .size(18.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (currentLang == AppLanguage.EN)
                                "Actual document will be available for download once processed and approved by institute administrator."
                            else
                                "यह दस्तावेज संस्थान एडमिन द्वारा सत्यापित और स्वीकृत होने पर डाउनलोड के लिए उपलब्ध होगा।",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DocumentViewerModal(
    doc: StudentDocument,
    student: Student?,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val langState = LocalAppLanguage.current
    val currentLang = langState.currentLanguage
    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val formattedDate = dateFormatter.format(Date(doc.issueDateMillis))

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 24.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(1.5.dp, NeonCyan, RoundedCornerShape(20.dp)),
            color = NavyDark
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Modal Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = SrishtiStrings.INSTITUTE_NAME,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                        Text(
                            text = doc.title,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_doc_modal")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = WhitePure
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = NavyBorder
                )

                // Render specific document visual representation
                when (doc.docType) {
                    DocumentType.ID_CARD -> {
                        StudentIdCardVisual(doc, student, formattedDate)
                    }
                    DocumentType.FEE_RECEIPT -> {
                        FeeReceiptVisual(doc, student, formattedDate)
                    }
                    DocumentType.ENROLLMENT_SLIP -> {
                        EnrollmentSlipVisual(doc, student, formattedDate)
                    }
                    DocumentType.ADMIT_CARD -> {
                        AdmitCardVisual(doc, student, formattedDate)
                    }
                    DocumentType.MARKSHEET -> {
                        MarksheetVisual(doc, student, formattedDate)
                    }
                    DocumentType.CERTIFICATE -> {
                        CertificateVisual(doc, student, formattedDate)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Actions: Download, Print, Share
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            Toast.makeText(
                                context,
                                "Document downloaded successfully to device storage.",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_download_doc"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonCyan,
                            contentColor = NavyDark
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Download", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            Toast.makeText(
                                context,
                                "Sent to network printer / print preview.",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_print_doc"),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Print,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Print")
                    }
                }
            }
        }
    }
}

@Composable
fun StudentIdCardVisual(
    doc: StudentDocument,
    student: Student?,
    dateStr: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NavyMidnight),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NeonCyan, NavyBorder)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Card Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CyanContainer)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    InstituteLogo(size = 32.dp, cornerRadius = 6.dp, showGlow = false)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = SrishtiStrings.INSTITUTE_NAME,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "STUDENT IDENTITY CARD",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = WhitePure
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Student Photo Avatar Box
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(NavySurface)
                    .border(2.dp, NeonCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = doc.studentName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = WhitePure
            )

            Text(
                text = "Enrollment: ${doc.studentEnrollmentNo}",
                fontWeight = FontWeight.SemiBold,
                color = NeonCyan,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyDark, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                InfoRow("Course", doc.courseName)
                InfoRow("Batch", student?.batchTiming ?: "Morning Batch")
                InfoRow("Issue Date", dateStr)
                InfoRow("Valid Till", "March 2027")
                InfoRow("Status", "ACTIVE / VERIFIED")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Barcode representation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .background(WhitePure, RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "* ${doc.studentEnrollmentNo} *",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    letterSpacing = 4.sp,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun FeeReceiptVisual(
    doc: StudentDocument,
    student: Student?,
    dateStr: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NavyMidnight),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(SuccessGreen, NavyBorder)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = SrishtiStrings.INSTITUTE_NAME,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "FEE PAYMENT RECEIPT",
                        fontWeight = FontWeight.SemiBold,
                        color = SuccessGreen,
                        fontSize = 12.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SuccessContainer)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("PAID", color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = NavyBorder)

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                InfoRow("Student Name", doc.studentName)
                InfoRow("Enrollment No", doc.studentEnrollmentNo)
                InfoRow("Course", doc.courseName)
                InfoRow("Receipt Date", dateStr)
                InfoRow("Payment Mode", "Online UPI / Certified Desk")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavySurface, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Amount Paid:",
                        fontWeight = FontWeight.Bold,
                        color = WhitePure,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "₹${student?.paidFee ?: 14500}",
                        fontWeight = FontWeight.ExtraBold,
                        color = NeonCyan,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "✓ Computer Generated Official Receipt - Srishti Computer Institute",
                fontSize = 11.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun EnrollmentSlipVisual(
    doc: StudentDocument,
    student: Student?,
    dateStr: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NavyMidnight),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NeonCyan, NavyBorder)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "OFFICIAL ENROLLMENT SLIP",
                fontWeight = FontWeight.Bold,
                color = NeonCyan,
                fontSize = 14.sp
            )
            Text(
                text = "Registration Acknowledgement & Details",
                fontSize = 12.sp,
                color = TextSecondary
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = NavyBorder)

            InfoRow("Student Name", doc.studentName)
            InfoRow("Enrollment ID", doc.studentEnrollmentNo)
            InfoRow("Course", doc.courseName)
            InfoRow("Father's Name", student?.fatherName ?: "Verified")
            InfoRow("Batch Timing", student?.batchTiming ?: "Morning Batch")
            InfoRow("Registration Date", dateStr)
            InfoRow("Lab Allocation", "Lab 1 (Individual Dedicated System)")
        }
    }
}

@Composable
fun AdmitCardVisual(
    doc: StudentDocument,
    student: Student?,
    dateStr: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NavyMidnight),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(GoldAccent, NavyBorder)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "EXAMINATION ADMIT CARD",
                fontWeight = FontWeight.Bold,
                color = GoldAccent,
                fontSize = 14.sp
            )
            Text(
                text = "Semester Theory & Practical Examination 2026",
                fontSize = 12.sp,
                color = TextSecondary
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = NavyBorder)

            InfoRow("Student Name", doc.studentName)
            InfoRow("Roll / Enrollment", doc.studentEnrollmentNo)
            InfoRow("Course", doc.courseName)
            InfoRow("Exam Center", "Srishti Computer Institute Lab 2")
            InfoRow("Exam Date", "15 Nov 2026")
            InfoRow("Shift Timing", "10:30 AM to 01:30 PM")

            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyDark, RoundedCornerShape(6.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = "Important: Candidates must carry this Admit Card & Student ID Card to the examination hall.",
                    fontSize = 11.sp,
                    color = WarningAmber
                )
            }
        }
    }
}

@Composable
fun MarksheetVisual(
    doc: StudentDocument,
    student: Student?,
    dateStr: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NavyMidnight),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(NeonCyan, NavyBorder)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "ACADEMIC MARKSHEET",
                fontWeight = FontWeight.Bold,
                color = NeonCyan,
                fontSize = 14.sp
            )
            Text(
                text = "Semester Evaluation & Performance Report",
                fontSize = 12.sp,
                color = TextSecondary
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = NavyBorder)

            InfoRow("Student Name", doc.studentName)
            InfoRow("Enrollment No", doc.studentEnrollmentNo)
            InfoRow("Course", doc.courseName)

            Spacer(modifier = Modifier.height(10.dp))

            // Marks Table
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyDark, RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Subject", fontWeight = FontWeight.Bold, color = NeonCyan, fontSize = 12.sp)
                    Text("Marks (100)", fontWeight = FontWeight.Bold, color = NeonCyan, fontSize = 12.sp)
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = NavyBorder)
                SubjectRow("Computer Fundamentals", "92")
                SubjectRow("Office Automation & Tools", "88")
                SubjectRow("Accounting & Tally Prime", "91")
                SubjectRow("Web Designing & Programming", "89")
                SubjectRow("Practical Lab & Project", "88")
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = NavyBorder)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total: 448 / 500", fontWeight = FontWeight.Bold, color = WhitePure, fontSize = 12.sp)
                    Text("Grade: A+ (89.6%)", fontWeight = FontWeight.Bold, color = SuccessGreen, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun CertificateVisual(
    doc: StudentDocument,
    student: Student?,
    dateStr: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NavyMidnight),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(GoldAccent, NeonCyan)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InstituteLogo(size = 46.dp, cornerRadius = 10.dp, showGlow = true)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = SrishtiStrings.INSTITUTE_NAME,
                fontWeight = FontWeight.ExtraBold,
                color = WhitePure,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "CERTIFICATE OF COMPLETION",
                fontWeight = FontWeight.Bold,
                color = GoldAccent,
                fontSize = 13.sp,
                letterSpacing = 1.sp
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GoldAccent.copy(alpha = 0.5f))

            Text(
                text = "This is to certify that",
                color = TextSecondary,
                fontSize = 12.sp
            )
            Text(
                text = doc.studentName,
                fontWeight = FontWeight.Bold,
                color = NeonCyan,
                fontSize = 18.sp
            )
            Text(
                text = "has successfully completed the prescribed curriculum in",
                color = TextSecondary,
                fontSize = 12.sp
            )
            Text(
                text = doc.courseName,
                fontWeight = FontWeight.Bold,
                color = WhitePure,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "with Grade A+ (Exemplary Performance)",
                color = GoldAccent,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Date: $dateStr", fontSize = 11.sp, color = TextMuted)
                    Text("Cert No: SCI-CRT-2026-904", fontSize = 11.sp, color = TextMuted)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Authorized Signatory", fontSize = 11.sp, color = WhitePure, fontWeight = FontWeight.Bold)
                    Text("Director, Srishti Institute", fontSize = 10.sp, color = NeonCyan)
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextMuted, fontSize = 12.sp)
        Text(text = value, color = WhitePure, fontWeight = FontWeight.Medium, fontSize = 12.sp)
    }
}

@Composable
private fun SubjectRow(subject: String, marks: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = subject, color = TextSecondary, fontSize = 11.sp)
        Text(text = marks, color = WhitePure, fontWeight = FontWeight.Bold, fontSize = 11.sp)
    }
}
