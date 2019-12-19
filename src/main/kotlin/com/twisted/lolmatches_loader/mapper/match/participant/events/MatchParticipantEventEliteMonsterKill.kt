package com.twisted.lolmatches_loader.mapper.match.participant.events

import com.twisted.dto.match.participant.events.MatchParticipantEventsEliteMonsterKill
import com.twisted.lolmatches_loader.mapper.match.MatchEventsEnum
import net.rithms.riot.api.endpoints.match.dto.MatchEvent

fun isEliteMonsterKillEvent(event: MatchEvent): Boolean =
        MatchEventsEnum.ELITE_MONSTER_KILL.toString() == event.type

fun parseEliteMonsterKillEvent(event: MatchEvent) = MatchParticipantEventsEliteMonsterKill(
        timestamp = event.timestamp,
        position = getPosition(event),
        type = event.monsterType,
        subType = event.monsterSubType
)
