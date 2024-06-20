package dev.toby7002.ddj

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class DailyDadJokes {
    companion object {
        suspend fun getAJoke(): String {
            val client = HttpClient(CIO)

            val res: HttpResponse = client.request("https://icanhazdadjoke.com/") {
                method = HttpMethod.Get
                headers {
                    append(HttpHeaders.Accept, "text/plain")
                }
            }

            if(res.status !== HttpStatusCode.OK) {
                return "Cannot find a joke :("
            }

            return res.bodyAsText()
        }
    }
}