package ru.mts.lessons.mvvm

import android.content.ContentValues
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mts.lessons.App
import ru.mts.lessons.common.User
import ru.mts.lessons.common.UserTable
import ru.mts.lessons.database.DbHandler
import ru.mts.lessons.mvp.UserData
import ru.mts.lessons.mvp.UsersModel

typealias MyViewState = MvvmActivity.ViewState

class MvvmViewModel : ViewModel() {

    private val model = UsersModel(DbHandler(App.applicationContext))

    val viewState: LiveData<MyViewState> get() = _viewState
    private val _viewState = MutableLiveData<MyViewState>()

    val dataList: LiveData<List<User>> get() = _dataList
    private val _dataList = MutableLiveData<List<User>>()


    fun loadUsers() {
        model.loadUsers(object : UsersModel.LoadUserCallback {
            override fun onLoad(users: List<User>?) {
                _dataList.postValue(users)
                _viewState.postValue(MyViewState(isDownloaded = false))
            }
        })
    }

    fun add(userData: UserData) {
        if (TextUtils.isEmpty(userData.name) || TextUtils.isEmpty(userData.email)) {
            return
        }
        val cv = ContentValues(2)
        cv.put(UserTable.COLUMN.NAME, userData.name)
        cv.put(UserTable.COLUMN.EMAIL, userData.email)
        _viewState.postValue(MyViewState(isDownloaded = true))
        model.addUser(cv, object : UsersModel.CompleteCallback {
            override fun onComplete() {
                loadUsers()
            }
        })
    }

    fun clear() {
        _viewState.postValue(MyViewState(isDownloaded = true))
        model.clearUsers(object : UsersModel.CompleteCallback {
            override fun onComplete() {
                loadUsers()
            }
        })
    }
}