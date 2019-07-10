package com.itsmeantonio.to_do.Model

import org.json.JSONObject

class User constructor(val id: String, val email: String) {
    override fun toString(): String {
        val json = JSONObject()
        json.put("id", id)
        json.put("email", email)

        return json.toString()
    }
}