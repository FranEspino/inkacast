package com.inkamedia.inkacast.presentation.screen_browser.browser_extract

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.digenio.inkacast.Chromecast
import com.fraporitmos.gdrivevideoplayer.repository.Webpage
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.inkamedia.inkacast.R
import com.inkamedia.inkacast.databinding.ActivityBrowserBinding
import com.inkamedia.inkacast.presentation.MainActivity
import com.inkamedia.inkacast.presentation.screen_browser.videos_extract.VideolistFragment
import com.inkamedia.inkacast.presentation.screen_browser.videos_extract.adapter.Video
import com.inkamedia.inkacast.presentation.screen_browser.viewModel.WebpageViewModel
import com.inkamedia.inkacast.utils.webclient.BrowserDebugUtils
import com.inkamedia.inkacast.utils.webclient.BrowserDownloadListener
import com.inkamedia.inkacast.utils.webclient.BrowserWebViewClient
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.util.Random


@AndroidEntryPoint
class BrowserActivity : AppCompatActivity() , FabDetector{
    private lateinit var webViewClientCustom: BrowserWebViewClient
    private lateinit var downloadListener: BrowserDownloadListener
    private lateinit var binding: ActivityBrowserBinding
    private val mainViewModel: WebpageViewModel by viewModels()
    private var titleWebPage = ""
    private var urlWebPage = ""
    private var bitmapWebPage = ""
    private var savedWebPage = false
    private  var scanned = false

    private  var goToListVideios = false
    companion object{
         var videoList : MutableList<Video> = mutableListOf()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoList.clear()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrowserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        BrowserDebugUtils.configWebView(this@BrowserActivity)
        webViewClientCustom = BrowserWebViewClient(this@BrowserActivity)
        downloadListener = BrowserDownloadListener(webViewClientCustom)
        binding.webView.webViewClient = webViewClientCustom
        binding.webView.setDownloadListener(downloadListener)
        urlWebPage = intent.getStringExtra("url_web_page").toString()
        titleWebPage = intent.getStringExtra("title_web_page").toString()
        savedWebPage = intent.getBooleanExtra("saved_web_page", false)
        setupToolbar(titleWebPage)

        binding.webView.loadUrl(urlWebPage)
        val webSettings: WebSettings = binding.webView.settings
        webSettings.loadWithOverviewMode = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.useWideViewPort = false
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.allowFileAccess = true
        webSettings.userAgentString = resources.getString(R.string.user_agent)
        if (Build.VERSION.SDK_INT >= 17) {
            webSettings.mediaPlaybackRequiresUserGesture = false
        }
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        binding.webView.apply {
            webChromeClient = object : WebChromeClient() {
                override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
                    super.onReceivedIcon(view, icon)
                    try {
                        val array = ByteArrayOutputStream()
                        icon?.compress(Bitmap.CompressFormat.PNG, 100, array)
                        val byteArray = array.toByteArray()
                        bitmapWebPage = Base64.encodeToString(byteArray, Base64.DEFAULT)
                    } catch (e: Exception) {
                    }
                }
            }
        }
        binding.webView.setInitialScale(0)
        binding.webView.isHorizontalScrollBarEnabled = false
        binding.webView.isVerticalScrollBarEnabled = false
        binding.webView.clearCache(true)
        binding.webView.clearHistory()
        binding.fabMovies.setOnClickListener {
            launchFragmnet()
        }
    }

    private fun launchFragmnet() {
        goToListVideios = true
        scanned = true
        val fragment = VideolistFragment()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
        fragmentTransaction.add(R.id.containerBrowser, fragment)
        fragmentTransaction.commit()
        fragmentTransaction.addToBackStack(null)
    }
    fun getVideoFormat(url: String): String {
        val regex = Regex("(mp4|mpd|m3u8)")
        val match = regex.find(url) ?: return ""
        return match.value
    }
    fun addSavedVideo(uri: String, mimeType: String, referer: String) {
        if(!scanned){
            val randomId= Random().nextInt(1000).toLong()
            val format = getVideoFormat(uri)
            val video = Video(randomId,titleWebPage, uri, mimeType, referer, format)
            videoList.add(video)
        }

        if(videoList.size > 0 && !goToListVideios ){
            binding.fabMovies.visibility = View.VISIBLE
        }
    }

    private fun setupToolbar(titleWebPage: String) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = titleWebPage
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }
    }


    private fun alertSavePage() {
        val dialogView = layoutInflater.inflate(R.layout.alert_save_page, null)

        val customAlert = MaterialAlertDialogBuilder(this, R.style.MaterialDialog)
            .setView(dialogView)
        val dialog: androidx.appcompat.app.AlertDialog = customAlert.show()
        dialog.setTitle("Guardar sitio web")
        val namesUser = dialogView.findViewById<TextInputEditText>(R.id.et_title_web)
        val urlFolder = dialogView.findViewById<TextInputEditText>(R.id.et_url_web)
        val btnUpdateInfo = dialogView.findViewById<MaterialButton>(R.id.btn_save_web)
        namesUser.setText(titleWebPage)
        urlFolder.setText(urlWebPage)
        dialog.show()
        btnUpdateInfo.setOnClickListener {
            if (namesUser.text!!.isNotEmpty() && urlFolder.text!!.isNotEmpty()) {
                val web = Webpage(
                    namesUser.text.toString(),
                    urlFolder.text.toString(),
                    bitmapWebPage
                )
                mainViewModel.countByUrl(web).observe(this) { exists ->
                    if (!exists) {
                        mainViewModel.createPage(web)
                        if (!savedWebPage) {
                            savedWebPage = !savedWebPage
                        }
                        MainActivity.seveItemReference.isVisible = false
                        dialog.dismiss()
                        Toast.makeText(
                            this,
                            "El sitio web se guardo correctamente",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        Toast.makeText(this, "El sitio web ya eesta registrado", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        MainActivity.searchItemReference = menu.findItem(R.id.menu)
        MainActivity.seveItemReference = menu.findItem(R.id.save)
        MainActivity.searchItemReference.isVisible = false
        MainActivity.seveItemReference.isVisible = !savedWebPage
        checkBookmark()
        return true
    }

    fun checkBookmark() {
        if (savedWebPage) {
            MainActivity.seveItemReference.setIcon(R.drawable.ic_bookmark_saved)
        } else {
            MainActivity.seveItemReference.setIcon(R.drawable.ic_bookmark_unsaved)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) {
            alertSavePage()
        }
        return true

    }

    override fun showFabDetector(isShow: Boolean) {
        if(isShow){
            goToListVideios = false
            binding.fabMovies.visibility = View.VISIBLE
        }else{
            scanned = true
            goToListVideios = true
            binding.fabMovies.visibility = View.GONE
        }
    }
}
