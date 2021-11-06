package com.johnmarsel.beatbox

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class BeatBoxViewModel(app: Application): AndroidViewModel(app) {
    val beatBox = BeatBox(app.assets)

    override fun onCleared() {
        super.onCleared()
        beatBox.release()
    }
}