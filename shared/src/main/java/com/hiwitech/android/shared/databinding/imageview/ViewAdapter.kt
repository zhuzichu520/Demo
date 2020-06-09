package com.hiwitech.android.shared.databinding.imageview

import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.RequestOptions
import com.hiwitech.android.shared.glide.GlideApp
import com.hiwitech.android.shared.widget.CheckRadioView
import com.hiwitech.android.widget.sharp.Sharp

@BindingAdapter(value = ["url", "placeholder", "error"], requireAll = false)
fun bindImageViewGlide(
    imageView: ImageView,
    url: Any?,
    placeholder: Int?,
    error: Int?
) {
    url?.apply {
        val requestOptions = RequestOptions()
        val glide = GlideApp.with(imageView).load(url)
        placeholder?.let {
            requestOptions.placeholder(it)
        }
        error?.let {
            requestOptions.error(it)
        }
        glide.apply(requestOptions)
        glide.into(imageView)
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

@BindingAdapter(value = ["src"], requireAll = false)
fun bindImageViewSrcResource(
    imageView: ImageView,
    @DrawableRes resId: Int?
) {
    resId?.let {
        imageView.setImageResource(resId)
        (imageView.drawable as? AnimationDrawable)?.let {animationDrawable ->
            imageView.post {
                animationDrawable.start()
            }
        }
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

@BindingAdapter(value = ["checked"], requireAll = false)
fun bindCheckRadioView(
    checkRadioView: CheckRadioView,
    checked: Boolean?
) {
    checked?.let {
        checkRadioView.setChecked(it)
    }
}