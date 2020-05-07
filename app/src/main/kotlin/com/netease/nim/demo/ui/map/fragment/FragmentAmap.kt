package com.netease.nim.demo.ui.map.fragment

import android.graphics.Color
import android.graphics.Point
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.animation.Interpolator
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.maps.model.animation.Animation
import com.amap.api.maps.model.animation.TranslateAnimation
import com.amap.api.services.core.LatLonPoint
import com.hiwitech.android.libs.tool.dp2px
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.shared.ext.logi
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentAmapBinding
import com.netease.nim.demo.ui.map.viewmodel.ViewModelAmap
import kotlinx.android.synthetic.main.fragment_amap.*
import kotlin.math.sqrt

/**
 * desc 高德地图定位
 * author: 朱子楚
 * time: 2020/5/6 10:08 PM
 * since: v 1.0.0
 */
class FragmentAmap : FragmentBase<FragmentAmapBinding, ViewModelAmap, ArgDefault>(),
    AMap.OnCameraChangeListener, AMap.OnMyLocationChangeListener {

    private lateinit var amap: AMap

    private var screenMarker: Marker? = null

    private var isFirstLocation = true

    private var zoomLevel = 14f

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_amap

    override fun initView() {
        super.initView()
        initAmap()
        initLocation()
    }

    /**
     * 初始化定位
     */
    private fun initLocation() {
    }

    /**
     * 初始化地图
     */
    private fun initAmap() {

        amap = view_map.map
        amap.setOnCameraChangeListener(this)

        setupLocationStyle()

        val uiSettings = amap.uiSettings
        uiSettings.isZoomControlsEnabled = false
        uiSettings.isMyLocationButtonEnabled = true

        amap.setOnMyLocationChangeListener(this)
        amap.isMyLocationEnabled = true
    }

    private fun setupLocationStyle() {
        val locationStyle = MyLocationStyle()
        locationStyle.strokeColor(Color.argb(0, 0, 0, 0))
        locationStyle.radiusFillColor(Color.argb(0, 0, 0, 0))
        amap.myLocationStyle =
            locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_map.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        view_map.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        view_map.onPause()
    }

    override fun onResume() {
        super.onResume()
        view_map.onResume()
    }

    override fun onDestroyView() {
        view_map.onDestroy()
        super.onDestroyView()
    }

    override fun onMyLocationChange(location: Location?) {
        if (isFirstLocation && location != null) {
            amap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        location.latitude,
                        location.longitude
                    ), zoomLevel
                )
            )
            addMarkerInScreenCenter()
            isFirstLocation = false
        }
    }


    override fun onCameraChangeFinish(cameraPosition: CameraPosition?) {
        if (cameraPosition == null)
            return
        zoomLevel = cameraPosition.zoom
        //屏幕中心的Marker跳动
        startJumpAnimation()
        val latLng: LatLng = amap.cameraPosition.target
        val latLonPoint = LatLonPoint(latLng.latitude, latLng.longitude)
    }

    override fun onCameraChange(cameraPosition: CameraPosition?) {
    }


    /**
     * 在屏幕中心添加一个Marker
     */
    private fun addMarkerInScreenCenter() {
        val latLng: LatLng = amap.cameraPosition.target
        val screenPosition: Point = amap.projection.toScreenLocation(latLng)
        screenMarker = amap.addMarker(
            MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.purple_pin))
        )
        //设置Marker在屏幕上,不跟随地图移动
        screenMarker?.setPositionByPixels(screenPosition.x, screenPosition.y)
    }


    /**
     * 屏幕中心marker 跳动
     */
    fun startJumpAnimation() {
        if (screenMarker != null) { //根据屏幕距离计算需要移动的目标点
            val latLng = screenMarker!!.position
            val point: Point = amap.projection.toScreenLocation(latLng)
            point.y -= dp2px(requireContext(), 50f)
            val target: LatLng = amap.projection
                .fromScreenLocation(point)
            //使用TranslateAnimation,填写一个需要移动的目标点
            val animation: Animation = TranslateAnimation(target)
            animation.setInterpolator(Interpolator { input ->
                // 模拟重加速度的interpolator
                if (input <= 0.5) {
                    (0.5f - 2 * (0.5 - input) * (0.5 - input)).toFloat()
                } else {
                    (0.5f - sqrt((input - 0.5f) * (1.5f - input).toDouble())).toFloat()
                }
            })
            //整个移动所需要的时间
            animation.setDuration(600)
            //设置动画
            screenMarker?.setAnimation(animation)
            //开始动画
            screenMarker?.startAnimation()
        } else {
            "screenMarker is null".logi("amap")
        }
    }

}