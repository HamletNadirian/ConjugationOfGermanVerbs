package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: VerbsViewModel
    private lateinit var adapter: VerbsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация RecyclerView
        adapter = VerbsAdapter(emptyMap())
        binding.verbsRecycler.layoutManager = LinearLayoutManager(this)
        binding.verbsRecycler.adapter = adapter

        // Инициализация ViewModel
        viewModel = ViewModelProvider(this).get(VerbsViewModel::class.java)

        // Настройка наблюдателей
        setupObservers()

        // Настройка слушателя нажатия кнопки поиска
        binding.searchBtn.setOnClickListener {
            val verb = binding.searchInput.text.toString().trim()
            if (verb.isNotEmpty()) {
                searchVerb(verb)
            }

        }
    }

    private fun setupObservers() {
        // Наблюдатель за данными о глаголе
        viewModel.verMap.observe(this) { data ->
            Log.d("MainActivity", "Получены данные: ${data.size} времен")
            adapter.updateNewData(data)
        }

        // Наблюдатель за текущим глаголом
        viewModel.currentVerb.observe(this) { verb ->
            binding.wordTextview.text = verb
            binding.searchInput.setText(verb)
        }

        // Наблюдатель за состоянием загрузки
        viewModel.isLoading.observe(this) { isLoading ->
            setInProgress(isLoading)
        }

        // Наблюдатель за ошибками
        viewModel.error.observe(this) { errorMsg ->
            Log.e("MainActivity", "Ошибка: $errorMsg")
            // Можно добавить отображение ошибки пользователю
        }

        // Наблюдатель за переводом
        viewModel.translation.observe(this) { translatedText ->
            binding.translateTextview.text = translatedText
        }
    }

    private fun searchVerb(verb: String) {
        Log.d("MainActivity", "Нажата кнопка поиска: $verb")
        setInProgress(true)

        // Загрузка данных о глаголе
        viewModel.loadVerbs(verb)

        // Перевод глагола
        lifecycleScope.launch {
            viewModel.translateVerb(verb)
        }
    }

    private fun setInProgress(inProgress: Boolean) {
        binding.searchBtn.visibility = if (inProgress) View.INVISIBLE else View.VISIBLE
        binding.progressBar.visibility = if (inProgress) View.VISIBLE else View.INVISIBLE
    }
}