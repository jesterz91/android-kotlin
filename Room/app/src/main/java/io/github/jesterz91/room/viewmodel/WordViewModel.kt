package io.github.jesterz91.room.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.github.jesterz91.room.repository.WordRepository
import io.github.jesterz91.room.repository.db.Word
import io.github.jesterz91.room.repository.db.WordRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// ViewModel 의 역할은 데이터를 UI에 제공하고 구성 변경 사항을 유지
// ViewModel 에서 Context 가 필요할 경우 AndroidViewModel 사용
class WordViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    // 코루틴 스코프 정의
    private val scope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val repository: WordRepository

    val allWords: LiveData<List<Word>>

    init {
        // DB 인스턴스에서 DAO 획득
        val wordsDao = WordRoomDatabase.getInstance(application)!!.wordDao()
        // Repository 에 DAO 를 전달하여 초기화
        repository = WordRepository(wordsDao)
        // LiveData 초기화
        allWords = repository.allWords
    }

    fun insert(word: Word) {
        scope.launch(Dispatchers.IO) {
            repository.insert(word)
        }
    }

    fun update(word: Word) {
        scope.launch(Dispatchers.IO) {
            repository.update(word)
        }
    }

    fun delete(word: Word) {
        scope.launch(Dispatchers.IO) {
            repository.delete(word)
        }
    }

    fun deleteAll() {
        scope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    // ViewModel 이 사라질 때 코루틴 job 취소
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}