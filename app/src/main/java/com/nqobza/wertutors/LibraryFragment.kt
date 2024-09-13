package com.nqobza.wetutors


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.nqobza.wertutors.R


// Replace with your actual package name
lateinit var videoView: VideoView
class LibraryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {val view = inflater.inflate(R.layout.fragment_library, container, false) // Inflate the layout

         // Find the VideoView

        // Set the video source (replace with your actual video path or URL)


        // Start video playback
        videoView.start()

        return view
    }
}