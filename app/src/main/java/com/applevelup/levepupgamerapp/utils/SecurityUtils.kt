package com.applevelup.levepupgamerapp.utils

import java.security.MessageDigest

object SecurityUtils {
    fun hashPassword(raw: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(raw.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString(separator = "") { byte ->
            "%02x".format(byte)
        }
    }
}
