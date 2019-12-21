package com.twisted.lolmatches_loader.mapper.match.participant.stats

import com.twisted.dto.match.participant.stats.MatchParticipantStats
import net.rithms.riot.api.endpoints.match.dto.ParticipantStats

/**
 * Extract participant stats
 */
fun participantStats(stats: ParticipantStats): MatchParticipantStats {
  return MatchParticipantStats(
          champLevel = stats.champLevel,
          combatPlayerScore = stats.combatPlayerScore,
          damageDealtToObjectives = stats.damageDealtToObjectives,
          damageDealtToTurrets = stats.damageDealtToTurrets,
          damageSelfMitigated = stats.damageSelfMitigated,
          doubleKills = stats.doubleKills,
          firstBloodAssist = stats.isFirstBloodAssist,
          firstBloodKill = stats.isFirstBloodKill,
          firstInhibitorAssist = stats.isFirstInhibitorAssist,
          firstInhibitorKill = stats.isFirstInhibitorKill,
          firstTowerAssist = stats.isFirstTowerAssist,
          firstTowerKill = stats.isFirstTowerKill,
          goldEarned = stats.goldEarned,
          goldSpent = stats.goldSpent,
          inhibitorKills = stats.inhibitorKills,
          killingSprees = stats.killingSprees,
          largestCriticalStrike = stats.largestCriticalStrike,
          largestKillingSpree = stats.largestKillingSpree,
          largestMultiKill = stats.largestMultiKill,
          longestTimeSpentLiving = stats.longestTimeSpentLiving,
          magicDamageDealt = stats.magicDamageDealt,
          magicDamageDealtToChampions = stats.magicDamageDealtToChampions,
          magicalDamageTaken = stats.magicalDamageTaken,
          neutralMinionsKilled = stats.neutralMinionsKilled,
          neutralMinionsKilledEnemyJungle = stats.neutralMinionsKilledEnemyJungle,
          neutralMinionsKilledTeamJungle = stats.neutralMinionsKilledTeamJungle,
          objectivePlayerScore = stats.objectivePlayerScore,
          participantId = stats.participantId,
          pentaKills = stats.pentaKills,
          physicalDamageDealt = stats.physicalDamageDealt,
          physicalDamageDealtToChampions = stats.physicalDamageDealtToChampions,
          physicalDamageTaken = stats.physicalDamageTaken,
          quadraKills = stats.quadraKills,
          sightWardsBoughtInGame = stats.sightWardsBoughtInGame,
          teamObjective = stats.teamObjective,
          timeCCingOthers = stats.timeCCingOthers.toLong(),
          totalDamageDealt = stats.totalDamageDealt,
          totalDamageDealtToChampions = stats.totalDamageDealtToChampions,
          totalDamageTaken = stats.totalDamageTaken,
          totalHeal = stats.totalHeal,
          totalMinionsKilled = stats.totalMinionsKilled,
          totalPlayerScore = stats.totalPlayerScore,
          totalScoreRank = stats.totalScoreRank,
          totalTimeCrowdControlDealt = stats.totalTimeCrowdControlDealt,
          totalUnitsHealed = stats.totalUnitsHealed,
          tripleKills = stats.tripleKills,
          trueDamageDealt = stats.trueDamageDealt,
          trueDamageDealtToChampions = stats.trueDamageDealtToChampions,
          trueDamageTaken = stats.trueDamageTaken,
          turretKills = stats.turretKills,
          unrealKills = stats.unrealKills,
          visionScore = stats.visionScore,
          visionWardsBoughtInGame = stats.visionWardsBoughtInGame,
          wardsKilled = stats.wardsKilled,
          wardsPlaced = stats.wardsPlaced
  )
}
