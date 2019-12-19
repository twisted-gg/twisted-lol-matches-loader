package com.twisted.lolmatches_loader.mapper.match

import net.rithms.riot.api.endpoints.match.dto.Match

/**
 * IsRemake
 * When both of teams don't have "firstTower" the match was a remake
 */
fun isRemake(match: Match): Boolean {
  for (team in match.teams) {
    if (team.isFirstTower) {
      return false
    }
  }
  return true
}

fun isWin(value: String): Boolean {
  val condition = "Win"
  return value == condition
}

