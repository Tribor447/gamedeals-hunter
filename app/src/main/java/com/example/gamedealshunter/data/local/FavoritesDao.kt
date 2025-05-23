package com.example.gamedealshunter.data.local
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorites")
    fun all(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(fav: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE dealId = :id")
    suspend fun remove(id: String)

    @Query("SELECT * FROM favorites WHERE dealId = :id LIMIT 1")
    suspend fun find(id: String): FavoriteEntity?
}
