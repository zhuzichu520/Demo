package com.netease.nim.demo.ui.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.RegeocodeResult
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.mvvm.event.SingleLiveEvent
import com.hiwitech.android.shared.bus.LiveDataBus
import com.hiwitech.android.shared.ext.createCommand
import com.hiwitech.android.shared.ext.map
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.ui.map.event.EventMap
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import javax.inject.Inject

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/6 10:09 PM
 * since: v 1.0.0
 */
class ViewModelAmap @Inject constructor(
) : ViewModelBase<ArgDefault>() {

    /**
     * 会话列表的会话数据
     */
    val pois = MutableLiveData<List<ItemViewModelPoi>>()

    /**
     * 会话列表所有数据
     */
    val items: LiveData<List<Any>> =
        Transformations.map<List<ItemViewModelPoi>, List<Any>>(pois) { input ->
            val list = ArrayList<Any>()
            list.addAll(input)
            list
        }

    /**
     * 注册布局
     */
    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelPoi>(BR.item, R.layout.item_map_poi)
    }

    /**
     * 取消
     */
    val onCancelCommand = createCommand {
        back()
    }

    /**
     * 发送地图消息
     */
    val onSendCommand = createCommand {
        onClickPoiItemEvent.value?.let {
            LiveDataBus.post(EventMap.OnSendLocationEvent(it))
        }
        back()
    }

    /**
     * PoiItem的点击事件
     */
    val onClickPoiItemEvent = SingleLiveEvent<ItemViewModelPoi>()

    /**
     * 刷新poi数据,默认选择第一项
     */
    fun updatePois(result: RegeocodeResult, latLonPoint: LatLonPoint) {
        pois.value = result.regeocodeAddress.pois.mapIndexed { index, poiItem ->
            val latLon = if (index == 0) latLonPoint else poiItem.latLonPoint
            ItemViewModelPoi(
                this,
                poiItem,
                result.regeocodeAddress,
                index,
                latLon,
                onClickPoiItemEvent
            )
        }
        onClickPoiItemEvent.value = pois.value?.get(0)
    }

    fun selectPoiItem(itemViewModel: ItemViewModelPoi?) {
        if (itemViewModel == null)
            return
        pois.value?.let {
            pois.value = it.map { item ->
                item.apply {
                    showSelected.value = itemViewModel.index == index
                }
            }
        }
    }

}