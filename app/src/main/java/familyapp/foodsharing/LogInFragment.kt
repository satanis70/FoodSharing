package familyapp.foodsharing

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import familyapp.foodsharing.databinding.FragmentLogInBinding
import familyapp.foodsharing.util.Validate

class LogInFragment : Fragment() {

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    companion object{
        val validate = Validate
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        auth = Firebase.auth
        registration()
        signIn()
        fargot_pass()
    }

    fun signIn(){

        val loadingDialog = LoadingDialog(requireActivity())

        binding.buttonSignIn.setOnClickListener {
            val email = binding.editTextLoginEmail.text.toString()
            val password = binding.editTextLoginPassword.text.toString()
            if (validate.validateLogin(email, password, requireContext())){
                loadingDialog.startLoading()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            view?.hideKeyboard()
                            //val user = auth.currentUser
                            Toast.makeText(context, "Успешно!", Toast.LENGTH_SHORT).show()
                            loadingDialog.dissmissDialog()
                        }
                    }.addOnFailureListener {
                        if (it.message=="There is no user record corresponding to this identifier. The user may have been deleted."){
                            Toast.makeText(
                                context,
                                R.string.error_sign_in_email_not_found,
                                Toast.LENGTH_LONG
                            ).show()
                            loadingDialog.dissmissDialog()
                        } else if (it.message=="The password is invalid or the user does not have a password."){
                            Toast.makeText(
                                context,
                                R.string.error_sign_in_password,
                                Toast.LENGTH_LONG
                            ).show()
                            loadingDialog.dissmissDialog()
                        }
                    }
            }
        }

    }

    fun registration(){
        binding.textViewRegistration.setOnClickListener {
            navController.navigate(R.id.action_logInFragment_to_registrationFragment)
        }
    }

    fun fargot_pass(){
        val fargotPasswordDialog = ForgotPasswordDialog()
        binding.forgotPass.setOnClickListener {
            fargotPasswordDialog.show(parentFragmentManager, "fargotPasswordDialog")
        }
    }


    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}