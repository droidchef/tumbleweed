package io.redgreen.tumbleweed.web.observablehq

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

data class BilevelEdgeBundlingGraph(
  val nodes: List<Node>,
  val links: List<Link>,
) {
  companion object {
    fun fromJson(json: String): BilevelEdgeBundlingGraph {
      return jacksonObjectMapper()
        .readValue(json, BilevelEdgeBundlingGraph::class.java)
    }
  }

  fun toJson(): String {
    return jacksonObjectMapper()
      .writerWithDefaultPrettyPrinter()
      .writeValueAsString(this)
  }

  data class Node(
    val id: String,
    val group: Int,
  ) {
    companion object
  }

  data class Link(
    val source: String,
    val target: String,
    val value: Int = 1,
  ) {
    companion object
  }
}
