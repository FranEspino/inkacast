package com.inkamedia.inkacast.presentation.screen_browser.videos_extract

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.amlcurran.showcaseview.ShowcaseView
import com.github.amlcurran.showcaseview.targets.ViewTarget
import com.inkamedia.inkacast.R
import com.inkamedia.inkacast.commons.ContextApp
import com.inkamedia.inkacast.databinding.FragmentVideolistBinding
import com.inkamedia.inkacast.presentation.MainActivity
import com.inkamedia.inkacast.presentation.screen_browser.browser_extract.BrowserActivity
import com.inkamedia.inkacast.presentation.screen_browser.browser_extract.FabDetector
import com.inkamedia.inkacast.presentation.screen_browser.videos_extract.adapter.OnClickListener
import com.inkamedia.inkacast.presentation.screen_browser.videos_extract.adapter.Video
import com.inkamedia.inkacast.presentation.screen_browser.videos_extract.adapter.VideoAdapter

class VideolistFragment : Fragment(), OnClickListener {
    private var _binding: FragmentVideolistBinding? = null
    private val mBinding get() = _binding!!
    private var mActivity: BrowserActivity? = null
    private lateinit var videoAdapter: VideoAdapter
    private var fabDetector: FabDetector? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideolistBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        mActivity = activity as? BrowserActivity
        mActivity!!.showFabDetector(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBar()
        initRecyclerView()
        MainActivity.chromecast!!.setMediaRoute(mBinding.mediaRouteVideo)
        if(!ContextApp.context.getShowCase()){
            ContextApp.context.setShowCase(true)
            var showcaseView = ShowcaseView.Builder(requireActivity())
                .setContentTitle("Transmite a tu TV")
                .setContentText("Ya tenemos los archivos listos para que reproduzcas en tu TV, por favor asegurate de  probar todos los enlaces si no te funciona el primero.")
                .withNewStyleShowcase()
                .setTarget(ViewTarget(mBinding.mediaRouteVideo))
                .hideOnTouchOutside()
                .setStyle(R.style.CustomShowcaseTheme)
                .build()
            showcaseView.setButtonText("Continuar")
            showcaseView.show()
        }
    }


    private fun initRecyclerView() {
        videoAdapter = VideoAdapter(BrowserActivity.videoList, this)
        mBinding.rvVideos.apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FabDetector) {
            fabDetector = context
            fabDetector?.showFabDetector(false)

        }
    }
    override fun onClickVideo(video: Video) {
        super.onClickVideo(video)
        Toast.makeText(requireContext(), video.referer, Toast.LENGTH_LONG).show()

        MainActivity.chromecast!!.playVideoUrl(
            video.uri,
            video.id.toInt(),
            video.title,
            video.referer,
           "https://inka.media/chromecast-inkaplay.png",
            video.referer,
            video.type
            )
    }
    override fun onDestroy() {
        super.onDestroy()
        mActivity = activity as? BrowserActivity
        mActivity!!.showFabDetector(true)
    }
    private fun setupActionBar() {
        mActivity = activity as? BrowserActivity
        mActivity?.setSupportActionBar(mBinding.toolbar)
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = "Video List"
        setHasOptionsMenu(true)
        mBinding.toolbar.setNavigationOnClickListener {
            mActivity?.onBackPressed()
            hideKeyboard()
        }
    }

    private fun hideKeyboard() {
        val imm = mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (view != null) {
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)
        }
    }
}