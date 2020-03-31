package com.netease.nim.demo.nim.tools

import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.team.TeamService
import com.netease.nimlib.sdk.team.model.Team

object ToolTeam {

    fun getTeam(contactId: String): Team {
        return NIMClient.getService(TeamService::class.java).queryTeamBlock(contactId)
    }

}