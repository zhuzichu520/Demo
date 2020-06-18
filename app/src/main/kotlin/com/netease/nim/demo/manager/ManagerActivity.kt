package com.netease.nim.demo.manager

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import java.lang.ref.WeakReference

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/15 4:22 PM
 * since: v 1.0.0
 */
class ManagerActivity private constructor() : ActivityLifecycleCallbacks {

    companion object {
        val INST: ManagerActivity = ManagerActivity()
    }

    private lateinit var top: WeakReference<Activity>

    fun init(application: Application?) {
        if (application == null) {
            // do nothing
        } else {
            application.unregisterActivityLifecycleCallbacks(INST)
            application.registerActivityLifecycleCallbacks(INST)
        }
    }

    fun getTopActivity(): Activity? {
        return top.get()
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        top = WeakReference<Activity>(activity)
    }


    override fun onActivityStarted(activity: Activity) {
        top = WeakReference<Activity>(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        top = WeakReference<Activity>(activity)
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }


}