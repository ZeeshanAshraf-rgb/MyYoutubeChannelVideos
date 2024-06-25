package com.example.youtubechanelproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class VideoAdapter : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {
    private var videos: ArrayList<Video> = arrayListOf()
    private var listener: ItemClickListener? = null

    fun setListener(listener1: ItemClickListener?) {
        listener = listener1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        holder.title.text = video.snippet.title
        Glide.with(holder.thumbnail.context).load(video.snippet.thumbnails.medium.url)
            .into(holder.thumbnail)
        holder.rootView.setOnClickListener {
            listener?.onVideoCLick(video.id.videoId)
        }
    }

    override fun getItemCount(): Int = videos.size

    fun setVideos(videos: List<Video>) {
        this.videos = videos as ArrayList<Video>
        notifyDataSetChanged()
    }

    class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.video_title)
        val thumbnail: ImageView = view.findViewById(R.id.video_thumbnail)
        val rootView: LinearLayout = view.findViewById(R.id.videoRootView)
    }
}
