package khudiakov.kirill.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import khudiakov.kirill.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Dynamically add on click listener for each button.
        for (child in binding.buttons.children) {
            child.setOnClickListener {
                viewModel.onButtonClicked((child as TextView).text.toString())
            }
        }
    }
}
