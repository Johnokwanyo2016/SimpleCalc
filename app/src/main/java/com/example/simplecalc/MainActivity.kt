package com .example.simplecalc
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

import com.example.simplecalc.databinding.ActivityMainBinding
import kotlin.properties.ReadOnlyProperty

class MainActivity : AppCompatActivity() {
    private val viewModel: CalculatorViewModel by viewModels()

    private fun viewModels(): ReadOnlyProperty<MainActivity, CalculatorViewModel> {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}
