package com.ko.keyora10.utils

import java.security.MessageDigest

object HashUtils {

    fun sha256(text: String): String {

        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(text.toByteArray())

        return bytes.joinToString("") {
            "%02x".format(it)
        }

    }

}