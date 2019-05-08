package io.github.jesterz91.pagingwithroom.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.github.jesterz91.pagingwithroom.util.CHEESE_DATA
import io.github.jesterz91.pagingwithroom.util.ioThread

@Database(entities = [Cheese::class], version = 1)
abstract class CheeseDb : RoomDatabase() {

    abstract fun cheeseDao(): CheeseDao

    companion object {
        private var instance: CheeseDb? = null

        @Synchronized
        fun getInstance(context: Context): CheeseDb {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                        CheeseDb::class.java, "CheeseDatabase")
                        .addCallback(object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                // DB 생성 시 데이터 삽입
                                fillInDb(context.applicationContext)
                            }
                        }).build()
            }
            return instance!!
        }

        private fun fillInDb(context: Context) {
            ioThread {
                getInstance(context).cheeseDao().insert(
                        CHEESE_DATA.map { Cheese(id = 0, name = it) })
            }
        }
    }

}