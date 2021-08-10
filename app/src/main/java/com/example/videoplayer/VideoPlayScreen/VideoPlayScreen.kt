package com.example.videoplayer.VideoPlayScreen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.videoplayer.R

class VideoPlayScreen : AppCompatActivity() {

    private val PERMISSION_READ = 0
    private lateinit var videoView: VideoView

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_play_screen)
        if (checkPermission()) {
            setVideo()
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

    private fun setVideo() {
        videoView =  findViewById(R.id.video_view)
        val videoUri: Uri = Uri.parse(intent.getStringExtra("uri"))
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(videoUri)
        videoView.requestFocus()
        videoView.start()
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
                    setVideo()
                }
            }
        }
    }
}