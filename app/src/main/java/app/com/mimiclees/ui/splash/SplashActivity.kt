package app.com.mimiclees.ui.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Base64
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import app.com.mimiclees.BuildConfig
import androidx.lifecycle.Observer
import app.com.mimiclees.R
import app.com.mimiclees.base.BaseActivity
import app.com.mimiclees.common.storage.AppPreference
import app.com.mimiclees.data.push.PushInfo
import app.com.mimiclees.data.push.PushRepository
import app.com.mimiclees.data.splash.AppMetaData
import app.com.mimiclees.data.splash.AppMetaRepository
import app.com.mimiclees.databinding.ActivitySplashBinding
import app.com.mimiclees.ui.webview.WebViewerActivity
import app.com.mimiclees.util.GoogleUtil
import app.com.mimiclees.util.MapUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import java.lang.Exception
import java.security.MessageDigest

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private lateinit var adid: String
    private var coroutineScope = CoroutineScope(Dispatchers.Main)

    private lateinit var pushRepository: PushRepository
    private lateinit var appMetaRepository: AppMetaRepository
    private val viewModel: SplashViewModel by viewModels()
//    {
//        object : ViewModelProvider.Factory {
//            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//                return SplashViewModel(pushRepository, appMetaRepository) as T
//            }
//        }
//    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel

        getDynamicLink()
        initViewModelCallback()

        checkIntent()
        //getAppMeta()
        checkPermission()
        getAdid()
        coroutineScope.launch(Dispatchers.Main){
            delay(400)
            playSound()
        }
        //키해시 얻기
//        try {
//            val packageInfo = packageManager.getPackageInfo(
//                packageName, PackageManager.GET_SIGNING_CERTIFICATES
//            )
//            val signingInfo = packageInfo.signingInfo.apkContentsSigners
//
//            for (signature in signingInfo) {
//                val messageDigest = MessageDigest.getInstance("SHA")
//                messageDigest.update(signature.toByteArray())
//                val keyHash = String(Base64.encode(messageDigest.digest(), 0))
//                Log.d("KeyHash", keyHash)
//            }
//
//        } catch (e: Exception) {
//            Log.e("Exception", e.toString())
//        }
    }

    //다이나믹 링크 받기
    private fun getDynamicLink() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData: PendingDynamicLinkData? ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    //ex) https://mimicle.art/app?linkurl=https://mimicle.art/app?no=101
                    var dynaminLink = deepLink?.getQueryParameter("linkurl")
                    if(dynaminLink != null)
                        AppPreference(baseContext).setLandingUrl(dynaminLink)
                }
            }
            .addOnFailureListener(this) { e -> Log.w("test", "getDynamicLink:onFailure", e) }
    }

    private fun initViewModelCallback() {
        val versionCode = BuildConfig.VERSION_CODE.toString()
        with(viewModel) {
            pushInfo.observe(this@SplashActivity, Observer {
                var pushInfo: PushInfo? = it
                //Log.d("test", pushInfo.memno.toString())
            })
            appMetaData.observe(this@SplashActivity, Observer {
                var appMeta: AppMetaData? = it
                AppPreference(baseContext).setMainUrl(appMeta?.data?.mainurl.toString())
                if(appMeta?.data?.vcode?.toInt()!! > versionCode?.toInt()){
                    if(appMeta.data.forcedyn.uppercase() == "Y"){
                        val builder = AlertDialog.Builder(this@SplashActivity)
                        builder.setTitle("")
                        builder.setMessage(appMeta?.data?.strupdate)
                        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                            MapUtils.goStore(this@SplashActivity);
                            finish()
                        }
                        builder.show()
                    }else{
                        val builder = AlertDialog.Builder(this@SplashActivity)
                        builder.setTitle("")
                        builder.setMessage(appMeta?.data?.strupdate)
                        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                            MapUtils.goStore(this@SplashActivity);
                            finish()
                        }
                        builder.setNegativeButton(android.R.string.no) { dialog, which ->
                            delayHandler.sendMessageDelayed(Message(), 1000)
                        }
                        builder.show()
                    }
                }else{
                    delayHandler.sendMessageDelayed(Message(), 1000)
                }

                Log.d("test", appMeta?.data?.mainurl.toString())
            })
            networkError.observe(this@SplashActivity, Observer {it ->
                if(it == true) {
                    val builder = AlertDialog.Builder(this@SplashActivity)
                    builder.setTitle("")
                    builder.setMessage(R.string.str_check_netword)
                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                        finish()
                    }
                    builder.show()
                }
            })
        }
    }

    private fun checkIntent() {
        val intent = intent
        val callUrl:String?
        if (intent != null) {
            callUrl = intent.getStringExtra("url")
            if(callUrl != null) {
                AppPreference(baseContext).setLandingUrl(callUrl)
                //Toast.makeText(baseContext, callUrl, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getAdid() {
        GoogleUtil.GAIDTask(baseContext, object : GoogleUtil.GAIDCallback {
            override fun onSucces(result: String?) {
                if (result != null) {
                    adid = result
                    if(adid != null)
                        AppPreference(baseContext).setAdid(adid!!)
                }
                myToken()
            }

            override fun onFail(e: Exception) {
                myToken()
            }

        }).execute()
    }

    //앱 정보 가져오기
    private fun getAppMeta() {
        val osType = "aos"
        val versionCode = BuildConfig.VERSION_CODE.toString()

        var param = HashMap<String, String>()
        param["osType"] = osType
        param["versionCode"] = versionCode
        viewModel.getAppMeta(param)

//        ApiClient.apiInterface.getMeta(osType, versionCode).enqueue(object: Callback<AppMetaData> {
//            override fun onResponse(call: Call<AppMetaData>, response: Response<AppMetaData>) {
//                if(response.isSuccessful) {
//                    var appMeta: AppMetaData = response.body()!!
//                    AppPreference.setMainUrl(appMeta.data.mainurl.toString())
//                    if(appMeta.data.vcode.toInt() > versionCode.toInt()){
//                        if(appMeta.data.forcedyn.uppercase() == "Y"){
//                            val builder = AlertDialog.Builder(this@SplashActivity)
//                            builder.setTitle("")
//                            builder.setMessage(appMeta.data.strupdate)
//                            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
//                                goStore(this@SplashActivity);
//                                finish()
//                            }
//                            builder.show()
//                        }else{
//                            val builder = AlertDialog.Builder(this@SplashActivity)
//                            builder.setTitle("")
//                            builder.setMessage(appMeta.data.strupdate)
//                            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
//                                goStore(this@SplashActivity);
//                                finish()
//                            }
//                            builder.setNegativeButton(android.R.string.no) { dialog, which ->
//                                delayHandler.sendMessageDelayed(Message(), 1000)
//                            }
//                            builder.show()
//                        }
//                    }else{
//                        delayHandler.sendMessageDelayed(Message(), 1000)
//                    }
//
//                    Log.d("test", appMeta.data.mainurl.toString())
//                }
//            }
//
//            override fun onFailure(call: Call<AppMetaData>, t: Throwable) {
//            }
//        })
    }

    //서버에 토큰 정보 전송
    private fun setPushInfo(token: String) {
        val osType = "aos"
        val versionCode = BuildConfig.VERSION_CODE.toString()
        var memno = ""
        if(AppPreference(baseContext).getMemNo() != null)
            memno = AppPreference(baseContext).getMemNo()!!//"1000001"
        var pushkey = ""
        if(token != null)
            pushkey = token!!
        var uuid = ""
        if(adid != null)
            uuid = adid!!
        var param = HashMap<String, String>()
        param["osType"] = osType
        param["versionCode"] = versionCode
        param["pushkey"] = pushkey
        param["uuid"] = uuid
        param["memno"] = memno
        viewModel.setPushInfo(param)

//        ApiClient.apiInterface.setPushInfo(osType, versionCode, pushkey, uuid, memno).enqueue(object: Callback<PushInfo> {
//            override fun onResponse(call: Call<PushInfo>, response: Response<PushInfo>) {
//                if(response.isSuccessful) {
//                    var pushInfo: PushInfo = response.body()!!
//
//                    Log.d("test", pushInfo.memno.toString())
//                }
//            }
//
//            override fun onFailure(call: Call<PushInfo>, t: Throwable) {
//            }
//        })
    }

    //권한체크
    private fun checkPermission() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                getAppMeta()
            }
            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                finish()
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setPermissions(
//                    Manifest.permission.READ_PHONE_STATE,
//                    Manifest.permission.READ_CALL_LOG,  // 안드로이드 9.0 에서는 이것도 추가하라고 되어 있음.
                Manifest.permission.WRITE_EXTERNAL_STORAGE,  // 전화걸기 및 관리
                //Manifest.permission.ACCESS_FINE_LOCATION,
                //Manifest.permission.ACCESS_COARSE_LOCATION
            ).check()
    }


    private var delayHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val intent = Intent(this@SplashActivity, WebViewerActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun myToken() {
        //쓰레드 사용할것
        Thread(Runnable {
            try {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if(!task.isSuccessful){
                        return@OnCompleteListener
                    }
                    val token = task.result.toString()
                    if(token != null)
                        AppPreference(baseContext).setPushToken(token)
//                    Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show()
                    FirebaseMessaging.getInstance().subscribeToTopic("all");
                    setPushInfo(token)
                })
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()
    }

    //미믹 사운드 플레이
    fun playSound() {
        var resId = getResources().getIdentifier(R.raw.mimic.toString(),
            "raw", this@SplashActivity?.packageName)

        val mediaPlayer = MediaPlayer.create(this@SplashActivity, resId)
        mediaPlayer.start()
    }
}


