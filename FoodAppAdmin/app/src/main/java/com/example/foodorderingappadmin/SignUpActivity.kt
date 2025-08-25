package com.example.foodorderingappadmin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.foodorderingappadmin.databinding.ActivitySignUpBinding
import com.example.foodorderingappadmin.model.UserModel
import com.example.foodorderingappadmin.viewModel.AuthViewModel


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        viewModel.firebaseUser.observe(this) { user ->
            if (user != null) {
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        viewModel.error.observe(this) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        binding.btnCreateAccount.setSafeOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()
            val name = binding.etName.text.toString().trim()
            val res = binding.etRestaurant.text.toString().trim()

            when {
                name.isBlank() || res.isBlank() || email.isBlank() || pass.isBlank() -> {
                    Toast.makeText(this, "Please fill all details", Toast.LENGTH_LONG).show()
                }

                !viewModel.isValidEmail(email) -> {
                    Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG)
                        .show()
                }

                !viewModel.isValidPassword(pass) -> {
                    Toast.makeText(
                        this,
                        "Password must be at least 6 characters",
                        Toast.LENGTH_LONG
                    ).show()
                }

                viewModel.checkPasswordStrength(pass) == "Weak" -> {
                    Toast.makeText(
                        this,
                        "Password is too weak! Use upper/lowercase, number, special char",
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {
                    val user = UserModel(name, res, email, pass)
                    viewModel.signUp(email, pass, user)
                }
            }
        }

        binding.tvAlreadyAccount.setSafeOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}


/*
private lateinit var userEmail: String
private lateinit var userPassword: String
private lateinit var userName: String
private lateinit var nameOfRestaurant: String
private lateinit var database: DatabaseReference
private lateinit var binding: ActivitySignUpBinding

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySignUpBinding.inflate(layoutInflater)
    setContentView(binding.root)
    auth = FirebaseAuth.getInstance()
    database = Firebase.database.reference

    val locationList: Array<String> = arrayOf("Dhaka", "Chittagong", "Comilla", "Barishal")
    val adapter: ArrayAdapter<String> =
        ArrayAdapter(this, R.layout.simple_list_item_1, locationList)
    val autoCompleteTextView: AutoCompleteTextView = binding.listOfLocation
    autoCompleteTextView.setAdapter(adapter)

    binding.tvAlreadyAccount.setSafeOnClickListener {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    binding.btnCreateAccount.setSafeOnClickListener {
        userEmail = binding.etEmail.text.toString().trim()
        nameOfRestaurant = binding.etRestaurant.text.toString().trim()
        userName = binding.etName.text.toString().trim()
        userPassword = binding.etPassword.text.toString().trim()

        when {
            userName.isBlank() || userEmail.isBlank() || nameOfRestaurant.isBlank() || userPassword.isBlank() -> {
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

//save data in to database
private fun saveUserData() {
    userEmail = binding.etEmail.text.toString().trim()
    nameOfRestaurant = binding.etRestaurant.text.toString().trim()
    userName = binding.etName.text.toString().trim()
    userPassword = binding.etPassword.text.toString().trim()
    val user = UserModel(userName, nameOfRestaurant, userEmail, userPassword)
    val userId = FirebaseAuth.getInstance().currentUser!!.uid
    //save user data Firebase database
    database.child("user").child(userId).setValue(user)
}
 */

/*
 //private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        viewModel.firebaseUser.observe(this) { user ->
            if (user != null) {
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        viewModel.error.observe(this) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        binding.btnCreateAccount.setSafeOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()
            val name = binding.etName.text.toString().trim()
            val res = binding.etRestaurant.text.toString().trim()

            val user = UserModel(name, res, email, pass)
            viewModel.signUp(email, pass, user)
        }

        binding.tvAlreadyAccount.setSafeOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
 */