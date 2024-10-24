package com.ratjatji.eskhathinitutors
import androidx.fragment.app.Fragment
//import android.content.DialogInterface
//import android.content.pm.PackageManager
//import android.hardware.biometrics.BiometricPrompt
//import android.os.Build
//import android.os.Bundle
//import android.os.CancellationSignal
//import androidx.annotation.RequiresApi
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import android.app.KeyguardManager
//import java.util.concurrent.Executor

class BiometricFragment : Fragment() {
//
//    private lateinit var executor: Executor
//    private lateinit var biometricPrompt: BiometricPrompt
//    private lateinit var promptInfo: BiometricPrompt.PromptInfo
//
//    @RequiresApi(Build.VERSION_CODES.P)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            // Handle arguments if needed
//        }
//        if (checkBiometricSupport()) {
//            showBiometricDialog()
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.P)
//    private fun checkBiometricSupport(): Boolean {
//        val keyguardManager = requireContext().getSystemService(KeyguardManager::class.java)
//
//        if (keyguardManager?.isDeviceSecure == false) {
//            Toast.makeText(requireContext(), "Fingerprint not enabled in settings", Toast.LENGTH_SHORT).show()
//            return false
//        }
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                android.Manifest.permission.USE_BIOMETRIC
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            Toast.makeText(requireContext(), "Fingerprint permission not granted", Toast.LENGTH_SHORT).show()
//            return false
//        }
//        return requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.P)
//    private fun showBiometricDialog() {
//        executor = ContextCompat.getMainExecutor(requireContext())
//        biometricPrompt = BiometricPrompt(
//            this,
//            executor,
//            object : BiometricPrompt.AuthenticationCallback() {
//                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
//                    super.onAuthenticationSucceeded(result)
//                    Toast.makeText(requireContext(), "Authentication succeeded", Toast.LENGTH_LONG).show()
//                }
//
//                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
//                    super.onAuthenticationError(errorCode, errString)
//                    Toast.makeText(requireContext(), errString.toString(), Toast.LENGTH_LONG).show()
//                }
//            }
//        )
//
//        promptInfo = BiometricPrompt.PromptInfo.Builder()
//            .setTitle("Unlock App")
//            .setNegativeButtonText("Cancel")
//            .build()
//
//        biometricPrompt.authenticate(promptInfo)
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_biometric, container, false)
//    }
//
//    companion object {
//        @JvmStatic
//        fun newInstance() = BiometricFragment()
//    }
//}
}