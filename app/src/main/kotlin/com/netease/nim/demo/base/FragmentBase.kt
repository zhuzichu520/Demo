package com.netease.nim.demo.base

import androidx.databinding.ViewDataBinding
import com.hiwitech.android.mvvm.base.BaseArg
import com.hiwitech.android.mvvm.base.BaseFragment
import com.hiwitech.android.mvvm.base.BaseViewModel

/**
 * desc Fragment基类
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
abstract class FragmentBase<TBinding : ViewDataBinding, TViewModel : BaseViewModel<TArg>, TArg : BaseArg> :
    BaseFragment<TBinding, TViewModel, TArg>() {

}