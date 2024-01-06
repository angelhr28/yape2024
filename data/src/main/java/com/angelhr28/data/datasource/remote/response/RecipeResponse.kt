package com.angelhr28.data.datasource.remote.response

import com.google.gson.annotations.SerializedName

class RecipeResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("preparation")
    val preparation: String?,
    @SerializedName("preparationTime")
    val preparationTime: String?,
    @SerializedName("numberOfDishes")
    val numberOfDishes: Int?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("ingredients")
    val ingredients: List<String>?,
    @SerializedName("originLocation")
    val originLocation: LocationResponse?
)
