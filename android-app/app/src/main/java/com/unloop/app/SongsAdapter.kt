package com.unloop.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unloop.app.data.Song
import com.unloop.app.databinding.ItemSongLibraryBinding

class SongsAdapter(
    private val onWhitelistClick: (Song) -> Unit,
    private val onBlacklistClick: (Song) -> Unit
) : ListAdapter<Song, SongsAdapter.ViewHolder>(SongDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSongLibraryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemSongLibraryBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(song: Song) {
            binding.textTitle.text = song.title
            binding.textArtist.text = song.artist
            binding.textStats.text = "Played ${song.playCount} • Skipped ${song.skipCount}"
            
            // Update icons based on state
            if (song.isWhitelisted) {
                binding.btnWhitelist.setImageResource(R.drawable.ic_heart_filled)
                binding.btnWhitelist.setColorFilter(binding.root.context.getColor(R.color.success))
            } else {
                binding.btnWhitelist.setImageResource(R.drawable.ic_heart_outline)
                binding.btnWhitelist.setColorFilter(binding.root.context.getColor(R.color.on_surface_secondary))
            }

            if (song.isBlacklisted) {
                binding.btnBlacklist.setImageResource(R.drawable.ic_block_filled)
                binding.btnBlacklist.setColorFilter(binding.root.context.getColor(R.color.error))
            } else {
                binding.btnBlacklist.setImageResource(R.drawable.ic_block_outline)
                binding.btnBlacklist.setColorFilter(binding.root.context.getColor(R.color.on_surface_secondary))
            }
            
            binding.btnWhitelist.setOnClickListener { onWhitelistClick(song) }
            binding.btnBlacklist.setOnClickListener { onBlacklistClick(song) }
        }
    }
}

class SongDiffCallback : DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem == newItem
    }
}
