package com.example.iterconnect
import java.io.Serializable

class Room : Serializable {
    //    setter
    var id: String? = null
    var roomName: String? = null
    var users: String? = null
    var roomId: String? = null
    var password: String? = null
    var description: String? = null
    var tags: String? = null
    var positions: String? = null

    constructor() {}
    constructor(
        id: String?,
        roomName: String?,
        users: String?,
        roomId: String?,
        password: String?,
        description: String?,
        tags: String?,
        positions: String?
    ) {
        this.id = id
        this.users = users
        this.roomName = roomName
        this.roomId = roomId
        this.password = password
        this.description = description
        this.tags = tags
        this.positions = positions
    }
}