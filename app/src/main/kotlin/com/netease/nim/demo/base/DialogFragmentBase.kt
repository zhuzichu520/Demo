package com.netease.nim.demo.base

import androidx.databinding.ViewDataBinding
import com.hiwitech.android.mvvm.base.BaseArg
import com.hiwitech.android.mvvm.base.BaseDialogFragment
import com.hiwitech.android.mvvm.base.BaseViewModel

/**
 * desc DialogFragment基类
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
abstract class DialogFragmentBase<TBinding : ViewDataBinding, TViewModel : BaseViewModel<TArg>, TArg : BaseArg> :
    BaseDialogFragment<TBinding, TViewModel, TArg>() {

}