package io.github.jesterz91.room.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import io.github.jesterz91.room.repository.db.Word
import io.github.jesterz91.room.repository.db.WordDao

// Repository 가 DAO 객체를 가지고 쿼리수행
class WordRepository(private val wordDao: WordDao) {

    val allWords: LiveData<List<Word>> = wordDao.getAllWords()

    // 작업 스레드에서 호출, UI 스레드에서 호출하게 되면 App 종료
    @WorkerThread
    suspend fun insert(word: Word) = wordDao.insert(word)

    @WorkerThread
    suspend fun update(word: Word) = wordDao.update(word)

    @WorkerThread
    suspend fun delete(word: Word) = wordDao.delete(word)

    @WorkerThread
    suspend fun deleteAll() = wordDao.deleteAll()

}