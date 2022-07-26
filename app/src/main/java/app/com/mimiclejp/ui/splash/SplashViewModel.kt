package app.com.mimiclejp.ui.splash

import androidx.lifecycle.*
import app.com.mimiclejp.MimicleApplication
import app.com.mimiclejp.data.push.PushInfo
import app.com.mimiclejp.data.push.PushRepository
import app.com.mimiclejp.data.splash.AppMetaData
import app.com.mimiclejp.data.splash.AppMetaRepository
import app.com.mimiclejp.util.MapUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val pushRepository: PushRepository,
    private val appMetaRepository: AppMetaRepository) : ViewModel(){

    private val _pushInfo = MutableLiveData<PushInfo>()
    val pushInfo: LiveData<PushInfo>
        get() = _pushInfo

    private val _appMetaData = MutableLiveData<AppMetaData>()
    val appMetaData: LiveData<AppMetaData>
        get() = _appMetaData

    fun setPushInfo(param: HashMap<String, String>) = viewModelScope.launch(Dispatchers.IO){
        if(MapUtils.checkNetworkState(MimicleApplication.ApplicationContext())) {
            val responseData = pushRepository.setPushInfo(param)

            withContext(Dispatchers.Main) {
                _pushInfo.value = responseData
            }
        }
    }

    fun getAppMeta(param: HashMap<String, String>) = viewModelScope.launch(Dispatchers.IO){
        if(MapUtils.checkNetworkState(MimicleApplication.ApplicationContext())) {
            val responseData = appMetaRepository.getMeta(param)

            withContext(Dispatchers.Main) {
                _appMetaData.value = responseData
            }
        }
    }
}