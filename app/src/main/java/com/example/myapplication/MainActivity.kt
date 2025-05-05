package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: VerbsViewModel

    lateinit var adapter: VerbsAdapter
    lateinit var verb: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //  adapter = VerbsAdapter(emptyList())
        adapter = VerbsAdapter(emptyMap()) // при инициализации
        binding.verbsRecycler.layoutManager = LinearLayoutManager(this)
        binding.verbsRecycler.adapter = adapter

        viewModel = ViewModelProvider(this).get(VerbsViewModel::class.java)
        viewModel.verMap.observe(this) { data ->
            Log.d("MainActivity", "Получены данные: ${data.size} времен")
            setInProgress(false)
            adapter.updateNewData(data)
            binding.wordTextview.text = binding.searchInput.text.toString()
        }
        viewModel.error.observe(this) { errorMsg ->

            setInProgress(false)
            Log.e("API", errorMsg)
        }

        binding.searchBtn.setOnClickListener {
            verb = binding.searchInput.text.toString()
            if (verb.isNotEmpty()) {
                Log.d("MainActivity", "Нажата кнопка поиска: $verb") // <- проверь это
                setInProgress(true)
                viewModel.loadVerbs(verb)
            }
        }
    }

    private fun setInProgress(inProgress: Boolean) {
        binding.searchBtn.visibility = if (inProgress) View.INVISIBLE else View.VISIBLE
        binding.progressBar.visibility = if (inProgress) View.VISIBLE else View.INVISIBLE
    }
}
