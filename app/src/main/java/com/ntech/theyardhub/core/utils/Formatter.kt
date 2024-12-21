package com.ntech.theyardhub.core.utils

import android.net.Uri
import com.google.firebase.Timestamp
import java.net.URI
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

fun Number?.toRupiahFormat(useCurrencySymbol: Boolean = true): String {
    if (this == null)
        if (useCurrencySymbol)
            return "Rp "

    val formatRupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    val result =
        formatRupiah
            .format(this)
            .replace(",00", "")
            .replace("Rp", "Rp")

    return if (!useCurrencySymbol) result.replace("Rp", "") else result
}

fun formatFirebaseTimestampToDate(timestamp: Timestamp): String {
    // Convert Timestamp to Date
    val date = timestamp.toDate()
    // Define the desired format
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.getDefault())
    // Format the date
    return sdf.format(date)
}
