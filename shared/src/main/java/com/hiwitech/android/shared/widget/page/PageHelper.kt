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

    private var itemHeader: ItemViewModelNetwork? = null
    private var itemFooter: ItemViewModelNetwork? = null


    val onLoadMoreCommand = createCommand {
        if (ItemViewModelNetwork.STATE_END == getFooterState()) {
            return@createCommand
        }
        setFooterState(ItemViewModelNetwork.STATE_LOADING)
        if (ItemViewModelNetwork.STATE_LOADING == getFooterState()) {
            MainHandler.postDelayed { onLoadMore?.invoke() }
        }
    }

    val onRefreshCommand = createCommand {
        if (ItemViewModelNetwork.STATE_END == getHeaderState()) {
            return@createCommand
        }
        setHeaderState(ItemViewModelNetwork.STATE_LOADING)
        if (ItemViewModelNetwork.STATE_LOADING == getHeaderState()) {
            MainHandler.postDelayed { onRefresh?.invoke() }
        }
    }

    private val onRetryCommand = createCommand {
        onRetry?.invoke()
    }

    init {
        onLoadMore?.let {
            itemFooter = ItemViewModelNetwork(viewModel, onRetryCommand)
        }
        onRefresh?.let {
            itemHeader = ItemViewModelNetwork(viewModel, onRetryCommand)
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
        map<ItemViewModelNetwork>(BR.item, R.layout.item_network)
        map<ItemViewModelNull>(BR.item, R.layout.item_null)
    }

    fun <T> add(list: List<T>, isReverse: Boolean = true): List<T> {
        if (isReverse) {
            if (list.size < pageSize) {
                setHeaderState(ItemViewModelNetwork.STATE_END)
            } else {
                setHeaderState(ItemViewModelNetwork.STATE_FINISH)
            }
            items.value = list.plus(items.value ?: listOf()).toCast()
        } else {
            if (list.size < pageSize) {
                setFooterState(ItemViewModelNetwork.STATE_END)
            } else {
                setFooterState(ItemViewModelNetwork.STATE_FINISH)
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
        return itemHeader?.state?.value ?: ItemViewModelNetwork.STATE_DEFAULT
    }

    private fun getFooterState(): Int {
        return itemFooter?.state?.value ?: ItemViewModelNetwork.STATE_DEFAULT
    }
}