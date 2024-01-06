package com.angelhr28.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
class RecipeEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "preparation")
    var preparation: String,
    @ColumnInfo(name = "preparationTime")
    var preparationTime: String,
    @ColumnInfo(name = "numberOfDishes")
    var numberOfDishes: Int,
    @ColumnInfo(name = "imageUrl")
    var imageUrl: String,
    @ColumnInfo(name = "ingredients")
    var ingredients: String,
    @ColumnInfo(name = "originLocation")
    var originLocation: String
)
