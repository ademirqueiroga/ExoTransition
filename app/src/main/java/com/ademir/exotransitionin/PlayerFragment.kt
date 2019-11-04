package com.ademir.exotransitionin

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import com.ademir.exotransitionin.databinding.FragmentPlayerBinding
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


class PlayerFragment : Fragment() {

    private lateinit var viewBinding: FragmentPlayerBinding
    private var player: Player? = null
    private var video: Video? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        readArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPlayerBinding.inflate(inflater, container, false)
        video?.let {
            viewBinding.playerView.transitionName = getTransitionName(it)
            sharedElementEnterTransition = TransitionInflater.from(context)
                .inflateTransition(R.transition.video_shared_element_transition)
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (player == null) {
            // Produces DataSource instances through which media data is loaded.
            val dataSourceFactory =
                DefaultDataSourceFactory(context, Util.getUserAgent(context, "yourApplicationName"))
            // This is the MediaSource representing the media to be played.
            val videoSource: MediaSource =
                ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(video?.url))
            // Prepare the player with the source.
            this.player = ExoPlayerFactory.newSimpleInstance(context).apply {
                prepare(videoSource)
            }
        }
        viewBinding.playerView.player = player
        startPostponedEnterTransition()
    }

    private fun readArgs() = arguments?.run {
        video = getParcelable(VIDEO_EXTRA)
    }

    companion object {
        private const val VIDEO_EXTRA = "args_video_extra"
        fun newInstance(video: Video, player: Player? = null) = PlayerFragment().apply {
            this.player = player
            arguments = bundleOf(VIDEO_EXTRA to video)
        }

        fun getTransitionName(video: Video): String {
            // TODO: this name won't be unique if the same video appears on the list twice
            return "tr_name_video:${video.hashCode()}"
        }

    }

}