package com.applevelup.levepupgamerapp.domain.model

data class SessionState(
    val isLoggedIn: Boolean = false,
    val userId: Long? = null,
    val email: String? = null,
    val fullName: String? = null,
    val rememberMe: Boolean = false,
    val profileRole: String? = null,
    val isSuperAdmin: Boolean = false
)
