package com.inkamedia.inkacast.presentation.screen_settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.inkamedia.inkacast.commons.ContextApp
import com.inkamedia.inkacast.presentation.MainActivity
import com.inkamedia.inkacast.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.swScreenMirror.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
               ContextApp.context.setDarkMode(true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
               ContextApp.context.setDarkMode(false)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}