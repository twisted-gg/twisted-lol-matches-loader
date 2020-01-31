package com.twisted.lolmatches_loader.mapper.match.participant.events

import com.twisted.dto.match.participant.events.MatchParticipantEventsSkillLevelUp
import com.twisted.lolmatches_loader.mapper.match.MatchEventsEnum

import net.rithms.riot.api.endpoints.match.dto.MatchEvent

fun isSkillLevelUpEvent(event: MatchEvent): Boolean =
        MatchEventsEnum.SKILL_LEVEL_UP.toString() == event.type

fun parseSkillLevelUpEvent(event: MatchEvent) =
        MatchParticipantEventsSkillLevelUp(
                timestamp = event.timestamp,
                skillSlot = event.skillSlot
        )
