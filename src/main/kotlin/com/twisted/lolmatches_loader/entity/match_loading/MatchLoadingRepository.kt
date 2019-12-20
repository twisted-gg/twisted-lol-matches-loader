package com.twisted.lolmatches_loader.entity.match_loading

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import java.util.stream.Stream

@Repository
interface MatchLoadingRepository : MongoRepository<MatchLoadingDocument, String> {

  @Query("{ matches: { \$elemMatch: { game_id: ?0 } }, region: \"?1\" }")
  fun findMatch(game_id: Long, region: String): List<MatchLoadingDocument>

  @Query("{ healthy: false, \"matches.loading\": true }")
  fun findOneHealthy(pageable: Pageable = PageRequest.of(0, 1)): Stream<MatchLoadingDocument>
}
