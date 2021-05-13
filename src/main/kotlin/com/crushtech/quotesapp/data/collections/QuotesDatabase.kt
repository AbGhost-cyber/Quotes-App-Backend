package com.crushtech.quotesapp.data.collections

import com.crushtech.quotesapp.data.model.Quote
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

//create mongo client and get it's coroutine version
private val client = KMongo.createClient().coroutine

//create our database
private val database = client.getDatabase("QuotesDatabase")

//create our collection
private val quotes = database.getCollection<Quote>()

//insert all quotes
//suspend fun insertAllQuotes(quoteQuote): Boolean {
//    return quotes.insertMany(list).wasAcknowledged()
//}

//insert one quote
suspend fun insertQuote(quote: Quote): Boolean {
    return if (checkIfQuoteExistsById(quote.id)) {
        quotes.updateOneById(quote.id, quote).wasAcknowledged()
    } else {
        quotes.insertOne(quote).wasAcknowledged()
    }
}

//fetch all notes
suspend fun fetchAllQuotes(): List<Quote> {
    return quotes.find().limit(100).toList()
}

//fetch favorited ones
suspend fun fetchAllFavouriteQuotes(): List<Quote> {
    return fetchAllQuotes().filter { it.isFavorite }
}

suspend fun getQuote(id: String): Quote {
    return quotes.findOneById(id)!!
}

suspend fun checkIfQuoteExistsById(id: String): Boolean {
    return quotes.findOne(Quote::id eq id) != null
}

suspend fun deleteQuote(id: String): Boolean {
    return quotes.deleteOneById(id).wasAcknowledged()
}

//suspend fun main() {
//   println(fetchAllFavouriteQuotes())
//}