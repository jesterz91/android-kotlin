package io.github.jesterz91.pagingwithnetwork.api

// Github Response

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

// Reddit Response

class ListingResponse(val data: ListingData)

class ListingData(
    val children: List<RedditChildrenResponse>,
    val after: String?,
    val before: String?
)

data class RedditChildrenResponse(
    val data: RedditPost
)

data class RedditPost(
    val name: String,
    val title: String,
    val score: Int,
    val author: String,
    val subreddit: String,
    val num_comments: Int,
    val created_utc: Long,
    val thumbnail: String?,
    val url: String?)