package com.netease.nim.demo.ui.user.domain

import com.google.common.base.Optional
import com.hiwitech.android.mvvm.domain.UseCase
import com.hiwitech.android.shared.ext.bindToException
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.netease.nim.demo.nim.repository.NimRepository
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/11 2:30 PM
 * since: v 1.0.0
 */
class UseCaseRemoveFromBlackList @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<String, Flowable<Optional<Void>>>() {

    override fun execute(parameters: String): Flowable<Optional<Void>> {
        return nimRepository.removeFromBlackList(parameters)
            .bindToSchedulers()
            .bindToException()
    }

}