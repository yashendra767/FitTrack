package com.example.fittrack

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginScr : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var databaseReference: DatabaseReference
    companion object{
        const val KEY ="com.example.fittrack.LoginScr.KEY"
        private const val RC_SIGN_IN = 9001
        private const val TAG = "GoogleActivity"
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_scr)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val loginBtn = findViewById<Button>(R.id.btnSignIn)
        val userEmail = findViewById<TextInputEditText>(R.id.userEmailEditText)
        val userPass = findViewById<TextInputEditText>(R.id.userPassEditText)
        val sign_in_button = findViewById<ImageView>(R.id.img_sign_in)
        loginBtn.setOnClickListener{
            val userEmailString = userEmail.text.toString().replace(".", ",")
            val passwordString = userPass.text.toString()
            if(userEmailString.isNotEmpty()&&passwordString.isNotEmpty()){
                readData(userEmailString,passwordString)
            }
            else{
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
        //Google Sign IN starts
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()

        sign_in_button.setOnClickListener {
            signIn()
        }
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            Log.w(TAG, "Google sign in failed", e)
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = firebaseAuth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }
    private fun updateUI(user: FirebaseUser?) {
        // Update your UI based on the FirebaseUser object
        if (user != null) {
            // Signed in successfully
            // user.displayName, user.email, etc.
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            val nameId = user.displayName
            val intentHome = Intent(this, HomeActivity::class.java)
            intentHome.putExtra(KEY,nameId)
            startActivity(intentHome)

        } else {
            // Signed out or sign-in failed
        }
    }
    //Google Sign IN over

    private fun readData(email: String, pass: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.child(email).get().addOnSuccessListener {
            if (it.exists()) {
                val storedPassword = it.child("pass").value.toString()
                if (storedPassword == pass) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    val nameId = it.child("name").value.toString()
                    val intentHome = Intent(this, HomeActivity::class.java)
                    intentHome.putExtra(KEY,nameId)
                    startActivity(intentHome)
                } else {
                    Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "User does not exist, SignUp first", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to read data from the database", Toast.LENGTH_SHORT).show()
        }
    }
}