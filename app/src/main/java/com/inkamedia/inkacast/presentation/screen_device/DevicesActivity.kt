package com.inkamedia.inkacast.presentation.screen_device

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.inkamedia.inkacast.R
import com.inkamedia.inkacast.commons.Constants
import com.inkamedia.inkacast.presentation.screen_device.adapter.DeviceAdapter
import com.inkamedia.inkacast.presentation.screen_device.adapter.OnClickListener
import com.inkamedia.inkacast.presentation.screen_device.entities.Devicetv
import com.inkamedia.inkacast.databinding.ActivityDevicesBinding
import com.inkamedia.inkacast.presentation.MainActivity
import com.inkamedia.inkacast.presentation.screen_browser.videos_extract.VideolistFragment
import com.inkamedia.inkacast.presentation.screen_home.ChromecastCennect
import com.inkamedia.inkacast.presentation.screen_home.HomeFragment
import com.inkamedia.inkacast.services.StreamService
import com.inkamedia.inkacast.utils.dlna.engine.DLNAContainer
import com.inkamedia.inkacast.utils.dlna.engine.MultiPointController
import com.inkamedia.inkacast.utils.dlna.inter.IController
import org.cybergarage.upnp.Device
import java.util.Random

class DevicesActivity : AppCompatActivity() , OnClickListener {
    private lateinit var mBinding: ActivityDevicesBinding
    private lateinit var mAdapter : DeviceAdapter
    private var listDevices = mutableListOf<Devicetv>()
    private var mDevices: List<Device>? = null
    private lateinit var mController: IController
    private lateinit var mDevice: Device
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDevicesBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        MainActivity.chromecast!!.setMediaRoute(mBinding.mediaRouteVideo)
        mBinding.cvCast.setOnClickListener {
            mBinding.mediaRouteVideo.performClick()
            val fragment = supportFragmentManager.findFragmentById(R.id.action_home) as? HomeFragment
            fragment?.onChromecastConnected()
        }
        mAdapter = DeviceAdapter(listDevices, this@DevicesActivity)

        setController(MultiPointController())

        mBinding.rvDevices.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                this@DevicesActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        }
        mDevices = DLNAContainer.getInstance().devices
        for (device in mDevices!!) {
            listDevices.add(Devicetv(device.udn,device.friendlyName,device.modelName))
            mAdapter.notifyDataSetChanged()
        }


        Handler().postDelayed({
            mBinding.llSearchDevices.visibility = View.GONE
            mBinding.llDevices.visibility = View.VISIBLE
            mBinding.cvCast.visibility = View.VISIBLE
        }, 1000)


//        mBinding.btnAddDevices.setOnClickListener {
//            mDevices = DLNAContainer.getInstance().devices
//            MainActivity.chromecast!!.playVideoUrl(
//                "http://www.youtube.com/watch?v=LmJOjnWf5Nk",
//                Random().nextInt(1000),
//                "Mi nuevo titulo",
//                "Una breve descripcion",
//                "https://res.cloudinary.com/frapoteam/image/upload/v1676054330/icono_naranja_vpblya.png"
//            )
//        }

        DLNAContainer.getInstance().setDeviceChangeListener {
            mDevices = DLNAContainer.getInstance().devices
            Log.d("devices", "mDevices size:" + mDevices!!.size)

        }


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

    override fun onSelectEntity(device: Devicetv) {
        super.onSelectEntity(device)
        Toast.makeText(this, "Selecciono ${device.name}", Toast.LENGTH_SHORT).show()
        DLNAContainer.getInstance().selectedDevice = mDevices!![0]
        mDevice = DLNAContainer.getInstance().selectedDevice
        Log.d("devicesfrapo", "mDevice:" + mDevice.friendlyName.toString())
        Log.d("devicesfrapo", "mDevice:" + mController?.getTransportState(mDevice))

        mController?.play(mDevice, "http://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
    }
    private fun setController(controller: IController) {
        mController = controller
    }


}