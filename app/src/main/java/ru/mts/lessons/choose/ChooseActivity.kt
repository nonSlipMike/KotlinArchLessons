package ru.mts.lessons.choose

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import ru.mts.lessons.R
import ru.mts.lessons.activity.SingleActivity
import ru.mts.lessons.mvp.UsersActivity
import ru.mts.lessons.mvvm.MvvmActivity

class ChooseActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)
        findViewById<View>(R.id.activity).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SingleActivity::class.java
                )
            )
        }
        findViewById<View>(R.id.mvp).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    UsersActivity::class.java
                )
            )
        }
        findViewById<View>(R.id.mvvm).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MvvmActivity::class.java
                )
            )
        }
    }
}