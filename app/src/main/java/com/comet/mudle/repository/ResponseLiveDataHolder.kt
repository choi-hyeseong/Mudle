package com.comet.mudle.repository

import androidx.lifecycle.LiveData

interface ResponseLiveDataHolder {

    fun getResponseLiveData() : LiveData<String>
}