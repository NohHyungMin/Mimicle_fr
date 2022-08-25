package app.com.mimiclejp.data.push

import com.skydoves.sandwich.ApiResponse

interface PushRepository {
    suspend fun setPushInfo(
        param: HashMap<String, String>
    ): ApiResponse<PushInfo>
}