package com.example.connect


class Message {
    //    setter
    //    getter
    var messageId: String? = null
    var roomId: String? = null
    var userId: String? = null
    var userName: String? = null
    var message: String? = null
    var date: String? = null
    var time: String? = null

    constructor() {}
    constructor(
        messageId: String?,
        roomId: String?,
        userId: String?,
        userName: String?,
        message: String?,
        date: String?,
        time: String?
    ) {
        this.messageId = messageId
        this.roomId = roomId
        this.userId = userId
        this.userName = userName
        this.message = message
        this.date = date
        this.time = time
    }
}