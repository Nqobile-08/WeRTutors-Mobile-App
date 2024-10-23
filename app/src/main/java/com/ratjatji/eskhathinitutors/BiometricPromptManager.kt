package com.ratjatji.eskhathinitutors

import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class BiometricPromptManager (
private val activity: AppCompatActivity
){
//
//    private val resultChannel = kotlinx.coroutines.channels.Channel<BiometricResult>()
//    val promptResult = resultChannel.recieveAsFlow()
//    @RequiresApi(Build.VERSION_CODES.P)
//    fun showBiometricPrompt(
//        title: String,
//        description: String
//    ){
//        val manager = BiometricManager.from(activity)
//    val authenticators = if(Build.VERSION.SDK_INT >=30){
//        BIOMETRIC_STRONG or DEVICE_CREDENTIAL}
//        else BIOMETRIC_STRONG
//        val promptInfo = PromptInfo.Builder()
//            .setTitle(title)
//            .setDescription(description)
//            .setAllowedAuthenticators(authenticators)
//            .setConfirmationRequired(false)
//    if(Build.VERSION.SDK_INT <30){
//     promptInfo.setNegativeButtonText("Cancel")
//    }
//        when(manager.canAuthenticate(authenticators)){
//            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE->{
//                resultChannel.trySend(BiometricResult.HardwareUnavailable)
//            return}
//            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE->{
//                resultChannel.trySend(BiometricResult.FeatureUnavailable)
//            return}
//            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED->{
//                resultChannel.trySend(BiometricResult.AuthenticationNotSet)
//            return}
//            else -> Unit
//        }
//
//        val prompt = BiometricPrompt(
//            activity,
//            object: BiometricPrompt.AuthenticationCallback(){
//                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
//                    super.onAuthenticationError(errorCode, errString)
//                    resultChannel.trySend(BiometricResult.AuthenticationError(errSring.toString()))
//                }
//
//                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
//                    super.onAuthenticationSucceeded(result)
//                    resultChannel.trySend(BiometricResult.AuthenticationSuccess)
//                }
//
//                override fun onAuthenticationFailed() {
//                    super.onAuthenticationFailed()
//                    resultChannel.trySend(BiometricResult.AuthenticationFailed)
//                }
//            }
//        )
//        prompt.authenticate(promptInfo.build())
//}
//sealed interface  BiometricResult{
//    data object HardwareUnavailable : BiometricResult
//    data object FeatureUnavailable : BiometricResult
//    data class AuthenticationError(val error: String) : BiometricResult
//    data object AuthenticationFailed : BiometricResult
//    data object AuthenticationSuccess : BiometricResult
//    data object AuthenticationNotSet : BiometricResult
//}
}