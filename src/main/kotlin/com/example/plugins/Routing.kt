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
                call.respondFile(Images.images[bus.toInt()])
            }
        }
        get("/web") {
            val bus = call.parameters["i"]

            if (bus == null) {
                val id = randomImage()
                call.respondRedirect("/web?i=$id", permanent = false)
            } else {
                call.respondFile(Images.images[bus.toInt()])
            }
        }
    }
}

object Images {
    val images: Array<File> = File("images").listFiles()!!
    fun getImg(n: Int): File? {
        return if (n >= images.size) null
        else images[n]
    }
}

fun randomImage(): Int {
    return Random().nextInt(Images.images.size)
}
