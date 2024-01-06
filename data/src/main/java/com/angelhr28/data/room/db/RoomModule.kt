package com.angelhr28.data.room.db

import android.content.Context
import androidx.room.Room
import com.angelhr28.data.room.dao.RecipeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    private const val DATABASE_NAME = "Yape.db"

    @Singleton
    @Provides
    fun provideYapeDataBase(@ApplicationContext context: Context): YapeDataBase {
        return Room.databaseBuilder(
            context = context,
            klass = YapeDataBase::class.java,
            name = DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideRecipeDao(db: YapeDataBase): RecipeDao = db.recipeDao()
}
