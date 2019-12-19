package com.twisted.lolmatches_loader

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableAsync
@EnableScheduling
class LolMatchesLoaderApplication

fun main(args: Array<String>) {
  runApplication<LolMatchesLoaderApplication>(*args)
}
