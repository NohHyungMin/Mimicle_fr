package app.com.mimiclejp.data.splash

import com.google.gson.annotations.SerializedName

data class AppMetaData(
    @SerializedName("result")
    val result: String,
    @SerializedName("data")
    val data: AppMetaItem
)

