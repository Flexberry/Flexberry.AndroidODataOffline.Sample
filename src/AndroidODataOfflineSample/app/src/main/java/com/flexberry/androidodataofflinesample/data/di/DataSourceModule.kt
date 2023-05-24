package com.flexberry.androidodataofflinesample.data.di

import android.content.Context
import androidx.room.Room
import com.flexberry.androidodataofflinesample.data.local.datasource.AppDataRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.ApplicationUserRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.LocalDatabase
import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataSourceTypeManager
import com.flexberry.androidodataofflinesample.data.local.datasource.VoteRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity
import com.flexberry.androidodataofflinesample.data.local.interfaces.LocalDataSource
import com.flexberry.androidodataofflinesample.data.network.datasource.ApplicationUserOdataDataSource
import com.flexberry.androidodataofflinesample.data.network.datasource.DetailOdataDataSource
import com.flexberry.androidodataofflinesample.data.network.datasource.MasterOdataDataSource
import com.flexberry.androidodataofflinesample.data.network.datasource.VoteOdataDataSource
import com.flexberry.androidodataofflinesample.data.network.interfaces.NetworkDataSource
import com.flexberry.androidodataofflinesample.data.network.models.NetworkApplicationUser
import com.flexberry.androidodataofflinesample.data.network.models.NetworkDetail
import com.flexberry.androidodataofflinesample.data.network.models.NetworkMaster
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
annotation class MasterNetworkDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DetailNetworkDatasource

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

    @MasterNetworkDataSource
    @Provides
    fun provideMasterNetworkDataSource(): NetworkDataSource<NetworkMaster> {
        return MasterOdataDataSource()
    }

    @DetailNetworkDatasource
    @Provides
    fun provideDetailNetworkDataSource(): NetworkDataSource<NetworkDetail> {
        return DetailOdataDataSource()
    }

    @AppDataLocalDataSource
    @Provides
    fun provideAppDataLocalDataSource(roomDataSourceTypeManager: RoomDataSourceTypeManager): LocalDataSource<AppDataEntity> {
        return AppDataRoomDataSource(roomDataSourceTypeManager)
    }

    @ApplicationUserLocalDataSource
    @Provides
    fun provideApplicationUserLocalDataSource(roomDataSourceTypeManager: RoomDataSourceTypeManager): LocalDataSource<ApplicationUserEntity> {
        return ApplicationUserRoomDataSource(roomDataSourceTypeManager)
    }

    @VoteLocalDatasource
    @Provides
    fun provideVoteLocalDatasource(roomDataSourceTypeManager: RoomDataSourceTypeManager): LocalDataSource<VoteEntity> {
        return VoteRoomDataSource(roomDataSourceTypeManager)
    }

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext appContext: Context): LocalDatabase {
        return Room.inMemoryDatabaseBuilder(appContext, LocalDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideRoomDataSourceManager(localDatabase: LocalDatabase): RoomDataSourceTypeManager {
        return RoomDataSourceTypeManager(localDatabase)
    }
}