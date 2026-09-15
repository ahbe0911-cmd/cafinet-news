package com.cafinet.news.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("SELECT * FROM news ORDER BY publishedAtEpochMillis DESC")
    fun observeAll(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): NewsEntity?

    @Query(
        "SELECT * FROM news " +
            "WHERE title LIKE '%' || :query || '%' " +
            "OR description LIKE '%' || :query || '%' " +
            "OR source LIKE '%' || :query || '%' " +
            "OR channelUsername LIKE '%' || :query || '%' " +
            "ORDER BY publishedAtEpochMillis DESC",
    )
    suspend fun search(query: String): List<NewsEntity>

    @Query("SELECT DISTINCT channelUsername FROM news ORDER BY channelUsername")
    suspend fun getChannelUsernames(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<NewsEntity>)

    @Query("DELETE FROM news WHERE channelUsername NOT IN (:activeChannels)")
    suspend fun deleteInactiveChannels(activeChannels: List<String>)

    @Query(
        "DELETE FROM news WHERE id IN (" +
            "SELECT id FROM news WHERE channelUsername = :channelUsername " +
            "ORDER BY publishedAtEpochMillis DESC LIMIT -1 OFFSET :keepCount" +
            ")",
    )
    suspend fun pruneChannel(channelUsername: String, keepCount: Int)

    @Transaction
    suspend fun cacheChannel(channelUsername: String, items: List<NewsEntity>, keepCount: Int = 200) {
        upsertAll(items)
        pruneChannel(channelUsername, keepCount)
    }

    @Query("DELETE FROM news")
    suspend fun clearAll()
}
