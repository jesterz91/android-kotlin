package io.github.jesterz91.pagingwithroom.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.github.jesterz91.pagingwithroom.db.Cheese
import io.github.jesterz91.pagingwithroom.db.CheeseDb
import io.github.jesterz91.pagingwithroom.util.ioThread

class CheeseViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = CheeseDb.getInstance(app).cheeseDao()

    private val config = PagedList.Config.Builder()
        .setPageSize(10)
        .setInitialLoadSizeHint(10)
        .setPrefetchDistance(5)
        .setEnablePlaceholders(false)
        .build()

    val allCheeses: LiveData<PagedList<Cheese>>
            = LivePagedListBuilder(dao.allCheeses(), config).build()

    fun insert(text: CharSequence) = ioThread {
        dao.insert(Cheese(id = 0, name = text.toString()))
    }

    fun remove(cheese: Cheese) = ioThread {
        dao.delete(cheese)
    }

}