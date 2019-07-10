package com.itsmeantonio.to_do.Services

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.itsmeantonio.to_do.Controller.App
import com.itsmeantonio.to_do.Model.User
import com.itsmeantonio.to_do.Utilities.URL_LOGIN
import com.itsmeantonio.to_do.Utilities.URL_REFRESH_TOKEN
import com.itsmeantonio.to_do.Utilities.URL_SIGNUP
import org.json.JSONException
import org.json.JSONObject

object AuthService {

    var user: User? = null

    fun logout() {
        App.prefs.authToken = ""
        App.prefs.authRefreshToken = ""
        App.prefs.userEmail = ""
        App.prefs.userId = ""
        App.prefs.isLoggedIn = false
    }

    fun refreshToken(complete: (Boolean) -> Unit) {

        val refreshTokenRequest = object : JsonObjectRequest(Method.POST, URL_REFRESH_TOKEN, null, Response.Listener { res ->

            try {
                if (res.getInt("errorCode") == 0) {
                    App.prefs.authToken = res.getString("token")
                    App.prefs.isLoggedIn = true
                    complete(true)
                } else {
                    complete(false)
                }
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: ${e.localizedMessage}")
                complete(false)
            }

        }, Response.ErrorListener { err ->
            Log.d("ERROR", "Could not refresh token: $err")
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authRefreshToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(refreshTokenRequest)
    }

    fun signup(email: String, password: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val signupRequest = object : JsonObjectRequest(Method.POST, URL_SIGNUP, null, Response.Listener{ res ->

            try {
                if (res.getInt("errorCode") == 0) {
                    complete(true)
                } else {
                    complete(false)
                }
            } catch(e: JSONException) {
                Log.d("JSON", "EXC: ${e.localizedMessage}")
                complete(false)
            }

        }, Response.ErrorListener { err ->
            Log.d("ERROR", "Could not create account: $err")
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        App.prefs.requestQueue.add(signupRequest)
    }

    fun login(email: String, password: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val loginRequest = object : JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener { res ->

            try {
                if (res.getInt("errorCode") == 0) {
                    App.prefs.authToken = res.getString("token")
                    App.prefs.authRefreshToken = res.getString("refreshToken")
                    val userObj = res.getJSONObject("user")
                    App.prefs.userId = userObj.getString("id")
                    App.prefs.userEmail = userObj.getString("email")
                    App.prefs.isLoggedIn = true

                    this.user = User(App.prefs.userId!!, App.prefs.userEmail!!)
                    complete(true)
                } else {
                    println(res.getString("message"))
                    complete(false)
                }
            } catch(e: JSONException) {
                Log.d("JSON", "EXC: ${e.localizedMessage}")
                complete(false)
            }

        }, Response.ErrorListener { err ->
            Log.d("ERROR", "Could not login: $err")
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        App.prefs.requestQueue.add(loginRequest)
    }
}