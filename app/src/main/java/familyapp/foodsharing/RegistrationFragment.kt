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
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import familyapp.foodsharing.databinding.FragmentLogInBinding
import familyapp.foodsharing.databinding.FragmentRegistrationBinding
import familyapp.foodsharing.util.Validate

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    companion object{
        val validate = Validate
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        auth = Firebase.auth
        registration()
    }

    private fun registration(){

        val loadingDialog = LoadingDialog(requireActivity())

        binding.buttonRegistration.setOnClickListener {
            val email = binding.editTextEmailRegistration.text.toString()
            val password = binding.editTextPassRegistration.text.toString()
            val repassword = binding.editTextPassRegistrationConfirm.text.toString()
            val name = binding.editTextUserName.text.toString()
            if (validate.validateRegistration(email, password, repassword, name, requireContext())){
                loadingDialog.startLoading()
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            //val user = auth.currentUser
                            view?.hideKeyboard()
                            firebaseDatabaseAddNametoId(name).addOnCompleteListener {
                                if (it.isSuccessful){
                                    loadingDialog.dissmissDialog()
                                }
                            }
                        }
                    }.addOnFailureListener {
                        if (it.message == "The email address is already in use by another account."){
                            Toast.makeText(context, R.string.email_use_already, Toast.LENGTH_SHORT).show()
                            loadingDialog.dissmissDialog()
                        } else {
                            Toast.makeText(context, R.string.registration_error, Toast.LENGTH_SHORT).show()
                            loadingDialog.dissmissDialog()
                        }

                    }
            }
        }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun firebaseDatabaseAddNametoId(username: String): Task<Void> {
       return FirebaseDatabase.getInstance().getReference("Users").child(auth.uid!!).setValue(username)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}