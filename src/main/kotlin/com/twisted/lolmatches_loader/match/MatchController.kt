package com.twisted.lolmatches_loader.match

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping
class MatchController(
        private val service: MatchService
) {
  @PostMapping("load/{id}")
  fun load(@PathVariable id: String): Mono<Unit> = Mono.just(service.loadMatchesById(id))
}