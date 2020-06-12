package com.netease.nim.demo.ui.profile.domain

import com.google.common.base.Optional
import com.hiwitech.android.mvvm.domain.UseCase
import com.hiwitech.android.shared.ext.bindToException
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.netease.nim.demo.nim.repository.NimRepository
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * desc 更新IM用户资料详情
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class UseCaseUpdateUserInfo @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<Map<UserInfoFieldEnum, Any>, Flowable<Optional<Void>>>() {

    override fun execute(parameters: Map<UserInfoFieldEnum, Any>): Flowable<Optional<Void>> {
        return nimRepository.updateUserInfo(parameters).bindToSchedulers().bindToException()
    }

}