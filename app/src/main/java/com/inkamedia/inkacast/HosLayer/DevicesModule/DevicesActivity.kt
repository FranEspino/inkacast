package com.inkamedia.inkacast.HosLayer.DevicesModule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.inkamedia.inkacast.databinding.ActivityDevicesBinding

class DevicesActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityDevicesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDevicesBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        Handler().postDelayed({
            mBinding.llSearchDevices.visibility = View.GONE
            mBinding.llDevices.visibility = View.VISIBLE
         }, 1000)

    }
}