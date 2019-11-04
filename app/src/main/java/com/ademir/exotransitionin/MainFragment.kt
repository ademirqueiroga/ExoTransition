package com.ademir.exotransitionin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ademir.exotransitionin.databinding.FragmentMainBinding
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private val activity: MainActivity?
        get() = super.getActivity() as? MainActivity

    private lateinit var viewBinding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMainBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onPause() {
        super.onPause()
        Log.e("PAUSE", "PAUSE")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = VideoAdapter {
            val playerView = it.findViewById<PlayerView>(R.id.player_view)
            val video = playerView?.tag as? Video
            if (video != null) {
                val playerFragment = PlayerFragment.newInstance(video, playerView.player)
                activity?.navigate(playerFragment, playerView)
            }
        }
        adapter.setData(
            listOf(
//                Video("", ""),
                Video(
                    "",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                )
//                Video("", "")
            )
        )
        list.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        Log.e("RESUME", "RESUME")
    }

}