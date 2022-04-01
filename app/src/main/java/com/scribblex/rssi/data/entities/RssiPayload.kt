package com.scribblex.rssi.data.entities

import com.google.gson.annotations.SerializedName

data class RssiPayload(
    @SerializedName("RssiPayload")
    val payload: List<Rssi>
)
