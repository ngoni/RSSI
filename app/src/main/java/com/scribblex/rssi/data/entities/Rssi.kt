package com.scribblex.rssi.data.entities

import com.google.gson.annotations.SerializedName

data class Rssi(
    @SerializedName("ssid")
    val ssid: String,
    @SerializedName("strength")
    val strength: Int
)
