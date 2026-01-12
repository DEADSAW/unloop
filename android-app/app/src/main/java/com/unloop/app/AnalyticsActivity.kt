package com.unloop.app

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.unloop.app.data.StatsRepository
import com.unloop.app.databinding.ActivityAnalyticsBinding
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AnalyticsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalyticsBinding
    private lateinit var repository: StatsRepository
    
    // Colors
    private val colorPrimary by lazy { getColor(R.color.primary) }
    private val colorAccent by lazy { getColor(R.color.accent) }
    private val colorText by lazy { getColor(R.color.on_surface) }
    private val colorTextSec by lazy { getColor(R.color.on_surface_secondary) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyticsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        repository = StatsRepository.getInstance(this)
        
        setupUI()
        loadData()
    }
    
    private fun setupUI() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnShare.setOnClickListener { shareStats() }
        
        setupBarChart()
        setupPieChart()
    }
    
    private fun setupBarChart() {
        binding.chartActivity.apply {
            description.isEnabled = false
            setDrawGridBackground(false)
            setDrawBorders(false)
            
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                textColor = colorTextSec
                granularity = 1f
            }
            
            axisLeft.apply {
                setDrawGridLines(false) // Clean look
                textColor = colorTextSec
                axisMinimum = 0f
            }
            
            axisRight.isEnabled = false
            legend.textColor = colorText
            animateY(1000)
        }
    }
    
    private fun setupPieChart() {
        binding.chartArtists.apply {
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.TRANSPARENT)
            setTransparentCircleColor(Color.TRANSPARENT)
            setTransparentCircleAlpha(0)
            holeRadius = 50f
            
            legend.isEnabled = false // Too clear without legend if labels are good
            setEntryLabelColor(Color.WHITE)
            animateY(1400)
        }
    }
    
    private fun loadData() {
        lifecycleScope.launch {
            val stats = repository.getStats()
            
            // Update small cards
            binding.cardNewSongs.statValue.text = "${stats.newSongsThisWeek}"
            binding.cardNewSongs.statLabel.text = "New Songs (Week)"
            binding.cardNewSongs.root.setCardBackgroundColor(getColor(R.color.card_background)) // Ensure correct color
            
            binding.cardLoops.statValue.text = "${stats.totalLoopsAvoided}"
            binding.cardLoops.statLabel.text = "Loops Avoided"
            binding.cardLoops.root.setCardBackgroundColor(getColor(R.color.card_background))

            // Load charts
            val dailyStats = repository.getDailyStatsForChart(7).reversed() // Chronological
            updateBarChart(dailyStats)
            
            val topArtists = stats.topArtists.take(5)
            updatePieChart(topArtists)
        }
    }
    
    private fun updateBarChart(stats: List<com.unloop.app.data.DailyStats>) {
        if (stats.isEmpty()) return
        
        val entries = stats.mapIndexed { index, stat ->
            BarEntry(index.toFloat(), floatArrayOf(stat.songsDiscovered.toFloat(), stat.loopsAvoided.toFloat()))
        }
        
        val dates = stats.map { stat -> 
            try {
                // Input: 2024-01-20 -> Output: Jan 20
                val input = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(stat.date)
                SimpleDateFormat("MMM d", Locale.US).format(input!!)
            } catch (e: Exception) { stat.date }
        }
        
        val set = BarDataSet(entries, "").apply {
            stackLabels = arrayOf("New Songs", "Skipped")
            setColors(colorPrimary, colorAccent)
            valueTextColor = colorText
            valueTextSize = 10f
        }
        
        binding.chartActivity.apply {
            data = BarData(set).apply { barWidth = 0.5f }
            xAxis.valueFormatter = IndexAxisValueFormatter(dates)
            invalidate()
        }
    }
    
    private fun updatePieChart(artists: List<com.unloop.app.data.ArtistStats>) {
        if (artists.isEmpty()) return
        
        val entries = artists.map { 
            PieEntry(it.songCount.toFloat(), it.name) 
        }
        
        val colors = listOf(
            getColor(R.color.primary),
            getColor(R.color.accent),
            getColor(R.color.success),
            getColor(R.color.warning),
            getColor(R.color.info)
        )
        
        val set = PieDataSet(entries, "Top Artists").apply {
            setColors(colors)
            valueTextColor = Color.WHITE
            valueTextSize = 12f
        }
        
        binding.chartArtists.apply {
            data = PieData(set)
            invalidate()
        }
    }
    
    private fun shareStats() {
        try {
            // Capture screenshot of scroll content
            val view = binding.scrollRoot.getChildAt(0)
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            
            // Save to cache
            val file = File(cacheDir, "unloop_stats.png")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            
            // Share
            val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(intent, "Share Stats"))
            
        } catch (e: Exception) {
            Toast.makeText(this, "Error sharing: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
