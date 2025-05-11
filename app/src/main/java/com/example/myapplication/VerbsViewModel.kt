package com.example.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class VerbsViewModel : ViewModel() {
    private val _currentVerb = MutableLiveData<String>()
    val currentVerb: LiveData<String> get() = _currentVerb

    private var _verbMap = MutableLiveData<Map<String, TenseData>>()
    val verMap: LiveData<Map<String, TenseData>> get() = _verbMap

    private val _translation = MutableLiveData<String>()
    val translation: LiveData<String> get() = _translation

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadVerbs(verb: String) {
        Log.d("ViewModel", "loadVerbs вызван с: $verb")

        _isLoading.value = true
        _verbMap.value = emptyMap()
        _currentVerb.value = verb


        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val api = RetrofitInstance.dictionaryApi
                    val response = api.getVerbInfo(verb)

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            val responseData = response.body()!!.data
                            _verbMap.postValue(responseData)
                            Log.d("ViewModel", "Получено ${responseData.size} времен")
                        } else {
                            _error.postValue("Ошибка загрузки данных: ${response.code()}")
                            Log.e("ViewModel", "Ошибка API: ${response.code()}")
                        }
                        _isLoading.postValue(false)
                    }
                }
            } catch (e: Exception) {
                _error.postValue("Ошибка: ${e.message}")
                _isLoading.postValue(false)
                Log.e("ViewModel", "Исключение: ${e.message}", e)
            }
        }
    }

    suspend fun translateVerb(verb: String): String {
        return try {
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.GERMAN)
                .setTargetLanguage(TranslateLanguage.RUSSIAN)
                .build()

            val translator = Translation.getClient(options)
            translator.downloadModelIfNeeded().await()

            val result = translator.translate(verb).await()
            _translation.postValue(result)
            result
        } catch (e: Exception) {
            val errorMsg = "Ошибка перевода: ${e.message}"
            _translation.postValue(errorMsg)
            Log.e("ViewModel", errorMsg, e)
            errorMsg
        }
    }
}