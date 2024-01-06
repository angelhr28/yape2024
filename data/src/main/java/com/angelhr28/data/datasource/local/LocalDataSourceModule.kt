package com.angelhr28.data.datasource.local

import com.angelhr28.data.datasource.local.recipe.RecipeLocalDataSource
import com.angelhr28.data.datasource.local.recipe.RecipeLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindRecipeLocalDataSource(
        recipeLocalDataSourceImpl: RecipeLocalDataSourceImpl
    ): RecipeLocalDataSource
}
