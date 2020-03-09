package io.cordacademy.webserver.controllers

import io.cordacademy.webserver.Controller
import net.corda.core.crypto.SecureHash
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream

@RestController
@RequestMapping("/attachments")
class AttachmentController : Controller() {

    @GetMapping(produces = ["application/json"])
    fun getAttachmentExists(@RequestParam("id") id: String) = response {
        mapOf("exists" to rpc.attachmentExists(SecureHash.parse(id)))
    }

    @PostMapping(produces = ["application/json"])
    fun upload(@RequestParam("file") file: MultipartFile) = response {
        val id = rpc.uploadAttachment(ByteArrayInputStream(file.bytes))
        mapOf("attachmentId" to id.toString())
    }
}