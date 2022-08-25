package app.com.mimiclees.ui.webview

import androidx.lifecycle.*
import app.com.mimiclees.data.push.PushInfo
import app.com.mimiclees.data.push.PushRepository
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
class WebViewerModel @Inject constructor(
    private val pushRepository: PushRepository)  : ViewModel(){
    private val _pushInfo = MutableLiveData<PushInfo?>()
    val pushInfo: MutableLiveData<PushInfo?>
        get() = _pushInfo

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
}