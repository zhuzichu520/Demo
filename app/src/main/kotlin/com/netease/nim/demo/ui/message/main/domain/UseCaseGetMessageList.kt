package com.netease.nim.demo.ui.message.main.domain

import com.netease.nim.demo.nim.repository.NimRepository
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.zhuzichu.android.mvvm.domain.UseCase
import com.zhuzichu.android.shared.ext.bindToException
import com.zhuzichu.android.shared.ext.bindToSchedulers
import io.reactivex.Observable
import javax.inject.Inject

class UseCaseGetMessageList @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<UseCaseGetMessageList.Parameters, Observable<List<IMMessage>>>() {

    override fun execute(parameters: Parameters): Observable<List<IMMessage>> {
        return nimRepository.getMessageList(parameters.anchor, parameters.pageSize).toObservable()
            .bindToSchedulers()
            .bindToException()
    }

    data class Parameters(
        val anchor: IMMessage,
        val pageSize: Int
    )
}