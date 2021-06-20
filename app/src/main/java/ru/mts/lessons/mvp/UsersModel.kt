package ru.mts.lessons.mvp

import android.content.ContentValues
import android.os.AsyncTask
import ru.mts.lessons.common.User
import ru.mts.lessons.common.UserTable
import ru.mts.lessons.database.DbHandler
import java.util.LinkedList
import java.util.concurrent.TimeUnit

class UsersModel(private val dbHandler: DbHandler) {
    fun loadUsers(callback: LoadUserCallback?) {
        val loadUsersTask: LoadUsersTask = LoadUsersTask(dbHandler, callback)
        loadUsersTask.execute()
    }

    fun addUser(contentValues: ContentValues?, callback: CompleteCallback?) {
        val addUserTask: AddUserTask = AddUserTask(dbHandler, callback)
        addUserTask.execute(contentValues)
    }

    fun clearUsers(completeCallback: CompleteCallback?) {
        val clearUsersTask: ClearUsersTask = ClearUsersTask(dbHandler, completeCallback)
        clearUsersTask.execute()
    }

    interface LoadUserCallback {
        fun onLoad(users: List<User>?)
    }

    interface CompleteCallback {
        fun onComplete()
    }
}

class LoadUsersTask(val dbHandler: DbHandler, val callback: UsersModel.LoadUserCallback?) :
    AsyncTask<Void?, Void?, List<User>>() {
    override fun doInBackground(vararg params: Void?): List<User> {
        val users: MutableList<User> = LinkedList()
        val cursor = dbHandler.readableDatabase.query(UserTable.TABLE, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val user = User()
            user.id = cursor.getLong(cursor.getColumnIndex(UserTable.COLUMN.ID))
            user.name = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN.NAME))
            user.email = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN.EMAIL))
            users.add(user)
        }
        cursor.close()
        return users
    }

    override fun onPostExecute(users: List<User>) {
        callback?.onLoad(users)
    }
}

class AddUserTask(val dbHandler: DbHandler, private val callback: UsersModel.CompleteCallback?) :
    AsyncTask<ContentValues?, Void?, Void?>() {
    override fun doInBackground(vararg params: ContentValues?): Void? {
        val cvUser = params[0]
        dbHandler.writableDatabase.insert(UserTable.TABLE, null, cvUser)
        try {
            TimeUnit.SECONDS.sleep(1)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(aVoid: Void?) {
        super.onPostExecute(aVoid)
        callback?.onComplete()
    }
}

class ClearUsersTask(val dbHandler: DbHandler, private val callback: UsersModel.CompleteCallback?) :
    AsyncTask<Void?, Void?, Void?>() {
    override fun doInBackground(vararg params: Void?): Void? {
        dbHandler.writableDatabase.delete(UserTable.TABLE, null, null)
        try {
            TimeUnit.SECONDS.sleep(1)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(aVoid: Void?) {
        super.onPostExecute(aVoid)
        callback?.onComplete()
    }
}
