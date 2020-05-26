package com.netease.nim.demo.base

import androidx.databinding.ViewDataBinding
import com.hiwitech.android.mvvm.base.BaseArg
import com.hiwitech.android.mvvm.base.BaseBottomDialogFragment
import com.hiwitech.android.mvvm.base.BaseViewModel

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/26 9:56 AM
 * since: v 1.0.0
 */
abstract class BottomDialogFragmentBase<TBinding : ViewDataBinding, TViewModel : BaseViewModel<TArg>, TArg : BaseArg> :
    BaseBottomDialogFragment<TBinding, TViewModel, TArg>() {

}