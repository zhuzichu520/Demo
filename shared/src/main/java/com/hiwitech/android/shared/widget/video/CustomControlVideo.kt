package com.hiwitech.android.shared.widget.video

import android.content.Context
import android.util.AttributeSet
import android.view.Surface
import android.view.View
import com.hiwitech.android.shared.R
import com.shuyu.gsyvideoplayer.utils.GSYVideoType

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
import kotlinx.android.synthetic.main.custom_control_video.view.*


/**
 * desc
 * author: 朱子楚
 * time: 2020/5/20 9:51 AM
 * since: v 1.0.0
 */

class CustomControlVideo : StandardGSYVideoPlayer {

    constructor(context: Context?, fullFlag: Boolean?) : super(context, fullFlag)
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun getLayoutId(): Int {
        return R.layout.custom_control_video
    }

    override fun init(context: Context?) {
        super.init(context)
        if (mThumbImageViewLayout != null &&
            (mCurrentState == -1 || mCurrentState == GSYVideoView.CURRENT_STATE_NORMAL || mCurrentState == GSYVideoView.CURRENT_STATE_ERROR)
        ) {
            mThumbImageViewLayout.visibility = View.VISIBLE
        }
    }

    fun showControlView(shown: Boolean) {
        if (shown) {
            start.visibility = View.VISIBLE
            layout_bottom.visibility = View.VISIBLE
        } else {
            start.visibility = View.GONE
            layout_bottom.visibility = View.GONE
        }
    }

    /******************* 下方两个重载方法，在播放开始前不屏蔽封面，不需要可屏蔽  */
    override fun onSurfaceUpdated(surface: Surface?) {
        super.onSurfaceUpdated(surface)
        if (mThumbImageViewLayout != null && mThumbImageViewLayout.visibility == View.VISIBLE) {
            mThumbImageViewLayout.visibility = View.INVISIBLE
        }
    }

    override fun setViewShowState(view: View?, visibility: Int) {
        if (view === mThumbImageViewLayout && visibility != View.VISIBLE) {
            return
        }
        super.setViewShowState(view, visibility)
    }

    override fun onSurfaceAvailable(surface: Surface?) {
        super.onSurfaceAvailable(surface)
        if (GSYVideoType.getRenderType() != GSYVideoType.TEXTURE) {
            if (mThumbImageViewLayout != null && mThumbImageViewLayout.visibility == View.VISIBLE) {
                mThumbImageViewLayout.visibility = View.INVISIBLE
            }
        }
    }

    fun setOnClickCloseListener(listener: OnClickListener) {
        close.setOnClickListener(listener)
    }

}