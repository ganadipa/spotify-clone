package com.example.adbpurrytify.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.example.adbpurrytify.R
import com.example.adbpurrytify.data.model.SongEntity
import com.example.adbpurrytify.ui.components.MinimalDropdownMenu
import com.example.adbpurrytify.utils.shareSong
import com.example.adbpurrytify.utils.shareSongQR

class SongAdapter(
    private var songs: List<SongEntity>,
    private val onSongClick: (SongEntity) -> Unit
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val albumArt: ImageView = view.findViewById(R.id.iv_album_art)
        val songTitle: TextView = view.findViewById(R.id.tv_song_title)
        val artistName: TextView = view.findViewById(R.id.tv_artist_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song_spotify, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]

        holder.songTitle.text = song.title
        holder.artistName.text = song.author

        // Load image with Coil3 - Updated syntax
        if (song.artUri.isNotEmpty()) {
            holder.albumArt.load(song.artUri) {
                placeholder(R.drawable.song_art_placeholder)
                error(R.drawable.song_art_placeholder)
                crossfade(true)
            }
        } else {
            holder.albumArt.setImageResource(R.drawable.song_art_placeholder)
        }

        holder.itemView.setOnClickListener {
            onSongClick(song)
        }

        val composeOverflowView = holder.itemView.findViewById<ComposeView>(R.id.compose_overflow_menu)
        composeOverflowView.setContent {
            MinimalDropdownMenu(
                onShareViaUrl = {
                    if (song.audioUri.startsWith("http")) {
                        shareSong(holder.itemView.context, song.id)
                    }
                },
                onShareViaQr = {
                    if (song.audioUri.startsWith("http")) {
                        shareSongQR(holder.itemView.context, song.id)
                    }
                }
            )
        }
    }

    override fun getItemCount() = songs.size

    fun updateSongs(newSongs: List<SongEntity>) {
        songs = newSongs
        notifyDataSetChanged()
    }
}

