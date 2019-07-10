package com.itsmeantonio.to_do.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.itsmeantonio.to_do.R
import com.itsmeantonio.to_do.Services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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

    fun onLogin(view: View) {
        val email = txtEmailLogin.text.toString()
        val password = txtPasswordLogin.text.toString()

        AuthService.login(email, password) { success ->
            if (success) {
                val homeIntent = Intent(this, HomeActivity::class.java)
                startActivity(homeIntent)
                finish()
            }
        }
    }

    fun onGoToSignup(view: View) {
        val signupIntent = Intent(this, SignupActivity::class.java)
        startActivity(signupIntent)
    }
}
