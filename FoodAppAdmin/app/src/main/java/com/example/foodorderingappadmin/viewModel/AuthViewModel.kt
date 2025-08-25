package com.example.foodorderingappadmin.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodorderingappadmin.model.UserModel
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AuthViewModel:ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = Firebase.database.reference

    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val firebaseUser: LiveData<FirebaseUser?> get() = _firebaseUser

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun checkPasswordStrength(password: String): String {
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

    fun signUp(email: String, password: String, user: UserModel) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    userId?.let {
                        database.child("user").child(it).setValue(user)
                    }
                    _firebaseUser.value = auth.currentUser
                } else {
                    _error.value = task.exception?.message
                }
            }
    }

    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _firebaseUser.value = auth.currentUser
                } else {
                    _error.value = task.exception?.message
                }
            }
    }

    fun signInWithGoogle(credential: AuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _firebaseUser.value = auth.currentUser
            } else {
                _error.value = task.exception?.message
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _firebaseUser.value = null
    }
}