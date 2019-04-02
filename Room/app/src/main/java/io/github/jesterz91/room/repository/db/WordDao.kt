package io.github.jesterz91.room.repository.db

import androidx.lifecycle.LiveData
import androidx.room.*

// 데이터 접근 객체
@Dao
interface WordDao {
    // @Query 는 컴파일시 검증
    @Query("SELECT * from word_table ORDER BY id ASC")
    fun getAllWords(): LiveData<List<Word>>

    @Query("DELETE FROM word_table")
    fun deleteAll()

    @Insert
    fun insert(word: Word)

    @Update
    fun update(word: Word)

    @Delete
    fun delete(word: Word)
}