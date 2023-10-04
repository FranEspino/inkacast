package com.inkamedia.inkacast.presentation.screen_browser.browser_main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.inkacast.gdrivevideoplayer.repository.Webpage
import com.inkamedia.inkacast.R
import com.inkamedia.inkacast.databinding.WebsiteItemBinding

class WebpageAdapter(
    private val webPage: MutableList<Webpage>,
    private val listener: OnClickListener
) : RecyclerView.Adapter<WebpageAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = WebsiteItemBinding.bind(view)

        @SuppressLint("DiscouragedPrivateApi")
        fun setListener(web: Webpage) {
            binding.root.setOnClickListener {
                listener.onClickebpage(web)
            }
            binding.btnWebpageOptions.setOnClickListener {
                val popupMenus = PopupMenu(context, binding.btnWebpageOptions)
                popupMenus.inflate(R.menu.web_item_bd)
                popupMenus.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.propiedades -> {
                            listener.onDeletepage(web)
                            true
                        }

                        else -> false
                    }
                }
                popupMenus.show()
                val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                popup.isAccessible = true
                val menu = popup.get(popupMenus)
                menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(menu, true)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.website_item, parent, false)
        return ViewHolder(view)
    }

    fun deleteWebpage(_webpage: Webpage) {
        val index = webPage.indexOf(_webpage)
        if (index != -1) {
            webPage.removeAt(index)
            notifyItemRemoved(index + 1)
        }


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val webpage = webPage[position]
        with(holder) {
            binding.tvWebpageName.text = webpage.title
            binding.tvWebpageUrl.text = webpage.url
            try {
                val byteArray = Base64.decode(webpage.bitmap, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

                if (bitmap != null) {
                    binding.ivWebpage.setImageBitmap(bitmap)
                } else {
                    binding.ivWebpage.setImageResource(R.drawable.webdefault)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            setListener(webpage)
        }
    }

    override fun getItemCount(): Int = webPage.size
}
