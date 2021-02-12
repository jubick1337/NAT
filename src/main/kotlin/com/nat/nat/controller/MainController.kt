package com.nat.nat.controller

import com.github.scribejava.apis.GoogleApi20
import com.github.scribejava.core.model.OAuth2AccessToken
import com.github.scribejava.core.oauth.OAuth20Service
import com.nat.nat.entity.Role
import com.nat.nat.entity.User
import com.nat.nat.repos.UserRepo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import java.util.*
import com.github.scribejava.apis.HHApi

import com.github.scribejava.core.builder.ServiceBuilder
import org.springframework.beans.factory.annotation.Value


@Controller
class MainController {

    @Autowired
    private val userRepo: UserRepo? = null

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

    @GetMapping("/addGoogle")
    fun addSpotify(): String? {
        val googleClientId: String = "365295871686-1jfq9997me458vnspsb2amcrp04jtgq6.apps.googleusercontent.com"

        val googelClientSecret: String = "z3NSgED2bH8ft8ofeCtULfEL"

        var service: OAuth20Service = ServiceBuilder(googleClientId)
                .apiSecret(googelClientSecret)
                .callback("http://localhost:8080/hello")
                .defaultScope("profile")
                .build(GoogleApi20.instance())

        val authorizationUrl = service.getAuthorizationUrl()
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