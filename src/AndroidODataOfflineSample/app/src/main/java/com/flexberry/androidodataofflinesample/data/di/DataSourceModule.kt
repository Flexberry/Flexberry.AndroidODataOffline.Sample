package com.flexberry.androidodataofflinesample.data.di

import android.content.Context
import androidx.room.Room
import com.flexberry.androidodataofflinesample.data.local.datasource.AppDataRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.ApplicationUserRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.LocalDatabase
import com.flexberry.androidodataofflinesample.data.local.datasource.VoteRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity
import com.flexberry.androidodataofflinesample.data.local.interfaces.LocalDataSource
import com.flexberry.androidodataofflinesample.data.network.datasource.ApplicationUserOdataDataSource
import com.flexberry.androidodataofflinesample.data.network.datasource.VoteOdataDataSource
import com.flexberry.androidodataofflinesample.data.network.interfaces.NetworkDataSource
import com.flexberry.androidodataofflinesample.data.network.models.NetworkApplicationUser
import com.flexberry.androidodataofflinesample.data.network.models.NetworkVote
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationUserNetworkDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class VoteNetworkDatasource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppDataLocalDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationUserLocalDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class VoteLocalDatasource

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @ApplicationUserNetworkDataSource
    @Provides
    fun provideApplicationUserNetworkDataSource(): NetworkDataSource<NetworkApplicationUser> {
        return ApplicationUserOdataDataSource()
    }

    @VoteNetworkDatasource
    @Provides
    fun provideVoteNetworkDataSource(): NetworkDataSource<NetworkVote> {
        return VoteOdataDataSource()
    }

    @AppDataLocalDataSource
    @Provides
    fun provideAppDataLocalDataSource(localDatabase: LocalDatabase): LocalDataSource<AppDataEntity> {
        return AppDataRoomDataSource(localDatabase)
    }

    @ApplicationUserLocalDataSource
    @Provides
    fun provideApplicationUserLocalDataSource(localDatabase: LocalDatabase): LocalDataSource<ApplicationUserEntity> {
        return ApplicationUserRoomDataSource(localDatabase)
    }

    @VoteLocalDatasource
    @Provides
    fun provideVoteLocalDatasource(localDatabase: LocalDatabase): LocalDataSource<VoteEntity> {
        return VoteRoomDataSource(localDatabase)
    }

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext appContext: Context): LocalDatabase {
        return Room.inMemoryDatabaseBuilder(appContext, LocalDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
}