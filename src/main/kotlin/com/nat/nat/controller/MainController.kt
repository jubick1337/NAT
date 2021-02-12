package com.nat.nat.controller

import com.nat.nat.entity.Role
import com.nat.nat.entity.User
import com.nat.nat.repos.UserRepo
import com.nat.nat.services.ApiWorker
import com.nat.nat.services.Oauth2Service
import com.nat.nat.utility.setFields
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import java.util.*
import org.springframework.security.core.userdetails.User as SpringUser


@Controller
class MainController(
    @Autowired
    @Qualifier("googleOauthService") var googleService: Oauth2Service,
    @Autowired
    @Qualifier("spotifyOauthService") var spotifyService: Oauth2Service,
    @Autowired
    @Qualifier("spotifyApiWorkerService") var spotifyApiWorkerService: ApiWorker,
    @Autowired
    @Qualifier("youtubeApiWorkerService") var youtubeApiWorkerService: ApiWorker
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
    fun services(model: MutableMap<String?, Any?>): String {
        model["username"] = SecurityContextHolder.getContext().authentication.name
        return "services"
    }

    @GetMapping("/getPlaylistInfo", params = ["from"])
    fun getPlaylistInfo(from: String): String {
        val springUser = SecurityContextHolder.getContext().authentication.principal as SpringUser
        val username: String = springUser.username
        val userFromDb: User? = userRepo?.findByUsername(username)
        val currentWorker: ApiWorker = if (from == "google") youtubeApiWorkerService else spotifyApiWorkerService
        val token = if (from == "google") userFromDb?.googleToken else userFromDb?.spotifyToken
        if (userFromDb != null) {
            currentWorker.getPlaylist(token)
        }
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