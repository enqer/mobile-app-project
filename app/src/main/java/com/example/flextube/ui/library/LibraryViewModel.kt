package com.example.flextube.ui.library

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flextube.api.ApiServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LibraryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text



}