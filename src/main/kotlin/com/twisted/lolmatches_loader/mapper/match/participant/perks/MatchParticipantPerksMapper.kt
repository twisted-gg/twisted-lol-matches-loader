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
          perk5 = stats.perk5,
          perk0Var1 = stats.perk0Var1.toInt(),
          perk0Var2 = stats.perk0Var2.toInt(),
          perk0Var3 = stats.perk0Var3.toInt(),
          perk1Var1 = stats.perk1Var1.toInt(),
          perk1Var2 = stats.perk1Var2.toInt(),
          perk1Var3 = stats.perk1Var3.toInt(),
          perk2Var1 = stats.perk2Var1.toInt(),
          perk2Var2 = stats.perk2Var2.toInt(),
          perk2Var3 = stats.perk2Var3.toInt(),
          perk3Var1 = stats.perk3Var1.toInt(),
          perk3Var2 = stats.perk3Var2.toInt(),
          perk3Var3 = stats.perk3Var3.toInt(),
          perk4Var1 = stats.perk4Var1.toInt(),
          perk4Var2 = stats.perk4Var2.toInt(),
          perk4Var3 = stats.perk4Var3.toInt(),
          perk5Var1 = stats.perk5Var1.toInt(),
          perk5Var2 = stats.perk5Var2.toInt(),
          perk5Var3 = stats.perk5Var3.toInt()
  )
}
