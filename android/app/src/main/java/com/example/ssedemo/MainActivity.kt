package com.example.ssedemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.bumptech.glide.Glide
import com.bumptech.glide.TransitionOptions
import com.example.ssedemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var viewModel: SSEViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SSEViewModel::class.java]

        val glide = Glide.with(this)

        viewModel?.sseEvents?.observe(this) {
            it?.let { event ->
                when(event.status) {

                    STATUS.OPEN -> {
                        binding.image.isVisible = false
                        binding.text.isVisible = true
                        binding.text.text = "Session opened"
                    }

                    STATUS.SUCCESS -> {
                        if (event.image.isNullOrEmpty().not()){
                            binding.image.isVisible = true
                            binding.text.isVisible = false
                            glide
                                .load(event.image)
                                .into(binding.image)
                        } else {
                            binding.image.isVisible = false
                            binding.text.isVisible = true
                            binding.text.text = "No image received"
                        }
                    }

                    STATUS.ERROR -> {
                        binding.image.isVisible = false
                        binding.text.isVisible = true
                        binding.text.text = "Session Error"
                    }

                    STATUS.CLOSED -> {
                        binding.image.isVisible = false
                        binding.text.isVisible = true
                        binding.text.text = "Session closed"
                    }

                    else -> {
                        // STATUS.NONE
                        binding.text.isVisible = false
                        binding.image.isVisible = false
                    }
                }
            }
        }

        viewModel?.getSSEEvents()

    }
}