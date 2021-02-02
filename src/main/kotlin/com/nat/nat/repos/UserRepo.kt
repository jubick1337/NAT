package com.nat.nat.repos


import com.nat.nat.entity.User
import org.springframework.data.jpa.repository.JpaRepository



interface UserRepo : JpaRepository<User?, Long?> {
    fun findByUsername(username: String?): User?
}