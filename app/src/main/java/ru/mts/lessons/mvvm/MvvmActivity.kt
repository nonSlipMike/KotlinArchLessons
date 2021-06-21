package ru.mts.lessons.mvvm

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import ru.mts.lessons.R
import ru.mts.lessons.common.UserAdapter
import ru.mts.lessons.common.UserData

class MvvmActivity : AppCompatActivity() {

    //region value naming zone
    private val myViewModel: MvvmViewModel by viewModels()

    private lateinit var userAdapter: UserAdapter
    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private val progressDialog by lazy { ProgressDialog.show(this, "", getString(R.string.please_wait)) }
    private lateinit var userList: RecyclerView
    private lateinit var addBtn: Button
    private lateinit var clearBtn: Button
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)
        init()
    }

    private fun init() {
        editTextName = findViewById(R.id.name)
        editTextEmail = findViewById(R.id.email)
        userList = findViewById(R.id.list)
        addBtn = findViewById(R.id.add)
        clearBtn = findViewById(R.id.clear)

        addBtn.setOnClickListener {
            myViewModel.add(
                UserData(
                    editTextName.text.toString(),
                    editTextEmail.text.toString()
                )
            )
        }
        clearBtn.setOnClickListener { myViewModel.clear() }

        userAdapter = UserAdapter()
        userList.adapter = userAdapter

        myViewModel.dataList.observe(this, Observer(userAdapter::initData))
        myViewModel.viewState.observe(this, Observer(::render))

        myViewModel.loadUsers()
    }

    data class ViewState(
        val isDownloaded: Boolean = false
    )

    private fun render(viewState: ViewState) = with(viewState) {
        if (isDownloaded) {
            progressDialog.show()
        } else {
            progressDialog.dismiss()
        }
    }
}