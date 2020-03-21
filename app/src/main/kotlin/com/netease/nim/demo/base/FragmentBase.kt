package com.netease.nim.demo.base

import androidx.databinding.ViewDataBinding
import com.zhuzichu.android.mvvm.base.BaseArg
import com.zhuzichu.android.mvvm.base.BaseFragment
import com.zhuzichu.android.mvvm.base.BaseViewModel

abstract class FragmentBase<TBinding : ViewDataBinding, TViewModel : BaseViewModel<TArg>, TArg : BaseArg> :
    BaseFragment<TBinding, TViewModel, TArg>() {

}