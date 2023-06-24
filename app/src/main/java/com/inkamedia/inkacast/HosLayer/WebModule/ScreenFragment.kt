package com.inkamedia.inkacast.HosLayer.WebModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import com.google.android.material.snackbar.Snackbar
import com.inkamedia.inkacast.MainActivity
import com.inkamedia.inkacast.R
import com.inkamedia.inkacast.changeTab
import com.inkamedia.inkacast.checkForInternet
import com.inkamedia.inkacast.databinding.FragmentScreenBinding


class ScreenFragment :  Fragment() {

    private lateinit var binding: FragmentScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_screen, container, false)
        binding = FragmentScreenBinding.bind(view)

        return view
    }

    override fun onResume() {
        super.onResume()
        MainActivity.tabsBtn.text = MainActivity.tabsList.size.toString()
        MainActivity.tabsList[MainActivity.myPager.currentItem].name = "Home"
        binding.searchView.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val searchText = textView.text.toString().trim()
                if (checkForInternet(requireContext())) {
                    changeTab(searchText, BrowseFragment(searchText))
                } else {
                    Snackbar.make(binding.root, "Internet Not Connected\uD83D\uDE03", 3000).show()
                }
                true
            } else {
                false
            }
        }




    }
}