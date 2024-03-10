package com.example.phoneauthtest

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth


class UsersAdapter(private val userList: List<UserModel>, private val context: Context, val onClickListener: onClickListener) :
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName = view.findViewById<TextView>(R.id.name)
        val delete = view.findViewById<ImageView>(R.id.delete_icon)
        val update = view.findViewById<ImageView>(R.id.edit_icon)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersAdapter.ViewHolder, position: Int) {
        val user_list = userList[position]
        holder.userName.text = user_list.name

        holder.update.setOnClickListener {
            val intent  = Intent(holder.itemView.context,UpdateActivity::class.java).apply {
                putExtra("id", userList[position].id.toString())
                putExtra("name",userList[position].name.toString())
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.delete.setOnClickListener {
            onClickListener.deleteUser(userList[position])
        }
    }

    override fun getItemCount(): Int = userList.size
}


interface onClickListener{
    fun deleteUser(userModel: UserModel)
}