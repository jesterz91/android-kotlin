package io.github.jesterz91.pagingwithnetwork.api

data class RepoSearchResponse(
    val total_count: Int,
    val incomplete_results: String,
    val items: List<Repo>
)

data class Repo(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val url: String,
    val stargazers_count: Int,
    val forks: Int,
    val language: String?
)