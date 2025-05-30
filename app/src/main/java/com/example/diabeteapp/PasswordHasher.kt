package com.example.diabeteapp

import at.favre.lib.crypto.bcrypt.BCrypt

object PasswordHasher {
    private const val BCRYPT_COST = 12 // Coût du hash (entre 4 et 31)

    fun hashPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(BCRYPT_COST, password.toCharArray())
    }

    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified
    }
}