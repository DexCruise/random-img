package com.example.plugins

import com.example.plugins.Images.getImg
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import java.io.File
import java.util.Random

fun Application.configureRouting() {

    routing {
        get("/") {
            val bus = call.parameters["i"]

            if (bus == null) {
                val id = randomImage()
                val author = getImg(id)?.name?.replace(Regex("\\s+\\(.*\\)"), "")?.replace(Regex("\\.[a-zA-Z]+$"), "")
                call.respondText(
                    """{
                    |   "location": "/?i=$id",
                    |   "author": "$author"
                    |}""".trimMargin())
            } else {
                if (ApiKeys.keys.contains(call.parameters["apiKey"]))
                    call.respondFile(Images.images[bus.toInt()])
                else
                    call.respondText("an api key is required")
            }
        }
        get("/web") {
            val bus = call.parameters["i"]

            if (bus == null) {
                val id = randomImage()
                call.respondRedirect("/web?i=$id", permanent = false)
            } else {
                if (ApiKeys.contains(call.parameters["apiKey"] ?: "\n")) // set will never contain a newline
                    call.respondFile(Images.images[bus.toInt()])
                else
                    call.respondText("an api key is required")
            }
        }
    }
}

object Images {
    val images: Array<File> = File("images").listFiles()!!
    fun getImg(n: Int): File? {
        return images.getOrNull(n)

    }
}

object ApiKeys {
    val keys = run {
        val s = HashSet<String>()
        s.addAll(File("api_keys").readLines())
        s
    }

    fun contains(s: String): Boolean {
        return keys.contains(s)
    }
}

fun randomImage(): Int {
    return Random().nextInt(Images.images.size)
}

