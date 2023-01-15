package app.com.mimiclefr.di

import app.com.mimiclefr.api.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideApiService(): ApiInterface {
        return ApiInterface.create()
    }
}