package com.example.phoneauthtest

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    var verificationID = ""
    private var Auth = FirebaseAuth.getInstance()
    private lateinit var buttonPhone:Button
    private lateinit var buttonOtp: Button
    private lateinit var editTextphone: EditText
    lateinit var editTextVerifyOtp: EditText
    lateinit var imageView: ImageView
    lateinit var buttonchoose: Button
    lateinit var buttonUpload: Button
    lateinit var buttonsecondpage: Button
    private var fileUri: Uri? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Auth = FirebaseAuth.getInstance()
        editTextphone = findViewById(R.id.phone_number)
        editTextVerifyOtp = findViewById(R.id.otp_verify)
        buttonOtp = findViewById(R.id.verifyOtp)
        buttonPhone = findViewById(R.id.send_button)
        imageView = findViewById(R.id.image_show)
        buttonchoose = findViewById(R.id.Btn_choose_image)
        buttonUpload = findViewById(R.id.Btn_Upload_image)
        buttonsecondpage = findViewById(R.id.second_page)
        buttonsecondpage.setOnClickListener {
            val intent = Intent(this,firebaseCURD::class.java)
            startActivity(intent)
        }

        buttonPhone.setOnClickListener {
            sendOtp()
        }
        buttonOtp.setOnClickListener {
            verifyOTP()
        }



        buttonchoose.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"

            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(

                Intent.createChooser(
                    intent,
                    "Pick your image to upload"
                ),
                22
            )
        }
        buttonUpload.setOnClickListener {
            uploadImage()
        }
    }

    // on below line adding on activity result method this method is called when user picks the image.
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 22 && resultCode == RESULT_OK && data != null && data.data != null) {
            fileUri = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri);
                imageView.setImageBitmap(bitmap)
            } catch (e: Exception) {
                // handling exception on below line
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage() {
        if (fileUri != null) {
            val progressDialog = ProgressDialog(this)
            // on below line setting title and message for our progress dialog and displaying our progress dialog.
            progressDialog.setTitle("Uploading...")
            progressDialog.setMessage("Uploading your image..")
            progressDialog.show()

            val ref: StorageReference = FirebaseStorage.getInstance().getReference()
                .child(UUID.randomUUID().toString())
            ref.putFile(fileUri!!).addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Image Uploaded..", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Fail to Upload Image..", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun sendOtp() {

        val options = PhoneAuthOptions.newBuilder(Auth)
            .setPhoneNumber("+91${editTextphone.text}")
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    Toast.makeText(this@MainActivity, "otp send", Toast.LENGTH_SHORT).show()
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(this@MainActivity, "otp failed", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    verificationID = p0
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun verifyOTP() {
        val otpText = editTextVerifyOtp.text.toString()
        val phoneAuthCredential = PhoneAuthProvider.getCredential(verificationID, otpText)
        Auth.signInWithCredential(phoneAuthCredential)
            .addOnSuccessListener {
                Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "login failed", Toast.LENGTH_SHORT).show()
            }

    }
}


