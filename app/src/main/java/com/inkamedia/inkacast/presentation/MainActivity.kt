package com.inkamedia.inkacast.presentation

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.digenio.inkacast.Chromecast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.inkamedia.inkacast.R
import com.inkamedia.inkacast.databinding.ActivityMainBinding
import com.inkamedia.inkacast.presentation.screen_browser.viewModel.WebpageViewModel
import com.inkamedia.inkacast.presentation.screen_device.DevicesActivity
import com.inkamedia.inkacast.services.StreamService
import com.inkamedia.inkacast.utils.dlna.service.DLNAService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding

    companion object{
          var chromecast: Chromecast? = null
         lateinit var searchItemReference: MenuItem
         lateinit var seveItemReference: MenuItem
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        chromecast = Chromecast(this@MainActivity)

   //     startDLNAService()
        val navController = this.findNavController(R.id.navHostMain)
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        navView.setupWithNavController(navController)

        mBinding.fabDevices.setOnClickListener {
            val intent = Intent(this, DevicesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startDLNAService() {
        val intent = Intent(this, DLNAService::class.java)
        startService(intent)
    }

    override fun onPause() {
        super.onPause()
        chromecast?.reconnectCast()
    }


    override fun onResume() {
        super.onResume()
        chromecast?.reconnectCast()
//        if(chromecast != null){
//            DMCApplication.getContext().setIsDeviceConnected(true)
//        }else{
//            DMCApplication.getContext().setIsDeviceConnected(false)
//        }
//        chromecast.reconnectCast()
    }

}

