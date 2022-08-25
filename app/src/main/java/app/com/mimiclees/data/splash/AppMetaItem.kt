package app.com.mimiclees.data.splash

import com.google.gson.annotations.SerializedName

data class AppMetaItem(
    @SerializedName("vname")
    val vname : String,
    @SerializedName("vcode")
    val vcode : String,
    @SerializedName("forcedyn")
    val forcedyn: String,
    @SerializedName("strupdate")
    val strupdate: String,
    @SerializedName("mainurl")
    val mainurl: String
)