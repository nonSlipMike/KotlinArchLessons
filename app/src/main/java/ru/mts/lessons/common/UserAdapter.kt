package ru.mts.lessons.common

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.mts.lessons.R
import ru.mts.lessons.common.UserAdapter.UserHolder
import java.util.ArrayList

class UserAdapter : RecyclerView.Adapter<UserHolder>() {
    var data: MutableList<User> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserHolder(view)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun initData(users: List<User>?) {
        data.clear()
        data.addAll(users!!)
        notifyDataSetChanged()
        Log.d("initDataBlock", "size  = $itemCount")
    }

    class UserHolder(itemView: View) : ViewHolder(itemView) {
        var text: TextView = itemView.findViewById(R.id.text)
        fun bind(user: User) {
            text.text = "id: ${user.id}, name: ${user.name}, email: ${user.email}"
        }
    }
}