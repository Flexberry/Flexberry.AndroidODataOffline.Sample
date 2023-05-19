package com.flexberry.androidodataofflinesample.data.sycnhonization

import com.flexberry.androidodataofflinesample.data.ApplicationUserRepository
import com.flexberry.androidodataofflinesample.data.VoteRepository
import javax.inject.Inject

/**
 * Сервис синхронизации данных между локальным и удаленным репозиториями.
 */
class SynchronizationService @Inject constructor(
    private val applicationUserRepository: ApplicationUserRepository,
    private val voteRepository: VoteRepository
) {
    /**
     * Взять данные из внешнего репозитория и положить их в локальный репозиторий.
     */
    fun sendRemoteDataToLocal() {
        val applicationUserData = applicationUserRepository.getApplicationUsersOnline()
        applicationUserRepository.updateApplicationUsersOffline(applicationUserData)

        val voteData = voteRepository.getVotesOnline()
        voteRepository.updateVotesOffline(voteData)
    }

    /**
     * Взять данные из локального репозитория и положить их во внешний репозиторий.
     */
    fun sendLocalDataToRemote() {
        val applicationUserData = applicationUserRepository.getApplicationUsersOffline()
        applicationUserRepository.updateApplicationUsersOnline(applicationUserData)

        val voteData = voteRepository.getVotesOffline()
        voteRepository.updateVotesOnline(voteData)
    }
}