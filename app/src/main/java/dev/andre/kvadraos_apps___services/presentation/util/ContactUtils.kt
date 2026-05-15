package dev.andre.kvadraos_apps___services.presentation.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import dev.andre.kvadraos_apps___services.R
import androidx.core.net.toUri

@SuppressLint("QueryPermissionsNeeded")
fun launchDialIntent(context: Context, phone: String) {
    val trimmed = phone.trim()
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = "tel:$trimmed".toUri()
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        showToast(context, context.getString(R.string.error_no_dial_app))
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message.trim(), Toast.LENGTH_SHORT).show()
}

fun normalizePhone(phone: String): String {
    return phone
        .replace("\\s".toRegex(), "")
        .replace("-", "")
        .replace("(", "")
        .replace(")", "")
}

fun buildInitials(name: String): String = name.trim()
    .split(" ")
    .filter { it.isNotEmpty() }
    .take(2)
    .joinToString("") { it.first().uppercaseChar().toString() }
