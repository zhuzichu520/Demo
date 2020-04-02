package com.zhuzichu.android.shared.widget.page

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zhuzichu.android.libs.internal.MainHandler
import com.zhuzichu.android.mvvm.base.BaseViewModel
import com.zhuzichu.android.shared.BR
import com.zhuzichu.android.shared.R
import com.zhuzichu.android.shared.ext.createCommand
import com.zhuzichu.android.shared.ext.createTypeCommand
import com.zhuzichu.android.shared.ext.map
import com.zhuzichu.android.shared.ext.toast
import com.zhuzichu.android.shared.http.entity.ResponsePageList
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList
import me.tatarka.bindingcollectionadapter2.collections.MergeObservableList
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import java.lang.ref.WeakReference

class PageHelper(
    val viewModel: BaseViewModel<*>,
    private val items: DiffObservableList<Any>,
    private var isFirstLoad: Boolean = true,
    private val onLoadMore: (Int.() -> Unit)? = null,
    private val onRefresh: (() -> Unit)? = null,
    private val onRetry: (() -> Unit)? = null
) {
    var page = 0
    private var weakRefresh: WeakReference<SwipeRefreshLayout?>? = null

    private val onClickRetry = createCommand {
        onBottomCommand.execute()
        onRetry?.invoke()
    }

    private val networkViewModel = ItemViewModelNetwork(viewModel, onClickRetry)

    val pageItems: MergeObservableList<Any> = MergeObservableList<Any>()
        .insertItem(ItemViewModelNull(viewModel))
        .insertList(items)
        .insertItem(networkViewModel)

    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelNetwork>(BR.item, R.layout.item_network)
        map<ItemViewModelNull>(BR.item, R.layout.item_null)
    }

    val onBottomCommand = createCommand {
        if (!isFirstLoad) {
            showDefalut()
            isFirstLoad = !isFirstLoad
            return@createCommand
        }
        val state = getStatus()
        if (
            state != ItemViewModelNetwork.STATE_LOADING &&
            state != ItemViewModelNetwork.STATE_END
        ) {
            showLoading()
            onLoadMore?.invoke(page)
        }
    }

    val onRefreshConmmand = createTypeCommand<SwipeRefreshLayout> {
        weakRefresh = WeakReference(this)
        if (getStatus() != ItemViewModelNetwork.STATE_LOADING) {
            page = 0
            onLoadMore?.invoke(page)
        } else {
            "数据正在加载中".toast()
            hideRefresh()
        }
        onRefresh?.invoke()
    }

    private fun hideRefresh() {
        weakRefresh?.get()?.isRefreshing = false
    }

    private fun showLoading() {
        networkViewModel.state.value = ItemViewModelNetwork.STATE_LOADING
    }

    fun showError() {
        networkViewModel.state.value = ItemViewModelNetwork.STATE_ERROR
        hideRefresh()
    }

    private fun showFinish() {
        networkViewModel.state.value = ItemViewModelNetwork.STATE_FINISH
    }

    private fun showEnd() {
        networkViewModel.state.value = ItemViewModelNetwork.STATE_END
    }

    private fun showDefalut() {
        networkViewModel.state.value = ItemViewModelNetwork.STATE_DEFAULT
    }

    private fun getStatus(): Int {
        return networkViewModel.state.value ?: ItemViewModelNetwork.STATE_LOADING
    }

    fun <T> put(responsePageList: ResponsePageList<T>?, closure: T.() -> Any) {
        hideRefresh()
        val datas = responsePageList?.datas
        if (datas.isNullOrEmpty()) {
            showEnd()
            return
        }
        val list = datas.map {
            closure.invoke(it)
        }
        if (responsePageList.curPage == 1) {
            items.update(list)
        } else {
            items.update(items.plus(list))
        }
        MainHandler.postDelayed(100) {
            if (responsePageList.curPage ?: 1 >= responsePageList.pageCount ?: 1) {
                showEnd()
            } else {
                showFinish()
                page = page.inc()
            }
        }
    }
}