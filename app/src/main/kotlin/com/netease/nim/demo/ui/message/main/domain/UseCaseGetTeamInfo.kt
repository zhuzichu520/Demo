package com.netease.nim.demo.ui.message.main.domain

import com.hiwitech.android.mvvm.domain.UseCase
import com.hiwitech.android.shared.ext.bindToException
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.netease.nim.demo.nim.repository.NimRepository
import com.netease.nimlib.sdk.team.model.Team
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * desc 获取群组信息
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class UseCaseGetTeamInfo @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<String, Flowable<Team>>() {

    override fun execute(parameters: String): Flowable<Team> {
        return nimRepository.getTeamById(parameters).bindToSchedulers().bindToException()

    }
}