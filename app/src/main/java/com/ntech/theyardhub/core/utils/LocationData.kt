package com.ntech.theyardhub.core.utils

object LocationData {
    val provinces = listOf(
        "Aceh", "Bali", "Banten", "Bengkulu", "DI Yogyakarta", "DKI Jakarta",
        "Gorontalo", "Jambi", "Jawa Barat", "Jawa Tengah", "Jawa Timur",
        "Kalimantan Barat", "Kalimantan Selatan", "Kalimantan Tengah",
        "Kalimantan Timur", "Kalimantan Utara", "Kepulauan Bangka Belitung",
        "Kepulauan Riau", "Lampung", "Maluku", "Maluku Utara",
        "Nusa Tenggara Barat", "Nusa Tenggara Timur", "Papua",
        "Papua Barat", "Riau", "Sulawesi Barat", "Sulawesi Selatan",
        "Sulawesi Tengah", "Sulawesi Tenggara", "Sulawesi Utara",
        "Sumatera Barat", "Sumatera Selatan", "Sumatera Utara"
    )

    val citiesByProvince = mapOf(
        "Jawa Timur" to listOf("Malang", "Surabaya", "Batu", "Sidoarjo", "Gresik", "Kediri", "Madiun", "Mojokerto", "Pasuruan", "Probolinggo", "Blitar"),
        "Jawa Tengah" to listOf("Semarang", "Surakarta", "Magelang", "Pekalongan", "Salatiga", "Tegal"),
        "Jawa Barat" to listOf("Bandung", "Bekasi", "Bogor", "Cimahi", "Cirebon", "Depok", "Sukabumi", "Tasikmalaya", "Banjar"),
        "DKI Jakarta" to listOf("Jakarta Pusat", "Jakarta Utara", "Jakarta Barat", "Jakarta Selatan", "Jakarta Timur"),
        "DI Yogyakarta" to listOf("Yogyakarta", "Sleman", "Bantul", "Kulon Progo", "Gunungkidul"),
        "Bali" to listOf("Denpasar", "Badung", "Gianyar", "Tabanan", "Buleleng"),
        "Sumatera Utara" to listOf("Medan", "Binjai", "Pematangsiantar", "Tanjungbalai", "Tebing Tinggi"),
        "Sumatera Selatan" to listOf("Palembang", "Lubuklinggau", "Pagar Alam", "Prabumulih"),
        "Sulawesi Selatan" to listOf("Makassar", "Palopo", "Parepare"),
        "Kalimantan Timur" to listOf("Samarinda", "Balikpapan", "Bontang"),
        "Kalimantan Barat" to listOf("Pontianak", "Singkawang")
        // Add more as needed
    )
}
