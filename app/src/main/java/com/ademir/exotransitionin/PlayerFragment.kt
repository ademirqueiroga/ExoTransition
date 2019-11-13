package com.ademir.exotransitionin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionInflater
import com.ademir.exotransitionin.databinding.FragmentPlayerBinding
import com.google.android.exoplayer2.ui.PlayerView


class PlayerFragment : Fragment() {

    private lateinit var viewBinding: FragmentPlayerBinding
    private var video: Video? = null

    private val sharedElementCallback = object : SharedElementCallback() {
        override fun onSharedElementEnd(
            sharedElementNames: MutableList<String>?,
            sharedElements: MutableList<View>?,
            sharedElementSnapshots: MutableList<View>?
        ) {
            super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)
            sharedElements?.forEach { sharedView ->
                if (sharedView.transitionName == video?.let(::getTransitionName)) {
                    val player = PlayerSingleton.getInstance(requireContext())
                    val oldView = sharedView as PlayerView
//                    PlayerView.switchTargetView(player, oldView, viewBinding.playerView)
                }
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewLifecycleOwner.lifecycle.addObserver(viewBinding.playerView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setEnterSharedElementCallback(sharedElementCallback)
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
        sharedElementReturnTransition = Fade()
            .setDuration(2000L)
            .excludeTarget(viewBinding.playerView, true)
        returnTransition = Slide().setDuration(2000L)
        viewBinding.playerView.lifecycle = viewLifecycleOwner.lifecycle
        viewBinding.playerView.player = PlayerSingleton.getInstance(requireContext())
        startPostponedEnterTransition()
    }

    private fun readArgs() = arguments?.run {
        video = getParcelable(VIDEO_EXTRA)
    }

    companion object {
        private const val VIDEO_EXTRA = "args_video_extra"
        fun newInstance(video: Video) = PlayerFragment().apply {
            arguments = bundleOf(VIDEO_EXTRA to video)
        }

        fun getTransitionName(video: Video): String {
            // TODO: this name won't be unique if the same video appears on the list twice
            return "tr_name_video:${video.hashCode()}"
        }

    }

}