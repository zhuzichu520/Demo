package com.netease.nim.demo.nim.tools

import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.team.TeamService
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum
import com.netease.nimlib.sdk.team.model.Team

object ToolTeam {

    fun getTeam(contactId: String): Team {
        return NIMClient.getService(TeamService::class.java).queryTeamBlock(contactId)
    }

    fun getTeamNick(tid: String, account: String): String? {
        val team = getTeam(tid)
        if (team.type == TeamTypeEnum.Advanced) {
            val member =
                NIMClient.getService(TeamService::class.java).queryTeamMemberBlock(tid, account)

            return member?.teamNick
        }
        return null
    }


}