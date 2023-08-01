package com.inkamedia.inkacast.HosLayer.HomeModule

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.digenio.inkacast.Chromecast
import com.inkamedia.inkacast.HosLayer.DevicesModule.DevicesActivity
import com.inkamedia.inkacast.HosLayer.SettingsModule.SettingsActivity
import com.inkamedia.inkacast.MainActivity
import com.inkamedia.inkacast.databinding.FragmentHomeBinding
import com.inkamedia.inkacast.dlna.DMCApplication
import com.inkamedia.inkacast.dlna.engine.DLNAContainer
import com.inkamedia.inkacast.dlna.engine.MultiPointController
import com.inkamedia.inkacast.dlna.inter.IController
import org.cybergarage.upnp.Device
import java.util.Random


class HomeFragment : Fragment() {
    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding
    private var mController: IController? = null

    private var mDevice: Device? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(
            inflater, container, false)
        return binding.root
    }

    private fun setController(controller: IController) {
        mController = controller
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingsButton.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        setController(MultiPointController())

        mDevice = DLNAContainer.getInstance().getSelectedDevice()
        setupViews()

    }

    private fun setupViews() {
       if(!DMCApplication.getContext().getIsDeviceConnected()){
           binding.cvDisconnected.visibility = View.VISIBLE
           binding.cvDeviceConnect.visibility = View.GONE
           binding.cvStreamming.visibility = View.GONE
       } else{
           binding.cvDisconnected.visibility = View.GONE
           binding.cvDeviceConnect.visibility = View.VISIBLE
       }

        binding.btnAddDevices.setOnClickListener {
            val intent = Intent(requireContext(), DevicesActivity::class.java)
            startActivity(intent)
        }

    }


}