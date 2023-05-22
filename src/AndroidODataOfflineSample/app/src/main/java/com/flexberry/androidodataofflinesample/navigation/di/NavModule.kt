package com.flexberry.androidodataofflinesample.navigation.di

import com.flexberry.androidodataofflinesample.navigation.AppNavigator
import com.flexberry.androidodataofflinesample.navigation.AppNavigatorImplement
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("kotlin:S6517")
@Module
@InstallIn(SingletonComponent::class)
interface NavModule {

    @Singleton
    @Binds
    fun bindAppNavigator(appNavigatorImplement: AppNavigatorImplement): AppNavigator
}