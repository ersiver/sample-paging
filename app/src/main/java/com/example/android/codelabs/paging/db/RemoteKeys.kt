package com.example.android.codelabs.paging.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  Stores the next and previous page keys for each Repo
 */
@Entity(tableName = "remote_keys")
class RemoteKeys(
        @PrimaryKey
        val repoId: Long,
        val prevKey: Int?,
        val nextKey: Int?) {

}