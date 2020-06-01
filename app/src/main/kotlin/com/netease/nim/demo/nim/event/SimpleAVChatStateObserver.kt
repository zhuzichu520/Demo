package com.netease.nim.demo.nim.event

import com.netease.nimlib.sdk.avchat.AVChatStateObserver
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame
import com.netease.nimlib.sdk.avchat.model.AVChatNetworkStats
import com.netease.nimlib.sdk.avchat.model.AVChatSessionStats
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame
import com.netease.nrtc.sdk.common.VideoFilterParameter
import com.netease.nrtc.sdk.video.VideoFrame

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/29 11:12 AM
 * since: v 1.0.0
 */
open class SimpleAVChatStateObserver : AVChatStateObserver {
    override fun onUnsubscribeAudioResult(result: Int) {
    }

    override fun onAudioMixingEvent(event: Int) {
    }

    override fun onUnpublishVideoResult(result: Int) {
    }

    override fun onAudioRecordingStart(fileDir: String?) {
    }

    override fun onLiveEvent(event: Int) {
    }

    override fun onVideoFrameResolutionChanged(
        account: String?,
        width: Int,
        height: Int,
        rotate: Int
    ) {
    }

    override fun onProtocolIncompatible(status: Int) {
    }

    override fun onJoinedChannel(code: Int, audioFile: String?, videoFile: String?, elapsed: Int) {
    }

    override fun onReportSpeaker(speakers: MutableMap<String, Int>?, mixedEnergy: Int) {
    }

    override fun onAudioDeviceChanged(device: Int, set: MutableSet<Int>?, shouldSelect: Boolean) {
    }

    override fun onAudioEffectPlayEvent(effectId: Int, event: Int) {
    }

    override fun onSessionStats(sessionStats: AVChatSessionStats?) {
    }

    override fun onRemoteUnpublishVideo(account: String?) {
    }

    override fun onVideoFpsReported(account: String?, fps: Int) {
    }

    override fun onAVRecordingStart(account: String?, fileDir: String?) {
    }

    override fun onAVRecordingCompletion(account: String?, filePath: String?) {
    }

    override fun onUserLeave(account: String?, event: Int) {
    }

    override fun onCallEstablished() {
    }

    override fun onAudioMixingProgressUpdated(progressMs: Long, durationMs: Long) {
    }

    override fun onSubscribeVideoResult(account: String?, videoType: Int, result: Int) {
    }

    override fun onUserJoined(account: String?) {
    }

    override fun onAudioFrameFilter(frame: AVChatAudioFrame?): Boolean {
        return false
    }

    override fun onTakeSnapshotResult(account: String?, success: Boolean, file: String?) {
    }

    override fun onNetworkQuality(account: String?, quality: Int, stats: AVChatNetworkStats?) {
    }

    override fun onVideoFrameFilter(frame: AVChatVideoFrame?, maybeDualInput: Boolean): Boolean {
        return false
    }

    override fun onVideoFrameFilter(
        input: VideoFrame?,
        outputFrames: Array<out VideoFrame>?,
        filterParameter: VideoFilterParameter?
    ): Boolean {
        return false
    }

    override fun onRemotePublishVideo(account: String?, videoTypes: IntArray?) {
    }

    override fun onUnsubscribeVideoResult(account: String?, videoType: Int, result: Int) {
    }

    override fun onDisconnectServer(code: Int) {
    }

    override fun onAudioRecordingCompletion(filePath: String?) {
    }

    override fun onSubscribeAudioResult(result: Int) {
    }

    override fun onAudioEffectPreload(effectId: Int, result: Int) {
    }

    override fun onDeviceEvent(code: Int, desc: String?) {
    }

    override fun onConnectionTypeChanged(netType: Int) {
    }

    override fun onLeaveChannel() {
    }

    override fun onFirstVideoFrameAvailable(account: String?) {
    }

    override fun onPublishVideoResult(result: Int) {
    }

    override fun onLowStorageSpaceWarning(availableSize: Long) {
    }

    override fun onFirstVideoFrameRendered(account: String?) {
    }
}