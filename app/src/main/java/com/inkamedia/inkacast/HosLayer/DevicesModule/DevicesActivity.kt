package com.inkamedia.inkacast.HosLayer.DevicesModule

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.inkamedia.inkacast.HosLayer.DevicesModule.adapter.DeviceAdapter
import com.inkamedia.inkacast.HosLayer.DevicesModule.adapter.OnClickListener
import com.inkamedia.inkacast.HosLayer.DevicesModule.entities.Devicetv
import com.inkamedia.inkacast.MainActivity
import com.inkamedia.inkacast.databinding.ActivityDevicesBinding
import com.inkamedia.inkacast.dlna.engine.DLNAContainer
import com.inkamedia.inkacast.dlna.engine.MultiPointController
import com.inkamedia.inkacast.dlna.inter.IController
import org.cybergarage.upnp.Device
import java.util.Random

class DevicesActivity : AppCompatActivity() ,OnClickListener{
    private lateinit var mBinding: ActivityDevicesBinding
    private lateinit var mAdapter : DeviceAdapter
    private var listDevices = mutableListOf<Devicetv>()
    private var mDevices: List<Device>? = null
    private lateinit var mController: IController
    private lateinit var mDevice: Device
    private val urls: List<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDevicesBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        MainActivity.chromecast.setMediaRoute(mBinding.mediaRouteVideo)

        mBinding.cvCast.setOnClickListener {
            mBinding.mediaRouteVideo.performClick()
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
        }, 1000)


        mBinding.btnAddDevices.setOnClickListener {
            mDevices = DLNAContainer.getInstance().devices
            MainActivity.chromecast.playVideoUrl(
                "http://www.youtube.com/watch?v=LmJOjnWf5Nk",
                Random().nextInt(1000),
                "Mi nuevo titulo",
                "Una breve descripcion",
                "https://res.cloudinary.com/frapoteam/image/upload/v1676054330/icono_naranja_vpblya.png"
            )
        }

        DLNAContainer.getInstance().setDeviceChangeListener {
            mDevices = DLNAContainer.getInstance().devices
            Log.d("devices", "mDevices size:" + mDevices!!.size)

        }


    }



    override fun onSelectEntity(device: Devicetv) {
        super.onSelectEntity(device)
        Toast.makeText(this, "Selecciono ${device.name}", Toast.LENGTH_SHORT).show()
        DLNAContainer.getInstance().selectedDevice = mDevices!![0]
        mDevice = DLNAContainer.getInstance().selectedDevice
        Log.d("devicesfrapo", "mDevice:" + mDevice.friendlyName.toString())
        Log.d("devicesfrapo", "mDevice:" + mController?.getTransportState(mDevice))

        //add string to urls
        mController?.play(mDevice, "http://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
    }
    private fun setController(controller: IController) {
        mController = controller
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

}