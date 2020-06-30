package com.example.android.codelabs.paging.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.android.codelabs.paging.api.GithubService
import com.example.android.codelabs.paging.db.RepoDatabase
import com.example.android.codelabs.paging.model.Repo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow


/**
 * Repository class that works with local and remote data sources.
 * Paging 3.0 does a lot of things for us:
 * Handles in-memory cache.
 * Requests data when the user is close to the end of the list.
 */
@ExperimentalCoroutinesApi
class GithubRepository(
        private val service: GithubService,
        private val db: RepoDatabase) {

    /**
     * Search repositories whose names match the query,
     * exposed as a stream of data that will emit
     * every time we get more data from the network.
     *
     *  To work with the network only use GithubPagingSource.
     *  To work with the network and database, implement a RemoteMediator.
     */
    @ExperimentalPagingApi
    fun getSearchResultStream(query: String): Flow<PagingData<Repo>> {
        // appending '%' so we can allow other characters to be before and after the query string
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory =  { db.reposDao().reposByName(dbQuery)}

        return Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
                remoteMediator = GithubRemoteMediator(
                        query,
                        service,
                        db
                ),
                pagingSourceFactory = pagingSourceFactory
        ).flow


      //To work with network only:
//        return Pager(config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
//                pagingSourceFactory = { GithubPagingSource(service, query) })
//                .flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}
