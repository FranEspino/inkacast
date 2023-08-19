package com.inkamedia.inkacast.presentation.screen_home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inkamedia.inkacast.databinding.FragmentHomeBinding
import com.inkamedia.inkacast.presentation.MainActivity
import com.inkamedia.inkacast.presentation.screen_device.DevicesActivity
import com.inkamedia.inkacast.presentation.screen_settings.SettingsActivity
import com.inkamedia.inkacast.utils.dlna.DMCApplication
import com.inkamedia.inkacast.utils.dlna.engine.DLNAContainer
import com.inkamedia.inkacast.utils.dlna.engine.MultiPointController
import com.inkamedia.inkacast.utils.dlna.inter.IController
import org.cybergarage.upnp.Device


class HomeFragment : Fragment() , ChromecastCennect{
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

        if(MainActivity.chromecast!=null){
            binding.cvDisconnected.visibility = View.GONE
            binding.cvDeviceConnect.visibility =View.VISIBLE

        }else{
            binding.cvDisconnected.visibility = View.VISIBLE
            binding.cvDeviceConnect.visibility =View.GONE
        }

        binding.settingsButton.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.btnChangeDevice.setOnClickListener {
            val intent = Intent(requireContext(), DevicesActivity::class.java)
            startActivity(intent)
        }
        binding.btnAddDevices.setOnClickListener {
            val intent = Intent(requireContext(), DevicesActivity::class.java)
            startActivity(intent)
        }
        binding.swScreenMirror.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

            } else {

            }

        }
        setController(MultiPointController())

        mDevice = DLNAContainer.getInstance().getSelectedDevice()

    }

    override fun onChromecastConnected() {
        binding.cvDisconnected.visibility = View.GONE
        binding.cvDeviceConnect.visibility =View.VISIBLE
    }

    override fun onChromecastDisconnected() {
        binding.cvDisconnected.visibility = View.VISIBLE
        binding.cvDeviceConnect.visibility =View.GONE
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