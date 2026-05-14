package dev.andre.kvadraos_apps___services.presentation.util

import android.content.Context
import android.widget.Toast

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message.trim(), Toast.LENGTH_SHORT).show()
}

fun buildInitials(name: String): String = name.trim()
    .split(" ")
    .filter { it.isNotEmpty() }
    .take(2)
    .joinToString("") { it.first().uppercaseChar().toString() }

fun normalizePhone(phone: String): String {
    return phone
        .replace("\\s".toRegex(), "")
        .replace("-", "")
        .replace("(", "")
        .replace(")", "")
}