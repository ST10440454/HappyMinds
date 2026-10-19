package com.happyminds.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.happyminds.app.data.UserRepository
import com.happyminds.app.ui.auth.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(1500)
            val user = UserRepository().restoreSession()

            val intent = when {
                user == null                              -> Intent(this@SplashActivity, LoginActivity::class.java)
                user.childName.isEmpty() || user.grade.isEmpty() ->
                    Intent(this@SplashActivity, com.happyminds.app.ui.auth.SetupProfileActivity::class.java)
                else                                      -> Intent(this@SplashActivity, MainActivity::class.java)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}