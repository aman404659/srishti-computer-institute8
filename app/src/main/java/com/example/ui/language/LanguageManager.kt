package com.example.ui.language

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class AppLanguage(val code: String, val displayName: String) {
    EN("en", "English"),
    HI("hi", "हिन्दी")
}

class LanguageState {
    var currentLanguage by mutableStateOf(AppLanguage.EN)
        private set

    fun switchLanguage(language: AppLanguage) {
        currentLanguage = language
    }

    fun toggle() {
        currentLanguage = if (currentLanguage == AppLanguage.EN) AppLanguage.HI else AppLanguage.EN
    }
}

val LocalAppLanguage = compositionLocalOf { LanguageState() }

object SrishtiStrings {
    const val INSTITUTE_NAME = "Srishti Computer Institute"
    const val OFFICIAL_INSTITUTE_NAME = "SRISHTI COMPUTER INSTITUTE"
    const val INSTITUTE_TAGLINE = "LEARN • PRACTICE • GROW"
    const val INSTITUTE_ADDRESS = "Bagdaudhi Kachhar Kothi, Mandhana, Kanpur Nagar, Uttar Pradesh, India"
    const val MOBILE_NUMBER = "8953994687"
    const val WHATSAPP_NUMBER = "8953994687"
    const val CALL_URI = "tel:8953994687"
    const val WHATSAPP_CHAT_URL = "https://wa.me/918953994687"
    const val WHATSAPP_GROUP_URL = "https://chat.whatsapp.com/L9Z8gxRL7QX3dJD3EEMBqO?s=cl&p=a&mlu=4&ilr=4&iam=2"
    const val TELEGRAM_CHANNEL_URL = "https://t.me/SrishtiComputerInstitute"
    const val GOOGLE_MAPS_LINK = "https://share.google/tSYX6LZlGeJInp8cv"
    const val WEBSITE_LINK = "https://sites.google.com/view/srishti-computer-institute/home"

    fun home(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Home"
        AppLanguage.HI -> "होम"
    }

    fun courses(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Courses"
        AppLanguage.HI -> "पाठ्यक्रम"
    }

    fun admission(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Admission"
        AppLanguage.HI -> "ऑनलाइन प्रवेश"
    }

    fun studentPortal(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Student Portal"
        AppLanguage.HI -> "छात्र पोर्टल"
    }

    fun adminPanel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Admin Panel"
        AppLanguage.HI -> "एडमिन पैनल"
    }

    fun aboutAndContact(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "About & Contact"
        AppLanguage.HI -> "परिचय एवं संपर्क"
    }

    fun notices(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Notice Board"
        AppLanguage.HI -> "सूचना पट्ट"
    }

    fun heroTagline(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Empowering Digital Careers with Practical Excellence"
        AppLanguage.HI -> "व्यावहारिक ज्ञान के साथ डिजिटल भविष्य का निर्माण"
    }

    fun heroDescription(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Join Srishti Computer Institute for certified job-oriented courses in Software, Accounting, Web Development, and Office Applications. State-of-the-art computer lab with dedicated individual systems."
        AppLanguage.HI -> "सृष्टि कंप्यूटर इंस्टीट्यूट में सॉफ्टवेयर, अकाउंटिंग, वेब डेवलपमेंट और बेसिक कंप्यूटर के जॉब-ओरिएंटेड कोर्सेज में प्रवेश लें। आधुनिक कंप्यूटर लैब और व्यक्तिगत सिस्टम प्रशिक्षण।"
    }

    fun applyNow(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Apply for Admission"
        AppLanguage.HI -> "अभी प्रवेश लें"
    }

    fun browseCourses(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Explore Courses"
        AppLanguage.HI -> "कोर्स देखें"
    }

    fun whatsappHelp(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "WhatsApp Support"
        AppLanguage.HI -> "व्हाट्सएप सहायता"
    }

    fun searchCourses(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Search course by name, code or skill..."
        AppLanguage.HI -> "कोर्स का नाम, कोड या स्किल खोजें..."
    }

    fun duration(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Duration"
        AppLanguage.HI -> "अवधि"
    }

    fun fees(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Total Fee"
        AppLanguage.HI -> "कुल शुल्क"
    }

    fun eligibility(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Eligibility"
        AppLanguage.HI -> "योग्यता"
    }

    fun syllabus(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Syllabus Modules"
        AppLanguage.HI -> "सिलेबस मॉड्यूल"
    }

    fun fullName(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Student Full Name"
        AppLanguage.HI -> "छात्र का पूरा नाम"
    }

    fun fatherName(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Father's / Guardian's Name"
        AppLanguage.HI -> "पिता / अभिभावक का नाम"
    }

    fun phoneNumber(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "10-Digit Mobile Number"
        AppLanguage.HI -> "10 अंकों का मोबाइल नंबर"
    }

    fun emailAddress(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Email Address"
        AppLanguage.HI -> "ईमेल पता"
    }

    fun selectCourse(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Select Desired Course"
        AppLanguage.HI -> "इच्छित कोर्स चुनें"
    }

    fun batchTiming(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Preferred Batch Timing"
        AppLanguage.HI -> "पसंदीदा बैच का समय"
    }

    fun qualification(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Highest Qualification"
        AppLanguage.HI -> "उच्चतम शैक्षणिक योग्यता"
    }

    fun residentialAddress(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Residential Address"
        AppLanguage.HI -> "वर्तमान पता"
    }

    fun submitApplication(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Submit Admission Form"
        AppLanguage.HI -> "फॉर्म सबमिट करें"
    }

    fun applicationStatus(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Application Status"
        AppLanguage.HI -> "आवेदन की स्थिति"
    }

    fun enrollmentNo(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Enrollment Number"
        AppLanguage.HI -> "नामांकन संख्या"
    }

    fun pendingAdminApproval(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Pending Admin Verification"
        AppLanguage.HI -> "एडमिन सत्यापन प्रक्रिया में"
    }

    fun approvedEnrolled(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Approved & Enrolled"
        AppLanguage.HI -> "स्वीकृत एवं नामांकित"
    }

    fun rejected(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Application Rejected"
        AppLanguage.HI -> "आवेदन अस्वीकृत"
    }

    fun documentVault(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Student Documents & Vault"
        AppLanguage.HI -> "छात्र दस्तावेज एवं प्रमाणपत्र"
    }

    fun idCard(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Student ID Card"
        AppLanguage.HI -> "छात्र पहचान पत्र (ID)"
    }

    fun feeReceipt(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Fee Payment Receipt"
        AppLanguage.HI -> "फीस रसीद"
    }

    fun enrollmentSlip(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Enrollment Slip"
        AppLanguage.HI -> "नामांकन पर्ची"
    }

    fun admitCard(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Examination Admit Card"
        AppLanguage.HI -> "परीक्षा प्रवेश पत्र (Admit Card)"
    }

    fun marksheet(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Academic Marksheet"
        AppLanguage.HI -> "अंकतालिका (Marksheet)"
    }

    fun certificate(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Course Completion Certificate"
        AppLanguage.HI -> "कोर्स पूर्णता प्रमाण-पत्र"
    }

    fun downloadDocument(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Download / Print"
        AppLanguage.HI -> "डाउनलोड / प्रिंट करें"
    }

    fun notYetIssued(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Document not yet issued or approved by Administrator"
        AppLanguage.HI -> "यह दस्तावेज अभी तक एडमिन द्वारा जारी या सत्यापित नहीं हुआ है"
    }
}
