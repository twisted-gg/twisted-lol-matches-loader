package com.twisted.lolmatches_loader.mapper.match.participant.perks

import com.twisted.dto.match.participant.perks.MatchParticipantPerks
import net.rithms.riot.api.endpoints.match.dto.ParticipantStats

/**
 * Participant perks
 */
fun participantPerks(stats: ParticipantStats): MatchParticipantPerks {
  return MatchParticipantPerks(
          perk0 = stats.perk0,
          perk1 = stats.perk1,
          perk2 = stats.perk2,
          perk3 = stats.perk3,
          perk4 = stats.perk4,
          perk5 = stats.perk5
  )
}
