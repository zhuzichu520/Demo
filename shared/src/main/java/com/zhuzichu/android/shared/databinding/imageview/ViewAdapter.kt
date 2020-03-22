package com.zhuzichu.android.shared.databinding.imageview

import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import com.facebook.drawee.view.SimpleDraweeView
import com.zhuzichu.android.widget.sharp.Sharp

@BindingAdapter(value = ["url"], requireAll = false)
fun bindSimpleDraweeView(
    simpleDraweeView: SimpleDraweeView,
    url: Any?
) {
    url?.let {
        if (it is String) {
            simpleDraweeView.setImageURI(it)
        } else if (it is Int) {
            simpleDraweeView.setActualImageResource(it)
        }
    }
}

@BindingAdapter(value = ["svgData"], requireAll = false)
fun bindImageViewShap(
    imageView: ImageView,
    svgData: String?
) {
    svgData?.let {
        Sharp.loadString(it).into(imageView)
    }
}

@BindingAdapter(value = ["srcColor"], requireAll = false)
fun bindImageViewSrcColor(
    imageView: ImageView,
    @ColorInt color: Int?
) {
    color?.let {
        imageView.setColorFilter(it)
    }
}

@BindingAdapter(value = ["srcBitmap"], requireAll = false)
fun bindImageViewSrcBitmap(
    imageView: ImageView,
    bitmap: Bitmap?
) {
    bitmap?.let {
        imageView.setImageBitmap(it)
    }
}

@BindingAdapter(value = ["srcDrawable"], requireAll = false)
fun bindImageViewSrcDrawable(
    imageView: ImageView,
    drawable: Drawable?
) {
    drawable?.let {
        imageView.setImageDrawable(it)
    }
}