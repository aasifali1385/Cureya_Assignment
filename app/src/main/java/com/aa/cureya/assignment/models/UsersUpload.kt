package com.aa.cureya.assignment.models

class UsersUpload {
    var name: String? = null
    var email: String? = null
    var mobile: String? = null

    var key: String? = null

    constructor() {}

    constructor(name: String?, email: String?, mobile: String?,  key: String?) {
        this.name = name
        this.email = email
        this.mobile = mobile

        this.key = key
    }
}