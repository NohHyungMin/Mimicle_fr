package app.com.mimiclees.data.push

import com.google.gson.annotations.SerializedName

data class PushInfoItem(
    @SerializedName("memno")
    val memno : String,
    @SerializedName("pushyn")
    val pushyn : String,
)