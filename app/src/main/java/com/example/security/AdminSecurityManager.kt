package com.example.security

import java.security.MessageDigest

object AdminSecurityManager {
    // Salt applied before cryptographic hashing
    private const val SECURITY_SALT = "srishti_salt_sci_2026"

    // Cryptographic one-way SHA-256 hash of initial authorized admin credentials
    // Plaintext password is never stored or exposed in code or database records
    private const val AUTHORIZED_ADMIN_HASH =
        "e53ed193ad074d3aef14211c6ef4c63545a9468f133d9382e1fbd4001f64558a"

    /**
     * Verifies provided password against cryptographic salted hash.
     * Uses constant-time comparison to prevent timing attacks.
     */
    fun verifyAdminPassword(candidate: String): Boolean {
        if (candidate.isBlank()) return false
        val computedHash = hashWithSalt(candidate.trim())
        return MessageDigest.isEqual(
            computedHash.toByteArray(Charsets.UTF_8),
            AUTHORIZED_ADMIN_HASH.toByteArray(Charsets.UTF_8)
        )
    }

    private fun hashWithSalt(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val saltedInput = "$SECURITY_SALT:$input"
        val bytes = md.digest(saltedInput.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
