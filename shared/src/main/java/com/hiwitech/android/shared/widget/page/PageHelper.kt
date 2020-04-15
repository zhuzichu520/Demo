package com.hiwitech.android.shared.widget.page

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.libs.internal.MainHandler
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.shared.BR
import com.hiwitech.android.shared.R
import com.hiwitech.android.shared.ext.createCommand
import com.hiwitech.android.shared.ext.map
import me.tatarka.bindingcollectionadapter2.collections.AsyncDiffObservableList
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList
import me.tatarka.bindingcollectionadapter2.collections.MergeObservableList
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class PageHelper(
    val viewModel: BaseViewModel<*>,
    val items: ObservableList<Any>,
    val pageSize: Int,
    onLoadMore: (() -> Unit)? = null,
    onRefresh: (() -> Unit)? = null,
    onRetry: (() -> Unit)? = null
) {

    private var itemHeader: ItemViewModelNetworkHeader? = null
    private var itemFooter: ItemViewModelNetworkFooter? = null

    val isRefresh = MutableLiveData<Boolean>()

    val onLoadMoreCommand = createCommand {
        if (ItemViewModelNetworkFooter.STATE_END == getFooterState()) {
            return@createCommand
        }
        setFooterState(ItemViewModelNetworkFooter.STATE_LOADING)
        if (ItemViewModelNetworkFooter.STATE_LOADING == getFooterState()) {
            onLoadMore?.invoke()
        }
    }

    val onRefreshCommand = createCommand {
        if (ItemViewModelNetworkFooter.STATE_END == getHeaderState()) {
            return@createCommand
        }
        setHeaderState(ItemViewModelNetworkFooter.STATE_LOADING)
        if (ItemViewModelNetworkHeader.STATE_LOADING == getHeaderState()) {
            onRefresh?.invoke()
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

    val pageItems: MergeObservableList<Any> = MergeObservableList<Any>().apply {
        insertList(items)
        itemFooter?.let {
            insertItem(it)
        }
    }

    val pageItemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelNetworkFooter>(BR.item, R.layout.item_network_footer)
        map<ItemViewModelNetworkHeader>(BR.item, R.layout.item_network_header)
    }

    fun add(list: List<Any>, isReverse: Boolean = true): List<Any> {
        if (isReverse) {
            val data = list + items
            (items as? DiffObservableList)?.let {
                items.update(data)
            }
            (items as? AsyncDiffObservableList)?.let {
                items.update(data)
            }
            (items as? ObservableArrayList)?.let {
                items.addAll(0, list)
            }
            MainHandler.postDelayed {
                if (list.size < pageSize) {
                    setHeaderState(ItemViewModelNetworkHeader.STATE_END)
                } else {
                    setHeaderState(ItemViewModelNetworkHeader.STATE_FINISH)
                }
            }
        } else {
            val data = items + list
            (items as? DiffObservableList)?.let {
                items.update(data)
            }
            (items as? AsyncDiffObservableList)?.let {
                items.update(data)
            }
            (items as? ObservableArrayList)?.let {
                items.addAll(list)
            }
            MainHandler.postDelayed {
                if (list.size < pageSize) {
                    setFooterState(ItemViewModelNetworkFooter.STATE_END)
                } else {
                    setFooterState(ItemViewModelNetworkFooter.STATE_FINISH)
                }
            }
        }
        return items
    }

    private fun setHeaderState(state: Int) {
        itemHeader?.state?.value = state
        isRefresh.value = ItemViewModelNetworkHeader.STATE_LOADING == state
    }

    private fun setFooterState(state: Int) {
        itemFooter?.state?.value = state
    }

    private fun getHeaderState(): Int {
        return itemHeader?.state?.value ?: ItemViewModelNetworkHeader.STATE_DEFAULT
    }

    private fun getFooterState(): Int {
        return itemFooter?.state?.value ?: ItemViewModelNetworkFooter.STATE_DEFAULT
    }
}