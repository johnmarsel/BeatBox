package com.johnmarsel.beatbox

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.SoundPool
import android.util.Log
import java.io.IOException

private const val TAG = "BeatBox"
private const val SOUNDS_FOLDER = "sample_sounds"
private const val MAX_SOUNDS = 5
private const val SOUND_RATE_MAX = 2.5f
private const val SOUND_RATE_MIN = 0.5f

class BeatBox(private val assets: AssetManager) {

    val sounds: List<Sound>
    private val soundPool = SoundPool.Builder()
        .setMaxStreams(MAX_SOUNDS)
        .build()
    var soundSpeed = 1.0f

    init {
        sounds = loadSounds()
    }

    fun play(sound: Sound) {
        if (soundSpeed != 0.0f) {
            sound.soundId?.let {
                soundPool.play(it, 1.0f, 1.0f, 1, 0, soundSpeed)
            }
        }
    }

    fun changeSoundSpeed(speed: Int) {
        soundSpeed = when (speed) {
            0 -> 0.0f
            else -> (SOUND_RATE_MAX - SOUND_RATE_MIN) / 100 * speed + SOUND_RATE_MIN
        }
    }

    fun release() {
        soundPool.release()
    }

    private fun loadSounds(): List<Sound> {
        val soundNames: Array<String>
        try {
            soundNames = assets.list(SOUNDS_FOLDER)!!
        } catch (e: Exception) {
            Log.e(TAG, "Could not list assets", e)
            return emptyList()
        }
        val sounds = mutableListOf<Sound>()
        soundNames.forEach { filename ->
            val assetPath = "$SOUNDS_FOLDER/$filename"
            val sound = Sound(assetPath)
            try {
                load(sound)
                sounds.add(sound)
            } catch (ioe: IOException) {
                Log.e(TAG, "Cound not load sound $filename", ioe)
            }
        }
        return sounds
    }

    private fun load(sound: Sound) {
        val afd: AssetFileDescriptor = assets.openFd(sound.assetPath)
        val soundId = soundPool.load(afd, 1)
        sound.soundId = soundId
    }
}