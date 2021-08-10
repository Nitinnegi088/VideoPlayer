package com.example.videoplayer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videoplayer.Adapters.VideoAdapter
import com.example.videoplayer.Model.VideoDataClass
import com.example.videoplayer.VideoPlayScreen.VideoPlayScreen

class MainActivity : AppCompatActivity(),VideoAdapter.OnItemClickListener {

    private var videoArrayList = ArrayList<VideoDataClass>()
    private val PERMISSION_READ = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler_view)
        if (checkPermission()) {
            videoList()
            setupRecyclerView()
        }
    }

    private fun setupRecyclerView() {
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        videoAdapter = VideoAdapter(this, ArrayList<VideoDataClass>(),this)
        videoAdapter.setData(videoArrayList)
        val gridLayoutManager= GridLayoutManager(this,3)
        recyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(SpacesItemDecoration(spacingInPixels))
            layoutManager = gridLayoutManager
            adapter = videoAdapter
        }
    }

    fun checkPermission(): Boolean {
        var READ_EXTERNAL_PERMISSION : Int = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this,permissions ,0)
            return false
        }
        return true
    }

    private fun videoList() {
        val contentResolver = contentResolver
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val cursor = contentResolver.query(uri, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                var title: String = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                var duration: String = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                var data: String = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                var uri = Uri.parse(data)
                var durationModel = timeConversion(duration.toLong())
                var videosDataClass  = VideoDataClass(title,durationModel,uri)
                    videoArrayList.add(videosDataClass)
            } while (cursor.moveToNext())
        }
    }
     private fun timeConversion(value: Long): String {
        var videoTime: String
        var dur: Int = value.toInt()
        var hrs: Int = (dur / 3600000)
        var mns: Int = (dur / 60000) % 60000
        var scs: Int = dur % 60000 / 1000

         videoTime = if (hrs > 0) {
             String.format("%02d:%02d:%02d", hrs, mns, scs)
         } else {
             String.format("%02d:%02d", mns, scs)
         }
         return videoTime
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_READ -> if (grantResults.isNotEmpty() && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)){
                if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(applicationContext, "Please allow storage permission", Toast.LENGTH_LONG).show();
                }else{
                    videoList()
                    setupRecyclerView()
                }
            }
        }
    }

    override fun onItemClick(uri: Uri) {
        intent = Intent(this, VideoPlayScreen::class.java)
        intent.putExtra("uri", uri.toString())
        startActivity(intent)
    }

    class SpacesItemDecoration(private val spaceSize: Int, private val spanCount: Int = 3, private val orientation: Int = GridLayoutManager.VERTICAL): RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            with(outRect) {
                if (orientation == GridLayoutManager.VERTICAL) {
                    if (parent.getChildAdapterPosition(view) < spanCount) {
                        top = spaceSize
                    }
                    if (parent.getChildAdapterPosition(view) % spanCount == 0) {
                        left = spaceSize
                    }
                } else {
                    if (parent.getChildAdapterPosition(view) < spanCount) {
                        left = spaceSize
                    }
                    if (parent.getChildAdapterPosition(view) % spanCount == 0) {
                        top = spaceSize
                    }
                }

                right = spaceSize
                bottom = spaceSize
            }
        }
    }
}