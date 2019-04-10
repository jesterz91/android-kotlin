package io.github.jesterz91.databinding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {

    val user = MutableLiveData<User>()

    init {
        user.value = User("토니 스타크", "아이언맨", 0)
    }

    fun increase() {
        val name = user.value?.name ?: "토니 스타크"
        val nickName = user.value?.nickNme ?: "아이언맨"
        val like = user.value?.like ?: 0

        user.value = User(name, nickName, like + 1)
    }
}