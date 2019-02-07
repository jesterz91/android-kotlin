package io.github.jesterz91.mygallery

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.*

/**
 *  FragmentPagerAdapter
 *
 *  페이지 내용이 영구적일 때 적합, 한번 로딩한 페이지는 메모리에 보관하기 때문에 빠름
 *
 *  FragmentStatePagerAdapter
 *
 *  많은 수의 페이지가 있을 때 적합, 보이지 않는 페이지를 메모리에서 제거할 수 있음
 *
 */

class MyPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    // 뷰 페이저가 표시할 프래그먼트 목록
    private val items = ArrayList<Fragment>()

    // 아이템 갱신
    fun updateFragments(items : List<Fragment>) {
        this.items.addAll(items)
    }

    override fun getItem(position: Int): Fragment {
        return items[position]
    }

    override fun getCount(): Int {
        return items.size
    }
}