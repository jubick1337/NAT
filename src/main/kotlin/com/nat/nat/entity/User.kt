package com.nat.nat.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
@Entity
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
    var username: String? = null
    var password: String? = null
    var isActive = false
    private var youtubeToken: String = ""
    private var spotifyToken: String = ""

}