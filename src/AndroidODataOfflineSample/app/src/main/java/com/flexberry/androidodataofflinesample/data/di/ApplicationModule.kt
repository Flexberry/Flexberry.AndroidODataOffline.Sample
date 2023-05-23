package com.flexberry.androidodataofflinesample.data.di

import com.flexberry.androidodataofflinesample.ApplicationState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppState

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @AppState
    @Provides
    fun provideApplicationState(): ApplicationState {
        return ApplicationState()
    }
}