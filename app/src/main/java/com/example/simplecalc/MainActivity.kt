package com.example.simplecalc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.simplecalc.databinding.ActivityMainBinding
import com.example.simplecalc.viewmodel.CalculatorViewModel
import kotlin.properties.ReadOnlyProperty

class MainActivity : AppCompatActivity() {

    private val viewModel: CalculatorViewModel by viewModels()

    private fun viewModels(): ReadOnlyProperty<MainActivity, CalculatorViewModel> {
        TODO("Not yet implemented")
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set up data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }
}
