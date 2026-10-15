package com.happyminds.app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.happyminds.app.data.UserRepository
import com.happyminds.app.ui.auth.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val repo = UserRepository(this)
            val user = repo.restoreSession()

            val intent = when {
                user == null                              -> Intent(this, LoginActivity::class.java)
                user.childName.isEmpty() || user.grade.isEmpty() ->
                    Intent(this, com.happyminds.app.ui.auth.SetupProfileActivity::class.java)
                else                                      -> Intent(this, MainActivity::class.java)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }, 1500)
    }
}
