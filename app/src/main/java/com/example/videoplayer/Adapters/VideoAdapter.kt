package com.example.videoplayer.Adapters

import android.content.Context
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.videoplayer.MainActivity
import com.example.videoplayer.Model.VideoDataClass
import com.example.videoplayer.R
import java.io.File

class VideoAdapter(private var context: Context,private var videoArrayList: ArrayList<VideoDataClass>,private var listner: OnItemClickListener):
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.video_adapter,null))
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.title.text = videoArrayList[position].videoTitle
        holder.duration.text = videoArrayList[position].videoDuration
        var filePath = "${videoArrayList[position].videoUrl}"
        Glide.with(context)
            .asBitmap()
            .load(Uri.fromFile(File(filePath)))
            .into(holder.image)
    }

    fun setData(videoArrayList: ArrayList<VideoDataClass>){
        this.videoArrayList = videoArrayList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = videoArrayList.size

    inner class VideoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),View.OnClickListener{
        val title: TextView = itemView.findViewById(R.id.title)
        val duration: TextView = itemView.findViewById(R.id.duration)
        val image: ImageView = itemView. findViewById(R.id.image)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = layoutPosition
            if(position != RecyclerView.NO_POSITION) {
                listner.onItemClick(videoArrayList.get(position).videoUrl)
            }
        }
    }
    interface OnItemClickListener{
        fun onItemClick(uri: Uri)
    }
}