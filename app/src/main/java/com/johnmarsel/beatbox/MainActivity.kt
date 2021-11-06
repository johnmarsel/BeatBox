package com.johnmarsel.beatbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.johnmarsel.beatbox.databinding.ActivityMainBinding
import com.johnmarsel.beatbox.databinding.ListItemSoundBinding

class MainActivity : AppCompatActivity() {

    private val beatBoxViewModel: BeatBoxViewModel by lazy {
        ViewModelProviders.of(this).get(BeatBoxViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = SoundAdapter(beatBoxViewModel.beatBox.sounds)
        }
        binding.seekBarText.text = getString(R.string.seekbar_text,
            binding.seekBar.progress.toString())
        binding.seekBar.apply {
            setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?,
                                               progress: Int,
                                               fromUser: Boolean) {
                    binding.seekBarText.text = getString(R.string.seekbar_text, progress.toString())
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {    }
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    if (seekBar != null) {
                        beatBoxViewModel.beatBox.changeSoundSpeed(seekBar.progress)
                    }
                }
            })
        }
    }

    private inner class SoundHolder(private val binding: ListItemSoundBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewModel = SoundViewModel(beatBoxViewModel.beatBox)
        }

        fun bind(sound: Sound) {
            binding.apply {
                viewModel?.sound = sound
                executePendingBindings()
            }
        }
    }

    private inner class SoundAdapter(private val sounds: List<Sound>)
        : RecyclerView.Adapter<SoundHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
            val binding = DataBindingUtil.inflate<ListItemSoundBinding>(
                layoutInflater,
                R.layout.list_item_sound,
                parent,
                false
            )
            return SoundHolder(binding)
        }

        override fun onBindViewHolder(holder: SoundHolder, position: Int) {
            val sound = sounds[position]
            holder.bind(sound)
        }

        override fun getItemCount() = sounds.size
    }
}