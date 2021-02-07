package com.nat.nat.entity

import javax.persistence.*


@Entity
@Table(name = "usr")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var username: String? = null
    var password: String? = null
    var isActive = false
    var youtubeToken: String? = null
    var spotifyToken: String? = null

    @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = [JoinColumn(name = "user_id")])
    @Enumerated(
        EnumType.STRING
    )
    var roles: Set<Role>? = null
}