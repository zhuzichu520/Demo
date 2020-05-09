package com.netease.nim.demo.ui.map.fragment

import android.graphics.Point
import android.location.Location
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.lifecycle.Observer
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.maps.model.animation.*
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
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

    private var breatheMarker: Marker? = null

    private var isFirstLocation = true

    private var zoomLevel = 14f

    private var isOpenSearch = true

    private lateinit var geocodeSearch: GeocodeSearch

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_amap

    override fun initView() {
        super.initView()

        geocodeSearch = GeocodeSearch(requireContext())

        initAmap()
        initLocation()

        btn_location.setOnClickListener {
            val location = amap.myLocation
            isOpenSearch = true
            amap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        location.latitude,
                        location.longitude
                    ), zoomLevel
                )
            )
        }

        amap.addOnMapTouchListener {
            if (it.action == MotionEvent.ACTION_DOWN) {
                isOpenSearch = true
            }
        }
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
        uiSettings.isMyLocationButtonEnabled = false

        amap.setOnMyLocationChangeListener(this)
        amap.isMyLocationEnabled = true

        amap.setOnMapLoadedListener {
            addMarkerInScreenCenter()
        }
    }

    override fun initViewObservable() {
        super.initViewObservable()

        viewModel.onClickPoiItemEvent.observe(viewLifecycleOwner, Observer {
            amap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(it.latLonPoint.latitude, it.latLonPoint.longitude),
                    zoomLevel
                )
            )
            viewModel.selectPoiItem(it)
        })

    }

    private fun setupLocationStyle() {
        // 自定义系统定位蓝点
        val locationStyle = MyLocationStyle()
        locationStyle.showMyLocation(false)
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
        if (location == null)
            return
        if (isFirstLocation) {
            amap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(location.latitude, location.longitude),
                    zoomLevel
                )
            )
            isFirstLocation = false
        }
        startBreatheAnimation(location)
    }


    override fun onCameraChangeFinish(cameraPosition: CameraPosition?) {
        if (cameraPosition == null)
            return
        zoomLevel = cameraPosition.zoom
        //屏幕中心的Marker跳动
        startJumpAnimation()
        val latLng: LatLng = amap.cameraPosition.target
        val latLonPoint = LatLonPoint(latLng.latitude, latLng.longitude)
        if (isOpenSearch) {
            searchLocation(latLonPoint)
            isOpenSearch = false
        }
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
    private fun startJumpAnimation() {
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

    private fun startBreatheAnimation(location: Location) { // 呼吸动画
        if (breatheMarker == null) {
            val latLng = LatLng(location.latitude, location.longitude)
            breatheMarker = amap.addMarker(
                MarkerOptions().position(latLng)
                    .zIndex(1f)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point))
            )
            // 中心的marker
            amap.addMarker(
                MarkerOptions().position(latLng)
                    .zIndex(2f)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point))
            )
        }
        // 动画执行完成后，默认会保持到最后一帧的状态
        val animationSet = AnimationSet(true)
        val alphaAnimation = AlphaAnimation(0.5f, 0f)
        alphaAnimation.setDuration(2000)
        // 设置不断重复
        alphaAnimation.repeatCount = Animation.INFINITE
        val scaleAnimation = ScaleAnimation(1.0f, 3.5f, 1.0f, 3.5f)
        scaleAnimation.setDuration(2000)
        // 设置不断重复
        scaleAnimation.repeatCount = Animation.INFINITE
        animationSet.addAnimation(alphaAnimation)
        animationSet.addAnimation(scaleAnimation)
        animationSet.setInterpolator(LinearInterpolator())
        breatheMarker?.setAnimation(animationSet)
        breatheMarker?.startAnimation()
    }


    private fun searchLocation(latLonPoint: LatLonPoint) {
        val regeocodeQuery = RegeocodeQuery(latLonPoint, 1000f, GeocodeSearch.AMAP)
        geocodeSearch.setOnGeocodeSearchListener(object : GeocodeSearch.OnGeocodeSearchListener {
            /**
             * 逆地理编码回调
             */
            override fun onRegeocodeSearched(result: RegeocodeResult?, code: Int) {
                if (code != AMapException.CODE_AMAP_SUCCESS) {
                    return
                }
                if (result == null)
                    return
                viewModel.updatePois(result,latLonPoint)
            }

            /**
             * 地理编码查询回调
             */
            override fun onGeocodeSearched(result: GeocodeResult?, code: Int) {
                if (code != AMapException.CODE_AMAP_SUCCESS) {
                    return
                }
                if (result == null)
                    return
            }
        })
        geocodeSearch.getFromLocationAsyn(regeocodeQuery)
    }

}