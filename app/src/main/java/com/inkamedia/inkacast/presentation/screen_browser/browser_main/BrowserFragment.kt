package com.inkamedia.inkacast.presentation.screen_browser.browser_main
import android.app.MediaRouteActionProvider
import android.app.MediaRouteButton
import android.content.Context
import android.content.Intent
import android.media.MediaRouter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.viewModels
import androidx.mediarouter.media.MediaRouteSelector
import androidx.recyclerview.widget.LinearLayoutManager
import com.fraporitmos.gdrivevideoplayer.repository.Webpage
import com.inkamedia.inkacast.R
import com.inkamedia.inkacast.presentation.MainActivity
import com.inkamedia.inkacast.databinding.FragmentBrowseBinding
import com.inkamedia.inkacast.presentation.screen_browser.browser_extract.BrowserActivity
import com.inkamedia.inkacast.presentation.screen_browser.browser_main.adapter.OnClickListener
import com.inkamedia.inkacast.presentation.screen_browser.browser_main.adapter.WebpageAdapter
import com.inkamedia.inkacast.presentation.screen_browser.viewModel.WebpageViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BrowseFragment : Fragment(),SearchView.OnQueryTextListener, OnClickListener {
    private var _binding: FragmentBrowseBinding? = null
    private lateinit var mActivity: MainActivity
    private val mainViewModel: WebpageViewModel by viewModels()
    private val mBinding get() = _binding!!
    private val listWebpage = ArrayList<Webpage>()
    private lateinit var adapterBrowser: WebpageAdapter
    private var gotoWebpage: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBrowseBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = requireActivity() as MainActivity
        setupActionBar()
        setupWebView()
        initRecyclerView()
        initViewModelObserver()
    }


    private fun setupWebView() {
        mBinding.webview.apply {
            settings.javaScriptEnabled = true
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            settings.displayZoomControls = true
            webViewClient = object: WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        val intent = Intent(requireContext(), BrowserActivity::class.java)
                        var cleanedTitle = "Website"
                        if(view?.title?.contains("- Google Search")!! ){
                             cleanedTitle = view.title?.replace("- Google Search", "")?.trim().toString()
                        }
                        if(view?.title?.contains("- Buscar con Google")!!){
                            cleanedTitle = view.title?.replace("- Buscar con Google", "")?.trim().toString()
                        }
                        intent.putExtra("url_web_page", url)
                        intent.putExtra("title_web_page", cleanedTitle)
                        startActivity(intent)
                        activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                        return true
                    }
            }
        }
    }

    private fun setupActionBar() {
        mBinding.toolbar.apply {
            mActivity.setSupportActionBar(this)
            mActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            mActivity.supportActionBar?.title = getString(R.string.title_fragment_webview)
            setNavigationOnClickListener {
                mActivity.onBackPressed()
                hideKeyboard()
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        MainActivity.searchItemReference =  menu.findItem(R.id.menu)
        MainActivity.seveItemReference = menu.findItem(R.id.save)
        val searchView = MainActivity.searchItemReference?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
        MainActivity.seveItemReference .isVisible = false
    }

    private fun initRecyclerView() {
        adapterBrowser = WebpageAdapter(listWebpage, this)
        mBinding.recyclerView.apply {
            adapter = adapterBrowser
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onClickebpage(web: Webpage) {
        super.onClickebpage(web)
        val intent = Intent(requireContext(), BrowserActivity::class.java)
        intent.putExtra("url_web_page", web.url)
        intent.putExtra("title_web_page", web.title)
        intent.putExtra("saved_web_page", true)

        startActivity(intent)
        activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun onDeletepage(web: Webpage) {
        super.onDeletepage(web)
        mainViewModel.deletePage(web)
        adapterBrowser.deleteWebpage(web)
    }

    private fun initViewModelObserver() {
                mainViewModel.readData.observe(viewLifecycleOwner) { webpages ->
                    if(webpages.isEmpty()){
                        mBinding.shimmer1.visibility = View.GONE
                        mBinding.llEmpty.visibility = View.VISIBLE
                    }else{
                        mBinding.llEmpty.visibility = View.GONE
                        mBinding.shimmer1.visibility = View.GONE
                        listWebpage.clear()
                        listWebpage.addAll(webpages)
                        adapterBrowser.notifyDataSetChanged()
                    }
                }
    }
    private fun hideKeyboard() {
        val imm = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        gotoWebpage = true
        mBinding.llWebsites.visibility = View.GONE
        mBinding.webview.visibility = View.VISIBLE
        mBinding.webview.loadUrl("https://www.google.com/search?q=$query")
        hideKeyboard()
        return true
    }
    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

}