package com.netease.nim.demo.di


import com.hiwitech.android.mvvm.di.ActivityScoped
import com.netease.nim.demo.ui.avchat.ActivityAvchat
import com.netease.nim.demo.ui.avchat.module.ModuleAvchat
import com.netease.nim.demo.ui.camera.ActivityCamera
import com.netease.nim.demo.ui.camera.module.ModuleCamera
import com.netease.nim.demo.ui.contact.module.ModuleContact
import com.netease.nim.demo.ui.dialog.module.ModuleOptions
import com.netease.nim.demo.ui.file.ActivityBrowseFile
import com.netease.nim.demo.ui.file.ActivityFile
import com.netease.nim.demo.ui.file.module.ModuleBrowseFile
import com.netease.nim.demo.ui.file.module.ModuleFile
import com.netease.nim.demo.ui.launcher.ActivityLauncher
import com.netease.nim.demo.ui.launcher.module.ModuleLauncher
import com.netease.nim.demo.ui.login.ActivityLogin
import com.netease.nim.demo.ui.login.main.module.ModuleLogin
import com.netease.nim.demo.ui.login.register.module.ModuleRegister
import com.netease.nim.demo.ui.main.ActivityMain
import com.netease.nim.demo.ui.main.module.ModuleMain
import com.netease.nim.demo.ui.map.ActivityAmap
import com.netease.nim.demo.ui.map.module.ModuleAmap
import com.netease.nim.demo.ui.me.main.module.ModuleMe
import com.netease.nim.demo.ui.message.ActivityMessage
import com.netease.nim.demo.ui.message.emoticon.module.ModuleEmoticon
import com.netease.nim.demo.ui.message.filedownload.ActivityFileDownload
import com.netease.nim.demo.ui.message.filedownload.module.ModuleFileDownload
import com.netease.nim.demo.ui.message.main.module.ModuleMessage
import com.netease.nim.demo.ui.message.more.module.ModuleMore
import com.netease.nim.demo.ui.permissions.module.ModulePermissions
import com.netease.nim.demo.ui.photobrowser.ActivityPhotoBrowser
import com.netease.nim.demo.ui.photobrowser.ActivityPhotoBrowserGif
import com.netease.nim.demo.ui.photobrowser.module.ModuleBrowseImage
import com.netease.nim.demo.ui.photobrowser.module.ModuleBrowseImageGif
import com.netease.nim.demo.ui.photobrowser.module.ModuleBrowseVideo
import com.netease.nim.demo.ui.photobrowser.module.ModulePhotoBrowser
import com.netease.nim.demo.ui.session.module.ModuleSession
import com.netease.nim.demo.ui.theme.module.ModuleTheme
import com.netease.nim.demo.ui.web.ActivityWeb
import com.netease.nim.demo.ui.web.module.ModuleWeb
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * todo 按模块抽出Module
 * desc Activity Module
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ModuleLauncher::class,
            ModulePermissions::class
        ]
    )
    internal abstract fun launcherActivity(): ActivityLauncher

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ModuleMain::class,
            ModuleMe::class,
            ModuleSession::class,
            ModuleContact::class,
            ModuleTheme::class
        ]
    )
    internal abstract fun mainActivity(): ActivityMain

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ModuleLogin::class,
            ModuleRegister::class
        ]
    )
    internal abstract fun loginActivity(): ActivityLogin


    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ModuleAmap::class
        ]
    )
    internal abstract fun amapActivity(): ActivityAmap


    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ModuleCamera::class
        ]
    )
    internal abstract fun cameraActivity(): ActivityCamera

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ModulePhotoBrowser::class,
            ModuleBrowseImage::class,
            ModuleBrowseVideo::class
        ]
    )
    internal abstract fun photoBrowserActivity(): ActivityPhotoBrowser

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ModuleBrowseImageGif::class
        ]
    )
    internal abstract fun photoBrowserGifActivity(): ActivityPhotoBrowserGif

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ModuleMore::class,
            ModuleMessage::class,
            ModuleEmoticon::class,
            ModulePermissions::class,
            ModuleOptions::class
        ]
    )
    internal abstract fun messageActivity(): ActivityMessage

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ModuleFile::class,
            ModulePermissions::class
        ]
    )
    internal abstract fun fileActivity(): ActivityFile

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ModuleBrowseFile::class,
            ModulePermissions::class
        ]
    )
    internal abstract fun browseFileActivity(): ActivityBrowseFile

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ModuleFileDownload::class,
            ModulePermissions::class
        ]
    )
    internal abstract fun fileDownloadActivity(): ActivityFileDownload

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ModuleWeb::class,
            ModulePermissions::class,
            ModuleOptions::class
        ]
    )
    internal abstract fun webActivity(): ActivityWeb


    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ModuleAvchat::class,
            ModulePermissions::class,
            ModuleOptions::class
        ]
    )
    internal abstract fun avchatActivity(): ActivityAvchat


}