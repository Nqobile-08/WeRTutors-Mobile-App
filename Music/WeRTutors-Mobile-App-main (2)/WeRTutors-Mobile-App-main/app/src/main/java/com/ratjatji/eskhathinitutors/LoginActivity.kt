package com.ratjatji.eskhathinitutors

import android.content.Intent
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

    private lateinit var biometricHelper: BiometricHelper
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityLoginBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        biometricHelper = BiometricHelper(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setupBiometricAuth()

        binding.btnLoginGoogle.setOnClickListener {
            //signInWithGoogle()
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegisterTutor.setOnClickListener {
            val intent = Intent(this, RegisterTutorActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPass.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("LoginActivity", "Sign-in successful")
                    checkBiometricAvailability(email, password)
                } else {
                    Toast.makeText(this, "Login unsuccessful. Try again!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnFingerPrintScan.setOnClickListener {
            showBiometricPrompt()
        }
    }

    private fun checkBiometricAvailability(email: String, password: String) {
        Log.d("LoginActivity", "Checking biometric availability")
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("LoginActivity", "Biometrics available")
                db.collection("userBiometrics")
                    .document(auth.currentUser?.uid ?: "")
                    .get()
                    .addOnSuccessListener { document ->
                        if (!document.exists() || document.toObject(BiometricHelper.UserBiometricData::class.java)?.isBiometricEnabled != true) {
                            Log.d("LoginActivity", "Biometrics not set up; showing setup prompt")
                            showBiometricSetupPrompt(email, password)
                        } else {
                            Log.d("LoginActivity", "Biometrics already set up; proceeding to main activity")
                            proceedToMainActivity()
                        }
                    }
                    .addOnFailureListener {
                        Log.d("LoginActivity", "Error fetching biometric setup data; proceeding to main activity")
                        proceedToMainActivity()
                    }
            }
            else -> {
                Log.d("LoginActivity", "Biometrics not available; proceeding to main activity")
                proceedToMainActivity()
            }
        }
    }

    private fun showBiometricSetupPrompt(email: String, password: String) {
        AlertDialog.Builder(this)
            .setTitle("Set Up Fingerprint Login")
            .setMessage("Would you like to set up fingerprint login for faster access next time?")
            .setPositiveButton("Yes") { _, _ ->
                setupBiometricEnrollment(email, password)
            }
            .setNegativeButton("Skip") { _, _ ->
                proceedToMainActivity()
            }
            .setCancelable(false)
            .show()
    }

    private fun setupBiometricEnrollment(email: String, password: String) {
        val prompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    Log.d("LoginActivity", "Biometric enrollment authentication succeeded")
                    saveBiometricEnrollment(email, password)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    Log.d("LoginActivity", "Biometric enrollment authentication error: $errString")
                    proceedToMainActivity()
                }

                override fun onAuthenticationFailed() {
                    Log.d("LoginActivity", "Biometric enrollment authentication failed")
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Set Up Fingerprint")
            .setSubtitle("Place your finger on the sensor")
            .setNegativeButtonText("Cancel")
            .build()

        prompt.authenticate(promptInfo)
    }

    private fun saveBiometricEnrollment(email: String, password: String) {
        val userId = auth.currentUser?.uid ?: return
        val biometricData = BiometricHelper.UserBiometricData(
            isBiometricEnabled = true,
            email = email,
            password = password
        )

        db.collection("userBiometrics")
            .document(userId)
            .set(biometricData)
            .addOnSuccessListener {
                Log.d("LoginActivity", "Biometric enrollment saved successfully")
                proceedToMainActivity()
            }
            .addOnFailureListener {
                Log.d("LoginActivity", "Failed to save biometric settings")
                proceedToMainActivity()
            }
    }

    private fun proceedToMainActivity() {
        Log.d("LoginActivity", "Proceeding to HowToActivity")
        val intent = Intent(this, SplashWin::class.java)
        finish()
        startActivity(intent)
    }

    private fun setupBiometricAuth() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    fingerPrintAction()
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

    private fun showBiometricPrompt() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("LoginActivity", "Biometric prompt shown")
                biometricPrompt.authenticate(promptInfo)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.d("LoginActivity", "No biometric hardware available")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.d("LoginActivity", "Biometric hardware unavailable")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Log.d("LoginActivity", "No biometric credentials enrolled")
        }
    }

    private fun fingerPrintAction() {
        Log.d("LoginActivity", "Fingerprint authentication successful")
        proceedToMainActivity()
    }
}