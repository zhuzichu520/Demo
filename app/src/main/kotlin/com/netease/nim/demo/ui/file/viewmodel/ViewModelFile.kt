package com.netease.nim.demo.ui.file.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.mvvm.event.SingleLiveEvent
import com.hiwitech.android.shared.ext.diffEquals
import com.hiwitech.android.shared.ext.itemBindingOf
import com.hiwitech.android.shared.ext.itemDiffOf
import com.hiwitech.android.shared.ext.map
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import java.io.File
import javax.inject.Inject

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ViewModelFile @Inject constructor(
) : ViewModelBase<ArgDefault>() {

    val index = MutableLiveData(0)

    val onClickItemEvent = SingleLiveEvent<ItemViewModelFile>()

    val navList = DiffObservableList<Any>(object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is ItemViewModelFile && newItem is ItemViewModelFile) {
                oldItem.file.absolutePath == newItem.file.absolutePath
            } else if (oldItem is ItemViewModelFiles && newItem is ItemViewModelFiles) {
                oldItem.file.absolutePath == newItem.file.absolutePath
            } else oldItem.diffEquals(newItem)
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean =
            oldItem.diffEquals(newItem)
    })

    val list = DiffObservableList<Any>(itemDiffOf<ItemViewModelFile> { oldItem, newItem ->
        oldItem.file.absolutePath == newItem.file.absolutePath
    })

    val navItemBind = itemBindingOf<Any>(BR.item, R.layout.item_file_nav)

    val itemBind = OnItemBindClass<Any>().apply {
        map<ItemViewModelFile>(BR.item, R.layout.item_file)
        map<ItemViewModelFiles>(BR.item, R.layout.item_files)
    }

    val onAddFileNavEvent = SingleLiveEvent<Boolean>()

    fun loadFileList(fileDir: File, isAddNav: Boolean = true) {
        val data = fileDir.listFiles()?.map {
            if (it.isDirectory) {
                ItemViewModelFiles(this, it)
            } else {
                ItemViewModelFile(this, it)
            }
        } ?: listOf()
        list.update(data)
        if (isAddNav) {
            navList.update(navList + ItemViewModelFileNav(this, fileDir))
            onAddFileNavEvent.call()
        }
    }

}