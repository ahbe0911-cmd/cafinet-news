package com.cafinet.news.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("SELECT * FROM news ORDER BY publishedAt DESC")
    fun observeAll(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): NewsEntity?

    @Query("SELECT * FROM news WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    suspend fun search(query: String): List<NewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<NewsEntity>)

    @Query("DELETE FROM news")
    suspend fun clearAll()
}
