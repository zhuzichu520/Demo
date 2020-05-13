package com.netease.nim.demo.ui.photobrowser.viewmodel

import androidx.lifecycle.MutableLiveData
import com.github.chrisbanes.photoview.PhotoView
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.mvvm.event.SingleLiveEvent
import com.hiwitech.android.shared.ext.createTypeCommand
import com.hiwitech.android.shared.tools.Weak
import com.netease.nim.demo.base.ItemViewModelBase

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/11 4:56 PM
 * since: v 1.0.0
 */
class ItemViewModelPhoto(
    viewModel: BaseViewModel<*>,
    url: Any,
    onPhotoTapEvent: SingleLiveEvent<ItemViewModelPhoto>
) : ItemViewModelBase(viewModel) {

    var photoView by Weak<PhotoView>()

    val initPhotoView = createTypeCommand<PhotoView> {
        photoView = this
        maximumScale = 4f
        setZoomTransitionDuration(350)
        scale = 3f

        setOnPhotoTapListener { _, _, _ ->
            if (viewModel is ViewModelPhotoBrowser) {
                onPhotoTapEvent.value = this@ItemViewModelPhoto
            }
        }
    }

    val imageUrl = MutableLiveData<Any>(url)

}