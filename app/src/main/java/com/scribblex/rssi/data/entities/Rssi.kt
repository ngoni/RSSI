package com.scribblex.rssi.data.entities

import com.google.gson.annotations.SerializedName

data class Rssi(
    @SerializedName("strength")
    val strength: Long
)
