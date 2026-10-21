package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.AdmissionApplication
import com.example.data.model.ApplicationStatus
import com.example.data.model.Course
import com.example.data.model.DocumentType
import com.example.data.model.FeePayment
import com.example.data.model.Notice
import com.example.data.model.NoticeCategory
import com.example.data.model.Student
import com.example.data.model.StudentDocument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Converters {
    @TypeConverter
    fun fromApplicationStatus(value: ApplicationStatus): String = value.name

    @TypeConverter
    fun toApplicationStatus(value: String): ApplicationStatus =
        runCatching { ApplicationStatus.valueOf(value) }.getOrDefault(ApplicationStatus.PENDING)

    @TypeConverter
    fun fromDocumentType(value: DocumentType): String = value.name

    @TypeConverter
    fun toDocumentType(value: String): DocumentType =
        runCatching { DocumentType.valueOf(value) }.getOrDefault(DocumentType.ID_CARD)

    @TypeConverter
    fun fromNoticeCategory(value: NoticeCategory): String = value.name

    @TypeConverter
    fun toNoticeCategory(value: String): NoticeCategory =
        runCatching { NoticeCategory.valueOf(value) }.getOrDefault(NoticeCategory.GENERAL)
}

@Database(
    entities = [
        Course::class,
        AdmissionApplication::class,
        Student::class,
        FeePayment::class,
        StudentDocument::class,
        Notice::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
    abstract fun admissionDao(): AdmissionDao
    abstract fun studentDao(): StudentDao
    abstract fun paymentDao(): PaymentDao
    abstract fun documentDao(): DocumentDao
    abstract fun noticeDao(): NoticeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "srishti_institute_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateInitialData(database)
                }
            }
        }

        private suspend fun populateInitialData(db: AppDatabase) {
            // Seed all official registered courses for Srishti Computer Institute
            db.courseDao().insertCourses(InitialCoursesData.allCourses)

            // Seed Demo Student 1 (Enrolled & Approved)
            val student1 = Student(
                id = "std-001",
                enrollmentNo = "SCI-2026-0104",
                fullName = "Rahul Sharma",
                fatherName = "Ramesh Sharma",
                phone = "9876543210",
                email = "rahul.sharma@example.com",
                courseId = "crs-ai03-292",
                courseName = "Advance Diploma in Computer Application",
                batchTiming = "10:00 AM - 11:30 AM (Morning)",
                enrolledAtMillis = System.currentTimeMillis() - (60L * 24 * 3600 * 1000), // 60 days ago
                totalFee = 1299,
                paidFee = 1299,
                isCompleted = false
            )

            // Seed Demo Student 2 (Enrolled with partial fee)
            val student2 = Student(
                id = "std-002",
                enrollmentNo = "SCI-2026-0105",
                fullName = "Priya Verma",
                fatherName = "Suresh Verma",
                phone = "9812345678",
                email = "priya.verma@example.com",
                courseId = "crs-ai08-287",
                courseName = "Certificate in TALLY Prime",
                batchTiming = "04:00 PM - 05:30 PM (Evening)",
                enrolledAtMillis = System.currentTimeMillis() - (25L * 24 * 3600 * 1000),
                totalFee = 1299,
                paidFee = 1000,
                isCompleted = false
            )
            db.studentDao().insertStudents(listOf(student1, student2))

            // Seed Payments
            val payment1 = FeePayment(
                id = "pay-001",
                receiptNo = "SCI-RCP-2026-0042",
                studentEnrollmentNo = "SCI-2026-0104",
                studentName = "Rahul Sharma",
                courseName = "Advance Diploma in Computer Application",
                amount = 1299,
                paymentDateMillis = System.currentTimeMillis() - (58L * 24 * 3600 * 1000),
                paymentMode = "UPI (Google Pay)",
                transactionRef = "UPI-4281907315",
                installmentType = "Full Settlement"
            )
            val payment2 = FeePayment(
                id = "pay-002",
                receiptNo = "SCI-RCP-2026-0089",
                studentEnrollmentNo = "SCI-2026-0105",
                studentName = "Priya Verma",
                courseName = "Certificate in TALLY Prime",
                amount = 1000,
                paymentDateMillis = System.currentTimeMillis() - (24L * 24 * 3600 * 1000),
                paymentMode = "Cash at Desk",
                transactionRef = "CASH-REC-089",
                installmentType = "1st Installment (Balance: ₹299)"
            )
            db.paymentDao().insertPayments(listOf(payment1, payment2))

            // Seed Student Documents for Rahul Sharma (Approved and Issued)
            val docsStudent1 = listOf(
                StudentDocument(
                    id = "doc-id-001",
                    studentEnrollmentNo = "SCI-2026-0104",
                    studentName = "Rahul Sharma",
                    courseName = "ADCA (12 Months)",
                    docType = DocumentType.ID_CARD,
                    title = "Student Identity Card 2026-27",
                    issueDateMillis = System.currentTimeMillis() - (55L * 24 * 3600 * 1000),
                    isApproved = true,
                    statusNotes = "Official Identity Card Issued by Director",
                    docPayload = "{\"bloodGroup\":\"B+\",\"validTill\":\"March 2027\",\"center\":\"Main Campus, City Road\"}"
                ),
                StudentDocument(
                    id = "doc-rcp-001",
                    studentEnrollmentNo = "SCI-2026-0104",
                    studentName = "Rahul Sharma",
                    courseName = "ADCA (12 Months)",
                    docType = DocumentType.FEE_RECEIPT,
                    title = "Official Fee Receipt - Full Payment",
                    issueDateMillis = System.currentTimeMillis() - (58L * 24 * 3600 * 1000),
                    isApproved = true,
                    statusNotes = "Payment verified and receipt generated",
                    docPayload = "{\"receiptNo\":\"SCI-RCP-2026-0042\",\"amountPaid\":14500,\"mode\":\"UPI\"}"
                ),
                StudentDocument(
                    id = "doc-enr-001",
                    studentEnrollmentNo = "SCI-2026-0104",
                    studentName = "Rahul Sharma",
                    courseName = "ADCA (12 Months)",
                    docType = DocumentType.ENROLLMENT_SLIP,
                    title = "Admission Registration Slip",
                    issueDateMillis = System.currentTimeMillis() - (59L * 24 * 3600 * 1000),
                    isApproved = true,
                    statusNotes = "Admission confirmed",
                    docPayload = "{\"batch\":\"Morning 10:00 AM\",\"rollNo\":\"SCI-26-ADCA-04\"}"
                ),
                StudentDocument(
                    id = "doc-adm-001",
                    studentEnrollmentNo = "SCI-2026-0104",
                    studentName = "Rahul Sharma",
                    courseName = "ADCA (12 Months)",
                    docType = DocumentType.ADMIT_CARD,
                    title = "Semester 1 Examination Hall Ticket",
                    issueDateMillis = System.currentTimeMillis() - (10L * 24 * 3600 * 1000),
                    isApproved = true,
                    statusNotes = "Hall Ticket authorized for Exam Lab 2",
                    docPayload = "{\"examDate\":\"15th Nov 2026\",\"time\":\"11:00 AM to 01:00 PM\",\"seatNo\":\"L2-08\"}"
                ),
                StudentDocument(
                    id = "doc-mrk-001",
                    studentEnrollmentNo = "SCI-2026-0104",
                    studentName = "Rahul Sharma",
                    courseName = "ADCA (12 Months)",
                    docType = DocumentType.MARKSHEET,
                    title = "Semester 1 Marksheet & Evaluation",
                    issueDateMillis = System.currentTimeMillis() - (2L * 24 * 3600 * 1000),
                    isApproved = true,
                    statusNotes = "Verified by Examination Controller",
                    docPayload = "{\"totalMarks\":500,\"obtained\":448,\"percentage\":\"89.6%\",\"grade\":\"A+\"}"
                ),
                StudentDocument(
                    id = "doc-crt-001",
                    studentEnrollmentNo = "SCI-2026-0104",
                    studentName = "Rahul Sharma",
                    courseName = "ADCA (12 Months)",
                    docType = DocumentType.CERTIFICATE,
                    title = "Course Completion Certificate",
                    issueDateMillis = System.currentTimeMillis(),
                    isApproved = false,
                    statusNotes = "Course in progress. Certificate will be generated upon final semester exam completion.",
                    docPayload = ""
                )
            )
            db.documentDao().insertDocuments(docsStudent1)

            // Seed Active Notices
            val notices = listOf(
                Notice(
                    id = "ntc-001",
                    titleEn = "New Batches Starting for ADCA, DCA & Tally Prime",
                    titleHi = "एडीसीए, डीसीए एवं टैली प्राइम के नए बैच प्रारंभ",
                    descriptionEn = "Admissions are currently OPEN for the upcoming batch starting next Monday. Limited 15 seats per batch for 1:1 computer lab practice. Register today to avail early student discount.",
                    descriptionHi = "सोमवार से प्रारंभ होने वाले नए सत्र हेतु प्रवेश जारी हैं। 1:1 कंप्यूटर प्रैक्टिकल के लिए प्रत्येक बैच में केवल 15 सीटें उपलब्ध। शीघ्र पंजीकरण करें।",
                    category = NoticeCategory.ADMISSION,
                    publishDateMillis = System.currentTimeMillis() - (1L * 24 * 3600 * 1000),
                    isActive = true
                ),
                Notice(
                    id = "ntc-002",
                    titleEn = "Semester Practical Examination Schedule Released",
                    titleHi = "सत्र प्रायोगिक परीक्षा समय सारिणी जारी",
                    descriptionEn = "All students enrolled in Diploma courses (ADCA / DCA) can download their examination Admit Cards from the Student Portal. Practical exams will commence from 15th of next month.",
                    descriptionHi = "डिप्लोमा (ADCA / DCA) के सभी छात्र-छात्राएं अपना एडमिट कार्ड छात्र पोर्टल से डाउनलोड कर सकते हैं। प्रायोगिक परीक्षाएं आगामी 15 तारीख से शुरू होंगी।",
                    category = NoticeCategory.EXAM,
                    publishDateMillis = System.currentTimeMillis() - (3L * 24 * 3600 * 1000),
                    isActive = true
                ),
                Notice(
                    id = "ntc-003",
                    titleEn = "Free Career Workshop on Web Development & AI Tools",
                    titleHi = "वेब डेवलपमेंट एवं एआई टूल्स पर निःशुल्क करियर कार्यशाला",
                    descriptionEn = "Join our specialized 2-hour interactive masterclass on modern software career paths and freelancing opportunities this Saturday at 03:00 PM in Lab 1.",
                    descriptionHi = "इस शनिवार दोपहर 3:00 बजे लैब 1 में आधुनिक सॉफ्टवेयर करियर और फ्रीलांसिंग अवसरों पर निःशुल्क कार्यशाला का आयोजन।",
                    category = NoticeCategory.GENERAL,
                    publishDateMillis = System.currentTimeMillis() - (5L * 24 * 3600 * 1000),
                    isActive = true
                )
            )
            db.noticeDao().insertNotices(notices)
        }
    }
}
