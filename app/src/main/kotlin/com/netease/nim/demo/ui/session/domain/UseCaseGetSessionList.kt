package com.netease.nim.demo.ui.session.domain

import com.hiwitech.android.mvvm.domain.UseCase
import com.hiwitech.android.shared.ext.bindToException
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.netease.nim.demo.nim.repository.NimRepository
import com.netease.nimlib.sdk.msg.model.RecentContact
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * desc 获取最近会话列表数据
 * author: 朱子楚
 * time: 2020/4/4 11:48 PM
 * since: v 1.0.0
 */
class UseCaseGetSessionList @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<Unit, Flowable<List<RecentContact>>>() {

    override fun execute(parameters: Unit): Flowable<List<RecentContact>> {

        return nimRepository.getRecentContactList().bindToSchedulers().bindToException()

    }
}