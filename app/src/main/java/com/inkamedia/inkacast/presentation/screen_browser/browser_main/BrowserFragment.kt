package com.inkamedia.inkacast.presentation.screen_browser.browser_main
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.WebSettings
import androidx.fragment.app.viewModels
import com.inkacast.gdrivevideoplayer.repository.Webpage
import com.inkamedia.inkacast.presentation.MainActivity
import com.inkamedia.inkacast.databinding.FragmentBrowseBinding
import com.inkamedia.inkacast.presentation.screen_browser.browser_extract.FabDetector
import com.inkamedia.inkacast.presentation.screen_browser.browser_main.adapter.WebpageAdapter
import com.inkamedia.inkacast.presentation.screen_browser.viewModel.WebpageViewModel
import com.inkamedia.inkacast.utils.webclient.BrowserDebugUtils
import com.inkamedia.inkacast.utils.webclient.BrowserDownloadListener
import com.inkamedia.inkacast.utils.webclient.BrowserWebViewClient
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BrowseFragment : Fragment(), FabDetector {
    private var _binding: FragmentBrowseBinding? = null
    private lateinit var mActivity: MainActivity
    private val mainViewModel: WebpageViewModel by viewModels()
    private lateinit var webViewClientCustom: BrowserWebViewClient
    private lateinit var downloadListener: BrowserDownloadListener
    private val mBinding get() = _binding!!
    private val listWebpage = ArrayList<Webpage>()
    private lateinit var adapterBrowser: WebpageAdapter
    private var gotoWebpage: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrowseBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = requireActivity() as MainActivity
        BrowserDebugUtils.configWebView(requireContext())
        webViewClientCustom = BrowserWebViewClient( mActivity)
        downloadListener = BrowserDownloadListener(webViewClientCustom)
        mBinding.webview.webViewClient = webViewClientCustom
        mBinding.webview.setDownloadListener(downloadListener)

        setupWebView()
        mBinding.edtSearchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchText = mBinding.edtSearchText.text.toString()
                hideKeyboard()

                mBinding.webview.loadUrl("https://www.google.com/search?q=$searchText")
                true
            } else {
                false
            }
        }
        //initViewModelObserver()
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun setupWebView() {
        mBinding.webview.loadUrl("https://www.google.com")
        mBinding.webview.isHorizontalScrollBarEnabled = false
        mBinding.webview.isVerticalScrollBarEnabled = false
        mBinding.webview.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_BUTTON_PRESS
                || event.action == MotionEvent.ACTION_DOWN
            ) {
                webViewClientCustom.setTouchedScreen(true)
            }
            false
        }

        mBinding.webview.apply {
            settings.javaScriptEnabled = true
            settings.builtInZoomControls = false
            settings.displayZoomControls = false
            settings.loadWithOverviewMode = true
            settings.setSupportZoom(true)
            settings.useWideViewPort = true
            settings.domStorageEnabled = true
            settings.allowFileAccess = true
            settings.domStorageEnabled = true
            settings.cacheMode = WebSettings.LOAD_DEFAULT
            settings.javaScriptCanOpenWindowsAutomatically = false
          //  settings.userAgentString = resources.getString(R.string.user_agent)
            if (Build.VERSION.SDK_INT >= 17) {
                settings.mediaPlaybackRequiresUserGesture = false
            }
            if (Build.VERSION.SDK_INT >= 21) {
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

        }
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.search_menu, menu)
//        MainActivity.searchItemReference =  menu.findItem(R.id.menu)
//        MainActivity.seveItemReference = menu.findItem(R.id.save)
//        val searchView = MainActivity.searchItemReference?.actionView as? SearchView
//        searchView?.isSubmitButtonEnabled = true
//        searchView?.setOnQueryTextListener(this)
//        MainActivity.seveItemReference .isVisible = false
//    }

//    private fun initRecyclerView() {
//        adapterBrowser = WebpageAdapter(listWebpage, this)
//        mBinding.recyclerView.apply {
//            adapter = adapterBrowser
//            layoutManager = LinearLayoutManager(requireContext())
//        }
//    }

//    override fun onClickebpage(web: Webpage) {
//        super.onClickebpage(web)
//        val intent = Intent(requireContext(), BrowserActivity::class.java)
//        intent.putExtra("url_web_page", web.url)
//        intent.putExtra("title_web_page", web.title)
//        intent.putExtra("saved_web_page", true)
//        startActivity(intent)
//        activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
//    }
//
//    override fun onDeletepage(web: Webpage) {
//        super.onDeletepage(web)
//        mainViewModel.deletePage(web)
//        adapterBrowser.deleteWebpage(web)
//    }

    //    private fun initViewModelObserver() {
//                mainViewModel.readData.observe(viewLifecycleOwner) { webpages ->
//                    if(!webpages.isEmpty()){
//                        listWebpage.clear()
//                        listWebpage.addAll(webpages)
//                        adapterBrowser.notifyDataSetChanged()
//                    }
//                }
//    }
    private fun hideKeyboard() {
        val imm = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun showFabDetector(isShow: Boolean) {

    }


}

