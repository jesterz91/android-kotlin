package io.github.jesterz91.room.repository.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

// Room DB에 엔티티(클래스)를 선언하고 버전 번호를 설정 하면 테이블이 생성
@Database(entities = [Word::class], version = 1)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    // Room 데이터베이스 빌더를 사용해 DB 인스턴스 반환
    companion object {

        // 싱글톤 인스턴스
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        // db 인스턴스 반환
        fun getInstance(context: Context): WordRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(context, WordRoomDatabase::class.java, "word.db")
                        .addCallback(object : RoomDatabase.Callback() {
                            // 데이터베이스가 처음 생성 될 때 호출
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                Log.d("DB", "DB created")
                            }
                            // 데이터베이스가 열릴 때 호출
                            override fun onOpen(db: SupportSQLiteDatabase) {
                                super.onOpen(db)
                                Log.d("DB", "DB opened")
                            }
                        }).build()
                }
            }
            return INSTANCE
        }
    }

}