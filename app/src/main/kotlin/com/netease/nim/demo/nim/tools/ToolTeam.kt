package com.netease.nim.demo.nim.tools

import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.team.TeamService
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum
import com.netease.nimlib.sdk.team.model.Team

object ToolTeam {

    fun getTeam(contactId: String): Team {
        return NIMClient.getService(TeamService::class.java).queryTeamBlock(contactId)
    }


    /**
     * 获取显示名称。用户本人显示“你”
     *
     * @param tid
     * @param account
     * @return
     */
    fun getTeamMemberDisplayNameYou(
        tid: String?,
        account: String
    ): String {
        return if (account == NimUserStorage.account) {
            "你"
        } else getDisplayNameWithoutMe(
            tid,
            account
        )
    }

    private fun getDisplayNameWithoutMe(tid: String?, account: String): String {

        val alias: String? = ToolUserInfo.getAlias(account)
        if (!alias.isNullOrEmpty()) {
            return alias
        }
        if (tid == null)
            return ToolUserInfo.getUserName(account)
        val memberNick: String? = getTeamNick(tid, account)
        if (!memberNick.isNullOrEmpty()) {
            return memberNick
        }
        return ToolUserInfo.getUserName(account)
    }

    private fun getTeamNick(tid: String, account: String): String? {
        val teamService = NIMClient.getService(TeamService::class.java)
        val team = teamService.queryTeamBlock(tid) ?: return null
        if (team.type != TeamTypeEnum.Advanced) {
            return null
        }
        val teamMember = teamService.queryTeamMemberBlock(tid, account) ?: return null
        return teamMember.teamNick
    }


    fun getTeamById(tid: String?): Team? {
        val teamService = NIMClient.getService(TeamService::class.java)
        if (tid == null)
            return null
        return teamService.queryTeamBlock(tid) ?: return null
    }

}