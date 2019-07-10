package com.itsmeantonio.to_do.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.itsmeantonio.to_do.R
import com.itsmeantonio.to_do.Services.AuthService

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        if (App.prefs.isLoggedIn && App.prefs.authRefreshToken != "") {
            AuthService.refreshToken { success ->
                if (success) {
                    val homeIntent = Intent(this, HomeActivity::class.java)
                    startActivity(homeIntent)
                    finish()
                }
            }
        }
    }
}
