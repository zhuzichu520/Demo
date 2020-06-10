package com.hiwitech.android.shared.ext

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.view.*
import android.view.View.*
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.forEachIndexed
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.hiwitech.android.libs.tool.showKeyboard
import com.hiwitech.android.libs.tool.toCast
import com.hiwitech.android.shared.R
import com.hiwitech.android.shared.global.AppGlobal.context
import com.hiwitech.android.shared.tools.MainExecutor
import com.hiwitech.android.widget.badge.Badge
import com.hiwitech.android.widget.badge.QBadgeView
import com.hiwitech.android.widget.toast.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executor


fun BottomNavigationView.setupWithViewPager(viewPager: ViewPager) {
    viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            this@setupWithViewPager.menu.getItem(position).isChecked = true
        }
    })
    this.setOnNavigationItemSelectedListener {
        this@setupWithViewPager.menu.forEachIndexed { index, item ->
            if (it.itemId == item.itemId) {
                viewPager.setCurrentItem(index, false)
            }
        }
        true
    }
}

fun View.showSoftKeyboard() {
    showKeyboard(this.context, this)
}

fun Context.isDark(): Boolean {
    val mode = this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return mode == Configuration.UI_MODE_NIGHT_YES
}

fun String.toast(): Toast {
    return toast(context, this)
}

fun Int.toast(): Toast {
    return toast(context, this)
}

fun BottomNavigationView.plusBadge(index: Int): Badge {
    val menuView: BottomNavigationMenuView = this.getChildAt(0).toCast()
    val itemView: BottomNavigationItemView = menuView.getChildAt(index).toCast()
    val badge = QBadgeView(context).bindTarget(itemView)
    badge.badgeGravity = Gravity.TOP or Gravity.START
    itemView.post {
        badge.setGravityOffset(itemView.width.toFloat() / 2, 0f, false)
    }
    return badge
}

fun EditText.clean() {
    this.setText("")
}

fun RecyclerView.openDefaultAnimator() {
    this.itemAnimator?.addDuration = 120
    this.itemAnimator?.changeDuration = 250
    this.itemAnimator?.moveDuration = 250
    this.itemAnimator?.removeDuration = 120
    this.itemAnimator?.let {
        (it as? SimpleItemAnimator)?.supportsChangeAnimations = true
    }
}

fun RecyclerView.closeDefaultAnimator() {
    this.itemAnimator?.addDuration = 0
    this.itemAnimator?.changeDuration = 0
    this.itemAnimator?.moveDuration = 0
    this.itemAnimator?.removeDuration = 0
    this.itemAnimator?.let {
        (it as? SimpleItemAnimator)?.supportsChangeAnimations = false
    }
}

fun View.fitSystemWindows() {
    systemUiVisibility =
        SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
}

fun View.onWindowInsets(action: (View, WindowInsets) -> Unit) {
    this.requestApplyInsets()
    this.setOnApplyWindowInsetsListener { v, insets ->
        action(v, insets)
        insets
    }
}

var View.topMargin: Int
    get() = (this.layoutParams as ViewGroup.MarginLayoutParams).topMargin
    set(value) {
        val params = this.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = value
        this.layoutParams = params
    }

var View.bottomMargin: Int
    get() = (this.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin
    set(value) {
        val params = this.layoutParams as ViewGroup.MarginLayoutParams
        params.bottomMargin = value
        this.layoutParams = params
    }

var View.startMargin: Int
    get() = (this.layoutParams as ViewGroup.MarginLayoutParams).marginStart
    set(value) {
        val params = this.layoutParams as ViewGroup.MarginLayoutParams
        params.marginStart = value
        this.layoutParams = params
    }

var View.endMargin: Int
    get() = (this.layoutParams as ViewGroup.MarginLayoutParams).marginEnd
    set(value) {
        val params = this.layoutParams as ViewGroup.MarginLayoutParams
        params.marginEnd = value
        this.layoutParams = params
    }

fun ImageButton.toggleButton(
    flag: Boolean, rotationAngle: Float, @DrawableRes firstIcon: Int, @DrawableRes secondIcon: Int,
    action: (Boolean) -> Unit
) {
    if (flag) {
        if (rotationY == 0f) rotationY = rotationAngle
        animate().rotationY(0f).apply {
            setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    action(!flag)
                }
            })
        }.duration = 200
        GlobalScope.launch(Dispatchers.Main) {
            delay(100)
            setImageResource(firstIcon)
        }
    } else {
        if (rotationY == rotationAngle) rotationY = 0f
        animate().rotationY(rotationAngle).apply {
            setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    action(!flag)
                }
            })
        }.duration = 200
        GlobalScope.launch(Dispatchers.Main) {
            delay(100)
            setImageResource(secondIcon)
        }
    }
}

fun ViewGroup.circularReveal(button: ImageButton) {
    this.visibility = VISIBLE
    ViewAnimationUtils.createCircularReveal(
        this,
        button.x.toInt() + button.width / 2,
        button.y.toInt() + button.height / 2,
        0f,
        if (button.context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) this.width.toFloat() else this.height.toFloat()
    ).apply {
        duration = 500
    }.start()
}

fun ViewGroup.circularClose(button: ImageButton, action: () -> Unit = {}) {
    ViewAnimationUtils.createCircularReveal(
        this,
        button.x.toInt() + button.width / 2,
        button.y.toInt() + button.height / 2,
        if (button.context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) this.width.toFloat() else this.height.toFloat(),
        0f
    ).apply {
        duration = 500
        doOnStart { action() }
        doOnEnd { this@circularClose.visibility = INVISIBLE }
    }.start()
}


fun Context.mainExecutor(): Executor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
    mainExecutor
} else {
    MainExecutor()
}

fun View.showSnackbar(
    @StringRes resId: Int,
    duration: Int? = null,
    maxLines: Int? = null,
    @StringRes actionId: Int? = null,
    onClickListener: OnClickListener? = null
) {
    val snacker = Snackbar.make(this, resId, duration ?: 3000)
    val textView = snacker.view.findViewById<TextView>(R.id.snackbar_text)
    textView.maxLines = maxLines ?: 3
    snacker.setAction(actionId ?: R.string.ok, onClickListener ?: OnClickListener {

    }).show()
}