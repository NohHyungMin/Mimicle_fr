package app.com.mimiclejp.data.push

import app.com.mimiclejp.api.ApiInterface
import javax.inject.Inject

class PushRepositoryImpl @Inject constructor(private val apiInterface: ApiInterface) :
    PushRepository {
    override suspend fun setPushInfo(
        param: HashMap<String, String>
    ) = apiInterface.setPushInfo(
        param["osType"]!!, param["versionCode"]!!,
        param["pushkey"]!!, param["uuid"]!!, param["memno"]!!)
}