package app.com.mimiclees.di

import app.com.mimiclees.api.ApiInterface
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