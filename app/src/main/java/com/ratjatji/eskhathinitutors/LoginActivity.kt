package com.ratjatji.eskhathinitutors

import android.content.Intent
import android.os.Bundle
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
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        biometricHelper = BiometricHelper(this)
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Setup biometric authentication
        setupBiometricAuth()

        // Handle Google Sign-In button click
        binding.btnLoginGoogle.setOnClickListener {
            signInWithGoogle()
        }

        // Register buttons
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegisterTutor.setOnClickListener {
            val intent = Intent(this, RegisterTutorActivity::class.java)
            startActivity(intent)
        }

        // Email/Password login
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
                    val intent = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                    checkBiometricAvailability(email, password)
                } else {
                    Toast.makeText(this, "Login unsuccessful. Try again!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            binding.btnFingerPrintScan.setOnClickListener {
                biometricHelper.authenticateWithBiometric()
            }
        }

        // Fingerprint button click
        binding.btnFingerPrintScan.setOnClickListener {
            showBiometricPrompt()
        }
    }
    private fun checkBiometricAvailability(email: String, password: String) {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // Check if user has already set up biometrics
                db.collection("userBiometrics")
                    .document(auth.currentUser?.uid ?: "")
                    .get()
                    .addOnSuccessListener { document ->
                        if (!document.exists() || document.toObject(BiometricHelper.UserBiometricData::class.java)?.isBiometricEnabled != true) {
                            // Biometrics available but not set up - show setup prompt
                            showBiometricSetupPrompt(email, password)
                        } else {
                            // Biometrics already set up - proceed to main activity
                            proceedToMainActivity()
                        }
                    }
                    .addOnFailureListener {
                        // In case of error, proceed to main activity
                        proceedToMainActivity()
                    }
            }
            else -> {
                // If biometrics are not available, proceed to main activity
                proceedToMainActivity()
            }
        }
    }

    private fun showBiometricSetupPrompt(email: String, password: String) {
        AlertDialog.Builder(this)
            .setTitle("Set Up Fingerprint Login")
            .setMessage("Would you like to set up fingerprint login for faster access next time?")
            .setPositiveButton("Yes") { _, _ ->
                // Initialize biometric enrollment
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
                    super.onAuthenticationSucceeded(result)
                    // Save biometric enrollment in Firestore
                    saveBiometricEnrollment(email, password)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(this@LoginActivity,
                        "Biometric setup failed: $errString", Toast.LENGTH_SHORT).show()
                    proceedToMainActivity()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(this@LoginActivity,
                        "Biometric authentication failed", Toast.LENGTH_SHORT).show()
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
            // Note: In a production app, you should encrypt the password
            password = password
        )

        db.collection("userBiometrics")
            .document(userId)
            .set(biometricData)
            .addOnSuccessListener {
                Toast.makeText(this, "Fingerprint login set up successfully", Toast.LENGTH_SHORT).show()
                proceedToMainActivity()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save biometric settings", Toast.LENGTH_SHORT).show()
                proceedToMainActivity()
            }
    }

//    private fun proceedToMainActivity() {
//        val intent = Intent(this, MainActivity::class.java)
//        finish()
//        startActivity(intent)}
//    private fun showEnableBiometricDialog(email: String, password: String) {
//        AlertDialog.Builder(this)
//            .setTitle("Enable Biometric Login")
//            .setMessage("Would you like to enable fingerprint login for future use?")
//            .setPositiveButton("Yes") { _, _ ->
//                biometricHelper.enableBiometricLogin(email, password)
//                proceedToMainActivity()
//            }
//            .setNegativeButton("No") { _, _ ->
//                proceedToMainActivity()
//            }
//            .show()
//    }

    private fun proceedToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun setupBiometricAuth() {
        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    fingerPrintAction()
                }

                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(this@LoginActivity,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(this@LoginActivity,
                        "Authentication failed", Toast.LENGTH_SHORT)
                        .show()
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
            BiometricManager.BIOMETRIC_SUCCESS ->
                biometricPrompt.authenticate(promptInfo)
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Toast.makeText(this, "No biometric features available on this device",
                    Toast.LENGTH_SHORT).show()
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Toast.makeText(this, "Biometric features are currently unavailable",
                    Toast.LENGTH_SHORT).show()
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Toast.makeText(this, "No biometric credentials are enrolled",
                    Toast.LENGTH_SHORT).show()
        }
    }

    private fun fingerPrintAction() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    // Google Sign-In related code remains the same
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        if (task.isSuccessful) {
            val account = task.result
            firebaseAuthWithGoogle(account.idToken)
        } else {
            Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Firebase Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}