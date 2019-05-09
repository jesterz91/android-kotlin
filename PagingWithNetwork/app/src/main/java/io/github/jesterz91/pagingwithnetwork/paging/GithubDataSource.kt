package io.github.jesterz91.pagingwithnetwork.paging

import androidx.paging.PageKeyedDataSource
import io.github.jesterz91.pagingwithnetwork.api.GithubApi
import io.github.jesterz91.pagingwithnetwork.api.Repo
import io.github.jesterz91.pagingwithnetwork.api.RepoSearchResponse
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubDataSource(private val githubApi: GithubApi) : PageKeyedDataSource<Int, Repo>(), AnkoLogger {

    companion object {
        val query = "tarantool"
        val first_page = 1 // 첫 페이지
        val page_size = 40 // 페이지 청크
    }

    // 첫 페이지 로드
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Repo>) {

        githubApi.searchRepos(query, first_page, page_size).enqueue(object : Callback<RepoSearchResponse>{
            override fun onResponse(call: Call<RepoSearchResponse>, response: Response<RepoSearchResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        // 현재페이지의 아이템과, 이전/다음 페이지 키를 전달
                        // 첫 페이지 이므로 이전 key 는 null
                        callback.onResult(it.items, null, first_page+1)
                    }
                }
            }

            override fun onFailure(call: Call<RepoSearchResponse>, t: Throwable) {
                error { t.message }
            }
        })
    }

    // 다음 페이지 로딩
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Repo>) {

        githubApi.searchRepos(query, params.key, page_size).enqueue(object : Callback<RepoSearchResponse>{
            override fun onResponse(call: Call<RepoSearchResponse>, response: Response<RepoSearchResponse>) {
                if (response.isSuccessful) {
                    var nextPageKey : Int? = null
                    val itemCount = params.key * page_size

                    response.body()?.let {
                        // 전체 아이템의 수가 현재까지 로드된 아이템의 수보다 클 경우, 키값을 증가
                        if (it.total_count > itemCount) nextPageKey = params.key + 1

                        info { "total : ${it.total_count}, current : $itemCount" }

                        // 현재페이지의 아이템과 다음 페이지 키값을 전달. 다음 페이지가 없을경우 null 값을 전달
                        callback.onResult(it.items, nextPageKey)
                    }
                }
            }

            override fun onFailure(call: Call<RepoSearchResponse>, t: Throwable) {
                error { t.message }
            }
        })
    }

    // 이전 페이지 로딩
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Repo>) {
        githubApi.searchRepos(query, params.key, page_size).enqueue(object : Callback<RepoSearchResponse>{
            override fun onResponse(call: Call<RepoSearchResponse>, response: Response<RepoSearchResponse>) {
                if (response.isSuccessful) {
                    var beforePagekey : Int? = null

                    // 첫 페이지가 아닐 경우, 키값을 감소
                    if (params.key > first_page) beforePagekey = params.key - 1

                    response.body()?.let {
                        // 현재페이지의 아이템과 이전 페이지 키값을 전달. 이전 페이지가 없을경우 null 값을 전달
                        callback.onResult(it.items, beforePagekey)
                    }
                }
            }

            override fun onFailure(call: Call<RepoSearchResponse>, t: Throwable) {
                error { t.message }
            }
        })
    }
}