package com.angelhr28.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.angelhr28.data.room.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Transaction
    suspend fun save(data: List<RecipeEntity>) {
        deleteAll()
        insert(data)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: List<RecipeEntity>)

    @Query("SELECT * FROM recipe")
    fun getRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipe WHERE id = :recipeId")
    suspend fun getRecipeById(recipeId: String): RecipeEntity?

    @Query("DELETE FROM recipe")
    suspend fun deleteAll()
}
