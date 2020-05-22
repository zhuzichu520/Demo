package com.netease.nim.demo.ui.file.fragment

import androidx.core.os.bundleOf
import com.hiwitech.android.shared.global.CacheGlobal
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentBrowseFileBinding
import com.netease.nim.demo.ui.file.arg.ArgBrowseFile
import com.netease.nim.demo.ui.file.viewmodel.ViewModelBrowseFile
import com.tencent.smtt.sdk.TbsReaderView
import kotlinx.android.synthetic.main.fragment_browse_file.*

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/21 11:55 AM
 * since: v 1.0.0
 */
class FragmentBrowseFile :
    FragmentBase<FragmentBrowseFileBinding, ViewModelBrowseFile, ArgBrowseFile>(),
    TbsReaderView.ReaderCallback {

    private lateinit var view_tbs: TbsReaderView

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_browse_file

    override fun initView() {
        super.initView()
        viewModel.title.value = arg.fileName
        view_content.removeAllViews()
        view_tbs = TbsReaderView(requireContext(), this)
        view_content.addView(view_tbs)
        val result = view_tbs.preOpen(arg.suffix, false)
        if (result) {
            view_tbs.openFile(
                bundleOf(
                    TbsReaderView.KEY_FILE_PATH to arg.path,
                    TbsReaderView.KEY_TEMP_PATH to CacheGlobal.getTbsReaderTempCacheDir()
                )
            )
        }
    }

    override fun onDestroyView() {
        view_tbs.onStop()
        super.onDestroyView()
    }

    override fun onCallBackAction(p0: Int?, p1: Any?, p2: Any?) {

    }

}