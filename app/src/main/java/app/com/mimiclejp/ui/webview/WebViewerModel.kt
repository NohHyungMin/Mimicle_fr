package app.com.mimiclejp.ui.webview

import androidx.lifecycle.*
import app.com.mimiclejp.MimicleApplication
import app.com.mimiclejp.data.push.PushInfo
import app.com.mimiclejp.data.push.PushRepository
import app.com.mimiclejp.util.MapUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WebViewerModel @Inject constructor(
    private val pushRepository: PushRepository)  : ViewModel(){
    private val _pushInfo = MutableLiveData<PushInfo>()
    val pushInfo: LiveData<PushInfo>
        get() = _pushInfo

    fun setPushInfo(param: HashMap<String, String>) = viewModelScope.launch(Dispatchers.IO){
        if(MapUtils.checkNetworkState(MimicleApplication.ApplicationContext())) {
            val responseData = pushRepository.setPushInfo(param)

            withContext(Dispatchers.Main) {
                _pushInfo.value = responseData
            }
        }
    }
}