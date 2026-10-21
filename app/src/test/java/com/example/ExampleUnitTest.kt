package com.example

import com.example.security.AdminSecurityManager
import com.example.ui.language.SrishtiStrings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun adminPassword_258036_isAuthorized() {
        // Must verify requested initial password 258036 securely
        assertTrue("Admin password 258036 should be verified as authorized", AdminSecurityManager.verifyAdminPassword("258036"))
        assertTrue("Trimmed admin password 258036 should also succeed", AdminSecurityManager.verifyAdminPassword(" 258036 "))
    }

    @Test
    fun adminPassword_invalid_isRejected() {
        // Any other password must fail
        assertFalse(AdminSecurityManager.verifyAdminPassword("123456"))
        assertFalse(AdminSecurityManager.verifyAdminPassword("admin"))
        assertFalse(AdminSecurityManager.verifyAdminPassword(""))
        assertFalse(AdminSecurityManager.verifyAdminPassword("   "))
    }

    @Test
    fun instituteName_spelledExact() {
        assertEquals("Srishti Computer Institute", SrishtiStrings.INSTITUTE_NAME)
    }
}
