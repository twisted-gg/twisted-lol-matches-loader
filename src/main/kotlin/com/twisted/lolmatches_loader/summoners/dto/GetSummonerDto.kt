package com.twisted.lolmatches_loader.summoners.dto

enum class ListRegions {
  LA1
}

data class GetSummonerDto(
        val summonerName: String,
        val accountID: String = "",
        val region: ListRegions
)
