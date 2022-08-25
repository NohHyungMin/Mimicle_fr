package app.com.mimiclejp.ui.splash

import androidx.lifecycle.*
import app.com.mimiclejp.MimicleApplication
import app.com.mimiclejp.data.push.PushInfo
import app.com.mimiclejp.data.push.PushRepository
import app.com.mimiclejp.data.splash.AppMetaData
import app.com.mimiclejp.data.splash.AppMetaRepository
import app.com.mimiclejp.util.MapUtils
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val pushRepository: PushRepository,
    private val appMetaRepository: AppMetaRepository) : ViewModel(){

    private val _pushInfo = MutableLiveData<PushInfo?>()
    val pushInfo: MutableLiveData<PushInfo?>
        get() = _pushInfo

    private val _appMetaData = MutableLiveData<AppMetaData?>()
    val appMetaData: MutableLiveData<AppMetaData?>
        get() = _appMetaData

    private val _networkError = MutableLiveData<Boolean?>()
    val networkError: MutableLiveData<Boolean?>
        get() = _networkError

    fun setPushInfo(param: HashMap<String, String>) = viewModelScope.launch(Dispatchers.IO){
//        if(MapUtils.checkNetworkState(MimicleApplication.ApplicationContext())) {
        val responseData = pushRepository.setPushInfo(param)

        withContext(Dispatchers.Main) {
            responseData.onSuccess {
                _pushInfo.value = data
            }.onError {
                _networkError.value = true
            }.onException {
                _networkError.value = true
            }.onFailure {
                _networkError.value = true
            }
        }
//        }
    }

    fun getAppMeta(param: HashMap<String, String>) = viewModelScope.launch(Dispatchers.IO){
//        if(MapUtils.checkNetworkState(MimicleApplication.ApplicationContext())) {
        val responseData = appMetaRepository.getMeta(param)

        withContext(Dispatchers.Main) {
            responseData.onSuccess {
                _appMetaData.value = data
            }.onError {
                _networkError.value = true
            }.onException {
                _networkError.value = true
            }.onFailure {
                _networkError.value = true
            }
        }
//        }
    }
}