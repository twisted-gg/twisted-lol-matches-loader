package com.twisted.lolmatches_loader.mapper.match.participant.items

import net.rithms.riot.api.endpoints.match.dto.ParticipantStats

/**
 * Participant items
 */
fun participantItems(stats: ParticipantStats) = listOf(
        stats.item0,
        stats.item1,
        stats.item2,
        stats.item3,
        stats.item4,
        stats.item5,
        stats.item6
)