package com.ratjatji.eskhathinitutors

import android.content.Context

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class BiometricHelper(private val context: Context) {

    data class UserBiometricData(
        val isBiometricEnabled: Boolean = false,
        val email: String = "",
        val password: String = "",
        val enrollmentTimestamp: Long = System.currentTimeMillis()
    )

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    fun authenticateWithBiometric(onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        val executor = ContextCompat.getMainExecutor(context)

        biometricPrompt = BiometricPrompt(
            context as FragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errString.toString())
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onError("Authentication failed")
                }
            }
        )

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    fun enableBiometricLogin(email: String, password: String) {
        // This method would be called when setting up biometric login
        authenticateWithBiometric(
            onSuccess = {
                // Save credentials securely after successful biometric authentication
                // Note: In a production app, you should encrypt these credentials
                saveCredentials(email, password)
            },
            onError = { /* Handle error */ }
        )
    }

    private fun saveCredentials(email: String, password: String) {
        // In a production app, implement secure credential storage here
        // For example, using EncryptedSharedPreferences or Android Keystore
    }
}