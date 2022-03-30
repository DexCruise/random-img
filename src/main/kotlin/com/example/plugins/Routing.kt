package com.example.plugins

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
                call.respondRedirect(permanent = false, url="/?i=" + randomImage())
            }
            if (bus != null) {
                call.respondFile(Images.images[bus.toInt()])
            }
        }
    }
}

object Images {
    var images: Array<File> = File("images").listFiles()!!
}

fun randomImage(): Int {
    return Random().nextInt(Images.images.size)
}
