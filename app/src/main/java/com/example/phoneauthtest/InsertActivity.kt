package com.example.phoneauthtest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class InsertActivity : AppCompatActivity() {
    private lateinit var editText: EditText
    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)
        editText = findViewById(R.id.name_insert)
        button = findViewById(R.id.button_insert)
       button.setOnClickListener {
           addData()
       }
    }


    fun addData(){
        val id = UUID.randomUUID().toString()
        val getName = editText.text.toString()
        val map = hashMapOf(
            "id" to id,
            "name" to getName)
        FirebaseFirestore.getInstance().collection("users").document(id).set(map)
            .addOnSuccessListener {
            Toast.makeText(this, "Success add", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@InsertActivity,firebaseCURD::class.java))
            finish()
            return@addOnSuccessListener
        }
            .addOnFailureListener {
                Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
                return@addOnFailureListener
            }
    }
}