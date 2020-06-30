package com.example.android.codelabs.paging.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    // Insert a list of RemoteKeys, as whenever
    // we get Repos from the network we will
    // generate the remote keys for them.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(remoteKey: List<RemoteKeys>)

    //Get a RemoteKey based on a Repo id.
    @Query("SELECT * FROM remote_keys WHERE repoId = :repoId")
    suspend fun remoteKeysRepoId(repoId: Long): RemoteKeys?

    //Clear the RemoteKeys, whenever we have a new query.
    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}