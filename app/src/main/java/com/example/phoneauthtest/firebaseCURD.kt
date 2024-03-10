package com.example.phoneauthtest

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class firebaseCURD : AppCompatActivity(),onClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton

    private val dataBase = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_curd)

        recyclerView = findViewById(R.id.recycle_View)
        floatingActionButton = findViewById(R.id.floatingactionbutton)
        getData()
        floatingActionButton.setOnClickListener {
            val intent = Intent(this, InsertActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getData() {
        FirebaseFirestore.getInstance().collection("users").get()
            .addOnSuccessListener {
                val list = ArrayList<UserModel>()
                for (a in it){
                    val data = a.toObject(UserModel::class.java)
                    list.add(data)
                }
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = UsersAdapter(list,this, this)

            }
    }

    override fun deleteUser(userModel: UserModel) {
        deleteUserView(userModel.id.toString())
    }

    private fun deleteUserView(id:String){
        dataBase.collection("users").document(id).delete()
            .addOnSuccessListener {
                Toast.makeText(this, "delete user success", Toast.LENGTH_SHORT).show()
                getData()
            }
            .addOnFailureListener {
                Toast.makeText(this, "delete user failed", Toast.LENGTH_SHORT).show()
            }
    }
}