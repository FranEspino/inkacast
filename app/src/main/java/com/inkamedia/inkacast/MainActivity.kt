package com.inkamedia.inkacast

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.digenio.inkacast.Chromecast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textview.MaterialTextView
import com.inkamedia.inkacast.HosLayer.DevicesModule.DevicesActivity
import com.inkamedia.inkacast.HosLayer.StreamService
import com.inkamedia.inkacast.HosLayer.WebModule.entities.Bookmark
import com.inkamedia.inkacast.HosLayer.WebModule.entities.Tab
import com.inkamedia.inkacast.MainActivity.Companion.myPager
import com.inkamedia.inkacast.MainActivity.Companion.tabsList
import com.inkamedia.inkacast.databinding.ActivityMainBinding
import com.inkamedia.inkacast.dlna.DMCApplication
import com.inkamedia.inkacast.dlna.engine.DLNAContainer
import com.inkamedia.inkacast.dlna.service.DLNAService
import org.cybergarage.upnp.Device
import java.util.Random

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding


    companion object{
        var tabsList: ArrayList<Tab> = ArrayList()
        var isDesktopSite: Boolean = false
        var bookmarkList: ArrayList<Bookmark> = ArrayList()
        var bookmarkIndex: Int = -1
        lateinit var myPager: ViewPager2
        lateinit var tabsBtn: MaterialTextView
        lateinit var chromecast: Chromecast
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        chromecast = Chromecast(this@MainActivity)

        startDLNAService()

        mBinding.fabDevices.setOnClickListener {
            val intent = Intent(this, DevicesActivity::class.java)
            startActivity(intent)
        }

        val navController = this.findNavController(R.id.navHostMain)
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        navView.setupWithNavController(navController)


    }

    private fun sendActionCommandService(action:String){
        Intent(
            this,
            StreamService::class.java
        ).apply {
            this.action = action
            startService(this)
        }
    }

    private fun startDLNAService() {
        val intent = Intent(this, DLNAService::class.java)
        startService(intent)
    }

    override fun onPause() {
        super.onPause()
        chromecast.reconnectCast()
    }

    override fun onResume() {
        super.onResume()
            if(chromecast != null){
                DMCApplication.getContext().setIsDeviceConnected(true)
            }else{
                DMCApplication.getContext().setIsDeviceConnected(false)
            }
        chromecast.reconnectCast()
    }

}


    @SuppressLint("NotifyDataSetChanged")
    fun changeTab(url: String, fragment: Fragment, isBackground: Boolean = false){
        tabsList.add(Tab(name = url, fragment = fragment))
        myPager.adapter?.notifyDataSetChanged()
        if(!isBackground) myPager.currentItem = tabsList.size - 1
    }
    fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }