package com.sameh.stopwatch

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sameh.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var stopWatchViewModel: StopWatchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        stopWatchViewModel = ViewModelProvider(this)[StopWatchViewModel::class.java]
        setActions()
        elapsedTimeObserver()
        isRunningObserver()
    }

    private fun setActions() {
        binding.btnStart.setOnClickListener {
            stopWatchViewModel.start()
        }
        binding.btnStop.setOnClickListener {
            stopWatchViewModel.stop()
        }
        binding.btnReset.setOnClickListener {
            stopWatchViewModel.reset()
        }
    }

    private fun elapsedTimeObserver() {
        stopWatchViewModel.elapsedTimeMillisLiveData.observe(this) { elapsedTime ->
            Log.d(Constants.AppTAG, "elapsedTimeObserver: $elapsedTime")
            val formattedTime = formatElapsedTime(elapsedTime)
            binding.chronometer.text = formattedTime
        }
    }

    private fun formatElapsedTime(elapsedTime: Long): String {
        val seconds = (elapsedTime / 1000).toInt()
        val minutes = seconds / 60
        val hours = minutes / 60
        val formattedSeconds = seconds % 60
        val formattedMinutes = minutes % 60
        val formattedHours = hours % 24
        return String.format("%02d:%02d:%02d", formattedHours, formattedMinutes, formattedSeconds)
    }

    private fun isRunningObserver() {
        stopWatchViewModel.isTimeRunningLiveData.observe(this) { isRunning ->
            Log.d(Constants.AppTAG, "isRunningObserver: $isRunning")
            if (isRunning) {
                stopWatchViewModel.start()
                if (!binding.lottieAnimation.isAnimating) {
                    binding.lottieAnimation.playAnimation()
                }
            } else {
                binding.lottieAnimation.cancelAnimation()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopWatchViewModel.stop()
    }
}