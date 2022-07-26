package app.com.mimiclejp.data.splash


interface AppMetaRepository {
    suspend fun getMeta(
        param: HashMap<String, String>
    ):AppMetaData
}