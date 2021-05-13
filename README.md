# Ktor Quotes-App-Backend
simple quotes api generator made with ktor and mongodb ktor libary kmongo 🚀


#HOW TO USE:
- install intellij, clone this repo and build project.
- currently this server runs on localhost.
- you can also use postman to test each endpoint before integrating on client side.
</br>



| END POINTS      | METHOD        | FUNCTION |
| ------------- |:-------------:| :-----|
| /quotes/      | GET | this endpoint returns all quotes available in the database, to get all favorited quotes specify "fav" as the query paremeter and set to true, for example: "https://yourdomain/quotes?fav=true".|,
| /quotes/{id}/      | GET | this endpoint returns a specific quote. |
| /quote/ | POST      |  this endpoint saves a quote to the database. |
| /quote/fav/{id}/ | POST      |  this endpoint saves a quote to the database. |
/quotes/  | DELETE | this endpoint deletes a specific quote from the database. |

