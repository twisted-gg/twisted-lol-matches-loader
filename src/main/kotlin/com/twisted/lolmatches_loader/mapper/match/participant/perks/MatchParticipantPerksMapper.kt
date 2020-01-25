package com.twisted.lolmatches_loader.mapper.match.participant.perks

import net.rithms.riot.api.endpoints.match.dto.ParticipantStats

/**
 * Participant perks
 */
fun participantPerks(stats: ParticipantStats) =
        listOf(
                stats.perk0,
                stats.perk1,
                stats.perk2,
                stats.perk3,
                stats.perk4,
                stats.perk5
        )
