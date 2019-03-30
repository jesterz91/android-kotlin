package io.github.jesterz91.lifecycles

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SeekBarViewModel : ViewModel() {

    val current = MutableLiveData<Int>()

}