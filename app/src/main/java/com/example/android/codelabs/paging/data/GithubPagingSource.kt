package com.example.android.codelabs.paging.data

import androidx.paging.PagingSource
import com.example.android.codelabs.paging.api.GithubService
import com.example.android.codelabs.paging.api.IN_QUALIFIER
import com.example.android.codelabs.paging.model.Repo
import retrofit2.HttpException
import retrofit2.http.HTTP
import java.io.IOException

private const val GITHUB_STARTING_PAGE_INDEX = 1

/**
 * To work with network use GithubPagingSource in the Repository class.
 * To work with the network and database, implement a RemoteMediator
 */

class GithubPagingSource(
        private val service: GithubService,
        private val query: String
) : PagingSource<Int, Repo>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {

        // If this is the first time that load is called, LoadParams.key
        // will be null. In this case, you will have to define the initial
        // page key, which will be GITHUB_STARTING_PAGE_INDEX.
        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX
        val apiQuery = query + IN_QUALIFIER

      return try{
          val response = service.searchRepos(apiQuery, position, params.loadSize)
          val repos = response.items
          LoadResult.Page(
                  data = repos,
                  prevKey = if(position == GITHUB_STARTING_PAGE_INDEX) null else position -1,
                  //If list is empty, we don't have any data left to be loaded; so the nextKey can be null.
                  nextKey = if(repos.isEmpty()) null else position + 1
          )

      } catch(e: IOException){
          return LoadResult.Error(e)
      }catch (e: HttpException){
          return LoadResult.Error(e)
      }
    }

}