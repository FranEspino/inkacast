package com.inkamedia.inkacast.HosLayer.SettingsModule

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.inkamedia.inkacast.ContextApp
import com.inkamedia.inkacast.MainActivity
import com.inkamedia.inkacast.R
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
        //go to Main Activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}