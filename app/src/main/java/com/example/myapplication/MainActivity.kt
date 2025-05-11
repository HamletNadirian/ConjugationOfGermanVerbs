package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
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

       // adapter = VerbsAdapter(emptyList())
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
/*    @Composable
    fun SearchButton(clicked: () -> Unit) {
        Button(onClick = clicked) {
            Text(text = "Search")
        }
    }*/

/*
    @Composable
    fun ResultCompose(viewModel: VerbsViewModel) {
        val inProgress by viewModel.inProgress.observeAsState(false)
        var verbText by remember { mutableStateOf("") }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(
                value = verbText,
                onValueChange = { verbText = it },
                label = { Text("Введите глагол") }
            )

            Button(
                onClick = {
                    if (verbText.isNotEmpty()) {
                        Log.d("Compose", "Нажата кнопка поиска: $verbText")
                        viewModel.loadVerbs(verbText)
                    }
                },
                modifier = Modifier.alpha(if (inProgress) 0f else 1f)
            ) {
                Text("Поиск")
            }

            if (inProgress) {
                CircularProgressIndicator()
            }
        }
    }*/

/*    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MaterialTheme {
            Surface {
                ResultCompose(viewModel)
            }




        }
    }*/
}
