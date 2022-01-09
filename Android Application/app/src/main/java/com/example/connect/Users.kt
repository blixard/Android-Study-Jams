package com.example.iterconnect

import java.io.Serializable

class Users : Serializable {
    //    setter
    //    getter
    var personName: String? = null
    var personGivenName: String? = null
    var personFamilyName: String? = null
    var personEmail: String? = null
    var personId: String? = null
    var personPhoto: String? = null
    var messages = ""
    var rooms = "chatbot;allchat"

    constructor() {}
    constructor(
        personName: String?,
        personGivenName: String?,
        personFamilyName: String?,
        personEmail: String?,
        personId: String?,
        personPhoto: String?
    ) {
        this.personName = personName
        this.personGivenName = personGivenName
        this.personFamilyName = personFamilyName
        this.personEmail = personEmail
        this.personId = personId
        this.personPhoto = personPhoto
    }

    //    custom methods
    //    method to add personal chat room of the user to rooms
    fun addPersonalRoom(userId: String) {
        rooms = "$rooms;$userId"
    }
}