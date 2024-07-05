package com.example.youtubechanelproject.others

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class VideoData(
    var videoId: String,
    var isWatched: Boolean = false
) : Parcelable