package com.inkamedia.inkacast.presentation.screen_browser.videos_extract.adapter

import android.annotation.SuppressLint
import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inkamedia.inkacast.R
import com.inkamedia.inkacast.databinding.VideoItemBinding

class VideoAdapter  (
    private val video: MutableList<Video>,
    private val listener: OnClickListener
) : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = VideoItemBinding.bind(view)

        @SuppressLint("DiscouragedPrivateApi")
        fun setListener(video: Video) {
            binding.root.setOnClickListener {
                listener.onClickVideo(video)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = video[position]
        with(holder) {
            binding.tvVideoName.text = video.title
            binding.tvVideoUri.text = video.uri
            binding.tvVideoTypefile.text = video.type
            setListener(video)
        }

    }

    override fun getItemCount(): Int = video.size
}
