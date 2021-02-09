package com.nat.nat.controller

import com.nat.nat.entity.Role
import com.nat.nat.entity.User
import com.nat.nat.repos.UserRepo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import java.util.*

import com.nat.nat.services.Oauth2Service
import com.nat.nat.utility.setFields
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.userdetails.User as SpringUser


@Controller
class MainController(
    @Autowired
    @Qualifier("googleService") var googleService: Oauth2Service,
    @Autowired
    @Qualifier("spotifyService") var spotifyService: Oauth2Service

) {
    @Autowired
    private val userRepo: UserRepo? = null
    private val map: Map<String, String> = mapOf("google" to "googleToken", "spotify" to "spotifyToken")

    @GetMapping("/")
    fun main(model: Map<String?, Any?>?): String {
        return "index"
    }

    @GetMapping("/hello")
    fun index(model: MutableMap<String?, Any?>): String {
        model["username"] = SecurityContextHolder.getContext().authentication.name
        return "hello"
    }

    @GetMapping("/registration")
    fun registration(): String {
        return "registration"
    }

    @GetMapping("/services")
    fun services(): String {
        return "services"
    }


    @GetMapping("/services", params = ["from", "code"])
    fun services(from: String, code: String): String {
        val currentService: Oauth2Service = if (from == "google") googleService else spotifyService
        val token: String = currentService.getToken(code)!!.accessToken
        val springUser = SecurityContextHolder.getContext().authentication.principal as SpringUser
        val username: String = springUser.username
        val userFromDb: User? = userRepo?.findByUsername(username)
        val attrName: String = map[from] as String

        if (userFromDb != null) {
            setFields(userFromDb, listOf(Pair(attrName, token)))
            userRepo?.save(userFromDb)
        }
        return "services"
    }

    @GetMapping("/addGoogle")
    fun addGoogle(): String? {
        val authorizationUrl = googleService.getUrl()
        return "redirect:${authorizationUrl}"
    }

    @GetMapping("/addSpotify")
    fun addSpotify(): String? {
        val authorizationUrl = spotifyService.getUrl()
        return "redirect:${authorizationUrl}"
    }

    @PostMapping("/registration")
    fun addUser(user: User, model: MutableMap<String?, Any?>): String? {
        val userFromDb = userRepo!!.findByUsername(user.username)
        if (userFromDb != null) {
            model["message"] = "User exists!"
            return "registration"
        }
        user.isActive = true
        user.roles = Collections.singleton(Role.USER)
        userRepo.save(user)
        return "redirect:/login"
    }
}