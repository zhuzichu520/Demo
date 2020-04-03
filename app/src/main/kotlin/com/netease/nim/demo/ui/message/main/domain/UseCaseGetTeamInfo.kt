package com.netease.nim.demo.ui.message.main.domain

import com.netease.nim.demo.nim.repository.NimRepository
import com.netease.nimlib.sdk.team.model.Team
import com.hiwitech.android.mvvm.domain.UseCase
import com.hiwitech.android.shared.ext.bindToException
import com.hiwitech.android.shared.ext.bindToSchedulers
import io.reactivex.Flowable
import javax.inject.Inject

class UseCaseGetTeamInfo @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<String, Flowable<Team>>() {

    override fun execute(parameters: String): Flowable<Team> {
        return nimRepository.getTeamById(parameters).bindToSchedulers().bindToException()

    }
}