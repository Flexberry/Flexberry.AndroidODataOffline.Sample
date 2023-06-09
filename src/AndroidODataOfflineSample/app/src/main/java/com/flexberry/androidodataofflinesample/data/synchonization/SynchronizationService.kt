package com.flexberry.androidodataofflinesample.data.synchonization

import com.flexberry.androidodataofflinesample.data.ApplicationUserRepository
import com.flexberry.androidodataofflinesample.data.DetailRepository
import com.flexberry.androidodataofflinesample.data.MasterRepository
import com.flexberry.androidodataofflinesample.data.VoteRepository
import javax.inject.Inject

/**
 * Сервис синхронизации между локальным и удаленным источниками данных.
 */
class SynchronizationService @Inject constructor(
    private val masterRepository: MasterRepository,
    private val detailRepository: DetailRepository,
) {
    /**
     * Взять данные из внешнего репозитория и положить их в локальный репозиторий.
     */
    fun sendRemoteDataToLocal() {
        val mastersData = masterRepository.getMastersOnline()
        masterRepository.updateMastersOffline(mastersData)

        val detailData = detailRepository.getDetailsOnline()
        detailRepository.updateDetailsOffline(detailData)
    }
}