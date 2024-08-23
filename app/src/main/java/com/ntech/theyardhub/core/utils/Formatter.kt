package com.ntech.theyardhub.core.utils

import java.text.NumberFormat
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