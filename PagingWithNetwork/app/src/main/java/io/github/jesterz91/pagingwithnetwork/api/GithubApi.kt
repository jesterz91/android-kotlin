package io.github.jesterz91.pagingwithnetwork.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *  RepoSearchResponse sample
 *
 *  "total_count": 100,
 *  "incomplete_results": false,
 *  "items": [...]
 */

interface GithubApi {
    @GET("search/repositories?sort=stars")
    fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<RepoSearchResponse>
}