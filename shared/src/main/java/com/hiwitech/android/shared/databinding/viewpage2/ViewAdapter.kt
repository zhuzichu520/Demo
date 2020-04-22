package com.hiwitech.android.shared.databinding.viewpage2

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.viewpager2.widget.ViewPager2

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/22 5:13 PM
 * since: v 1.0.0
 */
@BindingAdapter("currentIndex")
fun currentIndex(viewPager: ViewPager2, index: Int?) {
    index?.let {
        viewPager.setCurrentItem(it, false)
    }
}

@InverseBindingAdapter(attribute = "currentIndex", event = "OnPageChangeCallback")
fun getCurrentIndex(viewPager: ViewPager2): Int {
    return viewPager.currentItem
}

@BindingAdapter("OnPageChangeCallback")
fun setOnPageChangeCallback(
    viewPager: ViewPager2,
    bindingListener: InverseBindingListener?
) {
    bindingListener?.let {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager2.SCROLL_STATE_IDLE)
                    bindingListener.onChange()
            }
        })
    }
}