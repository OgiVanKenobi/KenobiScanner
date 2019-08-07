package com.example.kenobiscanner.utils

import android.telephony.PhoneNumberUtils

class DataUtils {
    companion object {
        fun getScannedDataType(scannedData: String): ScannedDataType {
            return when {
                android.util.Patterns.WEB_URL.matcher(scannedData).matches() -> return ScannedDataType.URL
                android.util.Patterns.PHONE.matcher(scannedData).matches() -> ScannedDataType.PHONE_NUMBER
                android.util.Patterns.EMAIL_ADDRESS.matcher(scannedData).matches() -> ScannedDataType.EMAIL
                else -> return ScannedDataType.PLAIN_TEXT
            }
        }
    }

    enum class ScannedDataType(val value: String) {
        PLAIN_TEXT("Plain text"),
        URL("URL"),
        PHONE_NUMBER("Phone number"),
        EMAIL("Email"),
        GEO_POINT("Geo point"),
        DRIVER_LICENCE("Driver licence"),
        CALENDAR_EVENT("Calendar event")
    }
}