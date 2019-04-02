package io.github.jesterz91.room.repository.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// 테이블 이름을 클래스명과 다르게 하려면
// 어노테이션으로 테이블 네임과 컬럼 네임을 지정
@Entity(tableName = "word_table")
data class Word (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "word") // Column 명도 변수 이름과 다르게 지정가능
    val word: String
)
