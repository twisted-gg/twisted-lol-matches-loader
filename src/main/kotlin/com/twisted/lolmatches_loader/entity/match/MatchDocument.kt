package com.twisted.lolmatches_loader.entity.match

import com.twisted.dto.league.MatchLeagueDocument
import com.twisted.dto.match.IMatchDocument
import com.twisted.dto.match.participant.MatchParticipant
import com.twisted.dto.match.team.MatchTeam
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "lol_matches")
data class MatchDocument(
        @Id
        val id: String = "",
        override val region: String,
        override val game_id: Long,
        override val match_break: Boolean,
        override val creation: Long,
        override val mode: Int,
        override val type: Int,
        override val version: String,
        override val map_id: Int,
        override val queue: Int,
        override val season: Int,
        override val remake: Boolean,
        override val duration: Long,
        override val teams: List<MatchTeam>,
        override val participants: List<MatchParticipant>,
        override val league: MatchLeagueDocument,
        override val framesInterval: Long,
        override val createdAt: Long = Date().time,
        override val updatedAt: Long = Date().time
) : IMatchDocument

