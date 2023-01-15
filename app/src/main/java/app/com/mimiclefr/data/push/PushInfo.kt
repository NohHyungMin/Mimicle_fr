package app.com.mimiclefr.data.push

import com.google.gson.annotations.SerializedName

data class PushInfo(
    @SerializedName("result")
    val result: String,
    @SerializedName("memno")
    val memno : String,
    @SerializedName("pushyn")
    val pushyn : String,
    @SerializedName("PushInfoItem")
    val data: PushInfoItem
)

