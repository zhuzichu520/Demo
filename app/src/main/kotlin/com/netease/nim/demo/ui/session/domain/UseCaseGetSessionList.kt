package com.netease.nim.demo.ui.session.domain

import com.netease.nim.demo.nim.NimRequestCallback
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.zhuzichu.android.mvvm.domain.UseCase
import com.zhuzichu.android.shared.ext.bindToException
import com.zhuzichu.android.shared.ext.bindToSchedulers
import com.zhuzichu.android.shared.ext.createFlowable
import io.reactivex.Flowable
import javax.inject.Inject

class UseCaseGetSessionList @Inject constructor(
    private val msgService: MsgService
) : UseCase<Unit, Flowable<List<RecentContact>>>() {

    override fun execute(parameters: Unit): Flowable<List<RecentContact>> {

        return createFlowable<List<RecentContact>> {
            msgService.queryRecentContacts().setCallback(NimRequestCallback(this))
        }.bindToSchedulers().bindToException()

    }
}