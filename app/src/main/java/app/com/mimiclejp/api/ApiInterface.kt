package app.com.mimiclejp.api

import app.com.mimiclejp.data.push.PushInfo
import app.com.mimiclejp.data.splash.AppMetaData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiInterface {
//    @FormUrlEncoded
//    @POST("/api/users")
//    fun requestList(
//        @FieldMap param: HashMap<String, String>
//    ) : Call<ArrayList<AppMetaData>>

//    @GET("/api/version")
//    fun getVersion() : Call<AppMetaData>

    //앱 정보 받기
    @FormUrlEncoded
    @POST("app/com/appmeta.php")
    suspend fun getMeta(
        @Field("ostype") osType : String,
        @Field("vcode") versionCode : String
    ) : AppMetaData

    //푸시 정보 전송
    @FormUrlEncoded
    @POST("app/com/set-push-info.php")
    suspend fun setPushInfo(
        @Field("ostype") osType : String,
        @Field("vcode") versionCode : String,
        @Field("pushkey") pushKey : String,
        @Field("uuid") uuid : String?,
        @Field("memno") memNo : String
    ) : PushInfo

    companion object {
        private const val BASE_URL = "https://enc.mimicle.kr/"

        fun create(): ApiInterface {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        }
    }
}