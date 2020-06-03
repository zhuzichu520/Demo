package com.netease.nim.demo.ui.contact.domain

import com.hiwitech.android.mvvm.domain.UseCase
import com.hiwitech.android.shared.ext.bindToException
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.netease.nim.demo.nim.repository.NimRepository
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * desc 获取消息列表数据
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class UseCaseGetFriendInfoList @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<Unit, Flowable<List<NimUserInfo>>>() {

    override fun execute(parameters: Unit): Flowable<List<NimUserInfo>> {
        return nimRepository.getFriendInfoList()
            .bindToSchedulers()
            .bindToException()
    }

}