package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
import com.example.data.repository.InstituteRepository
import com.example.security.AdminSecurityManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppScreen {
    HOME,
    COURSES,
    ADMISSION,
    STUDENT_PORTAL,
    ADMIN_PANEL,
    ABOUT_CONTACT
}

enum class AdminTab {
    DASHBOARD,
    APPLICATIONS,
    STUDENTS,
    COURSES,
    PAYMENTS,
    DOCUMENTS,
    NOTICES
}

class InstituteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: InstituteRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = InstituteRepository(database)
        viewModelScope.launch(Dispatchers.IO) {
            repository.ensureCoursesPopulated()
        }
    }

    // Active Screen
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    // Courses & Filtering
    val allCourses: StateFlow<List<Course>> = repository.courses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _courseSearchQuery = MutableStateFlow("")
    val courseSearchQuery: StateFlow<String> = _courseSearchQuery.asStateFlow()

    private val _selectedCategoryFilter = MutableStateFlow("All")
    val selectedCategoryFilter: StateFlow<String> = _selectedCategoryFilter.asStateFlow()

    private val _selectedCredentialFilter = MutableStateFlow("All Credentials")
    val selectedCredentialFilter: StateFlow<String> = _selectedCredentialFilter.asStateFlow()

    fun setCourseSearchQuery(query: String) {
        _courseSearchQuery.value = query
    }

    fun setCategoryFilter(category: String) {
        _selectedCategoryFilter.value = category
    }

    fun setCredentialFilter(credential: String) {
        _selectedCredentialFilter.value = credential
    }

    private fun matchesCredential(nameEn: String, filter: String): Boolean {
        if (filter == "All Credentials" || filter == "All") return true
        val credType = when {
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
            else -> "Certificate"
        }
        return credType.equals(filter, ignoreCase = true)
    }

    val filteredCourses: StateFlow<List<Course>> = combine(
        allCourses,
        courseSearchQuery,
        selectedCategoryFilter,
        selectedCredentialFilter
    ) { courses, query, category, credential ->
        courses.filter { course ->
            val matchesCategory = category == "All" || course.category.equals(category, ignoreCase = true)
            val matchesCred = matchesCredential(course.nameEn, credential)
            val matchesQuery = query.isBlank() ||
                    course.nameEn.contains(query, ignoreCase = true) ||
                    course.nameHi.contains(query, ignoreCase = true) ||
                    course.code.contains(query, ignoreCase = true) ||
                    course.category.contains(query, ignoreCase = true) ||
                    course.syllabusSummary.contains(query, ignoreCase = true)
            matchesCategory && matchesCred && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Admissions
    val applications: StateFlow<List<AdmissionApplication>> = repository.applications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Online Admission Form State
    var formFullName = MutableStateFlow("")
    var formFatherName = MutableStateFlow("")
    var formPhone = MutableStateFlow("")
    var formEmail = MutableStateFlow("")
    var formDob = MutableStateFlow("")
    var formGender = MutableStateFlow("Male")
    var formQualification = MutableStateFlow("12th Pass")
    var formSelectedCourseId = MutableStateFlow("")
    var formSelectedCourseName = MutableStateFlow("")
    var formBatchTiming = MutableStateFlow("10:00 AM - 11:30 AM (Morning)")
    var formAddress = MutableStateFlow("")

    private val _isSubmittingAdmission = MutableStateFlow(false)
    val isSubmittingAdmission: StateFlow<Boolean> = _isSubmittingAdmission.asStateFlow()

    private val _formSubmissionResult = MutableStateFlow<String?>(null)
    val formSubmissionResult: StateFlow<String?> = _formSubmissionResult.asStateFlow()

    private val _formErrorMessage = MutableStateFlow<String?>(null)
    val formErrorMessage: StateFlow<String?> = _formErrorMessage.asStateFlow()

    fun selectCourseForAdmission(course: Course) {
        formSelectedCourseId.value = course.id
        formSelectedCourseName.value = course.nameEn
        _currentScreen.value = AppScreen.ADMISSION
    }

    fun submitAdmissionForm() {
        if (_isSubmittingAdmission.value) return // Prevent accidental duplicate submissions

        val name = formFullName.value.trim()
        val father = formFatherName.value.trim()
        val phone = formPhone.value.trim()
        val email = formEmail.value.trim()
        val dob = formDob.value.trim()
        val courseName = formSelectedCourseName.value.trim()
        val address = formAddress.value.trim()

        // Validation
        if (name.length < 2) {
            _formErrorMessage.value = "Please enter student's full name (at least 2 characters)"
            return
        }
        if (father.length < 2) {
            _formErrorMessage.value = "Please enter father's or guardian's name"
            return
        }
        if (!phone.matches(Regex("^[0-9]{10}$"))) {
            _formErrorMessage.value = "Please enter a valid 10-digit mobile number"
            return
        }
        if (email.isNotBlank() && !email.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))) {
            _formErrorMessage.value = "Please enter a valid email address or leave blank"
            return
        }
        if (courseName.isBlank()) {
            _formErrorMessage.value = "Please select a course for admission"
            return
        }
        if (dob.isBlank()) {
            _formErrorMessage.value = "Please enter student's date of birth"
            return
        }
        if (address.length < 4) {
            _formErrorMessage.value = "Please enter complete residential address"
            return
        }

        _isSubmittingAdmission.value = true
        _formErrorMessage.value = null

        viewModelScope.launch {
            try {
                val randomSeq = (1000..9999).random()
                val refNo = "SCI-ADM-2026-$randomSeq"
                val app = AdmissionApplication(
                    id = UUID.randomUUID().toString(),
                    referenceNo = refNo,
                    fullName = name,
                    fatherName = father,
                    phone = phone,
                    email = email,
                    dob = dob,
                    gender = formGender.value,
                    qualification = formQualification.value,
                    courseId = formSelectedCourseId.value.ifBlank { "crs-custom" },
                    courseName = courseName,
                    batchTiming = formBatchTiming.value,
                    address = address,
                    status = ApplicationStatus.PENDING,
                    appliedAtMillis = System.currentTimeMillis()
                )
                repository.submitAdmission(app)
                _formSubmissionResult.value = refNo
                _formErrorMessage.value = null

                // Reset form fields ONLY upon successful database persistence
                formFullName.value = ""
                formFatherName.value = ""
                formPhone.value = ""
                formEmail.value = ""
                formDob.value = ""
                formAddress.value = ""
            } catch (e: Exception) {
                _formErrorMessage.value = "Database write failed: ${e.localizedMessage ?: "Unknown error"}. Please retry."
                _formSubmissionResult.value = null
            } finally {
                _isSubmittingAdmission.value = false
            }
        }
    }

    fun clearSubmissionResult() {
        _formSubmissionResult.value = null
        _formErrorMessage.value = null
    }

    // Application Status Tracker
    private val _trackResult = MutableStateFlow<AdmissionApplication?>(null)
    val trackResult: StateFlow<AdmissionApplication?> = _trackResult.asStateFlow()
    private val _trackNotFound = MutableStateFlow(false)
    val trackNotFound: StateFlow<Boolean> = _trackNotFound.asStateFlow()

    fun trackApplication(refOrPhone: String) {
        if (refOrPhone.isBlank()) return
        viewModelScope.launch {
            val trimmed = refOrPhone.trim()
            val byRef = repository.getApplicationByRef(trimmed)
            if (byRef != null) {
                _trackResult.value = byRef
                _trackNotFound.value = false
            } else {
                val byPhoneList = repository.getApplicationsByPhone(trimmed)
                if (byPhoneList.isNotEmpty()) {
                    _trackResult.value = byPhoneList.first()
                    _trackNotFound.value = false
                } else {
                    _trackResult.value = null
                    _trackNotFound.value = true
                }
            }
        }
    }

    fun clearTrackResult() {
        _trackResult.value = null
        _trackNotFound.value = false
    }

    // Student Portal
    val allStudents: StateFlow<List<Student>> = repository.students
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _loggedInStudent = MutableStateFlow<Student?>(null)
    val loggedInStudent: StateFlow<Student?> = _loggedInStudent.asStateFlow()

    private val _studentDocuments = MutableStateFlow<List<StudentDocument>>(emptyList())
    val studentDocuments: StateFlow<List<StudentDocument>> = _studentDocuments.asStateFlow()

    private val _studentPayments = MutableStateFlow<List<FeePayment>>(emptyList())
    val studentPayments: StateFlow<List<FeePayment>> = _studentPayments.asStateFlow()

    private val _studentLoginError = MutableStateFlow<String?>(null)
    val studentLoginError: StateFlow<String?> = _studentLoginError.asStateFlow()

    fun loginStudent(query: String) {
        val q = query.trim()
        if (q.isBlank()) {
            _studentLoginError.value = "Enter Enrollment No or Registered Mobile"
            return
        }
        viewModelScope.launch {
            val student = repository.getStudentByEnrollmentNo(q)
                ?: repository.getStudentByPhone(q)
            if (student != null) {
                _loggedInStudent.value = student
                _studentLoginError.value = null
                // Load documents & payments
                repository.getDocumentsForStudent(student.enrollmentNo).collect { docs ->
                    _studentDocuments.value = docs
                }
            } else {
                _studentLoginError.value = "No student found with $q. Check details or contact Admin."
            }
        }
    }

    fun loginDemoStudent() {
        loginStudent("SCI-2026-0104")
    }

    fun logoutStudent() {
        _loggedInStudent.value = null
        _studentDocuments.value = emptyList()
        _studentPayments.value = emptyList()
        _studentLoginError.value = null
    }

    // Selected document preview in modal
    private val _selectedDocPreview = MutableStateFlow<StudentDocument?>(null)
    val selectedDocPreview: StateFlow<StudentDocument?> = _selectedDocPreview.asStateFlow()

    fun openDocumentPreview(doc: StudentDocument) {
        _selectedDocPreview.value = doc
    }

    fun closeDocumentPreview() {
        _selectedDocPreview.value = null
    }

    // Admin Panel & Authentication
    private val _isAdminAuthenticated = MutableStateFlow(false)
    val isAdminAuthenticated: StateFlow<Boolean> = _isAdminAuthenticated.asStateFlow()

    private val _adminTab = MutableStateFlow(AdminTab.DASHBOARD)
    val adminTab: StateFlow<AdminTab> = _adminTab.asStateFlow()

    private val _adminPinInput = MutableStateFlow("")
    val adminPinInput: StateFlow<String> = _adminPinInput.asStateFlow()

    private val _adminLoginError = MutableStateFlow<String?>(null)
    val adminLoginError: StateFlow<String?> = _adminLoginError.asStateFlow()

    fun setAdminPin(pin: String) {
        _adminPinInput.value = pin
    }

    fun loginAdmin() {
        val pin = _adminPinInput.value.trim()
        if (AdminSecurityManager.verifyAdminPassword(pin)) {
            _isAdminAuthenticated.value = true
            _adminLoginError.value = null
            _adminPinInput.value = ""
        } else {
            _adminLoginError.value = "Invalid administrator password. Access denied."
        }
    }

    fun logoutAdmin() {
        _isAdminAuthenticated.value = false
        _adminTab.value = AdminTab.DASHBOARD
        _selectedAppForDetail.value = null
    }

    fun setAdminTab(tab: AdminTab) {
        _adminTab.value = tab
    }

    // Admin Admission Search & Filters
    private val _adminAppSearchQuery = MutableStateFlow("")
    val adminAppSearchQuery: StateFlow<String> = _adminAppSearchQuery.asStateFlow()

    private val _adminAppStatusFilter = MutableStateFlow("All")
    val adminAppStatusFilter: StateFlow<String> = _adminAppStatusFilter.asStateFlow()

    fun setAdminAppSearchQuery(query: String) {
        _adminAppSearchQuery.value = query
    }

    fun setAdminAppStatusFilter(filter: String) {
        _adminAppStatusFilter.value = filter
    }

    val filteredApplications: StateFlow<List<AdmissionApplication>> = combine(
        applications,
        adminAppSearchQuery,
        adminAppStatusFilter
    ) { apps, query, filter ->
        apps.filter { app ->
            val matchesFilter = when (filter.lowercase()) {
                "all" -> true
                "pending" -> app.status == ApplicationStatus.PENDING
                "approved" -> app.status == ApplicationStatus.APPROVED
                "rejected" -> app.status == ApplicationStatus.REJECTED
                else -> true
            }
            val matchesQuery = query.isBlank() ||
                    app.fullName.contains(query, ignoreCase = true) ||
                    app.referenceNo.contains(query, ignoreCase = true) ||
                    app.phone.contains(query, ignoreCase = true) ||
                    app.courseName.contains(query, ignoreCase = true) ||
                    app.fatherName.contains(query, ignoreCase = true)
            matchesFilter && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedAppForDetail = MutableStateFlow<AdmissionApplication?>(null)
    val selectedAppForDetail: StateFlow<AdmissionApplication?> = _selectedAppForDetail.asStateFlow()

    fun openApplicationDetail(app: AdmissionApplication) {
        _selectedAppForDetail.value = app
    }

    fun closeApplicationDetail() {
        _selectedAppForDetail.value = null
    }

    fun updateApplicationStatus(app: AdmissionApplication, status: ApplicationStatus, remarks: String = "") {
        viewModelScope.launch {
            if (status == ApplicationStatus.APPROVED && app.status != ApplicationStatus.APPROVED) {
                repository.approveApplication(app)
            } else {
                repository.updateApplicationStatus(app, status, remarks)
            }
            if (_selectedAppForDetail.value?.id == app.id) {
                _selectedAppForDetail.value = repository.getApplicationById(app.id)
            }
        }
    }

    // Admin Actions
    fun approveAdmission(app: AdmissionApplication) {
        viewModelScope.launch {
            repository.approveApplication(app)
            if (_selectedAppForDetail.value?.id == app.id) {
                _selectedAppForDetail.value = repository.getApplicationById(app.id)
            }
        }
    }

    fun rejectAdmission(app: AdmissionApplication, reason: String) {
        viewModelScope.launch {
            repository.rejectApplication(app, reason)
            if (_selectedAppForDetail.value?.id == app.id) {
                _selectedAppForDetail.value = repository.getApplicationById(app.id)
            }
        }
    }

    fun recordStudentPayment(
        student: Student,
        amount: Int,
        mode: String,
        ref: String,
        installmentType: String
    ) {
        viewModelScope.launch {
            repository.recordPayment(student, amount, mode, ref, installmentType)
            if (_loggedInStudent.value?.enrollmentNo == student.enrollmentNo) {
                _loggedInStudent.value = repository.getStudentByEnrollmentNo(student.enrollmentNo)
            }
        }
    }

    fun updateDocumentApproval(docId: String, isApproved: Boolean, statusNotes: String, payload: String) {
        viewModelScope.launch {
            repository.updateDocumentApproval(docId, isApproved, statusNotes, payload)
        }
    }

    fun addNewCourse(course: Course) {
        viewModelScope.launch {
            repository.addCourse(course)
        }
    }

    fun updateExistingCourse(course: Course) {
        viewModelScope.launch {
            repository.updateCourse(course)
        }
    }

    fun deleteCourse(course: Course) {
        viewModelScope.launch {
            repository.deleteCourse(course)
        }
    }

    // Notices
    val activeNotices: StateFlow<List<Notice>> = repository.activeNotices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotices: StateFlow<List<Notice>> = repository.allNotices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun publishNotice(
        titleEn: String,
        titleHi: String,
        descEn: String,
        descHi: String,
        category: NoticeCategory
    ) {
        viewModelScope.launch {
            repository.addNotice(titleEn, titleHi, descEn, descHi, category)
        }
    }

    fun deleteNotice(notice: Notice) {
        viewModelScope.launch {
            repository.deleteNotice(notice)
        }
    }

    val allPayments: StateFlow<List<FeePayment>> = repository.payments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allDocuments: StateFlow<List<StudentDocument>> = repository.documents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
