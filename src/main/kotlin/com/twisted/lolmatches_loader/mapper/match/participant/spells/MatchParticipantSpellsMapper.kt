package com.twisted.lolmatches_loader.mapper.match.participant.spells

import net.rithms.riot.api.endpoints.match.dto.Participant

fun getParticipantSpells(participant: Participant) = listOf(
        participant.spell1Id,
        participant.spell2Id
)