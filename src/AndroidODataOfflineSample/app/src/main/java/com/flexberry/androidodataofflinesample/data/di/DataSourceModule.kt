package com.flexberry.androidodataofflinesample.data.di

import android.content.Context
import androidx.room.Room
import com.flexberry.androidodataofflinesample.data.local.datasource.AppDataRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.ApplicationUserRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.DetailRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.LocalDatabase
import com.flexberry.androidodataofflinesample.data.local.datasource.MasterRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataBaseManager
import com.flexberry.androidodataofflinesample.data.local.datasource.VoteRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import com.flexberry.androidodataofflinesample.data.local.entities.DetailEntity
import com.flexberry.androidodataofflinesample.data.local.entities.MasterEntity
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

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MasterLocalDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DetailLocalDatasource

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
    fun provideAppDataLocalDataSource(roomDataBaseManager: RoomDataBaseManager): LocalDataSource<AppDataEntity> {
        return AppDataRoomDataSource(roomDataBaseManager)
    }

    @ApplicationUserLocalDataSource
    @Provides
    fun provideApplicationUserLocalDataSource(roomDataBaseManager: RoomDataBaseManager): LocalDataSource<ApplicationUserEntity> {
        return ApplicationUserRoomDataSource(roomDataBaseManager)
    }

    @VoteLocalDatasource
    @Provides
    fun provideVoteLocalDatasource(roomDataBaseManager: RoomDataBaseManager): LocalDataSource<VoteEntity> {
        return VoteRoomDataSource(roomDataBaseManager)
    }

    @MasterLocalDataSource
    @Provides
    fun provideMasterLocalDataSource(roomDataBaseManager: RoomDataBaseManager): LocalDataSource<MasterEntity> {
        return MasterRoomDataSource(roomDataBaseManager)
    }

    @DetailLocalDatasource
    @Provides
    fun provideDetailLocalDatasource(roomDataBaseManager: RoomDataBaseManager): LocalDataSource<DetailEntity> {
        return DetailRoomDataSource(roomDataBaseManager)
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
    fun provideRoomDataBaseManager(localDatabase: LocalDatabase): RoomDataBaseManager {
        return RoomDataBaseManager(localDatabase)
    }
}