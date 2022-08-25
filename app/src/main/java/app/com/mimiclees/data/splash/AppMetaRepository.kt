package app.com.mimiclees.data.splash

import com.skydoves.sandwich.ApiResponse


interface AppMetaRepository {
    suspend fun getMeta(
        param: HashMap<String, String>
    ): ApiResponse<AppMetaData>
}