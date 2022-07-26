package app.com.mimiclejp.data.splash

import app.com.mimiclejp.api.ApiInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppMetaRepositoryImpl @Inject constructor(private val apiInterface: ApiInterface) :
    AppMetaRepository {
    override suspend fun getMeta(
        param: HashMap<String, String>
    ) = apiInterface.getMeta(param["osType"]!!, param["versionCode"]!!)

}