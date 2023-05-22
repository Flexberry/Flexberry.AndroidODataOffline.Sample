package com.flexberry.androidodataofflinesample.data.sycnhonization

import com.flexberry.androidodataofflinesample.data.ApplicationUserRepository
import com.flexberry.androidodataofflinesample.data.VoteRepository
import javax.inject.Inject

/**
 * Сервис синхронизации между локальным и удаленным источниками данных.
 */
class SynchronizationService @Inject constructor(
    private val applicationUserRepository: ApplicationUserRepository,
    private val voteRepository: VoteRepository
) {
    /**
     * Взять данные из внешнего репозитория и положить их в локальный репозиторий.
     */
    fun sendRemoteDataToLocal() {
        val currentApplicationUsersData = applicationUserRepository.getApplicationUsersOnline()
        applicationUserRepository.updateApplicationUsersOffline(currentApplicationUsersData)

        val currentVotesData = voteRepository.getVotesOnline()
        voteRepository.updateVotesOffline(currentVotesData)
    }

    /**
     * Взять данные из локального репозитория и положить их во внешний репозиторий.
     */
    fun sendLocalDataToRemote() {
        val localApplicationUserData = applicationUserRepository.getApplicationUsersOffline()
        applicationUserRepository.updateApplicationUsersOnline(localApplicationUserData)

        val localVoteData = voteRepository.getVotesOffline()
        voteRepository.updateVotesOnline(localVoteData)
    }
}