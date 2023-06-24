package com.inkamedia.inkacast.HosLayer.WebModule

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.inkamedia.inkacast.ContextApp
import com.inkamedia.inkacast.HosLayer.WebModule.adapters.TabAdapter
import com.inkamedia.inkacast.HosLayer.WebModule.entities.Bookmark
import com.inkamedia.inkacast.HosLayer.WebModule.entities.Tab
import com.inkamedia.inkacast.MainActivity
import com.inkamedia.inkacast.R
import com.inkamedia.inkacast.changeTab
import com.inkamedia.inkacast.databinding.FragmentWebBinding
import com.inkamedia.inkacast.databinding.TabsViewBinding
import java.util.ArrayList


class WebFragment : Fragment() {
    private lateinit var binding: FragmentWebBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWebBinding.inflate(
            inflater, container, false)

        getAllBookmarks()

        MainActivity.tabsList.add(Tab("google", BrowseFragment("https://www.google.com")))
        binding.myPager.adapter = TabsAdapter(requireActivity().supportFragmentManager, lifecycle)
        binding.myPager.isUserInputEnabled = false
        MainActivity.myPager = binding.myPager
        MainActivity.tabsBtn = binding.tabsBtn

        initializeView()
        return binding.root
    }


    private inner class TabsAdapter(fa: FragmentManager, lc: Lifecycle) : FragmentStateAdapter(fa, lc) {
        override fun getItemCount(): Int = MainActivity.tabsList.size

        override fun createFragment(position: Int): Fragment = MainActivity.tabsList[position].fragment
    }


    private fun initializeView(){

        binding.tabsBtn.setOnClickListener {
            val viewTabs = layoutInflater.inflate(R.layout.tabs_view, binding.root, false)
            val bindingTabs = TabsViewBinding.bind(viewTabs)
            val title = if(ContextApp.context.getDarkMode()){
                Html.fromHtml("<font color='#FFFFFF'>Ventanas abiertas</font>")
            }else{
                Html.fromHtml("<font color='#000000'>Ventanas abiertas</font>")
            }
            val dialogTabs = MaterialAlertDialogBuilder(requireContext(), R.style.roundCornerDialog)
                .setTitle(title)

                .setNegativeButton("Cancelar"){self, _ ->
                    self.dismiss()
                }
                .setPositiveButton("Nueva Ventana"){self, _ ->
                    changeTab("Home", ScreenFragment())
                    self.dismiss()
                }
                .setView(viewTabs)
                .create()

            bindingTabs.tabsRV.setHasFixedSize(true)
            bindingTabs.tabsRV.layoutManager = LinearLayoutManager(requireContext())
            bindingTabs.tabsRV.adapter = TabAdapter(requireContext(), dialogTabs)

            dialogTabs.show()



        }


    }

    private fun getAllBookmarks(){
        //for getting bookmarks data using shared preferences from storage
        MainActivity.bookmarkList = ArrayList()
        val editor = requireActivity().getSharedPreferences("BOOKMARKS", AppCompatActivity.MODE_PRIVATE)
        val data = editor.getString("bookmarkList", null)

        if(data != null){
            val list: ArrayList<Bookmark> = GsonBuilder().create().fromJson(data, object: TypeToken<ArrayList<Bookmark>>(){}.type)
            MainActivity.bookmarkList.addAll(list)
        }
    }
}