package app.com.mimiclefr.di

import app.com.mimiclefr.api.ApiInterface
import app.com.mimiclefr.data.push.PushRepository
import app.com.mimiclefr.data.push.PushRepositoryImpl
import app.com.mimiclefr.data.splash.AppMetaRepository
import app.com.mimiclefr.data.splash.AppMetaRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    fun providePushRepository(apiInterface: ApiInterface): PushRepository {
        return PushRepositoryImpl(apiInterface)
    }

    @Singleton
    @Provides
    fun provideAppMetaRepository(apiInterface: ApiInterface): AppMetaRepository {
        return AppMetaRepositoryImpl(apiInterface)
    }
}