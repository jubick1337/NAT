package com.nat.nat.controller

import com.github.scribejava.apis.GoogleApi20
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

import com.github.scribejava.core.builder.ServiceBuilder
import com.nat.nat.services.Oauth2Service
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment


@Controller
class MainController(
        @Autowired
        @Qualifier("googleService") var googleService: Oauth2Service
)
{
    @Autowired
    private val env: Environment? = null
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
    fun services(): String {
        return "services"
    }

    @GetMapping("/addGoogle")
    fun addGoogle(): String? {
        val authorizationUrl = googleService.getUrl()
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