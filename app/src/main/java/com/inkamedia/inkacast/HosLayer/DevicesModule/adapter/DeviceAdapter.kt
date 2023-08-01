package com.inkamedia.inkacast.HosLayer.DevicesModule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inkamedia.inkacast.HosLayer.DevicesModule.entities.Devicetv
import com.inkamedia.inkacast.R
import com.inkamedia.inkacast.databinding.DeviceItemBinding

class DeviceAdapter(
    private var devices: MutableList<Devicetv>,
    private var listener: OnClickListener
) : RecyclerView.Adapter<DeviceAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mBinding = DeviceItemBinding.bind(view)

        fun setListener(device: Devicetv){
            mBinding.root.setOnClickListener {
                listener.onSelectEntity(device)
            }
        }
    }

    fun setAdapterEntities(entity: MutableList<Devicetv>) {
        this.devices = entity
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.device_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val device = devices[position]
        with(holder){
            setListener(device)
            mBinding.tvName.text = device.name
            mBinding.tvSubname.text = device.subname
        }
    }

    override fun getItemCount() = devices.size


}