package com.netease.nim.demo.ui.message.more.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.shared.ext.map
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import javax.inject.Inject

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/13 2:45 PM
 * since: v 1.0.0
 */
class ViewModelMore @Inject constructor(
) : ViewModelBase<ArgDefault>() {

    companion object {
        const val TYPE_ALBUM = 0
        const val TYPE_VIDEO = 1
        const val TYPE_LOCAL = 2
        const val TYPE_FILE = 3
        const val TYPE_CALL = 4
    }


    /**
     * 初始化数据
     */
    val items = MutableLiveData<List<ItemViewModelMore>>().also {
        it.value = listOf(
            ItemViewModelMore(
                this,
                R.string.input_panel_album,
                R.mipmap.nim_message_action_album,
                TYPE_ALBUM
            ),
            ItemViewModelMore(
                this,
                R.string.input_panel_video,
                R.mipmap.nim_message_action_video,
                TYPE_VIDEO
            ),
            ItemViewModelMore(
                this,
                R.string.input_panel_local,
                R.mipmap.nim_message_action_local,
                TYPE_LOCAL
            ),
            ItemViewModelMore(
                this,
                R.string.input_panel_file,
                R.mipmap.nim_message_action_file,
                TYPE_FILE
            ),
            ItemViewModelMore(
                this,
                R.string.input_panel_call,
                R.mipmap.nim_message_action_call,
                TYPE_CALL
            )
        )
    }

    /**
     * 注册布局
     */
    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelMore>(BR.item, R.layout.item_more)
    }


}