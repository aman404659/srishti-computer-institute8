package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AdmissionApplication
import com.example.data.model.ApplicationStatus
import com.example.data.model.Course
import com.example.data.model.FeePayment
import com.example.data.model.Notice
import com.example.data.model.Student
import com.example.data.model.StudentDocument
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses ORDER BY isPopular DESC, nameEn ASC")
    fun getAllCoursesFlow(): Flow<List<Course>>

    @Query("SELECT * FROM courses WHERE id = :id")
    suspend fun getCourseById(id: String): Course?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<Course>)

    @Update
    suspend fun updateCourse(course: Course)

    @Delete
    suspend fun deleteCourse(course: Course)

    @Query("SELECT COUNT(*) FROM courses")
    suspend fun getCourseCount(): Int
}

@Dao
interface AdmissionDao {
    @Query("SELECT * FROM admission_applications ORDER BY appliedAtMillis DESC")
    fun getAllApplicationsFlow(): Flow<List<AdmissionApplication>>

    @Query("SELECT * FROM admission_applications WHERE referenceNo = :refNo")
    suspend fun getApplicationByRef(refNo: String): AdmissionApplication?

    @Query("SELECT * FROM admission_applications WHERE phone = :phone ORDER BY appliedAtMillis DESC")
    suspend fun getApplicationsByPhone(phone: String): List<AdmissionApplication>

    @Query("SELECT * FROM admission_applications WHERE id = :id")
    suspend fun getApplicationById(id: String): AdmissionApplication?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(app: AdmissionApplication)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplications(apps: List<AdmissionApplication>)

    @Update
    suspend fun updateApplication(app: AdmissionApplication)

    @Query("UPDATE admission_applications SET status = :status, adminRemarks = :remarks WHERE id = :id")
    suspend fun updateStatus(id: String, status: ApplicationStatus, remarks: String)

    @Delete
    suspend fun deleteApplication(app: AdmissionApplication)
}

@Dao
interface StudentDao {
    @Query("SELECT * FROM students ORDER BY enrolledAtMillis DESC")
    fun getAllStudentsFlow(): Flow<List<Student>>

    @Query("SELECT * FROM students WHERE enrollmentNo = :enrollmentNo")
    suspend fun getStudentByEnrollmentNo(enrollmentNo: String): Student?

    @Query("SELECT * FROM students WHERE phone = :phone")
    suspend fun getStudentByPhone(phone: String): Student?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudents(students: List<Student>)

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("SELECT COUNT(*) FROM students")
    suspend fun getStudentCount(): Int
}

@Dao
interface PaymentDao {
    @Query("SELECT * FROM fee_payments ORDER BY paymentDateMillis DESC")
    fun getAllPaymentsFlow(): Flow<List<FeePayment>>

    @Query("SELECT * FROM fee_payments WHERE studentEnrollmentNo = :enrollmentNo ORDER BY paymentDateMillis DESC")
    fun getPaymentsForStudentFlow(enrollmentNo: String): Flow<List<FeePayment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: FeePayment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayments(payments: List<FeePayment>)
}

@Dao
interface DocumentDao {
    @Query("SELECT * FROM student_documents ORDER BY issueDateMillis DESC")
    fun getAllDocumentsFlow(): Flow<List<StudentDocument>>

    @Query("SELECT * FROM student_documents WHERE studentEnrollmentNo = :enrollmentNo ORDER BY docType ASC")
    fun getDocumentsForStudentFlow(enrollmentNo: String): Flow<List<StudentDocument>>

    @Query("SELECT * FROM student_documents WHERE id = :id")
    suspend fun getDocumentById(id: String): StudentDocument?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(doc: StudentDocument)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocuments(docs: List<StudentDocument>)

    @Update
    suspend fun updateDocument(doc: StudentDocument)

    @Delete
    suspend fun deleteDocument(doc: StudentDocument)
}

@Dao
interface NoticeDao {
    @Query("SELECT * FROM notices WHERE isActive = 1 ORDER BY publishDateMillis DESC")
    fun getActiveNoticesFlow(): Flow<List<Notice>>

    @Query("SELECT * FROM notices ORDER BY publishDateMillis DESC")
    fun getAllNoticesFlow(): Flow<List<Notice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotice(notice: Notice)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotices(notices: List<Notice>)

    @Delete
    suspend fun deleteNotice(notice: Notice)
}
