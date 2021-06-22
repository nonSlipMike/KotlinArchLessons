package ru.mts.lessons.mvp

import android.content.ContentValues
import ru.mts.lessons.R
import ru.mts.lessons.common.User
import ru.mts.lessons.common.UserTable
import ru.mts.lessons.common.UsersModel.CompleteCallback
import ru.mts.lessons.common.UsersModel.LoadUserCallback
import ru.mts.lessons.common.UsersModelApi

class UsersPresenter(private val model: UsersModelApi) {
    private var view: ViewApi? = null
     fun attachView(usersActivity: ViewApi?) {
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
                view?.showUsers(users)
            }
        })
    }

     fun add() {
        view?.getUsers()?.apply {
            if (this.name.isEmpty() || this.email.isEmpty()) {
                view?.showToast(R.string.empty_values)
                return
            }
            val cv = ContentValues(2)
            cv.put(UserTable.COLUMN.NAME, this.name)
            cv.put(UserTable.COLUMN.EMAIL, this.email)
            view?.showProgress()
            model.addUser(cv, object : CompleteCallback {
                override fun onComplete() {
                    view?.hideProgress()
                    loadUsers()
                }
            })
        }
    }

     fun clear() {
        view?.showProgress()
        model.clearUsers(object : CompleteCallback {
            override fun onComplete() {
                view?.hideProgress()
                loadUsers()
            }
        })
    }
}