package com.twisted.lolmatches_loader.mapper.match.participant.items

import com.twisted.dto.match.participant.items.MatchParticipantItems
import net.rithms.riot.api.endpoints.match.dto.ParticipantStats

/**
 * Participant items
 */
fun participantItems(stats: ParticipantStats): MatchParticipantItems {
  return MatchParticipantItems(
          item0 = stats.item0,
          item1 = stats.item1,
          item2 = stats.item2,
          item3 = stats.item3,
          item4 = stats.item4,
          item5 = stats.item5,
          item6 = stats.item6
  )
}
