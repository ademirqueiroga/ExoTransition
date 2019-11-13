package com.ademir.exotransitionin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ademir.exotransitionin.databinding.RowVideoBinding
import com.bumptech.glide.Glide

class VideoAdapter(
    private val onClick: (View) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private var dataSet: ArrayList<Video> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_video, parent, false)
        return VideoViewHolder(view, onClick)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    fun setData(data: List<Video>) {
        this.dataSet.clear()
        this.dataSet.addAll(data)
        notifyDataSetChanged()
    }

    fun fakeData() {
        setData(
            listOf(
                Video(
                    "https://image.shutterstock.com/image-photo/movie-projector-on-dark-background-600w-753798850.jpg",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
                ),
                Video(
                    "https://image.shutterstock.com/image-vector/online-cinema-art-movie-watching-600w-586719869.jpg",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                ),
                Video(
                    "https://image.shutterstock.com/image-photo/young-people-sitting-cinema-watching-600w-526930675.jpg",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
                ),
                Video(
                    "https://image.shutterstock.com/image-photo/young-woman-friends-watching-movie-600w-535946224.jpg",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
                ),
                Video(
                    "https://image.shutterstock.com/image-photo/movie-clapper-film-reel-on-600w-169841813.jpg",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"
                )
            )
        )
    }

    class VideoViewHolder(
        itemView: View,
        onClick: (View) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val viewBinding = RowVideoBinding.bind(itemView)
        var videoUrl: String? = null

        init {
            itemView.setOnClickListener {
                onClick(itemView)
            }
        }

        fun bind(item: Video) {
            if (!item.thumb.isBlank()) {
                Glide.with(viewBinding.playerView)
                    .load(item.thumb)
                    .into(viewBinding.playerView.thumb)
            }
            if (!item.url.isBlank()) {
                videoUrl = item.url
                viewBinding.playerView.tag = item
                viewBinding.playerView.transitionName = PlayerFragment.getTransitionName(item)
            }
        }

        fun removePlayer() {
            viewBinding.playerView.player?.playWhenReady = false
            viewBinding.playerView.player = null
        }

        fun playVideo() {
            viewBinding.playerView.player = PlayerSingleton.getInstance(itemView.context).apply {
                volume = 0F
                playWhenReady = true
            }
            videoUrl?.let { viewBinding.playerView.loadVideo(it, true) }
        }

    }

}