package ru.mts.lessons.mvp

import android.content.ContentValues
import android.text.TextUtils
import ru.mts.lessons.R
import ru.mts.lessons.common.User
import ru.mts.lessons.common.UserTable
import ru.mts.lessons.common.UsersModel
import ru.mts.lessons.common.UsersModel.CompleteCallback
import ru.mts.lessons.common.UsersModel.LoadUserCallback

class UsersPresenter(private val model: UsersModel) {
    private var view: UsersActivity? = null
    fun attachView(usersActivity: UsersActivity?) {
        view = usersActivity
    }

    fun detachView() {
        view = null
    }

    fun viewIsReady() {
        loadUsers()
    }

    fun loadUsers() {
        model.loadUsers(object : LoadUserCallback {
            override fun onLoad(users: List<User>?) {
                view!!.showUsers(users)
            }
        })
    }

    fun add() {
        val userData = view!!.userData
        if (TextUtils.isEmpty(userData.name) || TextUtils.isEmpty(userData.email)) {
            view!!.showToast(R.string.empty_values)
            return
        }
        val cv = ContentValues(2)
        cv.put(UserTable.COLUMN.NAME, userData.name)
        cv.put(UserTable.COLUMN.EMAIL, userData.email)
        view!!.showProgress()
        model.addUser(cv, object : CompleteCallback {
            override fun onComplete() {
                view!!.hideProgress()
                loadUsers()
            }
        })
    }

    fun clear() {
        view!!.showProgress()
        model.clearUsers(object : CompleteCallback {
            override fun onComplete() {
                view!!.hideProgress()
                loadUsers()
            }
        })
    }
}