package app.com.mimiclejp.data.push

interface PushRepository {
    suspend fun setPushInfo(
        param: HashMap<String, String>
    ): PushInfo
}