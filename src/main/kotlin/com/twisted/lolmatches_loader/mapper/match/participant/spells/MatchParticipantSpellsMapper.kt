package com.twisted.lolmatches_loader.mapper.match.participant.spells

import com.twisted.dto.match.participant.spells.MatchParticipantSpells
import net.rithms.riot.api.endpoints.match.dto.Participant

fun getParticipantSpells(participant: Participant) = MatchParticipantSpells(
        first = participant.spell1Id,
        second = participant.spell2Id
)