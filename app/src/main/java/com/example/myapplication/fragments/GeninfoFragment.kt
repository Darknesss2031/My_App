package com.example.myapplication.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentGeninfoBinding
import com.squareup.picasso.Picasso
import java.net.URI

class GeninfoFragment : Fragment() {

    lateinit var binding: FragmentGeninfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGeninfoBinding.inflate(layoutInflater)
        //binding.webView.loadUrl("https://www.youtube.com/watch?v=2IjQWQOkr1U&ab_channel=GamingGenius")
        var videoUrl = "youtube.com/watch?v=2IjQWQOkr1U&ab_channel=GamingGenius"
        val uri = Uri.parse(videoUrl)
        binding.videoView2.setVideoURI(uri)
        val mediaController = MediaController(this.context)
        mediaController.setAnchorView(binding.videoView2)
        mediaController.setMediaPlayer(binding.videoView2)
        binding.videoView2.setMediaController(mediaController)
        return binding.root
    }

}