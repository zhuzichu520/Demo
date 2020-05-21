package com.netease.nim.demo.ui.file.fragment

import android.os.Environment
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import com.hiwitech.android.libs.internal.MainHandler
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.shared.bus.LiveDataBus
import com.hiwitech.android.shared.ext.closeDefaultAnimator
import com.hiwitech.android.shared.global.CacheGlobal
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentFileBinding
import com.netease.nim.demo.ui.file.event.EventFile
import com.netease.nim.demo.ui.file.viewmodel.ItemViewModelFileNav
import com.netease.nim.demo.ui.file.viewmodel.ViewModelFile
import com.netease.nim.demo.view.ViewEmpty
import kotlinx.android.synthetic.main.fragment_file.*
import java.io.File

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class FragmentFile : FragmentBase<FragmentFileBinding, ViewModelFile, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_file

    override fun initView() {
        super.initView()
        nav.closeDefaultAnimator()
        files.closeDefaultAnimator()
        files.emptyView = ViewEmpty(requireContext())
        initBackListener()
    }

    /**
     * 双击退出
     */
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

        viewModel.index.observe(viewLifecycleOwner, Observer {
            viewModel.navList.update(listOf())
            viewModel.list.update(listOf())
            when (it) {
                0 -> {
                    CacheGlobal.getNimCacheDir()
                }
                1 -> {
                    Environment.getExternalStorageDirectory().absolutePath + "/tencent/MicroMsg/Download"
                }
                2 -> {
                    Environment.getExternalStorageDirectory().absolutePath
                }
                else -> {
                    null
                }
            }?.let { path ->
                viewModel.loadFileList(File(path))
            }
        })

        viewModel.onClickItemEvent.observe(viewLifecycleOwner, Observer {
            LiveDataBus.post(EventFile.OnSendFileEvent(it.file))
            requireActivity().finish()
        })
    }

}