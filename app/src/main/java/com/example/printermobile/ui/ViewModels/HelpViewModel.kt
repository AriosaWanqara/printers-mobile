package com.example.printermobile.ui.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.printermobile.data.network.repositories.FaQRepository
import com.example.printermobile.domain.models.FaQ
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor(private val repository: FaQRepository) : ViewModel() {
    var faQ = MutableLiveData<List<FaQ>>()
    var isFaQLoading = MutableLiveData<Boolean>()
    fun onCreate() {
        viewModelScope.launch {
            try {
                isFaQLoading.value = true
                val response = repository()
                faQ.value = response
            } catch (e: Exception) {
                println(e)
            } finally {
                isFaQLoading.value = false
            }
        }
    }
}