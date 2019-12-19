package com.twisted.lolmatches_loader.mapper.match.participant.frames

import com.twisted.dto.match.participant.frames.MatchParticipantFrames
import com.twisted.dto.match.participant.frames.MatchParticipantFramesPosition
import net.rithms.riot.api.endpoints.match.dto.MatchFrame
import net.rithms.riot.api.endpoints.match.dto.MatchParticipantFrame

private fun parsePosition(event: MatchParticipantFrame): MatchParticipantFramesPosition {
  val x = event.position?.x ?: 0
  val y = event.position?.y ?: 0
  return MatchParticipantFramesPosition(
          x = x,
          y = y
  )
}

private fun parseFrame(event: MatchParticipantFrame, frame: Int): MatchParticipantFrames =
        MatchParticipantFrames(
                position = parsePosition(event),
                currentGold = event.currentGold,
                totalGold = event.totalGold,
                level = event.level,
                xp = event.xp,
                totalMinionsKilled = event.minionsKilled + event.jungleMinionsKilled,
                minionsKilled = event.minionsKilled,
                jungleMinionsKilled = event.jungleMinionsKilled,
                teamScore = event.teamScore,
                frame = frame.toByte()
        )

fun matchParticipantFrames(frames: List<MatchFrame>, participantId: Int): List<MatchParticipantFrames> {
  val response = mutableListOf<MatchParticipantFrames>()
  for ((i, frame) in frames.withIndex()) {
    val participantFrame = frame.participantFrames[participantId] ?: continue
    response.add(parseFrame(participantFrame, i))
  }
  return response
}