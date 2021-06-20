package ru.mts.lessons.mvp

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ru.mts.lessons.R
import ru.mts.lessons.common.User
import ru.mts.lessons.common.UserAdapter
import ru.mts.lessons.database.DbHandler

class UsersActivity : Activity() {
    private var userAdapter: UserAdapter? = null
    private var editTextName: EditText? = null
    private var editTextEmail: EditText? = null
    private var progressDialog: ProgressDialog? = null
    private var userList: RecyclerView? = null
    private var addBtn: Button? = null
    private var clearBtn : Button? = null

    private var presenter: UsersPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)
        init()
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    private fun init() {
        editTextName = findViewById(R.id.name)
        editTextEmail = findViewById(R.id.email)
        userList = findViewById(R.id.list)
        addBtn =  findViewById(R.id.add)
        clearBtn =  findViewById(R.id.clear)

        addBtn?.setOnClickListener { presenter!!.add() }
        clearBtn?.setOnClickListener { presenter!!.clear() }
        userAdapter = UserAdapter()
        userList?.adapter = userAdapter

        presenter = UsersPresenter(UsersModel(DbHandler(this)))
        presenter?.attachView(this)
        presenter?.viewIsReady()
    }

    val userData: UserData
        get() {
            val userData = UserData()
            userData.name = editTextName?.text.toString()
            userData.email = editTextEmail?.text.toString()
            return userData
        }

    fun showUsers(users: List<User?>?) {
        if (users != null) userAdapter!!.data = users as MutableList<User>
        userAdapter?.notifyDataSetChanged()
    }

    fun showToast(resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }

    fun showProgress() {
        progressDialog = ProgressDialog.show(this, "", getString(R.string.please_wait))
    }

    fun hideProgress() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }
}