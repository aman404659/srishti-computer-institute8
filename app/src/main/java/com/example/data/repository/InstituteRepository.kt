package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.AdmissionApplication
import com.example.data.model.ApplicationStatus
import com.example.data.model.Course
import com.example.data.model.DocumentType
import com.example.data.model.FeePayment
import com.example.data.model.Notice
import com.example.data.model.NoticeCategory
import com.example.data.model.Student
import com.example.data.model.StudentDocument
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class InstituteRepository(private val database: AppDatabase) {

    // Courses
    val courses: Flow<List<Course>> = database.courseDao().getAllCoursesFlow()

    suspend fun getCourseById(id: String): Course? = database.courseDao().getCourseById(id)

    suspend fun addCourse(course: Course) {
        database.courseDao().insertCourse(course)
    }

    suspend fun updateCourse(course: Course) {
        database.courseDao().updateCourse(course)
    }

    suspend fun deleteCourse(course: Course) {
        database.courseDao().deleteCourse(course)
    }

    suspend fun ensureCoursesPopulated() {
        // Upsert all courses to synchronize latest official fees and PDF costs
        database.courseDao().insertCourses(com.example.data.local.InitialCoursesData.allCourses)
    }

    // Admissions
    val applications: Flow<List<AdmissionApplication>> = database.admissionDao().getAllApplicationsFlow()

    suspend fun submitAdmission(app: AdmissionApplication): String {
        database.admissionDao().insertApplication(app)
        return app.referenceNo
    }

    suspend fun getApplicationByRef(refNo: String): AdmissionApplication? =
        database.admissionDao().getApplicationByRef(refNo)

    suspend fun getApplicationsByPhone(phone: String): List<AdmissionApplication> =
        database.admissionDao().getApplicationsByPhone(phone)

    suspend fun getApplicationById(id: String): AdmissionApplication? =
        database.admissionDao().getApplicationById(id)

    suspend fun updateApplicationStatus(application: AdmissionApplication, status: ApplicationStatus, remarks: String = "") {
        val updated = application.copy(
            status = status,
            adminRemarks = remarks.ifBlank { application.adminRemarks }
        )
        database.admissionDao().updateApplication(updated)
    }

    suspend fun deleteApplication(application: AdmissionApplication) {
        database.admissionDao().deleteApplication(application)
    }

    suspend fun approveApplication(application: AdmissionApplication): Student {
        val studentCount = database.studentDao().getStudentCount()
        val nextSeq = 100 + studentCount + 1
        val enrollmentNo = "SCI-2026-0$nextSeq"

        val course = database.courseDao().getCourseById(application.courseId)
        val courseFee = course?.fee ?: 10000

        val newStudent = Student(
            id = UUID.randomUUID().toString(),
            enrollmentNo = enrollmentNo,
            fullName = application.fullName,
            fatherName = application.fatherName,
            phone = application.phone,
            email = application.email,
            courseId = application.courseId,
            courseName = application.courseName,
            batchTiming = application.batchTiming,
            enrolledAtMillis = System.currentTimeMillis(),
            totalFee = courseFee,
            paidFee = 0,
            isCompleted = false
        )
        database.studentDao().insertStudent(newStudent)

        // Update application record
        val updatedApp = application.copy(
            status = ApplicationStatus.APPROVED,
            adminRemarks = "Approved. Enrolled with No: $enrollmentNo"
        )
        database.admissionDao().updateApplication(updatedApp)

        // Seed initial documents in student vault
        val initialDocs = listOf(
            StudentDocument(
                id = UUID.randomUUID().toString(),
                studentEnrollmentNo = enrollmentNo,
                studentName = newStudent.fullName,
                courseName = newStudent.courseName,
                docType = DocumentType.ENROLLMENT_SLIP,
                title = "Official Enrollment Slip",
                issueDateMillis = System.currentTimeMillis(),
                isApproved = true,
                statusNotes = "Admission officially confirmed at Srishti Computer Institute",
                docPayload = "{\"batch\":\"${newStudent.batchTiming}\",\"enrollmentNo\":\"$enrollmentNo\"}"
            ),
            StudentDocument(
                id = UUID.randomUUID().toString(),
                studentEnrollmentNo = enrollmentNo,
                studentName = newStudent.fullName,
                courseName = newStudent.courseName,
                docType = DocumentType.ID_CARD,
                title = "Student Identity Card",
                issueDateMillis = System.currentTimeMillis(),
                isApproved = true,
                statusNotes = "Official Identity Card Issued",
                docPayload = "{\"validTill\":\"March 2027\",\"center\":\"Main Campus\"}"
            ),
            StudentDocument(
                id = UUID.randomUUID().toString(),
                studentEnrollmentNo = enrollmentNo,
                studentName = newStudent.fullName,
                courseName = newStudent.courseName,
                docType = DocumentType.ADMIT_CARD,
                title = "Semester Examination Admit Card",
                issueDateMillis = System.currentTimeMillis(),
                isApproved = false,
                statusNotes = "Will be generated when examination schedule is finalized.",
                docPayload = ""
            ),
            StudentDocument(
                id = UUID.randomUUID().toString(),
                studentEnrollmentNo = enrollmentNo,
                studentName = newStudent.fullName,
                courseName = newStudent.courseName,
                docType = DocumentType.MARKSHEET,
                title = "Academic Marksheet & Evaluation",
                issueDateMillis = System.currentTimeMillis(),
                isApproved = false,
                statusNotes = "Awaiting examination completion.",
                docPayload = ""
            ),
            StudentDocument(
                id = UUID.randomUUID().toString(),
                studentEnrollmentNo = enrollmentNo,
                studentName = newStudent.fullName,
                courseName = newStudent.courseName,
                docType = DocumentType.CERTIFICATE,
                title = "Course Completion Certificate",
                issueDateMillis = System.currentTimeMillis(),
                isApproved = false,
                statusNotes = "Issued upon full course and project completion.",
                docPayload = ""
            )
        )
        database.documentDao().insertDocuments(initialDocs)

        return newStudent
    }

    suspend fun rejectApplication(application: AdmissionApplication, remarks: String) {
        val updatedApp = application.copy(
            status = ApplicationStatus.REJECTED,
            adminRemarks = remarks.ifBlank { "Application declined by administration." }
        )
        database.admissionDao().updateApplication(updatedApp)
    }

    // Students
    val students: Flow<List<Student>> = database.studentDao().getAllStudentsFlow()

    suspend fun getStudentByEnrollmentNo(enrollmentNo: String): Student? =
        database.studentDao().getStudentByEnrollmentNo(enrollmentNo)

    suspend fun getStudentByPhone(phone: String): Student? =
        database.studentDao().getStudentByPhone(phone)

    suspend fun updateStudent(student: Student) {
        database.studentDao().updateStudent(student)
    }

    suspend fun deleteStudent(student: Student) {
        database.studentDao().deleteStudent(student)
    }

    // Payments
    val payments: Flow<List<FeePayment>> = database.paymentDao().getAllPaymentsFlow()

    fun getPaymentsForStudent(enrollmentNo: String): Flow<List<FeePayment>> =
        database.paymentDao().getPaymentsForStudentFlow(enrollmentNo)

    suspend fun recordPayment(
        student: Student,
        amount: Int,
        mode: String,
        transactionRef: String,
        installmentType: String
    ): FeePayment {
        val receiptSeq = (1000..9999).random()
        val receiptNo = "SCI-RCP-2026-$receiptSeq"

        val payment = FeePayment(
            id = UUID.randomUUID().toString(),
            receiptNo = receiptNo,
            studentEnrollmentNo = student.enrollmentNo,
            studentName = student.fullName,
            courseName = student.courseName,
            amount = amount,
            paymentDateMillis = System.currentTimeMillis(),
            paymentMode = mode,
            transactionRef = transactionRef,
            installmentType = installmentType
        )
        database.paymentDao().insertPayment(payment)

        // Update student's paid fee
        val updatedPaid = student.paidFee + amount
        val updatedStudent = student.copy(paidFee = updatedPaid)
        database.studentDao().updateStudent(updatedStudent)

        // Add fee receipt to documents
        val docReceipt = StudentDocument(
            id = UUID.randomUUID().toString(),
            studentEnrollmentNo = student.enrollmentNo,
            studentName = student.fullName,
            courseName = student.courseName,
            docType = DocumentType.FEE_RECEIPT,
            title = "Fee Receipt ($installmentType)",
            issueDateMillis = System.currentTimeMillis(),
            isApproved = true,
            statusNotes = "Payment verified. Receipt #$receiptNo",
            docPayload = "{\"receiptNo\":\"$receiptNo\",\"amount\":$amount,\"mode\":\"$mode\",\"balance\":${student.totalFee - updatedPaid}}"
        )
        database.documentDao().insertDocument(docReceipt)

        return payment
    }

    // Documents
    val documents: Flow<List<StudentDocument>> = database.documentDao().getAllDocumentsFlow()

    fun getDocumentsForStudent(enrollmentNo: String): Flow<List<StudentDocument>> =
        database.documentDao().getDocumentsForStudentFlow(enrollmentNo)

    suspend fun updateDocumentApproval(
        docId: String,
        isApproved: Boolean,
        statusNotes: String,
        payload: String
    ) {
        val existing = database.documentDao().getDocumentById(docId)
        if (existing != null) {
            val updated = existing.copy(
                isApproved = isApproved,
                statusNotes = statusNotes,
                docPayload = payload.ifBlank { existing.docPayload },
                issueDateMillis = System.currentTimeMillis()
            )
            database.documentDao().updateDocument(updated)
        }
    }

    suspend fun issueNewDocument(doc: StudentDocument) {
        database.documentDao().insertDocument(doc)
    }

    suspend fun deleteDocument(doc: StudentDocument) {
        database.documentDao().deleteDocument(doc)
    }

    // Notices
    val activeNotices: Flow<List<Notice>> = database.noticeDao().getActiveNoticesFlow()
    val allNotices: Flow<List<Notice>> = database.noticeDao().getAllNoticesFlow()

    suspend fun addNotice(
        titleEn: String,
        titleHi: String,
        descriptionEn: String,
        descriptionHi: String,
        category: NoticeCategory
    ) {
        val notice = Notice(
            id = UUID.randomUUID().toString(),
            titleEn = titleEn,
            titleHi = titleHi,
            descriptionEn = descriptionEn,
            descriptionHi = descriptionHi,
            category = category,
            publishDateMillis = System.currentTimeMillis(),
            isActive = true
        )
        database.noticeDao().insertNotice(notice)
    }

    suspend fun deleteNotice(notice: Notice) {
        database.noticeDao().deleteNotice(notice)
    }
}
