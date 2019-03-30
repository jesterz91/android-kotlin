package io.github.jesterz91.lifecycles

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.concurrent.timer

class TimerViewModel : ViewModel() {

    private val ONE_SECOND = 1000L

    private var elapsedTime = MutableLiveData<Long>()

    val time: LiveData<Long>
        get() = elapsedTime

    init {
        val initialTime = SystemClock.elapsedRealtime()

        timer(period = ONE_SECOND) {
            val newValue = (SystemClock.elapsedRealtime() - initialTime) / 1000L
            // UI 스레드에서는 setValue()를 호출하고
            // 작업 스레드에서는 postValue()를 호출
            elapsedTime.postValue(newValue)
        }
    }

}