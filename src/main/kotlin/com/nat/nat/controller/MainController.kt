package com.nat.nat.controller

import com.nat.nat.entity.User
import com.nat.nat.repos.UserRepo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping


@Controller
class MainController {
    @GetMapping("/")
    fun main(model: Map<String?, Any?>?): String {
        return "home"
    }

    @GetMapping("/hello")
    fun index(model: MutableMap<String?, Any?>): String {
        model["username"] = SecurityContextHolder.getContext().authentication.name
        return "hello"
    }

    @Autowired
    private val userRepo: UserRepo? = null

    @GetMapping("/registration")
    fun registration(): String {
        return "registration"
    }

    @PostMapping("/registration")
    fun addUser(user: User, model: MutableMap<String?, Any?>): String {
        val userFromDb: User? = userRepo!!.findByUsername(user.username)
        if (userFromDb != null) {
            model["message"] = "User exists!"
            return "registration"
        }
        user.isActive = true
        userRepo.save(user)
        return "redirect:/login"
    }
}