package com.hiwitech.android.shared.widget.page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hiwitech.android.libs.internal.MainHandler
import com.hiwitech.android.libs.tool.toCast
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.shared.BR
import com.hiwitech.android.shared.R
import com.hiwitech.android.shared.ext.createCommand
import com.hiwitech.android.shared.ext.map
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class PageHelper<T>(
    val viewModel: BaseViewModel<*>,
    val items: MutableLiveData<List<T>>,
    val pageSize: Int,
    onLoadMore: (() -> Unit)? = null,
    onRefresh: (() -> Unit)? = null,
    onRetry: (() -> Unit)? = null
) {

    private var itemHeader: ItemViewModelNetworkHeader? = null
    private var itemFooter: ItemViewModelNetworkFooter? = null

    val onLoadMoreCommand = createCommand {
        if (ItemViewModelNetworkFooter.STATE_END == getFooterState()) {
            return@createCommand
        }
        setFooterState(ItemViewModelNetworkFooter.STATE_LOADING)
        if (ItemViewModelNetworkFooter.STATE_LOADING == getFooterState()) {
            MainHandler.postDelayed(50){
                onLoadMore?.invoke()
            }
        }
    }

    val onRefreshCommand = createCommand {
        if (ItemViewModelNetworkFooter.STATE_END == getHeaderState()) {
            return@createCommand
        }
        setHeaderState(ItemViewModelNetworkFooter.STATE_LOADING)
        if (ItemViewModelNetworkFooter.STATE_LOADING == getHeaderState()) {
            MainHandler.postDelayed(50){
                onRefresh?.invoke()
            }
        }
    }

    private val onRetryCommand = createCommand {
        onRetry?.invoke()
    }

    init {
        onLoadMore?.let {
            itemFooter = ItemViewModelNetworkFooter(viewModel, onRetryCommand)
        }
        onRefresh?.let {
            itemHeader = ItemViewModelNetworkHeader(viewModel, onRetryCommand)
        }
    }


    val pageItems: LiveData<List<Any>> = Transformations.map<List<T>, List<Any>>(items) { input ->
        val list = mutableListOf<Any>()
        itemHeader?.let {
            list.add(it)
        }
        list.addAll(input.toCast())
        itemFooter?.let {
            list.add(it)
        }
        list
    }

    val pageItemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelNetworkFooter>(BR.item, R.layout.item_network_footer)
        map<ItemViewModelNetworkHeader>(BR.item, R.layout.item_network_header)
    }

    fun <T> add(list: List<T>, isReverse: Boolean = true): List<T> {
        if (isReverse) {
            if (list.size < pageSize) {
                setHeaderState(ItemViewModelNetworkFooter.STATE_END)
            } else {
                setHeaderState(ItemViewModelNetworkFooter.STATE_FINISH)
            }
            items.value = list.plus(items.value ?: listOf()).toCast()
        } else {
            if (list.size < pageSize) {
                setFooterState(ItemViewModelNetworkFooter.STATE_END)
            } else {
                setFooterState(ItemViewModelNetworkFooter.STATE_FINISH)
            }
            items.value = (items.value ?: listOf()).plus(list).toCast()
        }
        return (items.value ?: listOf()).toCast()
    }

    private fun setHeaderState(state: Int) {
        itemHeader?.state?.value = state
    }

    private fun setFooterState(state: Int) {
        itemFooter?.state?.value = state
    }

    private fun getHeaderState(): Int {
        return itemHeader?.state?.value ?: ItemViewModelNetworkFooter.STATE_DEFAULT
    }

    private fun getFooterState(): Int {
        return itemFooter?.state?.value ?: ItemViewModelNetworkFooter.STATE_DEFAULT
    }
}