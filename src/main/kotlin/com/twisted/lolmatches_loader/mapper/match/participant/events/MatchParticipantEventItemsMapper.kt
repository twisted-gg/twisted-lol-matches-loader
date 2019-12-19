package com.twisted.lolmatches_loader.mapper.match.participant.events

import com.twisted.dto.match.participant.events.MatchParticipantEventsItem
import com.twisted.lolmatches_loader.mapper.match.MatchEventsEnum
import net.rithms.riot.api.endpoints.match.dto.MatchEvent

fun isItemEvent(event: MatchEvent): Boolean =
        MatchEventsEnum.ITEM_DESTROYED.toString() == event.type ||
                MatchEventsEnum.ITEM_PURCHASED.toString() == event.type ||
                MatchEventsEnum.ITEM_SOLD.toString() == event.type ||
                MatchEventsEnum.ITEM_UNDO.toString() == event.type

fun parseItemEvent(event: MatchEvent) =
        MatchParticipantEventsItem(
                type = event.type,
                timestamp = event.timestamp,
                itemId = event.itemId
        )
