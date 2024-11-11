package com.ratjatji.eskhathinitutors

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.ratjatji.eskhathinitutors.Tutors.MainActivity
import com.ratjatji.eskhathinitutors.databinding.ActivityLoginBinding
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE)

        setupGoogleSignIn()
        setupBiometricAuth()

        binding.btnLoginGoogle.setOnClickListener { signInWithGoogle() }
        binding.btnRegister.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
        binding.btnRegisterTutor.setOnClickListener { startActivity(Intent(this, RegisterTutorActivity::class.java)) }

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPass.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            } else if (isOnline()) {
                performOnlineLogin(email, password, sharedPreferences)
            } else {
                performOfflineLogin(email, password, sharedPreferences)
            }
        }

        binding.btnFingerPrintScan.setOnClickListener { showBiometricPrompt() }
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun setupBiometricAuth() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                Log.d("LoginActivity", "Fingerprint authentication successful")
                proceedToMainActivity()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                Log.d("LoginActivity", "Authentication error: $errString")
            }

            override fun onAuthenticationFailed() {
                Log.d("LoginActivity", "Authentication failed")
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authentication Required")
            .setSubtitle("Use your fingerprint to login")
            .setNegativeButtonText("Cancel")
            .build()
    }

    private fun performOnlineLogin(email: String, password: String, sharedPreferences: SharedPreferences) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                sharedPreferences.edit().apply {
                    putString("email", email)
                    putString("password", password)
                    apply()
                }
                checkBiometricAvailability(email, password)
            } else {
                Toast.makeText(this, "Login unsuccessful. Try again!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performOfflineLogin(email: String, password: String, sharedPreferences: SharedPreferences) {
        val storedEmail = sharedPreferences.getString("email", "")
        val storedPassword = sharedPreferences.getString("password", "")
        if (email == storedEmail && password == storedPassword) {
            proceedToMainActivity()
        } else {
            Toast.makeText(this, "No internet and no saved credentials", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkBiometricAvailability(email: String, password: String) {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> showBiometricSetupPrompt(email, password)
            else -> proceedToMainActivity()
        }
    }

    private fun showBiometricSetupPrompt(email: String, password: String) {
        AlertDialog.Builder(this)
            .setTitle("Set Up Fingerprint Login")
            .setMessage("Would you like to set up fingerprint login for faster access next time?")
            .setPositiveButton("Yes") { _, _ -> setupBiometricEnrollment(email, password) }
            .setNegativeButton("Skip") { _, _ -> proceedToMainActivity() }
            .setCancelable(false)
            .show()
    }

    private fun setupBiometricEnrollment(email: String, password: String) {
        biometricPrompt.authenticate(
            BiometricPrompt.PromptInfo.Builder()
                .setTitle("Set Up Fingerprint")
                .setSubtitle("Place your finger on the sensor")
                .setNegativeButtonText("Cancel")
                .build()
        )
    }

    private fun proceedToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showBiometricPrompt() {
        if (BiometricManager.from(this).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS) {
            biometricPrompt.authenticate(promptInfo)
        } else {
            Log.d("LoginActivity", "Biometric authentication not available")
        }
    }

    private fun isOnline(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected == true
    }

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        if (task.isSuccessful) {
            firebaseAuthWithGoogle(task.result.idToken)
        } else {
            Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithGoogle() {
        googleSignInLauncher.launch(googleSignInClient.signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) proceedToMainActivity()
            else Toast.makeText(this, "Firebase Authentication failed", Toast.LENGTH_SHORT).show()
        }
    }
}
