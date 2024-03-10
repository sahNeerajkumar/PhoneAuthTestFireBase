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

class UpdateActivity : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var button: Button
    private var id:String? = null
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")

        button = findViewById(R.id.update_Btn)

        editTextName = findViewById(R.id.name_update)

        editTextName.setText(name)

        button.setOnClickListener {
            val getDateName = editTextName.text.toString()

            val map = mapOf(
                "name" to getDateName
            )

            db.collection("users").document(id!!).update(map)
                .addOnSuccessListener {
                    Toast.makeText(this, "user updated", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, firebaseCURD::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
                }
        }

    }
}
