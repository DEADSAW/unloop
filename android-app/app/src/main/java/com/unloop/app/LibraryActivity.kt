package com.unloop.app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.unloop.app.data.Song
import com.unloop.app.data.UnloopDatabase
import com.unloop.app.databinding.ActivityLibraryBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding
    private val database by lazy { UnloopDatabase.getDatabase(this) }
    private lateinit var adapter: SongsAdapter
    
    // Filter state
    private var currentFilterId = R.id.chipAll
    private var searchQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        loadSongs()
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener { finish() }

        // Adapter setup
        adapter = SongsAdapter(
            onWhitelistClick = { song ->
                toggleWhitelist(song)
            },
            onBlacklistClick = { song ->
                toggleBlacklist(song)
            }
        )
        binding.recyclerViewSongs.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSongs.adapter = adapter

        // Search listener
        binding.inputSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchQuery = s?.toString() ?: ""
                loadSongs()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Chip filter listener
        binding.chipGroupFilter.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId != -1) {
                currentFilterId = checkedId
                loadSongs()
            }
        }
    }

    private fun loadSongs() {
        lifecycleScope.launch {
            val dao = database.songDao()
            val flow = when (currentFilterId) {
                R.id.chipWhitelisted -> dao.searchWhitelistedSongs(searchQuery)
                R.id.chipBlacklisted -> dao.searchBlacklistedSongs(searchQuery)
                else -> dao.searchSongs(searchQuery)
            }

            flow.collectLatest { songs ->
                adapter.submitList(songs)
            }
        }
    }

    private fun toggleWhitelist(song: Song) {
        lifecycleScope.launch {
            val newState = !song.isWhitelisted
            // If whitelisting, remove blacklist
            if (newState) {
                database.songDao().setBlacklisted(song.id, false)
            }
            database.songDao().setWhitelisted(song.id, newState)
            // No need to reload, Flow will update automatically
        }
    }

    private fun toggleBlacklist(song: Song) {
        lifecycleScope.launch {
            val newState = !song.isBlacklisted
            // If blacklisting, remove whitelist
            if (newState) {
                database.songDao().setWhitelisted(song.id, false)
            }
            database.songDao().setBlacklisted(song.id, newState)
        }
    }
}
