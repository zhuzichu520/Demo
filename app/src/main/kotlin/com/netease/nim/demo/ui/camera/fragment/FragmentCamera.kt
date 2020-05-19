package com.netease.nim.demo.ui.camera.fragment

import android.annotation.SuppressLint
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.shared.bus.LiveDataBus
import com.hiwitech.android.shared.ext.loge
import com.hiwitech.android.shared.widget.camera.FlowCameraView.BUTTON_STATE_BOTH
import com.hiwitech.android.shared.widget.camera.listener.FlowCameraListener
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentCameraBinding
import com.netease.nim.demo.ui.camera.event.EventCamera
import com.netease.nim.demo.ui.camera.viewmodel.ViewModelCamera
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.File

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/9 9:23 PM
 * since: v 1.0.0
 */
@SuppressLint("RestrictedApi")
class FragmentCamera : FragmentBase<FragmentCameraBinding, ViewModelCamera, ArgDefault>() {

    companion object {
        private val TAG: String = FragmentCamera::class.java.simpleName
    }


    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_camera

    override fun initView() {
        super.initView()
        // 绑定生命周期 您就不用关心Camera的开启和关闭了 不绑定无法预览
        view_camera.setBindToLifecycle(this)
        // 设置白平衡模式
//        flowCamera.setWhiteBalance(WhiteBalance.AUTO)
        // 设置只支持单独拍照拍视频还是都支持
        // BUTTON_STATE_ONLY_CAPTURE  BUTTON_STATE_ONLY_RECORDER  BUTTON_STATE_BOTH
        view_camera.setCaptureMode(BUTTON_STATE_BOTH)
        // 开启HDR
//        flowCamera.setHdrEnable(Hdr.ON)
        // 设置最大可拍摄小视频时长 S
        view_camera.setRecordVideoMaxTime(10)
        // 设置拍照或拍视频回调监听
        view_camera.setFlowCameraListener(object : FlowCameraListener {
            // 录制完成视频文件返回
            override fun recordSuccess(file: File) {
                LiveDataBus.post(EventCamera.OnCameraVideoEvent(file.path))
                requireActivity().finish()
            }

            // 操作拍照或录视频出错
            override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
                videoCaptureError.toString().plus("----").plus(message).plus("---").plus(
                    cause.toString()
                ).loge(TAG, cause)
            }

            // 拍照返回
            override fun captureSuccess(file: File) {
                LiveDataBus.post(EventCamera.OnCameraImageEvent(file.path))
                requireActivity().finish()
            }
        })
        //左边按钮点击事件
        view_camera.setLeftClickListener {
            requireActivity().finish()
        }
    }

}