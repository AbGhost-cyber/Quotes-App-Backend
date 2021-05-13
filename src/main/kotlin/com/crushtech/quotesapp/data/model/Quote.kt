package com.crushtech.quotesapp.data.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Quote(
    val author: String,
    val quote: String,
    var isFavorite: Boolean = false,
    @BsonId
    val id: String = ObjectId().toString()
)