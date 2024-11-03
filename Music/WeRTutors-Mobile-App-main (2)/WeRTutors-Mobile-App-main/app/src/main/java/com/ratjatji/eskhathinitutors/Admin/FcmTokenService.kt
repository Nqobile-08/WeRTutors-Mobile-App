import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.ratjatji.eskhathinitutors.R

class FcmTokenService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FcmTokenService"
    }

    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. This is also called when the
     * FCM registration token is initially generated.
     *
     * @param token The new FCM registration token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // Send the FCM registration token to your app server.
        sendRegistrationToServer(token)
    }

    /**
     * Retrieves the FCM token on-demand, logging and displaying it.
     */
    fun retrieveAndLogToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and optionally display the token
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()

            // Optionally, send the token to the server if it hasn't been sent yet
            sendRegistrationToServer(token)


        }
    }

    /**
     * Sends the FCM registration token to the app server.
     *
     * @param token The FCM token to be sent to the server.
     */
    private fun sendRegistrationToServer(token: String) {
        // Implement the logic to send token to your app server
        Log.d(TAG, "Token sent to server: $token")
    }
}
