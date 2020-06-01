package com.netease.nim.demo.ui.file.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.hiwitech.android.mvvm.event.SingleLiveEvent
import com.hiwitech.android.shared.ext.diffEquals
import com.hiwitech.android.shared.ext.itemBindingOf
import com.hiwitech.android.shared.ext.itemDiffOf
import com.hiwitech.android.shared.ext.map
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.ui.file.arg.ArgFile
import com.netease.nim.demo.ui.popup.PopupMenus
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
) : ViewModelBase<ArgFile>() {

    /**
     * 右上角选择按钮的文案
     */
    val text = MutableLiveData<String>()

    /**
     * 右上角按钮是否可点击
     */
    val enable = MutableLiveData<Boolean>()

    /**
     * Item的选择按钮点击事件
     */
    val onClickItemEvent = SingleLiveEvent<ItemViewModelFile>()

    /**
     * 底部已选文件的大小文本
     */
    val count = MutableLiveData<String>()

    /**
     * 头部文件夹导航Recyclerview的Diff
     */
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

    /**
     * 文件列表数据
     */
    val list = DiffObservableList<Any>(itemDiffOf<ItemViewModelFile> { oldItem, newItem ->
        oldItem.file.absolutePath == newItem.file.absolutePath
    })

    /**
     * 头部文件导航的itemBinding
     */
    val navItemBind = itemBindingOf<Any>(BR.item, R.layout.item_file_nav)

    /**
     * 文件列表的itemBinding
     */
    val itemBind = OnItemBindClass<Any>().apply {
        //文件
        map<ItemViewModelFile>(BR.item, R.layout.item_file)
        //文件夹
        map<ItemViewModelFiles>(BR.item, R.layout.item_files)
    }

    /**
     * 当前menu
     */
    val menu = MutableLiveData<PopupMenus.ItemMenu>()

    /**
     * 当前menu的文本
     */
    val menuTitle = MutableLiveData<String>()

    /**
     * 已选文件数据
     */
    val listSelected = MutableLiveData<List<ItemViewModelFile>>(listOf())

    val onAddFileNavEvent = SingleLiveEvent<Boolean>()

    /**
     * 比较器
     */
    private val comparator: Comparator<File> = Comparator { left, right ->
        val l1 = left.isDirectory
        val l2 = right.isDirectory
        if (l1 && !l2)
            -1
        else if (!l1 && l2)
            1
        else {
            left.name.compareTo(right.name)
        }
    }

    /**
     * todo 这个方法可以提出来放到数据仓库中 LocalRepository
     * 获取文件夹所有数据
     */
    fun loadFileList(fileDir: File, isAddNav: Boolean = true) {
        val files = (fileDir.listFiles() ?: arrayOf()).toList().sortedWith(comparator)
        val data = files.map {
            if (it.isDirectory) {
                ItemViewModelFiles(this, it)
            } else {
                ItemViewModelFile(this, it)
            }
        }
        list.update(data)
        if (isAddNav) {
            navList.update(navList + ItemViewModelFileNav(this, fileDir))
            onAddFileNavEvent.call()
        }
    }

}