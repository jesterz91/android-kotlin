package io.github.jesterz91.searchview

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val disposables by lazy { CompositeDisposable() }

    private val cheeseAdapter by lazy { CheeseAdapter(CHEESE_DATA) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = cheeseAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu?.findItem(R.id.menu_search)?.actionView as SearchView

        searchView.apply {
            maxWidth = Int.MAX_VALUE
            queryHint = "검색어를 입력하세요"
        }.queryTextChanges()
         .debounce(1, TimeUnit.SECONDS)
         .subscribe(cheeseAdapter.filter::filter)
         .addTo(disposables)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}
