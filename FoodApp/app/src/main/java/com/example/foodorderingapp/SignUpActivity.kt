package com.example.foodorderingapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.foodorderingapp.databinding.ActivitySignUpBinding
import com.example.foodorderingapp.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userEmail: String
    private lateinit var userPassword: String
    private lateinit var userName: String
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        binding.textViewDesign.setSafeOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.buttonGoogle.setSafeOnClickListener {
            val signInIntent: Intent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }

        binding.btnSignUp.setSafeOnClickListener {
            userEmail = binding.etEmail.text.toString().trim()
            userName = binding.etName.text.toString().trim()
            userPassword = binding.etPassword.text.toString().trim()

            when {
                userName.isBlank() || userEmail.isBlank() || userPassword.isBlank() -> {
                    Toast.makeText(this, "Please fill all details", Toast.LENGTH_LONG).show()
                }

                !isValidEmail(userEmail) -> {
                    Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG)
                        .show()
                }

                !isValidPassword(userPassword) -> {
                    Toast.makeText(
                        this,
                        "Password must be at least 6 characters",
                        Toast.LENGTH_LONG
                    ).show()
                }

                checkPasswordStrength(userPassword) == "Weak" -> {
                    Toast.makeText(
                        this,
                        "Password is too weak! Use upper/lowercase, number, special char",
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {
                    createAccount(userEmail, userPassword)
                }
            }
        }
    }

    private fun isValidEmail(userEmail: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    private fun checkPasswordStrength(password: String): String {
        var strength = 0

        if (password.length >= 6) strength++
        if (password.matches(Regex(".*[a-z].*"))) strength++
        if (password.matches(Regex(".*[A-Z].*"))) strength++
        if (password.matches(Regex(".*\\d.*"))) strength++
        if (password.matches(Regex(".*[!@#\$%^&*(),.?\":{}|<>].*"))) strength++

        return when (strength) {
            in 0..2 -> "Weak"
            3, 4 -> "Medium"
            5 -> "Strong"
            else -> "Very Weak"
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_LONG).show()
                saveUserData()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Account creation failed", Toast.LENGTH_LONG).show()
                Log.d("Account", "createAccount: Failure", task.exception)
            }

        }
    }

    private fun saveUserData() {
        userEmail = binding.etEmail.text.toString().trim()
        userName = binding.etName.text.toString().trim()
        userPassword = binding.etPassword.text.toString().trim()
        val user = UserModel(userName, userEmail, userPassword)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        //save user data Firebase database
        database.child("user").child(userId).setValue(user)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    val credential: AuthCredential =
                        GoogleAuthProvider.getCredential(account?.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            Toast.makeText(this, "Sign-in successfully", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            //updateUi(authTask.result?.user)
                            finish()
                        } else {
                            Toast.makeText(this, "Google Sign-in failed", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Google Sign-in failed", Toast.LENGTH_LONG).show()
            }
        }
}