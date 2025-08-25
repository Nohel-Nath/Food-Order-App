package com.example.foodorderingapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodorderingapp.databinding.ActivityLoginBinding
import com.example.foodorderingapp.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var userEmail: String
    private lateinit var userPassword: String
    private lateinit var database: DatabaseReference
    private var userName: String? = null
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.tvDontAcc.setSafeOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.buttonGoogle.setSafeOnClickListener {
            val signInIntent: Intent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }

        binding.btnLogin.setSafeOnClickListener {
            userEmail = binding.etEmail.text.toString().trim()
            userPassword = binding.etPassword.text.toString().trim()

            when {
                userEmail.isBlank() || userPassword.isBlank() -> {
                    Toast.makeText(this, "Please fill all details", Toast.LENGTH_LONG).show()
                }

                !isValidEmail(userEmail) -> {
                    Toast.makeText(this, "Invalid email format", Toast.LENGTH_LONG).show()
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
                    createUserAccount(userEmail, userPassword)
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

    private fun createUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user: FirebaseUser? = auth.currentUser
                Toast.makeText(this, "Login Successfully", Toast.LENGTH_LONG).show()
                updateUi(user)
            } else {
                Toast.makeText(this, "Wrong email or password", Toast.LENGTH_LONG).show()
                Log.d("Login", "loginUser: Failed", task.exception)
            }
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun saveUserData() {
        userEmail = binding.etEmail.text.toString().trim()
        userPassword = binding.etPassword.text.toString().trim()

        val user = UserModel(userName, userEmail, userPassword)
        val userId: String? = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            database.child("user").child(it).setValue(user)
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount = task.result
                    val credential: AuthCredential =
                        GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Successfully sign-in with Google",
                                Toast.LENGTH_LONG
                            ).show()
                            updateUi(authTask.result?.user)
                        } else {
                            Toast.makeText(this, "Google Sign-in failed", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Google Sign-in failed", Toast.LENGTH_LONG).show()
            }
        }

    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}