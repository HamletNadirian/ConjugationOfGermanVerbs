package com.example.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VerbsViewModel : ViewModel() {
    private var _verbMap = MutableLiveData<Map<String, TenseData>>()

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error
    val verMap: LiveData<Map<String, TenseData>> get() = _verbMap

    fun loadVerbs(verb: String) {
        Log.d("ViewModel", "loadVerbs вызван с: $verb")

        viewModelScope.launch {
            try {

             /*   val api = Retrofit.Builder()
                    .baseUrl("https://german-verbs-api.onrender.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ConjugationApi::class.java)*/

                val api = RetrofitInstance.dictionaryApi
                val response = api.getVerbInfo(verb)

                if (response.isSuccessful && response.body() != null) {
                    _verbMap.value = response.body()!!.data
                    Log.d("ViewModel", "Получено ${response.body()!!.data.size} времен")

                }
                else {
                    _error.value = "Ошибка загрузки данных: ${response.code()}"

                }
            } catch (e: Exception) {
                _error.value = "Ошибка: ${e.message}"
                Log.e("ViewModel", "Ошибка: ${e.message}")

            }
        }

    }
}