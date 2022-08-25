package app.com.mimiclees

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

/**
 * 어플리케이션 메인컨텍스트
 * Created by nhm on 2021-09-08.
 */
@HiltAndroidApp
class MimicleApplication : Application() {
    init{
        instance = this
    }

    companion object {
        lateinit var instance: MimicleApplication
        fun ApplicationContext() : Context {
            return instance.applicationContext
        }
    }
}