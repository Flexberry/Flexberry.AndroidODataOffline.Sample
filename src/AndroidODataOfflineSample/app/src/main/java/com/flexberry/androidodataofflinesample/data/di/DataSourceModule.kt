package com.flexberry.androidodataofflinesample.data.di

import com.flexberry.androidodataofflinesample.data.network.datasource.ApplicationUserOdataDataSource
import com.flexberry.androidodataofflinesample.data.network.datasource.VoteOdataDataSource
import com.flexberry.androidodataofflinesample.data.network.interfaces.NetworkDataSource
import com.flexberry.androidodataofflinesample.data.network.models.NetworkApplicationUser
import com.flexberry.androidodataofflinesample.data.network.models.NetworkVote
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationUserNetworkDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class VoteNetworkDatasource

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
}