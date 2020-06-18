package com.netease.nim.demo.ui.file.fragment

import android.os.Environment
import android.view.Gravity
import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import com.hiwitech.android.libs.internal.MainHandler
import com.hiwitech.android.libs.tool.byteCountToDisplaySizeTwo
import com.hiwitech.android.libs.tool.setOnClickListener
import com.hiwitech.android.shared.bus.LiveDataBus
import com.hiwitech.android.shared.ext.closeDefaultAnimator
import com.hiwitech.android.shared.ext.toStringByResId
import com.hiwitech.android.shared.global.CacheGlobal
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentFileBinding
import com.netease.nim.demo.ui.file.arg.ArgFile
import com.netease.nim.demo.ui.file.event.EventFile
import com.netease.nim.demo.ui.file.viewmodel.ItemViewModelFileNav
import com.netease.nim.demo.ui.file.viewmodel.ViewModelFile
import com.netease.nim.demo.ui.popup.PopupMenus
import com.netease.nim.demo.view.ViewEmpty
import kotlinx.android.synthetic.main.fragment_file.*
import java.io.File

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class FragmentFile : FragmentBase<FragmentFileBinding, ViewModelFile, ArgFile>(),
    View.OnClickListener {

    companion object {
        const val TYPE_OPTION_MY = 1
        const val TYPE_OPTION_WEIXIN = 2
        const val TYPE_OPTION_PHONE = 3
    }

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_file

    private lateinit var onSelectFileEvent: EventFile.OnSelectFileEvent

    private val menus = listOf(
        PopupMenus.ItemMenu(TYPE_OPTION_MY, R.string.file_option_my),
        PopupMenus.ItemMenu(TYPE_OPTION_WEIXIN, R.string.file_option_weixin),
        PopupMenus.ItemMenu(TYPE_OPTION_PHONE, R.string.file_option_phone)
    )

    override fun initOneData() {
        super.initOneData()
        viewModel.menu.value = menus[0]
    }

    override fun initView() {
        super.initView()
        onSelectFileEvent = EventFile.OnSelectFileEvent(arg.type, listOf())
        nav.closeDefaultAnimator()
        files.closeDefaultAnimator()
        files.emptyView = ViewEmpty(requireContext())
        initBackListener()
    }

    override fun initListener() {
        super.initListener()
        setOnClickListener(this, cancel, submit, select)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cancel -> {
                requireActivity().finish()
            }
            R.id.submit -> {
                viewModel.listSelected.value?.let {
                    onSelectFileEvent.files = it.map { item -> item.file }
                    requireActivity().finish()
                }
            }
            R.id.select -> {
                PopupMenus(requireContext(), menus) {
                    viewModel.menu.value = this
                }.apply {
                    popupGravity = Gravity.TOP
                }.show(select)
            }
        }
    }

    private fun initBackListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (viewModel.navList.size <= 1) {
                requireActivity().finish()
            } else {
                viewModel.navList.apply {
                    (get(size - 2) as ItemViewModelFileNav).onClickCommand.execute()
                }
            }
        }
    }

    override fun initViewObservable() {
        super.initViewObservable()

        viewModel.onAddFileNavEvent.observe(viewLifecycleOwner, Observer {
            MainHandler.postDelayed(50) {
                nav.scrollToPosition(viewModel.navList.size - 1)
            }
        })

        viewModel.onClickItemEvent.observe(viewLifecycleOwner, Observer {
            val list = viewModel.listSelected.value ?: listOf()
            val checkted = !(it.checked.value ?: false)
            if (list.size >= arg.max && checkted)
                return@Observer
            if (checkted) {
                viewModel.listSelected.value = list + it
            } else {
                viewModel.listSelected.value = list - it
            }
            it.checked.value = checkted
        })

        viewModel.listSelected.observe(viewLifecycleOwner, Observer {
            viewModel.text.value = String.format("%d/%d", it.size, arg.max)
            var countLength = 0L
            it.forEach { item ->
                countLength += item.file.length()
            }
            viewModel.count.value =
                if (countLength == 0L) "" else ("已选：" + byteCountToDisplaySizeTwo(countLength))
            viewModel.enable.value = it.isNotEmpty()
        })

        /**
         * 底部导航Item点击事件
         */
        viewModel.menu.observe(viewLifecycleOwner, Observer {
            viewModel.navList.update(listOf())
            viewModel.list.update(listOf())
            when (it.type) {
                TYPE_OPTION_MY -> {
                    CacheGlobal.getDownloadDir()
                }
                TYPE_OPTION_WEIXIN -> {
                    getWeiXinFilePath()
                }
                TYPE_OPTION_PHONE -> {
                    getRootFilePath()
                }
                else -> {
                    null
                }
            }?.let { path ->
                viewModel.loadFileList(File(path))
            }
            viewModel.menuTitle.value = it.titleId.toStringByResId(requireContext())
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        LiveDataBus.post(onSelectFileEvent)
    }

    @Suppress("DEPRECATION")
    fun getWeiXinFilePath(): String {
        var file =
            File(getRootFilePath() + "/tencent/MicroMsg/Download")
        if (!file.exists()) {
            file = File(getRootFilePath() + "/Android/data/com.tencent.mm/MicroMsg/Download")
        }
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath
    }

    @Suppress("DEPRECATION")
    fun getRootFilePath(): String {
        return Environment.getExternalStorageDirectory().absolutePath
    }

}