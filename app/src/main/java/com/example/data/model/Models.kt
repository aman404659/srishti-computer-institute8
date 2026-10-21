package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey val id: String,
    val code: String,
    val nameEn: String,
    val nameHi: String,
    val category: String, // Section / Domain
    val durationEn: String,
    val durationHi: String,
    val fee: Int,
    val eligibilityEn: String,
    val eligibilityHi: String,
    val syllabusSummary: String, // Comma separated topics
    val isPopular: Boolean = false,
    val pdfCost: Int = 0
)

enum class ApplicationStatus {
    PENDING,
    APPROVED,
    REJECTED
}

@Entity(tableName = "admission_applications")
data class AdmissionApplication(
    @PrimaryKey val id: String,
    val referenceNo: String,
    val fullName: String,
    val fatherName: String,
    val phone: String,
    val email: String,
    val dob: String,
    val gender: String,
    val qualification: String,
    val courseId: String,
    val courseName: String,
    val batchTiming: String,
    val address: String,
    val status: ApplicationStatus = ApplicationStatus.PENDING,
    val appliedAtMillis: Long = System.currentTimeMillis(),
    val adminRemarks: String = ""
)

@Entity(tableName = "students")
data class Student(
    @PrimaryKey val id: String,
    val enrollmentNo: String,
    val fullName: String,
    val fatherName: String,
    val phone: String,
    val email: String,
    val courseId: String,
    val courseName: String,
    val batchTiming: String,
    val enrolledAtMillis: Long = System.currentTimeMillis(),
    val totalFee: Int,
    val paidFee: Int = 0,
    val isCompleted: Boolean = false
)

@Entity(tableName = "fee_payments")
data class FeePayment(
    @PrimaryKey val id: String,
    val receiptNo: String,
    val studentEnrollmentNo: String,
    val studentName: String,
    val courseName: String,
    val amount: Int,
    val paymentDateMillis: Long = System.currentTimeMillis(),
    val paymentMode: String, // UPI, Cash, NetBanking, Card
    val transactionRef: String,
    val installmentType: String // Full Payment, 1st Installment, 2nd Installment
)

enum class DocumentType {
    ID_CARD,
    FEE_RECEIPT,
    ENROLLMENT_SLIP,
    ADMIT_CARD,
    MARKSHEET,
    CERTIFICATE
}

@Entity(tableName = "student_documents")
data class StudentDocument(
    @PrimaryKey val id: String,
    val studentEnrollmentNo: String,
    val studentName: String,
    val courseName: String,
    val docType: DocumentType,
    val title: String,
    val issueDateMillis: Long = System.currentTimeMillis(),
    val isApproved: Boolean = false,
    val statusNotes: String = "",
    val docPayload: String = "" // Formatted details or JSON string
)

enum class NoticeCategory {
    URGENT,
    ADMISSION,
    EXAM,
    HOLIDAY,
    GENERAL
}

@Entity(tableName = "notices")
data class Notice(
    @PrimaryKey val id: String,
    val titleEn: String,
    val titleHi: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val category: NoticeCategory,
    val publishDateMillis: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)
