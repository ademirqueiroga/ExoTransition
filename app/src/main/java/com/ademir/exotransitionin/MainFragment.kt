package com.ademir.exotransitionin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ademir.exotransitionin.databinding.FragmentMainBinding
import com.google.android.exoplayer2.ui.PlayerView
import com.jakewharton.rxbinding3.recyclerview.scrollStateChanges
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class MainFragment : Fragment() {

    private val activity: MainActivity?
        get() = super.getActivity() as? MainActivity

    private lateinit var viewBinding: FragmentMainBinding
    private var recyclerViewDisposable: Disposable? = null

    private val scrollStateChangesObservable by lazy {
        with(viewBinding.list) {
            scrollStateChanges()
                .debounce { state ->
                    when (state) {
                        RecyclerView.SCROLL_STATE_IDLE -> Observable.just(state).delay(
                            1000,
                            TimeUnit.MILLISECONDS
                        )
                        else -> Observable.just(state)
                    }.observeOn(Schedulers.io())
                }
                .scan(Pair(scrollState, findHighlightedView(this).wrap()), { oldPair, newState ->
                    val newView = when (newState) {
                        RecyclerView.SCROLL_STATE_IDLE -> findHighlightedView(this).wrap()
                        else -> oldPair.second
                    }
                    Pair(newState, newView)
                })
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMainBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onPause() {
        super.onPause()
        recyclerViewDisposable?.dispose()
    }

    override fun onResume() {
        super.onResume()
        observeScrollStateChanges()
    }

    private fun setupRecyclerView() {
        viewBinding.list.adapter = VideoAdapter {
            val playerView = it.findViewById<PlayerView>(R.id.player_view)
            val video = playerView?.tag as? Video
            if (video != null) {
                val playerFragment = PlayerFragment.newInstance(video)
                activity?.navigate(playerFragment, playerView)
            }
        }.apply { fakeData() }

        observeScrollStateChanges()
    }

    private fun observeScrollStateChanges() {
        recyclerViewDisposable = scrollStateChangesObservable.subscribe(
            { stateAndViewPair ->
                val view = stateAndViewPair.second.ref?.get() ?: return@subscribe
                val state = stateAndViewPair.first
                val viewHolder = viewBinding.list.getChildViewHolder(view)
                viewHolder as VideoAdapter.VideoViewHolder
                when (state) {
                    RecyclerView.SCROLL_STATE_IDLE -> viewHolder.playVideo()
                    else -> viewHolder.removePlayer()
                }
            },
            { e -> Log.e("ScrollStateChange", "Error", e) }
        )
    }

    private fun findHighlightedView(recyclerView: RecyclerView): View? {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisible = layoutManager.findFirstCompletelyVisibleItemPosition()
        val lastVisible = layoutManager.findLastVisibleItemPosition()
        val visibleItemsCount = (lastVisible - firstVisible) + 1
        val screenCenter = recyclerView.resources.displayMetrics.heightPixels / 2

        return when {
            firstVisible == 0 && recyclerView.scrollY == 0 -> {
                layoutManager.getChildAt(0)
            }
            lastVisible + 1 == recyclerView.adapter?.itemCount
                    && lastVisible == layoutManager.findLastCompletelyVisibleItemPosition() -> {
                layoutManager.getChildAt(lastVisible - visibleItemsCount)
            }
            else -> {
                var highlightedView: View? = null
                var minCenterOffset = Integer.MAX_VALUE
                for (i in 0 until visibleItemsCount) {
                    val view = layoutManager.getChildAt(i) ?: return null
                    val centerOffset =
                        abs(view.bottom - screenCenter) + abs(view.top - screenCenter)
                    if (minCenterOffset > centerOffset) {
                        minCenterOffset = centerOffset
                        highlightedView = view
                    }
                }
                return highlightedView
            }
        }
    }

    class ViewWrapper(view: View?) {
        val ref: WeakReference<View>? = view?.let { WeakReference(it) }
    }

    fun View?.wrap() = ViewWrapper(this)

}