package com.crushtech.quotesapp.routes


import com.crushtech.quotesapp.data.collections.*
import com.crushtech.quotesapp.data.model.Quote
import com.crushtech.quotesapp.data.response.SimpleResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


fun Route.quotesRouting() {
    route("/quote") {
        get {
            //get all  fav quotes
            if (call.request.queryParameters["fav"] == "true") {
                val favQuotes = fetchAllFavouriteQuotes()
                call.respond(OK, favQuotes)
            } else {
                //get all quotes regardless
                val allQuotes = fetchAllQuotes()
                call.respond(OK, allQuotes)
            }
        }

        get("{id}") {
            val id =
                call.parameters["id"] ?: return@get call.respond(
                    OK,
                    SimpleResponse(false, "Missing or malformed id")
                )

            if (checkIfQuoteExistsById(id)) {
                val quote = getQuote(id)
                call.respond(OK, quote)
            } else {
                call.respond(OK, SimpleResponse(false, "quote not found"))
            }

        }
        post {
            val quote = try {
                call.receive<Quote>()
            } catch (e: ContentTransformationException) {
                call.respond(BadRequest)
                return@post
            }
            if (insertQuote(quote)) {
                call.respond(OK)
            } else {
                call.respond(Conflict)
            }
        }
        //favorite quote
        post("fav/{id}") {
            val quoteId = call.parameters["id"] ?: return@post call.respond(
                OK,
                SimpleResponse(false, "Missing or malformed id")
            )

            fun getMessage(isFav: Boolean): String {
                return if (isFav) "quote favorited" else "quote unfavorited"
            }
            if (checkIfQuoteExistsById(quoteId)) {
                val quote = getQuote(quoteId)
                quote.isFavorite = !quote.isFavorite
                if (insertQuote(quote.also { it.isFavorite = quote.isFavorite })) {
                    call.respond(OK, SimpleResponse(true, getMessage(quote.isFavorite)))
                } else {
                    call.respond(
                        Conflict, SimpleResponse(
                            false,
                            "an unknown error occurred"
                        )
                    )
                }
            } else {
                call.respond(
                    OK,
                    SimpleResponse(false, "quote not found")
                )
            }
        }
        delete("{id}") {
            val quoteId = call.parameters["id"] ?: return@delete call.respond(
                OK,
                SimpleResponse(false, "Missing or malformed id")
            )
            if (checkIfQuoteExistsById(quoteId)) {
                if (deleteQuote(quoteId)) {
                    call.respond(OK, SimpleResponse(true, "quote deleted"))
                }
            } else {
                call.respond(
                    OK,
                    SimpleResponse(false, "quote not found")
                )
            }
        }
    }
}

fun Application.registerQuotesRouting() {
    routing {
        quotesRouting()
    }
}