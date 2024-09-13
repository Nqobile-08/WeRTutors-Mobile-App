import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.nqobza.wertutors.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener {
            val email = binding.edtREmail.text.toString()
            val password = binding.edtRPass.text.toString()
            createUser(email, password)
        }
    }

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Store user role (Student/Tutor/Admin) in database
                    val user = auth.currentUser
                    // Redirect to login or dashboard
                } else {
                    Toast.makeText(baseContext, "Registration Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
