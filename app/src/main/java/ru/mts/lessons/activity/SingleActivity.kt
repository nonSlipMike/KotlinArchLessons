package ru.mts.lessons.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.mts.lessons.R
import ru.mts.lessons.common.User
import ru.mts.lessons.common.UserAdapter
import ru.mts.lessons.common.UserTable
import ru.mts.lessons.database.DbHandler
import java.util.LinkedList
import java.util.concurrent.TimeUnit

class SingleActivity : Activity() {
    var userAdapter: UserAdapter? = null
    var mDbHandler: DbHandler? = null
    private var editTextName: EditText? = null
    private var editTextEmail: EditText? = null
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)
        init()
        loadUsers()
    }

    private fun init() {
        editTextName = findViewById(R.id.name)
        editTextEmail = findViewById<View>(R.id.email) as EditText
        findViewById<View>(R.id.add).setOnClickListener { addUser() }
        findViewById<View>(R.id.clear).setOnClickListener { clearUsers() }
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        userAdapter = UserAdapter()
        val userList = findViewById<View>(R.id.list) as RecyclerView
        userList.layoutManager = layoutManager
        userList.adapter = userAdapter
        mDbHandler = DbHandler(this)
    }

    fun loadUsers() {
        val loadUsersTask =
            LoadUsersTask(this)
        loadUsersTask.execute()
    }

    private fun addUser() {
        val name = editTextName!!.text.toString()
        val email = editTextEmail!!.text.toString()
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, R.string.empty_values, Toast.LENGTH_SHORT).show()
            return
        }
        val cv = ContentValues(2)
        cv.put(UserTable.COLUMN.NAME, name)
        cv.put(UserTable.COLUMN.EMAIL, email)
        showProgress()
        val addUserTask =
            AddUserTask(this)
        addUserTask.execute(cv)
    }

    private fun clearUsers() {
        showProgress()
        val clearUsersTask =
            ClearUsersTask(this)
        clearUsersTask.execute()
    }

    private fun showProgress() {
        progressDialog = ProgressDialog.show(this, "", getString(R.string.please_wait))
    }

    fun hideProgress() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

}

class LoadUsersTask(val singleActivity: SingleActivity) : AsyncTask<Void?, Void?, List<User>>() {
    override fun doInBackground(vararg params: Void?): List<User> {
        val users: MutableList<User> =
            LinkedList()
        val cursor =
            singleActivity.mDbHandler!!.readableDatabase.query(UserTable.TABLE, null, null, null, null, null, null)
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
        singleActivity.userAdapter!!.initData(users)
    }
}

class AddUserTask(private val singleActivity: SingleActivity) : AsyncTask<ContentValues?, Void?, Void?>() {
    override fun doInBackground(vararg params: ContentValues?): Void? {
        val cvUser = params[0]
        singleActivity.mDbHandler!!.writableDatabase.insert(UserTable.TABLE, null, cvUser)
        try {
            TimeUnit.SECONDS.sleep(1)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(aVoid: Void?) {
        super.onPostExecute(aVoid)
        singleActivity.hideProgress()
        singleActivity.loadUsers()
    }
}

class ClearUsersTask(val singleActivity: SingleActivity) : AsyncTask<Void?, Void?, Void?>() {
     override fun doInBackground(vararg params: Void?): Void? {
         singleActivity.mDbHandler!!.writableDatabase.delete(UserTable.TABLE, null, null)
        try {
            TimeUnit.SECONDS.sleep(1)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(aVoid: Void?) {
        super.onPostExecute(aVoid)
        singleActivity.hideProgress()
        singleActivity.loadUsers()
    }
}